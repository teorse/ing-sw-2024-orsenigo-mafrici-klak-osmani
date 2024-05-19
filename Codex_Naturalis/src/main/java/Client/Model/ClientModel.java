package Client.Model;

import Client.Model.Records.*;
import Client.Model.States.ClientState;
import Client.Model.States.ConnectionState;
import Client.Network.ClientConnector;
import Model.Player.PlayerStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ClientModel class represents the client-side data model for the game.
 * <p>
 * This class manages the state and data of the client, including player information,
 * game records, lobby details, and connection settings. It also handles the transition
 * between different client states, such as connection, game play, and waiting states.
 */
public class ClientModel {
    //ATTRIBUTES
    boolean operationSuccesful;
    boolean isGameOver;
    boolean isSetUpFinished;
    String myUsername;
    PlayerStates myPlayerState;
    ClientConnector clientConnector;
    Map<PlayerRecord, CardMapRecord> playerCardMapRecord;
    List<CardRecord> cardStarterRecords;
    GameRecord gameRecord;
    List<LobbyPreviewRecord> lobbyPreviewRecords;
    LobbyRecord lobbyRecord;
    List<LobbyUserRecord> lobbyUserRecords;
    List<ObjectiveRecord> objectiveRecords;
    List<PlayerRecord> playerRecords;
    PlayerSecretInfoRecord playerSecretInfoRecord;
    TableRecord tableRecord;
    ClientState clientState;
    List<PlayerRecord> winners;
    List<ObjectiveRecord> objectiveCandidates;


    


    //CONSTRUCTOR
    public ClientModel() {
        this.playerCardMapRecord = new HashMap<>();
        this.cardStarterRecords = new ArrayList<>();
        this.lobbyPreviewRecords = new ArrayList<>();
        this.lobbyUserRecords = new ArrayList<>();
        this.objectiveRecords = new ArrayList<>();
        this.playerRecords = new ArrayList<>();
        this.winners = new ArrayList<>();
        clientState = new ConnectionState(this);
    }





    //GETTERS
    public boolean isOperationSuccesful() {
        return operationSuccesful;
    }
    public boolean isGameOver() {return isGameOver;}
    public boolean isSetUpFinished() {
        return isSetUpFinished;
    }
    public String getMyUsername() {return myUsername; }
    public PlayerStates getMyPlayerState() {return myPlayerState;}
    public ClientConnector getClientConnector() { return clientConnector; }
    public Map<PlayerRecord, CardMapRecord> getPlayerCardMapRecord() {
        return playerCardMapRecord;
    }
    public List<CardRecord> getCardStarterRecords() {
        return cardStarterRecords;
    }
    public GameRecord getGameRecord() {
        return gameRecord;
    }
    public List<LobbyPreviewRecord> getLobbyPreviewRecords() {
        return lobbyPreviewRecords;
    }
    public LobbyRecord getLobbyRecord() {
        return lobbyRecord;
    }
    public List<LobbyUserRecord> getLobbyUserRecords() {
        return lobbyUserRecords;
    }
    public List<ObjectiveRecord> getObjectiveRecords() {
        return objectiveRecords;
    }
    public List<PlayerRecord> getPlayerRecords() {
        return playerRecords;
    }
    public PlayerSecretInfoRecord getPlayerSecretInfoRecord() {
        return playerSecretInfoRecord;
    }
    public TableRecord getTableRecord() {
        return tableRecord;
    }
    public ClientState getClientState() {
        return clientState;
    }
    public List<PlayerRecord> getWinners() {
        return winners;
    }
    public List<ObjectiveRecord> getObjectiveCandidates() {
        return objectiveCandidates;
    }

    //SETTERS
    public void setOperationSuccesful(boolean operationSuccesful) {
        this.operationSuccesful = operationSuccesful;
    }

    public void setGameOver(boolean gameOver) {isGameOver = gameOver;}

    public void setSetUpFinished(boolean setUpFinished) {
        isSetUpFinished = setUpFinished;
    }

    public void setMyUsername(String myUsername) {this.myUsername = myUsername;}

    public void setMyPlayerState(PlayerStates myPlayerState) {this.myPlayerState = myPlayerState;}

    public void setClientConnector(ClientConnector clientConnector) {
        this.clientConnector = clientConnector;
        Thread clientConnectorThread = new Thread(this.clientConnector);
        clientConnectorThread.start();
    }
    public void setPlayerCardMapRecord(Map<PlayerRecord, CardMapRecord> playerCardMapRecord) {
        this.playerCardMapRecord = playerCardMapRecord;
    }
    public void setCardStarterRecords(List<CardRecord> cardStarterRecords) {
        this.cardStarterRecords = cardStarterRecords;
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
    public void setObjectiveRecords(List<ObjectiveRecord> objectiveRecords) {
        this.objectiveRecords = objectiveRecords;
    }
    public void setPlayerRecords(List<PlayerRecord> playerRecords) {
        this.playerRecords = playerRecords;
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




    //METHODS
    public void nextState() {
        clientState.nextState();
    }
    public void handleInput(String input) {clientState.handleInput(input);}
    public void print() {clientState.print();}
}
