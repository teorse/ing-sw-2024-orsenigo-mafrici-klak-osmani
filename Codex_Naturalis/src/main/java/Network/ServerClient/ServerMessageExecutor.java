package Network.ServerClient;

import Client.Model.Records.*;
import Model.Player.PlayerStates;
import Network.ServerClient.Packets.ErrorsDictionary;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ServerMessageExecutor extends Serializable {
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

    void updateLobby(LobbyRecord lobby);

    void changeColorFailed();

    void receiveMessage(ChatMessageRecord chatMessage);

    void gameStarted(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game);

    void updatePlayers(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps);

    void updateSpecificPlayer(PlayerRecord player);

    void updateCardMap(String ownerUsername, CardMapRecord cardMap);

    void updateSecret(PlayerSecretInfoRecord secret);

    void updateTable(TableRecord table);

    void updateGame(GameRecord game);

    void updateSecretObjectiveCandidates(List<ObjectiveRecord> candidates);

    void updateCardPoolDrawability(CardPoolDrawabilityRecord cardPoolDrawability);

    void updateClientGameState(PlayerStates newState);

    void gameOver(List<PlayerRecord> players);
}
