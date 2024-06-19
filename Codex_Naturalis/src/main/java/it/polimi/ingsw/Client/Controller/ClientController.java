package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.Model.*;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.*;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The ClientController class manages client-side operations and interactions with the server.
 * <p>
 * This class implements the ServerMessageExecutor interface, allowing it to handle various
 * server responses and updates, such as login results, lobby updates, game state changes,
 * and more. The controller also interacts with the ClientModel to update the client state
 * and data based on server messages and user inputs.
 */
public class ClientController  implements ServerClientMessageExecutor {
    private final ClientModel model;
    private final Logger logger;


    public ClientController(ClientModel model) {
        this.model = model;

        logger = Logger.getLogger(ClientController.class.getName());
    }

    public void handleInput(String input) {
        model.getView().handleInput(input);
    }

    /**
     * Handles the acknowledgment of a successful connection to the server.
     * <p>
     * This method sets the operation success flag to true in the client model
     * and transitions the client to the next state.
     *
     * @param serverNotification a message from the server acknowledging the connection
     */
    @Override
    public void connectionAck(String serverNotification) {
        logger.info("connectionAck method called.");
        logger.fine("Received serverNotification: " + serverNotification);

        model.setConnected(true);
    }

    /**
     * Handles a failed login attempt.
     * <p>
     * This method sets the operation success flag to false in the client model,
     * displays an appropriate error message based on the cause of the failure,
     * and transitions the client to the next state.
     *
     * @param errorCause the reason for the login failure, represented by an ErrorsDictionary value
     */
    @Override
    public void loginFailed(ErrorsDictionary errorCause) {

        logger.info("loginFailed method called.");
        logger.fine("Received errorCause: " + errorCause);

        model.setLogInFailed(errorCause);
    }

    /**
     * Handles a successful login attempt.
     * <p>
     * This method sets the operation success flag to true in the client model,
     * updates the client model with the username of the logged-in user,
     * and transitions the client to the next state.
     *
     * @param username the username of the successfully logged-in user
     */
    @Override
    public void loginSuccess(String username) {

        logger.info("loginSuccess method called.");
        logger.fine("Received username: " + username);

        model.setLogInSuccess();
        MyPlayer.getInstance().setUsername(username);
    }

    /**
     * Handles a failed sign-up attempt.
     * <p>
     * This method sets the operation success flag to false in the client model,
     * displays an appropriate error message based on the cause of the failure,
     * and transitions the client to the next state.
     *
     * @param errorCause the reason for the sign-up failure
     */
    @Override
    public void signUpFailed(ErrorsDictionary errorCause) {

        logger.info("signUpFailed method called.");
        logger.fine("Received errorCause: " + errorCause);

        model.setSignUpFailed(errorCause);
    }

    /**
     * Handles a successful sign-up attempt.
     * <p>
     * This method sets the operation success flag to true in the client model,
     * updates the client's username, and transitions the client to the next state.
     *
     * @param username the username of the newly signed-up client
     */
    @Override
    public void signUpSuccess(String username) {

        logger.info("signUpSuccess method called.");
        logger.fine("Received username: " + username);

        model.setSignUpSuccess();
        MyPlayer.getInstance().setUsername(username);
    }

    /**
     * Handles a successful attempt to join a lobby.
     * <p>
     * This method sets the operation success flag to true in the client model,
     * updates the model with the provided lobby record and list of lobby users,
     * and transitions the client to the next state.
     *
     * @param lobbyRecord the record of the lobby the client has joined
     * @param lobbyUsers  the list of users in the lobby
     */
    @Override
    public void joinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {

        logger.info("joinLobbySuccessful method called.");
        logger.fine("Received lobbyRecord: " + lobbyRecord);
        logger.fine("Received lobbyUsers: " + lobbyUsers);

        model.setJoinLobbySuccess();
        Lobby.getInstance().setLobby(lobbyRecord);
        LobbyUsers.getInstance().setLobbyUserRecords(lobbyUsers);
    }

    /**
     * Handles a failed attempt to join a lobby.
     * <p>
     * This method sets the operation success flag to false in the client model,
     * displays an appropriate error message based on the provided error cause,
     * and transitions the client to the next state.
     *
     * @param errorCause the cause of the failure to join the lobby
     */
    @Override
    public void joinLobbyFailed(ErrorsDictionary errorCause) {

        logger.info("joinLobbyFailed method called.");
        logger.fine("Received errorCause: " + errorCause);

        model.setJoinLobbyFailed(errorCause);
    }

