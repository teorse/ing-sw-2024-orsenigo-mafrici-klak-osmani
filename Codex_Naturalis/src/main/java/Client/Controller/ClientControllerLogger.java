package Client.Controller;

import Client.Model.Records.*;
import Client.Model.Records.LobbyPreviewRecord;
import Model.Player.PlayerStates;
import Network.ServerClient.Packets.ErrorsDictionary;
import Network.ServerClient.ServerMessageExecutor;

import java.util.List;
import java.util.Map;

public class ClientControllerLogger implements ServerMessageExecutor {

    @Override
    public void connectionAck(String serverNotification) {
        System.out.println("connectionAck method called.");
        System.out.println("Received serverNotification: " + serverNotification);
    }

    @Override
    public void loginFailed(ErrorsDictionary errorCause) {
        System.out.println("loginFailed method called.");
        System.out.println("Received errorCause: " + errorCause);
    }

    @Override
    public void loginSuccess(String username) {
        System.out.println("loginSuccess method called.");
        System.out.println("Received username: " + username);
    }

    @Override
    public void signUpFailed(ErrorsDictionary errorCause) {
        System.out.println("signUpFailed method called.");
        System.out.println("Received errorCause: " + errorCause);
    }

    @Override
    public void signUpSuccess(String username) {
        System.out.println("signUpSuccess method called.");
        System.out.println("Received username: " + username);
    }

    @Override
    public void updateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviewRecords) {
        System.out.println("updateLobbyPreviews method called.");
        System.out.println("Received lobbyPreviewRecords: " + lobbyPreviewRecords);
    }

    @Override
    public void joinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {
        System.out.println("joinLobbySuccessful method called.");
        System.out.println("Received lobbyRecord: " + lobbyRecord);
        System.out.println("Received lobbyUsers: " + lobbyUsers);
    }

    @Override
    public void joinLobbyFailed(ErrorsDictionary errorCause) {
        System.out.println("joinLobbyFailed method called.");
        System.out.println("Received errorCause: " + errorCause);
    }

    @Override
    public void startLobbySuccess(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {
        System.out.println("startLobbySuccess method called.");
        System.out.println("Received lobbyRecord: " + lobbyRecord);
        System.out.println("Received lobbyUsers: " + lobbyUsers);
    }

    @Override
    public void startLobbyFailed(ErrorsDictionary errorCause) {
        System.out.println("startLobbyFailed method called.");
        System.out.println("Received errorCause: " + errorCause);
    }

    @Override
    public void updateLobbyUsers(List<LobbyUserRecord> lobbyUsers) {
        System.out.println("updateLobbyUsers method called.");
        System.out.println("Received lobbyUsers: " + lobbyUsers);
    }

    @Override
    public void gameStarted(Map<PlayerRecord, CardMapRecord> players, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game) {
        System.out.println("gameStarted method called.");
        System.out.println("Received players: " + players);
        System.out.println("Received secret: " + secret);
        System.out.println("Received table: " + table);
        System.out.println("Received game: " + game);
    }

    @Override
    public void updatePlayers(Map<PlayerRecord, CardMapRecord> players) {
        System.out.println("updatePlayers method called.");
        System.out.println("Received players: " + players);
    }

    @Override
    public void updateSpecificPlayer(PlayerRecord player) {
        System.out.println("updateSpecificPlayer method called.");
        System.out.println("Received player: " + player);
    }

    @Override
    public void updateCardMap(String ownerUsername, CardMapRecord cardMap) {
        System.out.println("updateCardMap method called.");
        System.out.println("Received ownerUsername: " + ownerUsername);
        System.out.println("Received cardMap: " + cardMap);
    }

    @Override
    public void updateSecret(PlayerSecretInfoRecord secret) {
        System.out.println("updateSecret method called.");
        System.out.println("Received secret: " + secret);
    }

    @Override
    public void updateTable(TableRecord table) {
        System.out.println("updateTable method called.");
        System.out.println("Received table: " + table);
    }

    @Override
    public void updateGame(GameRecord game) {
        System.out.println("updateGame method called.");
        System.out.println("Received game: " + game);
    }

    @Override
    public void updateSecretObjectiveCandidates(List<ObjectiveRecord> candidates) {
        System.out.println("updateSecretObjectiveCandidates method called.");
        System.out.println("Received candidates: " + candidates);
    }

    @Override
    public void updateClientGameState(PlayerStates newState) {
        System.out.println("updateClientGameState method called.");
        System.out.println("Received newState: " + newState);
    }

    @Override
    public void gameOver(List<PlayerRecord> players) {
        System.out.println("gameOver method called.");
        System.out.println("Received players: " + players);
    }
}