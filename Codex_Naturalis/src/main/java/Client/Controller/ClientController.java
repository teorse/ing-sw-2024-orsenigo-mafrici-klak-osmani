package Client.Controller;

import Client.Model.ClientModel;
import Client.Model.Records.*;
import Client.Model.Records.LobbyPreviewRecord;
import Client.View.TextUI;
import Model.Player.PlayerStates;
import Network.ServerClient.Packets.ErrorsDictionary;
import Network.ServerClient.ServerMessageExecutor;

import java.util.List;
import java.util.Map;

import static Client.View.UserInterface.myPlayerRecord;


public class ClientController  implements ServerMessageExecutor {
    ClientModel model;

    //TODO fix updates methods

    public ClientController(ClientModel clientModel) {
        this.model = clientModel;
    }

    public void handleInput(String input) {
        model.handleInput(input);
    }

    @Override
    public void connectionAck(String serverNotification) {
        model.setOperationSuccesful(true);
        model.nextState();
    }

    @Override
    public void loginFailed(ErrorsDictionary errorCause) {
        model.setOperationSuccesful(false);
        switch (errorCause) {
            case WRONG_PASSWORD -> System.out.println("Wrong password.");
            case USERNAME_NOT_FOUND -> System.out.println("Username not found.");
            case YOU_ARE_ALREADY_LOGGED_IN -> System.out.println("You are already logged in.");
            case ACCOUNT_ALREADY_LOGGED_IN_BY_SOMEONE_ELSE -> System.out.println("Account already logged on another computer.");
        }
        model.nextState();
    }

    @Override
    public void loginSuccess(String username) {
        model.setOperationSuccesful(true);
        model.setMyUsername(username);
        model.nextState();
    }

    @Override
    public void signUpFailed(ErrorsDictionary errorCause) {
        model.setOperationSuccesful(false);
        switch (errorCause) {
            case USERNAME_ALREADY_TAKEN -> System.out.println("Username already taken.");
            case GENERIC_ERROR -> System.out.println("Something happened in the server, please try again.");
        }
        model.nextState();
    }

    @Override
    public void signUpSuccess(String username) {
        model.setOperationSuccesful(true);
        model.setMyUsername(username);
        model.nextState();
    }

    @Override
    public void joinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {
        model.setOperationSuccesful(true);
        model.setLobbyRecord(lobbyRecord);
        model.setLobbyUserRecords(lobbyUsers);
        model.nextState();
    }

    @Override
    public void joinLobbyFailed(ErrorsDictionary errorCause) {
        model.setOperationSuccesful(false);
        switch (errorCause) {
            case LOBBY_IS_CLOSED -> System.out.println("Lobby closed.");
            case GENERIC_ERROR -> System.out.println("Generic error.");
            case LOBBY_NAME_NOT_FOUND -> System.out.println("Lobby name not found.");
        }
        model.nextState();
    }

    @Override
    public void startLobbySuccess(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {
        model.setOperationSuccesful(true);
        model.setLobbyRecord(lobbyRecord);
        model.setLobbyUserRecords(lobbyUsers);
        model.nextState();
    }

    @Override
    public void startLobbyFailed(ErrorsDictionary errorCause) {
        model.setOperationSuccesful(false);
        switch (errorCause) {
            case GENERIC_ERROR -> System.out.println("Generic error.");
            case INVALID_LOBBY_SIZE -> System.out.println("Invalid lobby size.");
            case LOBBY_NAME_ALREADY_TAKEN -> System.out.println("Lobby name already taken.");
        }
        model.nextState();
    }

    @Override
    public void updateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviewRecords) {
        model.setLobbyPreviewRecords(lobbyPreviewRecords);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        model.print();
    }

    @Override
    public void updateLobbyUsers(List<LobbyUserRecord> lobbyUsers) {
        model.setLobbyUserRecords(lobbyUsers);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        model.print();
    }

    @Override
    public void gameStarted(Map<PlayerRecord, CardMapRecord> players, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game) {
        model.setPlayerCardMapRecord(players);
        model.setPlayerSecretInfoRecord(secret);
        model.setTableRecord(table);
        model.setGameRecord(game);
        model.setMyPlayerState(PlayerStates.WAIT);
    }

    @Override
    public void updatePlayers(Map<PlayerRecord, CardMapRecord> players) {
        model.setPlayerCardMapRecord(players);
    }

    @Override
    public void updateSpecificPlayer(PlayerRecord player) {
        int i = 0;
        for(PlayerRecord playerRecord : model.getPlayerRecords()) {
            if (playerRecord.nickname().equals(player.nickname())) {
                model.getPlayerRecords().remove(i);
                model.getPlayerRecords().add(player);
            } else
                i++;
        }
    }

    @Override
    public void updateCardMap(String ownerUsername, CardMapRecord cardMap) {
        PlayerRecord playerRecord = null;
        for (PlayerRecord pr : model.getPlayerRecords()) {
            if (pr.nickname().equals(ownerUsername))
                playerRecord = pr;
        }
        model.getPlayerCardMapRecord().put(playerRecord, cardMap);
    }

    @Override
    public void updateSecret(PlayerSecretInfoRecord secret) {
        model.setPlayerSecretInfoRecord(secret);

    }

    @Override
    public void updateTable(TableRecord table) {
        model.setTableRecord(table);

    }

    @Override
    public void updateGame(GameRecord game) {
        model.setGameRecord(game);

    }

    @Override
    public void updateSecretObjectiveCandidates(List<ObjectiveRecord> candidates) {
        model.setObjectiveRecords(candidates);

    }

    @Override
    public void updateClientGameState(PlayerStates newState) {
        model.setOperationSuccesful(true);
        model.setMyPlayerState(newState);
        model.nextState();
    }

    @Override
    public void gameOver(List<PlayerRecord> players) {
        model.setOperationSuccesful(true);
        model.setWinners(players);
        model.nextState();
    }
}
