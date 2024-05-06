package Network;

import Client.Model.Records.*;

import java.util.List;
import java.util.Map;

public interface ServerMessageExecutor {
    void connectionAck(String serverNotification);

    void loginFailed(String serverNotification);

    void loginSuccess(String serverNotification, String username);

    void updateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviewRecords);

    void joinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers, String notification);

    void joinLobbyFailed(String notification);

    void updateLobbyUsers(List<LobbyUserRecord> lobbyUsers);

    void gameStarted(Map<PlayerRecord, CardMapRecord> players, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game);

    void updatePlayers(Map<PlayerRecord, CardMapRecord> players);

    void updateSpecificPlayer(PlayerRecord player);

    void updateCardMap(String ownerUsername, CardMapRecord cardMap);

    void updateSecret(PlayerSecretInfoRecord secret);

    void updateTable(TableRecord table);

    void updateGame(GameRecord game);

    void gameOver();
}
