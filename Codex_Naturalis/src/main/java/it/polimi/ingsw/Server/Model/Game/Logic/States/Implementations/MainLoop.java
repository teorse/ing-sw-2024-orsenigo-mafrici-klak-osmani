package it.polimi.ingsw.Server.Model.Game.Logic.States.Implementations;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.GameRecord;
import it.polimi.ingsw.Exceptions.Game.Model.InvalidGameInputException;
import it.polimi.ingsw.Server.Model.Game.Cards.Card;
import it.polimi.ingsw.Exceptions.Game.InvalidActionForPlayerStateException;
import it.polimi.ingsw.Exceptions.Game.NotYourTurnException;
import it.polimi.ingsw.Server.Model.Game.Logic.Game;
import it.polimi.ingsw.Server.Model.Game.Logic.States.PlayerSwitchers.PlayerSwitcherResilient;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Game.Logic.States.SynchronousGameState;
import it.polimi.ingsw.Server.Model.Game.Table.Table;
import it.polimi.ingsw.Server.Model.Game.Player.Player;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateCardPoolDrawability;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateGame;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The MainLoop class represents the main game loop state in a synchronous game.
 * This class extends SynchronousGameState and handles the main logic for the game
 * during its active state.
 */
public class MainLoop extends SynchronousGameState {
    //ATTRIBUTES
    private final Table table;

    private final Map<String, PlayerStates> playerStatesBeforeDisconnection;

    private boolean lastRound;
    private boolean waitingForReconnections;





    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public MainLoop(Game game){
        super(game, new PlayerSwitcherResilient(game.getPlayers()));

        logger = Logger.getLogger(MainLoop.class.getName());
        logger.info("Initializing Main Loop State in game");

        table = game.getTable();
        playerStatesBeforeDisconnection = new HashMap<>();

        lastRound = false;
        waitingForReconnections = false;

        findFirstPlayer();
        logger.info("Main Loop State in game initialized");
    }

    //PRIVATE SETTERS
    /**
     * Sets the game to its last round state and updates all game observers.
     * Notifies observers with the updated game state.
     */
    private void startLastRound() {
        lastRound = true;
        gameObserverRelay.update(new SCPUpdateGame(game.toRecord()));
    }

