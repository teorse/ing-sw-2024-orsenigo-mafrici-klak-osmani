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
import java.util.logging.Logger;

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

    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public MainLoop(Game game){
        logger = Logger.getLogger(MainLoop.class.getName());
        logger.info("Initializing Main Loop State in game");

        this.game = game;
        players = game.getPlayers();
        table = game.getTable();
        gameObserverRelay = game.getGameObserverRelay();
        playerStatesBeforeDisconnection = new HashMap<>();

        findFirstPlayer();
        logger.info("Main Loop State in game initialized");
    }

    public MainLoop(Game game, Map<String, PlayerStates> playerStatesBeforeDisconnection){
        logger = Logger.getLogger(MainLoop.class.getName());
        logger.info("Initializing Main Loop State in game");

        this.game = game;
        players = game.getPlayers();
        table = game.getTable();
        gameObserverRelay = game.getGameObserverRelay();
        this.playerStatesBeforeDisconnection = playerStatesBeforeDisconnection;

        findFirstPlayer();
        logger.info("Main Loop State in game initialized");
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

        //After placing the card, if the two card pools are not empty then set the player to the DRAW state
        if(!table.areAllCardPoolsEmpty())
            player.setPlayerState(PlayerStates.DRAW);
        else {
            //If the two card Pools are empty then set the player state to wait and go to the next player
            player.setPlayerState(PlayerStates.WAIT);
            player.incrementRoundsCompleted();
            game.checkGameEndingConditions();
            nextPlayer();
            return;
        }

        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(player.getPlayerState()));
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
        player.incrementRoundsCompleted();

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

        if(!game.isWaitingForReconnections())
            oneOnlinePlayerLeftProcedure();

        if(player.getPlayerState()!=PlayerStates.WAIT) {
            logger.fine("Backing-up the player state before the disconnection");
            playerStatesBeforeDisconnection.put(player.getUsername(), player.getPlayerState());
        }

        logger.fine("Setting the disconnected player state to WAIT");
        player.setPlayerState(PlayerStates.WAIT);

        if(player.equals(players.get(currentPlayerIndex))) {
            logger.fine("The disconnecting player was the current player, proceeding to look for next player");
            nextPlayer();
        }
    }

    @Override
    public void userReconnectionProcedure(Player player) {
        logger.info("Player "+player.getUsername()+" is reconnecting to the game Main Loop state");
        if(game.isWaitingForReconnections()){
            logger.info("Detected waitingForReconnection true, restoring the old current player from their wait state");
            game.setWaitingForReconnections(false);
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.setPlayerState(playerStatesBeforeDisconnection.remove(currentPlayer.getUsername()));
            gameObserverRelay.update(currentPlayer.getUsername(), new SCPUpdateClientGameState(currentPlayer.getPlayerState()));
            logger.info("Game Loop restored");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit(Player player) {
        logger.info("Player "+player.getUsername()+" requested to quit from the game.");
        removePlayer(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void removePlayer(Player player) {
        logger.info("Removing player "+player.getUsername()+" from game.");
        int indexPlayerToRemove = players.indexOf(player);
        logger.fine("Player "+player.getUsername()+" is number "+indexPlayerToRemove+" in the current order of the game.");

        //Remove the player from the player list
        if(indexPlayerToRemove == currentPlayerIndex){
            logger.fine("The player to remove is the current player");
            players.remove(player);
            logger.fine("Player removed");

            if(!game.isWaitingForReconnections())
                oneOnlinePlayerLeftProcedure();

            currentPlayerIndex--;
            logger.fine("Current player index decremented");
            nextPlayer();
            logger.fine("Looking for the next player");
        }

        else if(indexPlayerToRemove < currentPlayerIndex){
            logger.fine("The player to remove is before the current player");
            players.remove(player);
            logger.fine("Player removed");

            if(!game.isWaitingForReconnections())
                oneOnlinePlayerLeftProcedure();

            currentPlayerIndex--;
            logger.fine("Current player index decremented");
        }

        else{
            logger.fine("The player to remove is after the current player");
            players.remove(player);
            logger.fine("Player removed");

            if(!game.isWaitingForReconnections())
                oneOnlinePlayerLeftProcedure();
        }
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
            playerStatesBeforeDisconnection.put(onlinePlayer.getUsername(), onlinePlayer.getPlayerState());
            logger.finest("Setting current player to wait");
            onlinePlayer.setPlayerState(PlayerStates.WAIT);
            logger.finest("Updating client state");
            gameObserverRelay.update(onlinePlayer.getUsername(), new SCPUpdateClientGameState(onlinePlayer.getPlayerState()));
            logger.finest("Setting waiting for reconnection to true.");
            game.setWaitingForReconnections(true);
        }
        logger.fine("Exiting Game disconnection procedure");
    }





    //TURN SWITCHER
    /**
     * Advances the round to the next player's turn.
     */
    private void nextPlayer(){
        logger.info("Looking for the next player");

        int playersSize = players.size();

        logger.fine("Current player was: "+players.get(currentPlayerIndex).getUsername()+
                "\nCurrent player index was: "+currentPlayerIndex+", real life number is: "+currentPlayerIndex+1+
                "\nTotal players in this game: "+playersSize);

        //If current player is the last players call next state.
        if(currentPlayerIndex +1 == playersSize) {
            logger.fine("Player index+1 equals player size (the current player was the last player for the round), proceeding to next state");
            nextState();
        }

        else {
            logger.fine("Current player was not the last player");
            //For all the remaining players
            for (int i = currentPlayerIndex + 1; i < playersSize; i++) {
                logger.fine("Checking player number "+i);
                Player nextPlayer = players.get(i);
                logger.fine("Player number "+i+" is "+nextPlayer.getUsername()+"\nTheir connection status is: "+nextPlayer.getConnectionStatus());
                //Set as current player the first online player found among the remaining ones.
                if(nextPlayer.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                    logger.fine("Setting current player index to: "+i);
                    currentPlayerIndex = i;

                    //If the player had previously disconnected then the old saved value for their state is retrieved, otherwise
                    //by default they are assigned the PLACE state.
                    logger.fine("Checking if the player has a cached stated from a previous disconnection");
                    nextPlayer.setPlayerState(playerStatesBeforeDisconnection.getOrDefault(nextPlayer.getUsername(), PlayerStates.PLACE));
                    logger.fine("Updating player "+nextPlayer.getUsername()+" client state to: "+nextPlayer.getPlayerState());
                    gameObserverRelay.update(nextPlayer.getUsername(), new SCPUpdateClientGameState(nextPlayer.getPlayerState()));
                    return;
                }
            }
            logger.fine("No eligible players were found in this round, proceeding to next round.");
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
        logger.info("Looking for first player of this round");
        currentPlayerIndex = -1;
        Player firstPlayer;

        //Look for the first(in the list's order) online player in the list and set them as the current player
        for(int i = 0; i < game.getPlayers().size(); i++){
            logger.fine("Checking player: "+players.get(i).getUsername()+"\nConnection status: "+players.get(i).getConnectionStatus());
            if(players.get(i).getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                logger.fine("Setting player "+players.get(i)+" as current player");
                currentPlayerIndex = i;
                //The player found is set as current player.
                firstPlayer = players.get(currentPlayerIndex);
                //The player's state is updated to reflect their next expected move


                //Check which state to assign to the first player
                if(!game.isWaitingForReconnections()) {
                    //If the game is not waiting for reconnections then assign to the first player either their state before disconnections
                    //or give them the default state of PLACE
                    firstPlayer.setPlayerState(playerStatesBeforeDisconnection.getOrDefault(firstPlayer.getUsername(), PlayerStates.PLACE));
                    gameObserverRelay.update(firstPlayer.getUsername(), new SCPUpdateClientGameState(firstPlayer.getPlayerState()));
                }
                else{
                    //If the game is waiting for reconnections then set the first player to wait and if they do not have any cached
                    //state from earlier disconnections then cache their state as PLACE
                    firstPlayer.setPlayerState(PlayerStates.WAIT);
                    if(!playerStatesBeforeDisconnection.containsKey(firstPlayer.getUsername()))
                        playerStatesBeforeDisconnection.put(firstPlayer.getUsername(), PlayerStates.PLACE);
                    gameObserverRelay.update(firstPlayer.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT));
                }
                break;
            }
        }

        if(currentPlayerIndex == -1)
            logger.warning("No players found online in game");
    }





    //STATE SWITCHER
    /**
     * Switches the game state to the next state.<br>
     * Check for the boolean LastRoundFlag. If true then the next state will be the FinalRound, otherwise
     * the next state will be another MainLoop.
     */
    private void nextState(){
        game.incrementRoundsCompleted();


        int playersSize = players.size();

        if(playersSize < 2){
            game.gameOver();
            return;
        }


        if(game.isLastRoundFlag())
            game.setState(new FinalRound(game));
        else
            game.setState(new MainLoop(game, playerStatesBeforeDisconnection));
    }
}