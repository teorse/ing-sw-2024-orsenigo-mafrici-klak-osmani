package it.polimi.ingsw.Server.Model.Game.Logic;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.GameRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Exceptions.Game.Model.Player.CoordinateIndexOutOfBounds;
import it.polimi.ingsw.Server.Model.Game.Cards.Card;
import it.polimi.ingsw.Server.Model.Game.Logic.States.Implementations.PlaceStarterCard;
import it.polimi.ingsw.Server.Model.Game.Logic.States.GameState;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Game.Table.Table;
import it.polimi.ingsw.Server.Model.Game.Objectives.Objective;
import it.polimi.ingsw.Server.Model.Game.Player.Player;
import it.polimi.ingsw.Server.Interfaces.ServerModelLayer;
import it.polimi.ingsw.Server.Model.Lobby.Lobby;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUser;
import it.polimi.ingsw.Server.Model.Lobby.ObserverRelay;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.*;
import it.polimi.ingsw.Exceptions.Game.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This Class stores all the information about the current game, including the list of players, the current game phase,
 * the current round etc.
 */
public class Game implements ServerModelLayer {
    //ATTRIBUTES
    private List<Player> players;
    private final Lobby lobby;
    private final ObserverRelay gameObserverRelay;

    private final Table table;
    private GameState state;

    private final Logger logger;





    /**
     * Stores number of rounds completed by the table.<br>
     * A round is deemed completed if all the currently online players have completed their turn for the current round.
     */
    private int roundsCompleted;





    //CONSTRUCTOR
    /**
     * Default Constructor.<br>
     * Initializes a Model.Game.Game object by taking Players ArrayList, Golden and Resource Model.Game.Deck, and ArrayList of Objectives.<br>
     * Automatically
     *
     * @param lobbyUsers    List of lobby users taking part to this game.
     * @param goldenCards   List of Golden Cards to use during this game.
     * @param resourceCards List of Resource Cards to use during this game.
     * @param starterCards  List of Starter Cards to use during this game.
     * @param objectives    List of Objectives to use during this game.
     */
    public Game(Lobby lobby, List<LobbyUser> lobbyUsers, ObserverRelay gameObserverRelay, List<Card> goldenCards, List<Card> resourceCards, List<Card> starterCards, List<Objective> objectives) {
        logger = Logger.getLogger(Game.class.getName());
        logger.info("Initializing Game");

        this.lobby = lobby;
        this.gameObserverRelay = gameObserverRelay;

        players = new ArrayList<>();
        for(LobbyUser lobbyUser : lobbyUsers){
            Player player = new Player(lobbyUser, gameObserverRelay);
            players.add(player);
        }

        Collections.shuffle(this.players);
        this.table = new Table(goldenCards, resourceCards, starterCards, objectives, gameObserverRelay);
        state = new PlaceStarterCard(this);
        
        for(Player player : players) {

            if(gameObserverRelay != null)
                gameObserverRelay.update(player.getUsername(), new SCPGameStarted(toPlayerRecordList(), toCardMapRecordsMap(), player.toSecretPlayer(), table.toRecord(), toRecord()));
        }
        logger.info("Game Initialized");
    }





    //GETTERS
    public List<Player> getPlayers() {
        return players;
    }
    public Table getTable() {
        return table;
    }
    public Lobby getLobby(){
        return this.lobby;
    }
    public ObserverRelay getGameObserverRelay(){
        return this.gameObserverRelay;
    }




    //STATE PATTERN METHODS
    /**
     * Sets the current state of the game and updates observers with the updated game state.
     *
     * @param state the new state to set for the game
     */
    public void setState(GameState state){
        this.state = state;
        gameObserverRelay.update(new SCPUpdateGame(this.toRecord()));
    }

