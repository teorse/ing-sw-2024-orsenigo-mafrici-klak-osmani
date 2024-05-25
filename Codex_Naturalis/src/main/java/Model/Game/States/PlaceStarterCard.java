package Model.Game.States;

import Exceptions.Game.*;
import Model.Game.CardPoolTypes;
import Model.Game.Game;
import Model.Player.Player;
import Model.Game.Table;
import Model.Player.PlayerStates;
import Network.ServerClient.Packets.SCPUpdateClientGameState;
import Server.Model.Lobby.ObserverRelay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents the initial setup state in the game, where players receive their starting cards and choose their colors.<br>
 * This state implements the GameState interface and handles starter card distribution and placement, initial card draws and player color selection.
 */
public class PlaceStarterCard implements GameState{
    //ATTRIBUTES
    private final Game game;
    private final List<Player> players;
    private final ObserverRelay gameObserverRelay;

    private final Logger logger;

    /**
     * Map that keeps track of which players have completed the setups of this game phase and which haven't.<br>
     * If true, it means the player HAS completed all setups and is ready for the next phase.
     */
    private final Map<Player, Boolean> playerReadiness;


    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public PlaceStarterCard(Game game){

        logger = Logger.getLogger(PlaceStarterCard.class.getName());
        logger.info("Initializing Game state: 'PlaceStarterCard'.");

        this.game = game;
        players = game.getPlayers();
        Table table = game.getTable();
        gameObserverRelay = game.getGameObserverRelay();

        //Map containing the readiness status of each player.
        //if true, player has completed all required setups for this game state.
        //if false, player is still missing some steps.
        playerReadiness = new HashMap<>();

        for(Player player : players){
            //Give each player a starter card
            player.addCardHeld(table.drawStarterCard());

            //Set each player as unready
            playerReadiness.put(player, false);
        }

        //Only AFTER everything else has been set up, set the player status as PLACE.
        //Setting it before this point can cause problems as players could try to access un-initialized variables.
        for(Player player : players){
            player.setPlayerState(PlayerStates.PLACE);
        }

        logger.fine("Game state PlaceStarterCard initialized.\n" +
                "Broadcasting Client State Update PLACE.");
        gameObserverRelay.update(new SCPUpdateClientGameState(PlayerStates.PLACE));
    }





    //STATE PATTERN METHODS

    /**
     * {@inheritDoc}
     *
     * @param player          The player who is placing the card.
     * @param cardIndex       The index of the card in the player's hand.
     * @param coordinateIndex The index of the available coordinate in the cardMap where the card will be placed.
     * @param faceUp          True if the card should be placed face up, false if face down.
     * @throws MoveAttemptOnWaitStateException      Thrown if the player attempts a move while in a "wait" state.
     * @throws InvalidActionForPlayerStateException Thrown if the player attempts an action that is not valid in their current state.
     */
    @Override
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException {
        logger.info("Player "+player.getUsername()+" is now placing a card");
        logger.fine("Checking if player has the correct internal state to place cards");
        //Throws exception if the player has already performed all his moves for this turn.
        if (player.getPlayerState().equals(PlayerStates.WAIT))
            throw new MoveAttemptOnWaitStateException("You have already performed all moves for this turn.");

        //Throws exception if the player can't perform this move.
        else if (!player.getPlayerState().equals(PlayerStates.PLACE))
            throw new InvalidActionForPlayerStateException("You can't perform this move at the moment.");

        logger.fine("Player does have the required state to place cards, proceeding to place card.");
        //The rest of the method is executed if the player is actually allowed to perform the move.
        player.playCard(cardIndex, coordinateIndex, faceUp);
        logger.fine("Updating Client State to WAIT");
        player.setPlayerState(PlayerStates.WAIT);
        logger.fine("Sending to player "+player.getUsername()+" Update Client State package to "+PlayerStates.WAIT);
        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT));

        logger.fine("Setting player "+player.getUsername()+" as READY for this state.");
        playerReadiness.put(player, true);
        nextState();
    }

    /**
     * {@inheritDoc}
     *
     * @param player       The player who is drawing the card.
     * @param cardPoolType The type of card pool from which the card will be drawn.
     * @param index        The index of the card in the card pool.
     * @throws MoveAttemptOnWaitStateException      Thrown if the player attempts a move while in a "wait" state.
     * @throws InvalidActionForPlayerStateException Thrown if the player attempts an action that is not valid in their current state.
     * @throws MaxGoldenCardsDrawnException         Thrown if the player attempts to draw an extra golden card beyond the maximum allowed during the cards setup state.
     * @throws MaxResourceCardsDrawnException       Thrown if the player attempts to draw an extra resource card beyond the maximum allowed during the cards setup state.
     */
    @Override
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, MaxGoldenCardsDrawnException, MaxResourceCardsDrawnException, InvalidActionForGameStateException {
        throw new InvalidActionForGameStateException("You can't draw cards right now.");
    }

    /**
     * The method throws an exception as players are not allowed to perform this move during this state of the game.
     * @param player                The player who is picking the objective.
     * @param objectiveIndex        The index of the objective the player is picking.
     * @throws InvalidActionForGameStateException   Always thrown as this action is not allowed in this specific game state
     */
    @Override
    public void pickPlayerObjective(Player player, int objectiveIndex) throws InvalidActionForGameStateException {
        throw new InvalidActionForGameStateException("You can't pick your objective yet.");
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
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(Player player) {
        playerReadiness.remove(player);
        players.remove(player);

        if(players.size() > 1)
            nextState();
        else
            game.gameOver();
    }

    /**
     * Handles disconnection of a user from the game.<br>
     * This method prepares the game for later player removal (which will be triggered by the outside lobby by calling
     * the remove method).
     *
     * @param player The user who disconnected.
     */
    @Override
    public void userDisconnectionProcedure(Player player) {
        logger.info("Player "+player.getUsername()+" in Final Round of game, waiting for signal from lobby to remove the player");
    }

    @Override
    public void userReconnectionProcedure(Player player) {
        logger.info("Player "+player.getUsername()+" reconnected to Final Round of game.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit(Player player) {
        logger.info("Player "+player.getUsername()+" requested to quit from the game.");
        removePlayer(player);
    }





    //STATE SWITCHER
    /**
     * Handles the logic to determine whether the conditions for switching to the next state are met and performs
     * the transition itself.
     */
    private void nextState(){
        //If at least one player is not yet ready return
        for(Map.Entry<Player, Boolean> entry : playerReadiness.entrySet()) {
            if (!entry.getValue())
                return;
        }
        //If all players are ready then go to next state.
        game.setState(new DrawInitialCards(game));
    }
}