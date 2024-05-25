package Model.Game.States;

import Model.Game.CardPoolTypes;
import Exceptions.Game.InvalidActionForGameStateException;
import Exceptions.Game.InvalidActionForPlayerStateException;
import Exceptions.Game.NotYourTurnException;
import Model.Player.*;
import Model.Game.Game;
import Network.ServerClient.Packets.SCPUpdateClientGameState;
import Server.Model.Lobby.LobbyUserConnectionStates;
import Server.Model.Lobby.ObserverRelay;

import java.util.List;
import java.util.logging.Logger;

/**
 * Represents the final round state in the game, where players can only place cards but not draw new ones.
 * This state implements the GameState interface and defines methods to handle actions during the final round.
 */
public class FinalRound implements GameState{
    //ATTRIBUTES
    private final Game game;
    private final List<Player> players;
    private final ObserverRelay gameObserverRelay;

    private int currentPlayerIndex;

    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public FinalRound(Game game){
        logger = Logger.getLogger(MainLoop.class.getName());
        logger.info("Initializing Main Loop State in game");

        this.game = game;
        players = game.getPlayers();

        gameObserverRelay = game.getGameObserverRelay();

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
        player.setPlayerState(PlayerStates.WAIT);
        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT));
        player.incrementRoundsCompleted();

        nextPlayer();
    }

    /**
     * The method throws an exception as players are not allowed to perform this move during this state of the game.<br>
     * (Drawing cards in the final round is useless as they are not going to be ever used as this is the last round)
     *
     * @param player       The player who is drawing the card.
     * @param cardPoolType The type of card pool from which the card will be drawn.
     * @param index        The index of the card in the card pool.
     * @throws InvalidActionForGameStateException   Always thrown as players are not allowed to draw cards in the final round.
     */
    @Override
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws InvalidActionForGameStateException {
        throw new InvalidActionForGameStateException("You don't need to draw a new card during the final round");
    }

    /**
     * The method throws an exception as players are not allowed to perform this move during this state of the game.
     *
     * @param player        The player who is picking the objective.
     * @param objectiveIndex The index of the objective the player is picking.
     * @throws InvalidActionForGameStateException   Always thrown as players are not allowed to pick objectives in the final round.
     */
    @Override
    public void pickPlayerObjective(Player player, int objectiveIndex) throws InvalidActionForGameStateException {
        throw new InvalidActionForGameStateException("You can't pick your secret objective in this state");
    }





    //DISCONNECTION METHODS
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldRemovePlayerOnDisconnect() {
        return true;
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
        logger.info("Player "+player.getUsername()+" in Final Round of game, waiting for signal from lobby to remove the player");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(Player player) {
        logger.info("Removing player "+player.getUsername()+" from game.");
        int indexPlayerToRemove = players.indexOf(player);
        logger.fine("Player "+player.getUsername()+" is number "+indexPlayerToRemove+" in the current order of the game.");

        //Remove the player from the player list
        if(indexPlayerToRemove == currentPlayerIndex){
            logger.fine("The player to remove is the current player");
            players.remove(player);
            logger.fine("Player removed");
            currentPlayerIndex--;
            logger.fine("Current player index decremented");
            nextPlayer();
            logger.fine("Looking for the next player");
        }

        else if(indexPlayerToRemove < currentPlayerIndex){
            logger.fine("The player to remove is before the current player");
            players.remove(player);
            logger.fine("Player removed");
            currentPlayerIndex--;
            logger.fine("Current player index decremented");
        }

        else{
            logger.fine("The player to remove is after the current player");
            players.remove(player);
            logger.fine("Player removed");
        }
    }

    @Override
    public void userReconnectionProcedure(Player player) {
        logger.info("Player "+player.getUsername()+" reconnected to Final Round of game.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player The user who is quitting.
     */
    @Override
    public void quit(Player player) {
        logger.info("Player "+player.getUsername()+" requested to quit from the game.");
        removePlayer(player);
    }





    //TURN SWITCHER
    /**
     * Advances the game to the next player's turn.
     */
    private void nextPlayer(){
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setPlayerState(PlayerStates.WAIT);
        int playerCount = players.size();

        //If the current player is the last player then go to the next state
        if(currentPlayerIndex +1 == playerCount)
            nextState();
        else {
            //If the current player is not the last one then go through the remaining players
            for (int i = currentPlayerIndex + 1; i < players.size(); i++) {
                Player nextPlayer = players.get(i);
                //The first among the remaining players that is found online is set as next player.
                if(nextPlayer.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                    currentPlayerIndex = i;
                    nextPlayer.setPlayerState(PlayerStates.PLACE);
                    gameObserverRelay.update(nextPlayer.getUsername(), new SCPUpdateClientGameState(PlayerStates.PLACE));
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
     * It is necessary to prevent the game from stalling if the player at position 0 in the list has disconnected.
     *
     * @throws RuntimeException If no players are found online.
     */
    private void findFirstPlayer(){
        currentPlayerIndex = -1;
        Player firstPlayer;

        //Look for the first(in the list's order) online player in the list and set them as the current player
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                currentPlayerIndex = i;
                //The player found is set as current player.
                firstPlayer = players.get(currentPlayerIndex);
                //The player's state is updated to reflect their next expected move
                firstPlayer.setPlayerState(PlayerStates.PLACE);
                gameObserverRelay.update(firstPlayer.getUsername(), new SCPUpdateClientGameState(PlayerStates.PLACE));
                break;
            }
        }

        if(currentPlayerIndex == -1)
            //todo
            throw new RuntimeException("No players found online");
    }





    //STATE SWITCHER
    /**
     * Switches the game state to the next state: GameEnd.
     */
    private void nextState(){
        game.incrementRoundsCompleted();
        game.gameOver();
    }
}