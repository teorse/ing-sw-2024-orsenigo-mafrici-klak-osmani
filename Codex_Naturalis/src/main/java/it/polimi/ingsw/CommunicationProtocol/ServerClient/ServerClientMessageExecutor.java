package it.polimi.ingsw.CommunicationProtocol.ServerClient;

import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.*;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This interface defines the methods that a server can call to communicate with a client.
 * It includes methods for acknowledging connections, handling login and sign-up processes,
 * updating lobby information, managing game states, and handling in-game updates.
 */
public interface ServerClientMessageExecutor extends Serializable {

    /**
     * Acknowledges a successful connection to the server.
     *
     * @param serverNotification The notification message from the server.
     */
    void connectionAck(String serverNotification);

    /**
     * Notifies the client that the login attempt failed.
     *
     * @param errorCause The cause of the login failure.
     */
    void loginFailed(ErrorsDictionary errorCause);

    /**
     * Notifies the client that the login attempt was successful.
     *
     * @param username The username of the successfully logged-in client.
     */
    void loginSuccess(String username);

    /**
     * Notifies the client that the sign-up attempt failed.
     *
     * @param errorCause The cause of the sign-up failure.
     */
    void signUpFailed(ErrorsDictionary errorCause);

    /**
     * Notifies the client that the sign-up attempt was successful.
     *
     * @param username The username of the successfully signed-up client.
     */
    void signUpSuccess(String username);

    /**
     * Updates the client with the current lobby previews.
     *
     * @param lobbyPreviewRecords The list of lobby preview records.
     */
    void updateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviewRecords);

    /**
     * Notifies the client that they have successfully joined a lobby.
     *
     * @param lobbyRecord The record of the lobby.
     * @param lobbyUsers  The list of users in the lobby.
     */
    void joinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers);

    /**
     * Notifies the client that the attempt to join a lobby has failed.
     *
     * @param errorCause The cause of the failure.
     */
    void joinLobbyFailed(ErrorsDictionary errorCause);

    /**
     * Notifies the client that the lobby has successfully started.
     *
     * @param lobbyRecord The record of the lobby.
     * @param lobbyUsers  The list of users in the lobby.
     */
    void startLobbySuccess(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers);

    /**
     * Notifies the client that the attempt to start the lobby has failed.
     *
     * @param errorCause The cause of the failure.
     */
    void startLobbyFailed(ErrorsDictionary errorCause);

    /**
     * Updates the client with the current list of users in the lobby.
     *
     * @param lobbyUsers The list of lobby user records.
     */
    void updateLobbyUsers(List<LobbyUserRecord> lobbyUsers);

    /**
     * Updates the client with the current state of the lobby.
     *
     * @param lobby The record of the lobby.
     */
    void updateLobby(LobbyRecord lobby);

    /**
     * Notifies the client that the attempt to change color has failed.
     */
    void changeColorFailed();

    /**
     * Sends a chat message to the client.
     *
     * @param chatMessage The chat message record.
     */
    void receiveMessage(ChatMessageRecord chatMessage);

    /**
     * Notifies the client that the game has started.
     *
     * @param players    The list of player records.
     * @param cardMaps   The map of card maps for each player.
     * @param secret     The secret information record for the player.
     * @param table      The record of the game table.
     * @param game       The record of the game.
     */
    void gameStarted(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game);

    /**
     * Updates the client with the current list of players and their card maps.
     *
     * @param players  The list of player records.
     * @param cardMaps The map of card maps for each player.
     */
    void updatePlayers(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps);

    /**
     * Updates the client with the information of a specific player.
     *
     * @param player The record of the player.
     */
    void updateSpecificPlayer(PlayerRecord player);

    /**
     * Updates the client with the card map of a specific player.
     *
     * @param ownerUsername The username of the player whose card map is being updated.
     * @param cardMap       The card map record.
     */
    void updateCardMap(String ownerUsername, CardMapRecord cardMap);

    /**
     * Updates the client with the secret information.
     *
     * @param secret The secret information record.
     */
    void updateSecret(PlayerSecretInfoRecord secret);

    /**
     * Updates the client with the current state of the game table.
     *
     * @param table The record of the game table.
     */
    void updateTable(TableRecord table);

    /**
     * Updates the client with the current state of the game.
     *
     * @param game The record of the game.
     */
    void updateGame(GameRecord game);

    /**
     * Updates the client with the candidate secret objectives.
     *
     * @param candidates The list of candidate objective records.
     */
    void updateSecretObjectiveCandidates(List<ObjectiveRecord> candidates);

    /**
     * Updates the client with the drawability status of card pools.
     *
     * @param drawabilityMap The map of card pool types to their drawability status.
     */
    void updateCardPoolDrawability(Map<CardPoolTypes, Boolean> drawabilityMap);

    /**
     * Updates the client with the current game state for the player.
     *
     * @param newState The new state of the player.
     */
    void updateClientGameState(PlayerStates newState);

    /**
     * Notifies the client that the game is over and provides the final list of players.
     *
     * @param players The list of player records.
     */
    void gameOver(List<PlayerRecord> players);
}