    /**
     * Sets whether the game is waiting for player reconnections.
     *
     * @param value The boolean value indicating if the game is waiting for reconnections.
     *              True if waiting for reconnections, false otherwise.
     */
    private void setWaitingForReconnections(boolean value) {
        waitingForReconnections = value;
        gameObserverRelay.update(new SCPUpdateGame(game.toRecord()));
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
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, InvalidActionForPlayerStateException, InvalidGameInputException {

        //Throws exception if it's not the player's turn.
        if (!players.get(currentPlayerIndex).equals(player))
            throw new NotYourTurnException("Wait for your turn!");

        //Throws exception if the player can't perform this move.
        else if (!player.getPlayerState().equals(PlayerStates.PLACE))
            throw new InvalidActionForPlayerStateException("You can't perform this move in your current state, please perform a DRAW action.");

        //The rest of the method is executed if the player is actually allowed to perform the move.
        player.playCard(cardIndex, coordinateIndex, faceUp);

        //After placing the card, if the two card pools are not empty then set the player to the DRAW state
        if(!table.areAllCardPoolsEmpty() && !lastRound) {
            Map<CardPoolTypes, Boolean> drawability = new HashMap<>();
            if(table.isCardPoolEmpty(CardPoolTypes.RESOURCE))
                drawability.put(CardPoolTypes.RESOURCE, false);
            else
                drawability.put(CardPoolTypes.RESOURCE, true);

            if(table.isCardPoolEmpty(CardPoolTypes.GOLDEN))
                drawability.put(CardPoolTypes.GOLDEN, false);
            else
                drawability.put(CardPoolTypes.GOLDEN, true);

            gameObserverRelay.update(player.getUsername(), new SCPUpdateCardPoolDrawability(drawability));


            player.setPlayerState(PlayerStates.DRAW);
        }
        else {
            //If the two card Pools are empty then set the player state to wait and go to the next player
            player.setPlayerState(PlayerStates.WAIT);
            player.incrementRoundsCompleted();
            findNextPlayer();
            return;
        }

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
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, InvalidActionForPlayerStateException {
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
        player.incrementRoundsCompleted();

        findNextPlayer();
    }





    //DISCONNECTION METHODS
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldRemovePlayerOnDisconnect() {
        return false;
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
        logger.info("Disconnection procedure initiated for player "+player.getUsername()+" in game.");

        if(!waitingForReconnections)
            oneOnlinePlayerLeftProcedure();

        if(player.getPlayerState()!=PlayerStates.WAIT) {
            logger.fine("Backing-up the player state before the disconnection");
            playerStatesBeforeDisconnection.put(player.getUsername(), player.getPlayerState());
        }

        logger.fine("Setting the disconnected player state to WAIT");
        player.setPlayerState(PlayerStates.WAIT);

        if(player.equals(players.get(currentPlayerIndex))) {
            logger.fine("The disconnecting player was the current player, proceeding to look for next player");
            findNextPlayer();
        }
    }

    /**
     * Handles the reconnection procedure for a player in the game's Main Loop state.
     * Restores the player's previous state and updates card drawability for the reconnected player.
     *
     * @param player The player who is reconnecting to the game.
     */
    @Override
    public void userReconnectionProcedure(Player player) {
        logger.info("Player " + player.getUsername() + " is reconnecting to the game Main Loop state");

        // Check if the game was waiting for player reconnections
        if (waitingForReconnections) {
            logger.info("Detected waitingForReconnection true, restoring the old current player from their wait state");

            // Reset waitingForReconnections flag and restore current player's state
            setWaitingForReconnections(false);
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.setPlayerState(playerStatesBeforeDisconnection.remove(currentPlayer.getUsername()));

            logger.info("Game Loop restored");
        }

        // Update card drawability for the reconnected player
        Map<CardPoolTypes, Boolean> cardDrawabilityMap = new HashMap<>();
        cardDrawabilityMap.put(CardPoolTypes.GOLDEN, true);
        cardDrawabilityMap.put(CardPoolTypes.RESOURCE, true);

        gameObserverRelay.update(player.getUsername(), new SCPUpdateCardPoolDrawability(cardDrawabilityMap));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void removePlayer(Player player) {
        if(!waitingForReconnections)
            oneOnlinePlayerLeftProcedure();

        super.removePlayer(player);
    }

    /**
     * Handles the procedure when only one player is left online in the game.
     * Sets the remaining player to wait for reconnection and manages state restoration if the player disconnects.
     */
    private void oneOnlinePlayerLeftProcedure() {
        logger.info("Checking how many players are left online");
        int onlinePlayersCounter = 0;
        Player onlinePlayer = null;

        // Count online players and identify the remaining online player
        for (Player player : players) {
            logger.finest("Checking player: " + player.getUsername());
            logger.finest("Player connection status: " + player.getConnectionStatus());
            if (player.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)) {
                onlinePlayersCounter++;
                onlinePlayer = player;
            }
        }
        logger.fine(onlinePlayersCounter + " players found online");

        // Perform actions if only one player is online
        if (onlinePlayersCounter == 1) {
            logger.info("Only 1 player was found online, setting current player to wait while waiting for reconnections.");
            logger.finest("Only online player found is: " + onlinePlayer.getUsername() + "\nAdding player state to playerStatesBeforeDisconnection");

            // Set flag to wait for reconnections
            setWaitingForReconnections(true);

            // Save current player state if it's not already in wait state
            if (!onlinePlayer.getPlayerState().equals(PlayerStates.WAIT)) {
                playerStatesBeforeDisconnection.put(onlinePlayer.getUsername(), onlinePlayer.getPlayerState());
            }

            // Set current player to wait state
            logger.finest("Setting current player to wait");
            onlinePlayer.setPlayerState(PlayerStates.WAIT);

            logger.finest("Setting waiting for reconnection to true.");
        }

        logger.fine("Exiting Game disconnection procedure");
    }


    /**
     * Determines the state of the player based on the current game phase and connection status.
     *
     * @param player The player for whom the state is to be determined.
     * @return The state of the player.
     */
    public PlayerStates determinePlayerState(Player player) {
        // Check if the game is waiting for reconnections
        if (!waitingForReconnections) {
            // If the game is not waiting for reconnections
            if (!lastRound) {
                // Not in the last round: players can both place and draw cards
                return playerStatesBeforeDisconnection.getOrDefault(player.getUsername(), PlayerStates.PLACE);
            } else {
                // In the last round: players can only place cards
                return PlayerStates.PLACE;
            }
        } else {
            // If the game is waiting for reconnections
            if (!playerStatesBeforeDisconnection.containsKey(player.getUsername())) {
                // Set default state to PLACE if player state before disconnection is not recorded
                playerStatesBeforeDisconnection.put(player.getUsername(), PlayerStates.PLACE);
            }
            // All players are in WAIT state while waiting for reconnections
            return PlayerStates.WAIT;
        }
    }






    //STATE SWITCHER
    /**
     * Switches the game state to the next state.<br>
     * Check for the boolean LastRoundFlag. If true then the next state will be the FinalRound, otherwise
     * the next state will be another MainLoop.
     */
    protected void nextState(){
        game.incrementRoundsCompleted();

        int playersSize = players.size();
        if(playersSize < 2 || this.lastRound){
            game.gameOver();
            return;
        }

        if(game.checkGameEndingConditions())
            startLastRound();

        findFirstPlayer();
    }




    //TO RECORD
    @Override
    public GameRecord toRecord() {
        return new GameRecord(0, lastRound, true, waitingForReconnections);
    }
}