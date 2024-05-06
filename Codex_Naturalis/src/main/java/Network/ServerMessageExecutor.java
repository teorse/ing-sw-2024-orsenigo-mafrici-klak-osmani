package Network;

import Client.Model.Records.*;

import java.util.List;
import java.util.Map;

public interface ServerMessageExecutor {
    void connectionAck(ServerNotificationRecord notification);

    void loginFailed(ServerNotificationRecord notification);

    void loginSuccess(ServerNotificationRecord notification, String username);

    void updateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviewRecords);

    void joinLobbySuccessful(LobbyRecord lobbyRecord, ServerNotificationRecord notification);

    void joinLobbyFailed(ServerNotificationRecord notification);

    void updateLobbyView(LobbyRecord lobbyRecord);

    void gameStarted(Map<PlayerRecord, CardMapRecord> players, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game);

    void updatePlayers(Map<PlayerRecord, CardMapRecord> players);

    void updatePlayer(PlayerRecord player);

    void updateCardMap(String ownerUsername, CardMapRecord cardMap);

    void updateSecret(PlayerSecretInfoRecord secret);

    void updateTable(TableRecord table);

    void updateGame(GameRecord game);

    void gameOver();
}