    /**
     * Initiates playing a card by a specified player.
     *
     * @param username the username of the player initiating the action
     * @param cardIndex the index of the card to be played
     * @param coordinateIndex the index of the coordinate where the card will be played
     * @param faceUp whether the card should be played face up or not
     * @throws NotYourTurnException if it's not the player's turn to perform this action
     * @throws MoveAttemptOnWaitStateException if an attempt to perform a move in a WAIT state is made
     * @throws InvalidActionForPlayerStateException if the action is invalid for the current player's state
     * @throws InvalidActionForGameStateException if the action is invalid for the current game state
     * @throws CoordinateIndexOutOfBounds if the coordinate index is out of bounds
     */
    public void playCard(String username, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, CoordinateIndexOutOfBounds {
        Player player = getPlayerByUsername(username);
        state.playCard(player, cardIndex, coordinateIndex, faceUp);
    }

    /**
     * Initiates drawing a card by a specified player.
     *
     * @param username the username of the player initiating the action
     * @param cardPoolType the type of card pool from which to draw the card
     * @param index the index of the card to draw
     * @throws NotYourTurnException if it's not the player's turn to perform this action
     * @throws MoveAttemptOnWaitStateException if an attempt to perform a move in a WAIT state is made
     * @throws MaxResourceCardsDrawnException if the maximum number of resource cards has been drawn already
     * @throws InvalidActionForPlayerStateException if the action is invalid for the current player's state
     * @throws InvalidActionForGameStateException if the action is invalid for the current game state
     * @throws MaxGoldenCardsDrawnException if the maximum number of golden cards has been drawn already
     */
    public void drawCard(String username, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, MoveAttemptOnWaitStateException, MaxResourceCardsDrawnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, MaxGoldenCardsDrawnException {
        Player player = getPlayerByUsername(username);
        state.drawCard(player, cardPoolType, index);
    }

    /**
     * Initiates a player picking an objective.
     *
     * @param username the username of the player initiating the action
     * @param objectiveIndex the index of the objective to pick
     * @throws MoveAttemptOnWaitStateException if an attempt to perform a move in a WAIT state is made
     * @throws InvalidActionForPlayerStateException if the action is invalid for the current player's state
     * @throws InvalidActionForGameStateException if the action is invalid for the current game state
     */
    public void pickPlayerObjective(String username, int objectiveIndex) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        Player player = getPlayerByUsername(username);
        state.pickPlayerObjective(player, objectiveIndex);
    }

    /**
     * Checks if a player should be removed upon disconnection based on the current game state.
     *
     * @return true if the player should be removed on disconnect, false otherwise
     */
    public boolean shouldRemovePlayerOnDisconnect(){
        return state.shouldRemovePlayerOnDisconnect();
    }

    /**
     * Removes a player from the game.
     *
     * @param username the username of the player to be removed
     */
    public void removePlayer(String username){
        Player player = getPlayerByUsername(username);
        state.removePlayer(player);
        gameObserverRelay.update(new SCPUpdatePlayers(this.toPlayerRecordList(), this.toCardMapRecordsMap()));
    }

    /**
     * Executes the disconnection procedure for a user.
     *
     * @param username the username of the disconnected user
     */
    @Override
    public void userDisconnectionProcedure(String username) {
        Player player = getPlayerByUsername(username);
        state.userDisconnectionProcedure(player);

    }

    /**
     * Executes the reconnection procedure for a user.
     *
     * @param username the username of the reconnected user
     */
    public void userReconnectionProcedure(String username){
        Player player = getPlayerByUsername(username);
        state.userReconnectionProcedure(player);

        gameObserverRelay.update(username, new SCPGameStarted(toPlayerRecordList(), toCardMapRecordsMap(), player.toSecretPlayer(), table.toRecord(), toRecord()));
        gameObserverRelay.update(username, new SCPUpdateClientGameState(player.getPlayerState()));
    }

    /**
     * Executes the quit procedure for a user.
     *
     * @param username the username of the user quitting the game
     */
    @Override
    public void quit(String username) {
        Player player = getPlayerByUsername(username);
        state.quit(player);
    }

    //HELPER METHODS
    /**
     * Retrieves the player object corresponding to the provided username.
     *
     * @param username The username of the player to retrieve.
     * @return The Player object associated with the given username.
     * @throws PlayerNotFoundException If no player with the specified username is found in the game.
     */
    private Player getPlayerByUsername(String username){
        for(Player player : players){
            if(player.getUsername().equals(username))
                return player;
        }
        throw new PlayerNotFoundException();
    }





    //METHODS
    /**
     * Increments the count of rounds completed in the game.
     * This method increments the internal counter tracking the number of completed rounds.
     * If a game observer relay is set, it updates the observers with the latest game state using SCPUpdateGame packet.
     */
    public void incrementRoundsCompleted(){
        this.roundsCompleted++;

        if(gameObserverRelay != null)
            gameObserverRelay.update(new SCPUpdateGame(toRecord()));
    }

    /**
     * Method verifies whether the conditions for the game-end are met, if so sets to true the lastRound flag.
     */
    public boolean checkGameEndingConditions(){

        if(table.areDecksEmpty()){
            return true;
        }

        for(Player player : players){
            if(player.getPoints() >= 20){
                return true;
            }
        }

        return false;
    }

    /**
     * Determines the winners of the game based on points and objectives completed.
     * Players are sorted first by points in descending order, and then by objectives completed.
     * If there is a tie on both points and objectives, multiple players can be selected as winners.
     * Updates the player objects by setting the `winner` flag for those who are winners.
     * Logs the winners to the logger.
     */
    protected void selectWinners(){
        //Comparator to sort players first by points and then by objectives completed
        Comparator<Player> comparator = Comparator.comparing(Player::getPoints);
        comparator = comparator.thenComparing(Player::getObjectivesCompleted);

        //Sort players using above comparator
        Stream<Player> playerStream = players.stream().sorted(comparator.reversed());
        players = playerStream.collect(Collectors.toList());

        //Select winners, multiple winners if tie on points and objectives.
        Player referenceWinner = players.getFirst();
        for(Player player : players){
            if(player.getPoints() == referenceWinner.getPoints() && player.getObjectivesCompleted() == referenceWinner.getObjectivesCompleted())
                player.setWinner();
        }

        logger.info("Winners are:\n"+toPlayerRecordList());
    }

    /**
     * Handles the game-over scenario by performing the following steps:
     * - Retrieves the shared objectives from the table.
     * - Calculates points for each player based on the shared objectives.
     * - Determines the winners using the `selectWinners()` method.
     * - Updates observers with the game-over information using SCPGameOver packet.
     * - Signals the end of the game to the lobby.
     */
    public void gameOver(){
        List<Objective> sharedObjectives = table.getSharedObjectives();

        //Count all the points for each player.
        for(Player player : players){
            player.countAllPoints(sharedObjectives);
        }

        //After all players are given their points the winner is calculated.
        selectWinners();

        gameObserverRelay.update(new SCPGameOver(toPlayerRecordList()));
        lobby.gameOver();
    }






    //OBSERVERS
    /**
     * Converts the list of players into a list of PlayerRecord objects.
     * Each PlayerRecord represents a transferable data object containing essential player information.
     *
     * @return a list of PlayerRecord objects representing each player in the game
     */
    public List<PlayerRecord> toPlayerRecordList(){
        List<PlayerRecord> playerRecords = new ArrayList<>();
        for(Player player : players){
            playerRecords.add(player.toTransferableDataObject());
        }
        return playerRecords;
    }

    /**
     * Converts the list of players into a map where the key is the player's username
     * and the value is the corresponding CardMapRecord object.
     * Each CardMapRecord contains information about the cards held by the player.
     *
     * @return a map of player usernames to CardMapRecord objects representing the cards held by each player
     */
    public Map<String, CardMapRecord> toCardMapRecordsMap() {
        Map<String, CardMapRecord> CardMapRecordsMap = new HashMap<>();
        for(Player player : players){
            Map.Entry<String, CardMapRecord> entry = player.toCardMapRecord();
            CardMapRecordsMap.put(entry.getKey(), entry.getValue());
        }
        return CardMapRecordsMap;
    }

    /**
     * Converts the current state of the game into a GameRecord object.
     * The GameRecord encapsulates information about the game's current state, including rounds completed and setup status.
     *
     * @return a GameRecord object representing the current state of the game
     */
    public GameRecord toRecord() {
        GameRecord gameRecord = state.toRecord();

        return new GameRecord(roundsCompleted, gameRecord.lastRoundFlag(), gameRecord.setupFinished(), gameRecord.waitingForReconnections());
    }
}
