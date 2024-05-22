package Client.Model;

import Client.Model.ErrorDictionary.ErrorDictionaryLogIn;
import Client.Model.ErrorDictionary.ErrorDictionarySignUp;
import Client.Model.ErrorDictionary.ErrorDictionaryStartLobbyFailed;
import Client.Model.Records.*;
import Client.Model.States.ClientState;
import Client.Model.States.ConnectionState;
import Client.Network.ClientConnector;
import Client.View.TextUI;
import Model.Player.PlayerStates;

import java.util.*;

/**
 * The ClientModel class represents the client-side data model for the game.
 * <p>
 * This class manages the state and data of the client, including player information,
 * game records, lobby details, and connection settings. It also handles the transition
 * between different client states, such as connection, game play, and waiting states.
 */
public class ClientModel {
    //ATTRIBUTES

    //Internal Logic
    boolean connected;
    boolean loggedIn;
    boolean inLobby;
    boolean gameStarted;
    boolean gameOver;
    boolean setUpFinished;
    ClientState clientState;
    String myUsername;
    PlayerStates myPlayerGameState;

    //Error Managing
    ErrorDictionaryLogIn errorDictionaryLogIn;
    ErrorDictionarySignUp errorDictionarySignUp;
    ErrorDictionaryJoinLobbyFailed errorDictionaryJoinLobbyFailed;
    ErrorDictionaryStartLobbyFailed errorDictionaryStartLobbyFailed;

    //Thread Locks
    private final Object playerMapThreadLock = new Object();

    //NETWORK
    ClientConnector clientConnector;


    //SERVER MODEL MIRROR
    //LOBBY
    List<LobbyPreviewRecord> lobbyPreviewRecords;
    LobbyRecord lobbyRecord;
    List<LobbyUserRecord> lobbyUserRecords;

    //GAME
    //Publicly-visible game attributes
    Map<String, CardMapRecord> cardMaps;
    List<PlayerRecord> players;
    GameRecord gameRecord;
    TableRecord tableRecord;
    List<PlayerRecord> winners;

    //Player private game attributes
    List<ObjectiveRecord> objectiveCandidates;
    PlayerSecretInfoRecord playerSecretInfoRecord;





    //CONSTRUCTOR
    public ClientModel() {
        this.cardMaps = new HashMap<>();
        this.lobbyPreviewRecords = new ArrayList<>();
        this.lobbyUserRecords = new ArrayList<>();
        this.players = new ArrayList<>();
        this.winners = new ArrayList<>();
        clientState = new ConnectionState(this);
    }





    //GETTERS
    public boolean isGameOver() {return gameOver;}
    public boolean isSetUpFinished() {
        return setUpFinished;
    }
    public String getMyUsername() {return myUsername; }
    public PlayerStates getMyPlayerGameState() {return myPlayerGameState;}
    public ClientConnector getClientConnector() { return clientConnector; }
    public Map<String, CardMapRecord> getCardMaps() {
        return cardMaps;
    }
    public GameRecord getGameRecord() {
        return gameRecord;
    }
    public List<LobbyPreviewRecord> getLobbyPreviewRecords() {
        return lobbyPreviewRecords;
    }
    public List<LobbyUserRecord> getLobbyUserRecords() {
        return lobbyUserRecords;
    }
    public List<PlayerRecord> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    public PlayerSecretInfoRecord getPlayerSecretInfoRecord() {
        return playerSecretInfoRecord;
    }
    public TableRecord getTableRecord() {
        return tableRecord;
    }
    public List<PlayerRecord> getWinners() {
        return winners;
    }
    public List<ObjectiveRecord> getObjectiveCandidates() {
        return objectiveCandidates;
    }

    public boolean isConnected(){
        return this.connected;
    }
    public boolean isLoggedIn(){
        return this.loggedIn;
    }
    public boolean isInLobby() {
        return inLobby;
    }
    public boolean isGameStarted() {
        return gameStarted;
    }


    //SETTERS
    public void setMyUsername(String myUsername) {this.myUsername = myUsername;}
    public void setMyPlayerGameState(PlayerStates myPlayerGameState) {this.myPlayerGameState = myPlayerGameState;}
    public void setClientConnector(ClientConnector clientConnector) {
        this.clientConnector = clientConnector;
        Thread clientConnectorThread = new Thread(this.clientConnector);
        clientConnectorThread.start();
    }

    public void setCardMaps(Map<String, CardMapRecord> cardMaps) {
        synchronized (playerMapThreadLock) {
            this.cardMaps = cardMaps;
        }
    }
    public void setPlayers(List<PlayerRecord> players) {
        synchronized (playerMapThreadLock) {
            this.players = players;
        }
    }
    //todo fix how map keys are updated
    public void setSpecificPlayer(PlayerRecord player){
        synchronized (playerMapThreadLock) {
            PlayerRecord currentPlayer;

            for (int i = 0; i < players.size(); i++) {

                currentPlayer = players.get(i);

                if (currentPlayer.username().equals(player.username())) {
                    players.set(i, player);
                    return;
                }
            }
        }
    }
    public void setSpecificCardMap(String owner, CardMapRecord cardMap){
        synchronized (playerMapThreadLock) {
            cardMaps.put(owner, cardMap);
        }
    }


    public void setGameRecord(GameRecord gameRecord) {
        this.gameRecord = gameRecord;
    }
    public void setLobbyPreviewRecords(List<LobbyPreviewRecord> lobbyPreviewRecords) {
        this.lobbyPreviewRecords = lobbyPreviewRecords;
    }
    public void setLobbyRecord(LobbyRecord lobbyRecord) {
        this.lobbyRecord = lobbyRecord;
    }
    public void setLobbyUserRecords(List<LobbyUserRecord> lobbyUserRecords) {
        this.lobbyUserRecords = lobbyUserRecords;
    }

    public void setPlayerSecretInfoRecord(PlayerSecretInfoRecord playerSecretInfoRecord) {
        this.playerSecretInfoRecord = playerSecretInfoRecord;
    }
    public void setTableRecord(TableRecord tableRecord) {
        this.tableRecord = tableRecord;
    }
    public void setClientState(ClientState clientState) {
        this.clientState = clientState;
    }
    public void setWinners(List<PlayerRecord> winners) {
        this.winners = winners;
    }
    public void setObjectiveCandidates(List<ObjectiveRecord> objectiveCandidates) {
        this.objectiveCandidates = objectiveCandidates;
    }

    public void setSetUpFinished(boolean setUpFinished) {
        this.setUpFinished = setUpFinished;
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;}
    public void setConnected(boolean connected){
        this.connected = connected;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    public void setInLobby(boolean inLobby) {
        this.inLobby = inLobby;
    }
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }


    //METHODS
    public void nextState() {
        clientState.nextState();
    }
    public void handleInput(String input) {clientState.handleInput(input);}
    public void print() {clientState.print();}
}
