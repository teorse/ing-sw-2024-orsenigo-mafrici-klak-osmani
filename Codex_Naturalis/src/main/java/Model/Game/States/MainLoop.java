package Model.Game.States;

import Model.Cards.Card;
import Exceptions.Game.InvalidActionForGameStateException;
import Exceptions.Game.InvalidActionForPlayerStateException;
import Exceptions.Game.NotYourTurnException;
import Model.Game.Game;
import Model.Game.CardPoolTypes;
import Model.Game.Table;
import Model.Player.Player;
import Network.ServerClient.Packets.SCPUpdateClientGameState;
import Server.Model.Lobby.LobbyUserConnectionStates;
import Model.Player.PlayerStates;
import Server.Model.Lobby.ObserverRelay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the main loop state in the game, where players take turns to place cards and draw new cards.<br>
 * This state implements the GameState interface and defines methods to handle actions during the main gameplay loop.
 */
public class MainLoop implements GameState{
    //ATTRIBUTES
    private final Game game;
    private final List<Player> players;
    private final Table table;
    private final ObserverRelay gameObserverRelay;

    private int currentPlayerIndex;
    private final Map<String, PlayerStates> playerStatesBeforeDisconnection;





    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public MainLoop(Game game){
        this.game = game;
        players = game.getPlayers();
        table = game.getTable();
        gameObserverRelay = game.getGameObserverRelay();
        playerStatesBeforeDisconnection = new HashMap<>();

        findFirstPlayer();
    }

    public MainLoop(Game game, Map<String, PlayerStates> playerStatesBeforeDisconnection){
        this.game = game;
        players = game.getPlayers();
        table = game.getTable();
        gameObserverRelay = game.getGameObserverRelay();
        this.playerStatesBeforeDisconnection = playerStatesBeforeDisconnection;

        findFirstPlayer();
    }





    //STATE PATTERN METHODS

