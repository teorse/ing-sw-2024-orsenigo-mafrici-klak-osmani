package Model.Game.States;

import Exceptions.Game.*;
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

public class DrawInitialCards implements GameState{

    //ATTRIBUTES
    private final Game game;
    private final List<Player> players;
    private final Table table;
    private final ObserverRelay gameObserverRelay;

    private int currentPlayerIndex;

    private final Map<Player, Integer> goldenCardsDrawn;
    private final Map<Player, Integer> resourceCardsDrawn;

    private final int goldenCardsToDraw;
    private final int resourceCardsToDraw;

    private int setupStateCounter;



    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public DrawInitialCards(Game game){
        this.game = game;
        players = game.getPlayers();
        table = game.getTable();
        gameObserverRelay = game.getGameObserverRelay();

        goldenCardsDrawn = new HashMap<>();
        resourceCardsDrawn = new HashMap<>();

        goldenCardsToDraw = 1;
        resourceCardsToDraw = 2;

        setupStateCounter = 0;


        for(Player player : players){

            //Set up the card-draw counters.
            resourceCardsDrawn.put(player, 0);
            goldenCardsDrawn.put(player,0);
        }

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
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        throw new InvalidActionForGameStateException("You can't play cards right now");
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
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, InvalidActionForPlayerStateException, MaxGoldenCardsDrawnException, MaxResourceCardsDrawnException {
        //Throws exception if it's not the player's turn.
        if (!players.get(currentPlayerIndex).equals(player))
            throw new NotYourTurnException("Wait for your turn!");

        //Throws exception if the player can't perform this move.
        PlayerStates playerState = player.getPlayerState();
        if(playerState != PlayerStates.DRAW && playerState != PlayerStates.DRAW_RESOURCE && playerState != PlayerStates.DRAW_GOLDEN)
            //todo implement exception
            throw new RuntimeException("You can't perform this move in your current state.");

        if(playerState.equals(PlayerStates.DRAW_GOLDEN) && cardPoolType != CardPoolTypes.GOLDEN){
            //todo implement exception
            throw new RuntimeException("Player State DRAW_GOLDEN but player wants to draw something else");
        }
        if(playerState.equals(PlayerStates.DRAW_RESOURCE) && cardPoolType != CardPoolTypes.RESOURCE)
            //todo implement exception
            throw new RuntimeException("Player State DRAW_RESOURCE but player wants to draw something else");



        //The rest of the method is executed if the player is actually allowed to perform the move.

        switch (cardPoolType) {
            case GOLDEN: {
                if (goldenCardsDrawn.containsKey(player)) {
                    int counter = goldenCardsDrawn.get(player);
                    if (counter >= goldenCardsToDraw)
                        throw new MaxGoldenCardsDrawnException("You have already drawn your golden card");
                    else {
                        player.addCardHeld(table.drawCard(cardPoolType, index));
                        goldenCardsDrawn.put(player, counter + 1);
                        player.setPlayerState(PlayerStates.WAIT);
                        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT));
                        nextPlayer();
                    }
                }
            }

            case RESOURCE: {
                if (resourceCardsDrawn.containsKey(player)) {
                    int counter = resourceCardsDrawn.get(player);
                    if (counter >= resourceCardsToDraw)
                        throw new MaxResourceCardsDrawnException("You have already drawn your two resource cards");
                    else {
                        player.addCardHeld(table.drawCard(cardPoolType, index));
                        resourceCardsDrawn.put(player, counter + 1);
                        player.setPlayerState(PlayerStates.WAIT);
                        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT));
                        nextPlayer();
                    }
                }
            }
        }

        //todo think about moving this block before the statements, to block the player from sending multiple actions at once.
        //player.setPlayerState(PlayerStates.WAIT_SETUP);
        //gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT_SETUP));
//        if (cardPoolType == CardPoolTypes.GOLDEN)
//            System.out.println("Wait per il caso golden");
//
//        nextPlayer();
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
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(Player player) {
        players.remove(player);

        //If we are removing the current player then we need to advance to the next player
        if(player.equals(players.get(currentPlayerIndex)))
            nextPlayer();
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
        if(currentPlayerIndex + 1 == playersSize)
            nextState();

        else {
            //For all the remaining players
            for (int i = currentPlayerIndex + 1; i < playersSize; i++) {
                Player nextPlayer = players.get(i);
                //Set as current player the first online player found among the remaining ones.
                if(nextPlayer.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                    currentPlayerIndex = i;

                    //Evaluates what cards the next player has still to draw.
                    determinePlayerState(nextPlayer);

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
                determinePlayerState(firstPlayer);
                return;
            }
        }

        if(currentPlayerIndex == -1)
            throw new RuntimeException("No players found online");
    }

    /**
     * Determines the player's state based on what cards he has already drawn.
     * @param player
     */
    private void determinePlayerState(Player player) {
        PlayerStates playerState;
        if(resourceCardsDrawn.get(player) < resourceCardsToDraw)
            playerState = PlayerStates.DRAW_RESOURCE;
        else if(goldenCardsDrawn.get(player) < goldenCardsToDraw)
            playerState = PlayerStates.DRAW_GOLDEN;
        else {
            playerState = PlayerStates.WAIT;
        }


        player.setPlayerState(playerState);
        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(playerState));
    }


    //STATE SWITCHER
    /**
     * Switches the game state to the next state.<br>
     * Check for the boolean LastRoundFlag. If true then the next state will be the FinalRound, otherwise
     * the next state will be another MainLoop.
     */
    private void nextState(){
        //Next state checks if the setup state has been completed, if so it starts the pick objective state
        //otherwise it continues this state by finding the next first player.
        setupStateCounter++;
        if(setupStateCounter == 3)
            game.setState(new ObjectivesSetup(game));
        else
            findFirstPlayer();
    }
}
