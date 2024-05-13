package Network;

import Client.Model.Records.*;
import Model.Player.PlayerStates;
import Network.ServerClient.Packets.ErrorsDictionary;

import java.util.List;
import java.util.Map;

public interface ServerMessageExecutor {
    void connectionAck(String serverNotification);

    void loginFailed(ErrorsDictionary errorCause);

    void loginSuccess(String username);

    void signUpFailed(ErrorsDictionary errorCause);

    void signUpSuccess(String username);

    void updateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviewRecords);

    void joinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers);

    void joinLobbyFailed(ErrorsDictionary errorCause);

    void startLobbySuccess(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers);

    void startLobbyFailed(ErrorsDictionary errorCause);

    void updateLobbyUsers(List<LobbyUserRecord> lobbyUsers);

    void gameStarted(Map<PlayerRecord, CardMapRecord> players, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game);

    void updatePlayers(Map<PlayerRecord, CardMapRecord> players);

    void updateSpecificPlayer(PlayerRecord player);

    void updateCardMap(String ownerUsername, CardMapRecord cardMap);

    void updateSecret(PlayerSecretInfoRecord secret);

    void updateTable(TableRecord table);

    void updateGame(GameRecord game);

    void updateSecretObjectiveCandidates(List<ObjectiveRecord> candidates);

    void updateClientGameState(PlayerStates newState);

    void gameOver(List<PlayerRecord> players);
}