    /**
     * {@inheritDoc}
     *
     * @param player          The player who is placing the card.
     * @param cardIndex       The index of the card in the player's hand.
     * @param coordinateIndex The index of the available coordinate in the cardMap where the card will be placed.
     * @param faceUp          True if the card should be placed face up, false if face down.
     * @throws NotYourTurnException                 Thrown if the player attempts to place a card out of turn.
     * @throws InvalidActionForPlayerStateException Thrown if the player attempts an invalid action in their current state.
     */
    @Override
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, InvalidActionForPlayerStateException {

        //Throws exception if it's not the player's turn.
        if (!players.get(currentPlayerIndex).equals(player))
            throw new NotYourTurnException("Wait for your turn!");

        //Throws exception if the player can't perform this move.
        else if (!player.getPlayerState().equals(PlayerStates.PLACE))
            throw new InvalidActionForPlayerStateException("You can't perform this move in your current state, please perform a DRAW action.");

        //The rest of the method is executed if the player is actually allowed to perform the move.
        player.playCard(cardIndex, coordinateIndex, faceUp);
        player.setPlayerState(PlayerStates.DRAW);
        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.DRAW));


        game.checkGameEndingConditions();
    }

    /**
     * {@inheritDoc}
     *
     * @param player       The player who is drawing the card.
     * @param cardPoolType The type of card pool from which the card will be drawn.
     * @param index        The index of the card in the card pool.
     * @throws NotYourTurnException                 Thrown if the player attempts to draw a card out of turn.
     * @throws InvalidActionForPlayerStateException Thrown if the player attempts an invalid action in their current state.
     */
    @Override
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, InvalidActionForPlayerStateException{
        //Throws exception if it's not the player's turn.
        if (!players.get(currentPlayerIndex).equals(player))
            throw new NotYourTurnException("Wait for your turn!");

        //Throws exception if the player can't perform this move.
        else if (!player.getPlayerState().equals(PlayerStates.DRAW))
            throw new InvalidActionForPlayerStateException("You can't perform this move in your current state, please perform a PLACE action.");

        //The rest of the method is executed if the player is actually allowed to perform the move.

        Card cardDrawn = table.drawCard(cardPoolType, index);
        player.addCardHeld(cardDrawn);

        player.setPlayerState(PlayerStates.WAIT);
        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT));

        game.checkGameEndingConditions();
        nextPlayer();
    }

    /**
     * The method throws an exception as players are not allowed to perform this move during this state of the game.
     *
     * @param player        The player who is picking the objective.
     * @param objectiveIndex The index of the objective the player is picking.
     * @throws InvalidActionForGameStateException   Always thrown as players are not allowed to pick objectives in the main loop state.
     */
    @Override
    public void pickPlayerObjective(Player player, int objectiveIndex) throws InvalidActionForGameStateException {
        throw new InvalidActionForGameStateException("You can't pick your secret objective in this game state");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldRemovePlayerOnDisconnect() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(Player player) {
        Player currentPlayer = players.get(currentPlayerIndex);
        int indexPlayerToRemove = players.indexOf(player);

        if(indexPlayerToRemove <= currentPlayerIndex)
            currentPlayerIndex--;

        players.remove(player);


        //todo update current player index after removal adjusting for list shrinking

        //If we are removing the current player then we need to advance to the next player
        if(player.equals(currentPlayer) && players.size() > 1)
            nextPlayer();
        else
            game.gameOver();
    }

    /**
     * Handles user disconnection.<br>
     * If the user that disconnected was the current player then it makes the next player the current to avoid
     * getting stuck on a disconnected player.
     *
     * @param player The user who disconnected.
     */
    @Override
    public void userDisconnectionProcedure(Player player) {

        if(player.getPlayerState()!=PlayerStates.WAIT)
            playerStatesBeforeDisconnection.put(player.getUsername(), player.getPlayerState());

        player.setPlayerState(PlayerStates.WAIT);

        if(players.equals(players.get(currentPlayerIndex)))
            nextPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit(Player player) {
        removePlayer(player);
    }


    //TURN SWITCHER
    /**
     * Advances the round to the next player's turn.
     */
    private void nextPlayer(){
        int playersSize = players.size();

        //If current player is the last players call next state.
        if(currentPlayerIndex +1 == playersSize)
            nextState();

        else {
            //For all the remaining players
            for (int i = currentPlayerIndex + 1; i < playersSize; i++) {
                Player nextPlayer = players.get(i);
                //Set as current player the first online player found among the remaining ones.
                if(nextPlayer.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                    currentPlayerIndex = i;

                    //If the player had previously disconnected then the old saved value for their state is retrieved, otherwise
                    //by default they are assigned the PLACE state.
                    nextPlayer.setPlayerState(playerStatesBeforeDisconnection.getOrDefault(nextPlayer.getUsername(), PlayerStates.PLACE));
                    return;
                }
            }

            //If no eligible players were found after the current player then advance to next round.
            nextState();
        }
    }





    //FIRST PLAYER FINDER
    /**
     * Determines the initial player for each round.<br>
     * It is necessary to prevent the game from stalling if the player at position 0 in the list has temporarily disconnected.
     *
     * @throws RuntimeException If no players are found online.
     */
    private void findFirstPlayer(){
        currentPlayerIndex = -1;
        Player firstPlayer;

        //Look for the first(in the list's order) online player in the list and set them as the current player
        for(int i = 0; i < game.getPlayers().size(); i++){
            if(players.get(i).getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                currentPlayerIndex = i;
                //The player found is set as current player.
                firstPlayer = players.get(currentPlayerIndex);
                //The player's state is updated to reflect their next expected move


                //If the player had previously disconnected then the old saved value for their state is retrieved, otherwise
                //by default they are assigned the PLACE state.
                firstPlayer.setPlayerState(playerStatesBeforeDisconnection.getOrDefault(firstPlayer.getUsername(), PlayerStates.PLACE));
                gameObserverRelay.update(firstPlayer.getUsername(), new SCPUpdateClientGameState(PlayerStates.PLACE));
                break;
            }
        }

        if(currentPlayerIndex == -1)
            throw new RuntimeException("No players found online");
    }





    //STATE SWITCHER
    /**
     * Switches the game state to the next state.<br>
     * Check for the boolean LastRoundFlag. If true then the next state will be the FinalRound, otherwise
     * the next state will be another MainLoop.
     */
    private void nextState(){
        game.incrementRoundsCompleted();
        if(game.isLastRoundFlag())
            game.setState(new FinalRound(game));
        else
            game.setState(new MainLoop(game, playerStatesBeforeDisconnection));
    }
}