    /**
     * Handles a successful attempt to start a lobby.
     * <p>
     * This method sets the operation success flag to true in the client model,
     * updates the lobby record and the list of lobby users in the model,
     * and transitions the client to the next state.
     *
     * @param lobbyRecord the record of the started lobby
     * @param lobbyUsers the list of users in the lobby
     */
    @Override
    public void startLobbySuccess(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {

        logger.info("startLobbySuccess method called.");
        logger.fine("Received lobbyRecord: " + lobbyRecord);
        logger.fine("Received lobbyUsers: " + lobbyUsers);

        model.setJoinLobbySuccess();
        Lobby.getInstance().setLobby(lobbyRecord);
    }

    /**
     * Handles a failed attempt to start a lobby.
     * <p>
     * This method sets the operation success flag to false in the client model,
     * and prints an appropriate error message based on the cause of the failure.
     * It then transitions the client to the next state.
     *
     * @param errorCause the error cause indicating why the lobby start attempt failed
     */
    @Override
    public void startLobbyFailed(ErrorsDictionary errorCause) {

        logger.info("startLobbyFailed method called.");
        logger.fine("Received errorCause: " + errorCause);

        model.setJoinLobbyFailed(errorCause);
    }

    /**
     * Updates the lobby previews with new information received from the server.
     * <p>
     * This method sets the lobby preview records in the client model with the provided list of lobby preview records.
     * It then clears the command-line interface, displays the game title using the TextUI class,
     * and prints the updated lobby information using the model's print method.
     *
     * @param lobbyPreviewRecords the list of lobby preview records received from the server
     */
    @Override
    public void updateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviewRecords) {

        logger.info("updateLobbyPreviews method called.");
        logger.fine("Received lobbyPreviewRecords: " + lobbyPreviewRecords);

        LobbyPreviews.getInstance().setLobbyPreviews(lobbyPreviewRecords);
    }

    /**
     * Updates the lobby users with new information received from the server.
     * <p>
     * This method sets the lobby user records in the client model with the provided list of lobby user records.
     * It then clears the command-line interface, displays the game title using the TextUI class,
     * and prints the updated lobby information using the model's print method.
     *
     * @param lobbyUsers the list of lobby user records received from the server
     */
    @Override
    public void updateLobbyUsers(List<LobbyUserRecord> lobbyUsers) {

        logger.info("updateLobbyUsers method called.");
        logger.fine("Received lobbyUsers: " + lobbyUsers);

        LobbyUsers.getInstance().setLobbyUserRecords(lobbyUsers);
    }

    @Override
    public void updateLobby(LobbyRecord lobby) {
        logger.info("updateLobby method called.");
        logger.fine("Received lobby: " + lobby);

        Lobby.getInstance().setLobby(lobby);
    }

    @Override
    public void changeColorFailed() {
        //todo add error communication
        logger.info("changeColorFailed method called");
    }

    @Override
    public void receiveMessage(ChatMessageRecord chatMessage) {
        logger.info("receiveMessage method called.");
        logger.fine("Received newState: " + chatMessage);

        Chat.getInstance().receiveChatMessage(chatMessage);
    }

    /**
     * Signals the client that the game has started and provides initial game data.
     * <p>
     * This method updates the player card map record, player secret info record, table record, and game record
     * in the client model with the provided data. Additionally, it sets the player's state to "WAIT" to indicate
     * that the player is waiting for their turn.
     *
     * @param players the map of player records to card map records indicating each player's cards
     * @param secret the player secret info record containing the player's secret information
     * @param table the table record containing the current state of the game table
     * @param game the game record containing general information about the game
     */
    @Override
    public void gameStarted(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game) {

        logger.info("gameStarted method called.");
        logger.fine("Received players: " + players);
        logger.fine("Received secret: " + secret);
        logger.fine("Received table: " + table);
        logger.fine("Received game: " + game);

        model.setGameStarted(players, cardMaps, secret, table, game);
    }

    /**
     * Updates the player card map record in the client model with the provided data.
     * <p>
     * This method updates the mapping of player records to card map records in the client model based on the
     * provided map of players and their corresponding card map records.
     *
     * @param players a map containing player records as keys and their corresponding card map records as values
     */
    @Override
    public void updatePlayers(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps) {

        logger.info("updatePlayers method called.");
        logger.fine("Received players: " + players);

        Players.getInstance().setPlayers(players);
        CardMaps.getInstance().setCardMaps(cardMaps);
    }

