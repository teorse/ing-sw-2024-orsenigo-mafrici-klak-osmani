package Client.Controller;

import Client.Model.Records.*;
import Client.Model.Records.LobbyPreviewRecord;
import Network.ServerMessageExecutor;

import java.util.List;
import java.util.Map;

public class ClientController implements ServerMessageExecutor {

    @Override
    public void connectionAck(ServerNotificationRecord notification) {
        //todo
    }

    @Override
    public void loginFailed(ServerNotificationRecord notification) {
        //todo
    }

    @Override
    public void loginSuccess(ServerNotificationRecord notification, String username) {
        //todo
    }

    @Override
    public void updateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviewRecords) {
        //todo
    }

    @Override
    public void joinLobbySuccessful(LobbyRecord lobbyRecord, ServerNotificationRecord notification) {
        //todo
    }

    @Override
    public void joinLobbyFailed(ServerNotificationRecord notification) {
        //todo
    }

    @Override
    public void updateLobbyView(LobbyRecord lobbyRecord) {
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
    public void updatePlayer(PlayerRecord player) {
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
    public void gameOver() {
        //todo
    }
}
