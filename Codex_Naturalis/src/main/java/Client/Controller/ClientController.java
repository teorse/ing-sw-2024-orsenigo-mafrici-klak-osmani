package Client.Controller;

import Client.Model.Records.*;
import Client.Model.Records.LobbyPreviewRecord;
import Model.Player.PlayerStates;
import Network.ServerClient.Packets.ErrorsDictionary;
import Network.ServerMessageExecutor;

import java.util.List;
import java.util.Map;

public class ClientController implements ServerMessageExecutor {

    @Override
    public void connectionAck(String serverNotification) {
        //todo
    }

    @Override
    public void loginFailed(ErrorsDictionary errorCause) {
        //todo
    }

    @Override
    public void loginSuccess(String username) {
        //todo
    }

    @Override
    public void signUpFailed(ErrorsDictionary errorCause) {

    }

    @Override
    public void signUpSuccess(String username) {
        //todo
    }

    @Override
    public void updateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviewRecords) {
        //todo
    }

    @Override
    public void joinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {
        //todo
    }

    @Override
    public void joinLobbyFailed(ErrorsDictionary errorCause) {
        //todo
    }

    @Override
    public void startLobbySuccess(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {
        //todo
    }

    @Override
    public void startLobbyFailed(ErrorsDictionary errorCause) {
        //todo
    }

    @Override
    public void updateLobbyUsers(List<LobbyUserRecord> lobbyUsers) {
        //todo
    }

    @Override
    public void gameStarted(Map<PlayerRecord, CardMapRecord> players, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game) {
        //todo
    }

    @Override
    public void updatePlayers(Map<PlayerRecord, CardMapRecord> players) {
        //todo
    }

    @Override
    public void updateSpecificPlayer(PlayerRecord player) {
        //todo
    }

    @Override
    public void updateCardMap(String ownerUsername, CardMapRecord cardMap) {
        //todo
    }

    @Override
    public void updateSecret(PlayerSecretInfoRecord secret) {
        //todo
    }

    @Override
    public void updateTable(TableRecord table) {
        //todo
    }

    @Override
    public void updateGame(GameRecord game) {
        //todo
    }

    @Override
    public void updateSecretObjectiveCandidates(List<ObjectiveRecord> candidates) {
        //todo
    }

    @Override
    public void updateClientGameState(PlayerStates newState) {
        //todo
    }

    @Override
    public void gameOver(List<PlayerRecord> players) {
        //todo
    }
}