    /**
     * Updates a specific player record in the client model with the provided player record data.
     * <p>
     * This method searches for the player record with the same username as the provided player record
     * within the list of player records in the client model. If found, it replaces the existing player record
     * with the provided player record data.
     *
     * @param player the player record containing the updated data
     */
    @Override
    public void updateSpecificPlayer(PlayerRecord player) {

        logger.info("updateSpecificPlayer method called.");
        logger.fine("Received player: " + player);

        Players.getInstance().setSpecificPlayer(player);
    }

    /**
     * Updates the card map record associated with a specific player in the client model.
     * <p>
     * This method retrieves the player record with the provided owner username from the list of player records
     * in the client model. Once the player record is found, it updates the corresponding card map record with
     * the provided card map data.
     *
     * @param ownerUsername the username of the player whose card map record is to be updated
     * @param cardMap the card map record containing the updated data
     */
    @Override
    public void updateCardMap(String ownerUsername, CardMapRecord cardMap) {

        logger.info("updateCardMap method called.");
        logger.fine("Received ownerUsername: " + ownerUsername);
        logger.fine("Received cardMap: " + cardMap);

        CardMaps.getInstance().setSpecificCardMap(ownerUsername, cardMap);
    }

    /**
     * Updates the secret information record of the player in the client model.
     * <p>
     * This method updates the secret information record of the player in the client model with the provided data.
     *
     * @param secret the player's secret information record to be updated
     */
    @Override
    public void updateSecret(PlayerSecretInfoRecord secret) {

        logger.info("updateSecret method called.");
        logger.fine("Received secret: " + secret);

        CardsHeld.getInstance().setCardsHeld(secret.cardsHeld(), secret.cardPlayability());
        SecretObjective.getInstance().setSecretObjective(secret.objectiveRecord());
    }

    /**
     * Updates the table record in the client model.
     * <p>
     * This method updates the table record in the client model with the provided data.
     *
     * @param table the table record to be updated
     */
    @Override
    public void updateTable(TableRecord table) {

        logger.info("updateTable method called.");
        logger.fine("Received table: " + table);

        SharedObjectives.getInstance().setSharedObjectives(table.sharedObjectives());
        CardPools.getInstance().setCardPools(table.cardPools());
    }

    /**
     * Updates the game record in the client model.
     * <p>
     * This method updates the game record in the client model with the provided data.
     *
     * @param game the game record to be updated
     */
    @Override
    public void updateGame(GameRecord game) {

        logger.info("updateGame method called.");
        logger.fine("Received game: " + game);

        Game.getInstance().updateGame(game);
    }

    /**
     * Updates the list of secret objective candidates in the client model.
     * <p>
     * This method updates the list of secret objective candidates in the client model with the provided data.
     *
     * @param candidates the list of secret objective candidates to be updated
     */
    @Override
    public void updateSecretObjectiveCandidates(List<ObjectiveRecord> candidates) {

        logger.info("updateSecretObjectiveCandidates method called.");
        logger.fine("Received candidates: " + candidates);

        ObjectiveCandidates.getInstance().setCandidates(candidates);
    }

    @Override
    public void updateCardPoolDrawability(Map<CardPoolTypes, Boolean> cardPoolDrawability) {
        CardPools.getInstance().setCardPoolDrawability(cardPoolDrawability);
    }

    /**
     * Updates the client game state in the client model.
     * <p>
     * This method updates the client game state in the client model with the provided new state.
     * Additionally, it sets the operation successful flag to true and proceeds to the next state.
     *
     * @param newState the new game state to be updated
     */
    @Override
    public void updateClientGameState(PlayerStates newState) {

        logger.info("updateClientGameState method called.");
        logger.fine("Received newState: " + newState);

        MyPlayer.getInstance().setMyPlayerGameState(newState);
    }

    /**
     * Notifies the client that the game is over.
     * <p>
     * This method updates the client model to mark the operation as successful,
     * sets the winners of the game, and proceeds to the next state.
     *
     * @param players the list of players who won the game
     */
    @Override
    public void gameOver(List<PlayerRecord> players) {

        logger.info("gameOver method called.");
        logger.fine("Received players: " + players);

        model.setGameOver(true);
        GameOver.getInstance().setWinners(players);
    }
}
