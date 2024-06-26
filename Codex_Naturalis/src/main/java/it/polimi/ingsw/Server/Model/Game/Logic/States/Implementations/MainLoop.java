package it.polimi.ingsw.Server.Model.Game.Logic.States.Implementations;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.GameRecord;
import it.polimi.ingsw.Exceptions.Game.Model.Player.CoordinateIndexOutOfBounds;
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
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateClientGameState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateGame;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//todo fix javadoc
/**
 * Represents the main loop state in the game, where players take turns to place cards and draw new cards.<br>
 * This state implements the GameState interface and defines methods to handle actions during the main gameplay loop.
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
    private void startLastRound(){
        lastRound = true;
        gameObserverRelay.update(new SCPUpdateGame(game.toRecord()));
    }
    private void setWaitingForReconnections(boolean value){
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
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, InvalidActionForPlayerStateException, CoordinateIndexOutOfBounds {

        //Throws exception if it's not the player's turn.
        if (!players.get(currentPlayerIndex).equals(player))
            throw new NotYourTurnException("Wait for your turn!");

        //Throws exception if the player can't perform this move.
        else if (!player.getPlayerState().equals(PlayerStates.PLACE))
            throw new InvalidActionForPlayerStateException("You can't perform this move in your current state, please perform a DRAW action.");

        //The rest of the method is executed if the player is actually allowed to perform the move.
        player.playCard(cardIndex, coordinateIndex, faceUp);

        //After placing the card, if the two card pools are not empty then set the player to the DRAW state
        if(!table.areAllCardPoolsEmpty() && !lastRound)
            player.setPlayerState(PlayerStates.DRAW);
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

    @Override
    public void userReconnectionProcedure(Player player) {
        logger.info("Player "+player.getUsername()+" is reconnecting to the game Main Loop state");
        if(waitingForReconnections){
            logger.info("Detected waitingForReconnection true, restoring the old current player from their wait state");
            setWaitingForReconnections(false);
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.setPlayerState(playerStatesBeforeDisconnection.remove(currentPlayer.getUsername()));
            logger.info("Game Loop restored");
        }
        Map<CardPoolTypes, Boolean> cardDrawbilityMap = new HashMap<>();
        cardDrawbilityMap.put(CardPoolTypes.GOLDEN, true);
        cardDrawbilityMap.put(CardPoolTypes.RESOURCE, true);

        gameObserverRelay.update(player.getUsername(), new SCPUpdateCardPoolDrawability(cardDrawbilityMap));
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

    private void oneOnlinePlayerLeftProcedure(){
        logger.info("Checking how many players are left online");
        int onlinePlayersCounter = 0;

        Player onlinePlayer = null;

        for(Player player : players){
            logger.finest("Checking player: "+player.getUsername());
            logger.finest("Player connection status: "+player.getConnectionStatus());
            if(player.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)) {
                onlinePlayersCounter++;
                onlinePlayer = player;
            }
        }
        logger.fine(onlinePlayersCounter+" players found online");

        if(onlinePlayersCounter == 1){
            logger.info("Only 1 player was found online, while waiting for reconnections setting current player to wait.");
            logger.finest("Only online player found is: "+onlinePlayer.getUsername()+"\nAdding player state to playerStatesBeforeDisconnection");

            setWaitingForReconnections(true);

            if(!onlinePlayer.getPlayerState().equals(PlayerStates.WAIT))
                playerStatesBeforeDisconnection.put(onlinePlayer.getUsername(), onlinePlayer.getPlayerState());

            logger.finest("Setting current player to wait");
            onlinePlayer.setPlayerState(PlayerStates.WAIT);
            logger.finest("Updating client state");
            gameObserverRelay.update(onlinePlayer.getUsername(), new SCPUpdateClientGameState(onlinePlayer.getPlayerState()));
            logger.finest("Setting waiting for reconnection to true.");

        }
        logger.fine("Exiting Game disconnection procedure");
    }

    public PlayerStates determinePlayerState(Player player){
        //Check which state to assign to the player

        if(!waitingForReconnections) {
            //If the game is not waiting for reconnections then assign to the first player either their state before disconnections
            //or give them the default state of PLACE
            if(!lastRound)
                //If we are not in the last round then the player can both place and draw cards so we recover their state from the cache
                return playerStatesBeforeDisconnection.getOrDefault(player.getUsername(), PlayerStates.PLACE);
            else
                //If we are in the last round then they can only place cards
                return PlayerStates.PLACE;
        }
        else{
            if(!playerStatesBeforeDisconnection.containsKey(player.getUsername()))
                playerStatesBeforeDisconnection.put(player.getUsername(), PlayerStates.PLACE);

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