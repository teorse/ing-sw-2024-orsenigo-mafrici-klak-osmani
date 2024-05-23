package Model.Game;

import Client.Model.Records.*;
import Exceptions.Game.*;
import Model.Cards.Card;
import Model.Objectives.Objective;
import Model.Player.Player;
import Model.Game.States.*;
import Network.ServerClient.Packets.*;
import Server.Interfaces.ServerModelLayer;
import Server.Model.Lobby.Lobby;
import Server.Model.Lobby.LobbyUser;
import Server.Model.Lobby.ObserverRelay;

import java.util.*;
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
    private boolean setupFinished;
    private boolean lastRoundFlag;
    private GameState state;


    //todo add procedures to start time-out if only 1 player is left in session or if no players are left


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
        this.lobby = lobby;
        this.gameObserverRelay = gameObserverRelay;

        players = new ArrayList<>();
        for(LobbyUser lobbyUser : lobbyUsers){
            Player player = new Player(lobbyUser, gameObserverRelay);
            players.add(player);
        }

        Collections.shuffle(this.players);
        this.table = new Table(goldenCards, resourceCards, starterCards, objectives, gameObserverRelay);
        this.lastRoundFlag = false;
        this.setupFinished = false;
        state = new PlaceStarterCard(this);
        
        for(Player player : players) {

            if(gameObserverRelay != null)
                gameObserverRelay.update(player.getUsername(), new SCPGameStarted(toPlayerRecordList(), toCardMapRecordsMap(), player.toSecretPlayer(), table.toRecord(), toRecord()));
        }
    }





    //GETTERS
    public List<Player> getPlayers() {
        return players;
    }
    public Table getTable() {
        return table;
    }
    public boolean isLastRoundFlag(){
        return this.lastRoundFlag;
    }
    public Lobby getLobby(){
        return this.lobby;
    }
    public ObserverRelay getGameObserverRelay(){
        return this.gameObserverRelay;
    }




    //STATE PATTERN METHODS
    public void setState(GameState state){
        this.state = state;
        gameObserverRelay.update(new SCPUpdateGame(this.toRecord()));
    }
    public void playCard(String username, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        Player player = getPlayerByUsername(username);
        state.playCard(player, cardIndex, coordinateIndex, faceUp);
    }
    public void drawCard(String username, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, MoveAttemptOnWaitStateException, MaxResourceCardsDrawnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, MaxGoldenCardsDrawnException {
        Player player = getPlayerByUsername(username);
        state.drawCard(player, cardPoolType, index);
    }
    public void pickPlayerObjective(String username, int objectiveIndex) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        Player player = getPlayerByUsername(username);
        state.pickPlayerObjective(player, objectiveIndex);
    }
    public boolean shouldRemovePlayerOnDisconnect(){
        return state.shouldRemovePlayerOnDisconnect();
    }
    public void removePlayer(String username){
        Player player = getPlayerByUsername(username);
        state.removePlayer(player);
        gameObserverRelay.update(new SCPUpdatePlayers(this.toPlayerRecordList(), this.toCardMapRecordsMap()));
    }
    @Override
    public void userDisconnectionProcedure(String username) {
        Player player = getPlayerByUsername(username);
        state.userDisconnectionProcedure(player);
    }

    public void userReconnectionProcedure(String username){
        Player player = getPlayerByUsername(username);
        gameObserverRelay.update(username, new SCPGameStarted(toPlayerRecordList(), toCardMapRecordsMap(), player.toSecretPlayer(), table.toRecord(), toRecord()));
        gameObserverRelay.update(username, new SCPUpdateClientGameState(player.getPlayerState()));
    }

    @Override
    public void quit(String username) {
        Player player = getPlayerByUsername(username);
        state.quit(player);
    }
    //HELPER METHODS
    private Player getPlayerByUsername(String username){
        for(Player player : players){
            if(player.getUsername().equals(username))
                return player;
        }
        throw new PlayerNotFoundException();
    }





    //SETTER
    public void finishSetup() {
        this.setupFinished = true;
    }





    //METHODS
    public void incrementRoundsCompleted(){
        this.roundsCompleted++;

        if(gameObserverRelay != null)
            gameObserverRelay.update(new SCPUpdateGame(toRecord()));
    }

    /**
     * Method verifies whether the conditions for the game-end are met, if so sets to true the lastRound flag.
     */
    public void checkGameEndingConditions(){

        if(table.areDecksEmpty()){
            this.lastRoundFlag = true;
            return;
        }

        for(Player player : players){
            if(player.getPoints() >= 20){
                this.lastRoundFlag = true;
                return;
            }
        }
    }

    protected void selectWinners(){
        //Comparator to sort players first by points and then by objectives completed
        Comparator<Player> comparator = Comparator.comparing(Player::getPoints);
        comparator = comparator.thenComparing(Player::getObjectivesCompleted);

        //Sort players using above comparator
        Stream<Player> playerStream = players.stream().sorted(comparator);
        players = playerStream.collect(Collectors.toList());

        //Select winners, multiple winners if tie on points and objectives.
        Player referenceWinner = players.getFirst();
        for(Player player : players){
            if(player.getPoints() == referenceWinner.getPoints() && player.getObjectivesCompleted() == referenceWinner.getObjectivesCompleted())
                player.setWinner();
        }
    }

    public void gameOver(){
        List<Objective> sharedObjectives = table.getSharedObjectives();

        //Count all the points for each player.
        for(Player player : players){
            player.countAllPoints(sharedObjectives);
        }

        //After all players are given their points the winner is calculated.
        selectWinners();

        List<PlayerRecord> winners = new ArrayList<>();
        for(Player player : players){
            winners.add(player.toTransferableDataObject());
        }

        gameObserverRelay.update(new SCPGameOver(winners));
        lobby.gameOver();
    }






    //OBSERVERS
    public List<PlayerRecord> toPlayerRecordList(){
        List<PlayerRecord> playerRecords = new ArrayList<>();
        for(Player player : players){
            playerRecords.add(player.toTransferableDataObject());
        }
        return playerRecords;
    }

    public Map<String, CardMapRecord> toCardMapRecordsMap() {
        Map<String, CardMapRecord> CardMapRecordsMap = new HashMap<>();
        for(Player player : players){
            Map.Entry<String, CardMapRecord> entry = player.toCardMapRecord();
            CardMapRecordsMap.put(entry.getKey(), entry.getValue());
        }
        return CardMapRecordsMap;
    }

    public GameRecord toRecord() {return new GameRecord(roundsCompleted, lastRoundFlag, setupFinished);}
}
