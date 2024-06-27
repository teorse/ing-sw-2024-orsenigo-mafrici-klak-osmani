package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.Network.ClientConnector;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ConnectionState;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.*;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The ClientModel class represents the client-side model of the application.
 * It manages the state of the client, including connection status, login state, lobby state,
 * game state, and error handling. It also interacts with various parts of the application,
 * such as the network connector, view state, and game-related data.
 */
public class ClientModel extends Observable {
    // SINGLETON PATTERN
    private static ClientModel INSTANCE;

    /**
     * Retrieves the singleton instance of ClientModel.
     *
     * @return The singleton instance of ClientModel.
     */
    public synchronized static ClientModel getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ClientModel();
        }
        return INSTANCE;
    }

    private final Logger logger;
    private ClientModel(){
        logger = Logger.getLogger(ClientModel.class.getName());
        logger.info("Initializing ClientModel");

        logger.fine("Assigning view object");
        view = new ConnectionState();

        logger.fine("Printing view object");
        view.print();
    }
    private boolean fancyGraphics;


    // Internal Logic
    boolean connected;
    boolean loggedIn;
    boolean inLobby;
    boolean gameStartable;
    boolean gameStarted;
    boolean gameOver;
    ViewState view;

    // Connector
    ClientConnector clientConnector;

    // Error Management
    ErrorsDictionary logInError;
    ErrorsDictionary signUpError;
    ErrorsDictionary joinLobbyError;
    ErrorsDictionary startLobbyError;

    // GETTERS
    /**
     * Checks if the client is connected to the server.
     *
     * @return true if the client is connected, false otherwise.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Checks if the client is logged in.
     *
     * @return true if the client is logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Checks if the client is currently in a lobby.
     *
     * @return true if the client is in a lobby, false otherwise.
     */
    public boolean isInLobby() {
        return inLobby;
    }

    /**
     * Retrieves the client's network connector.
     *
     * @return The ClientConnector object associated with this client.
     */
    public ClientConnector getClientConnector() {
        return clientConnector;
    }

    /**
     * Checks if a game can be started.
     *
     * @return true if a game can be started, false otherwise.
     */
    public boolean isGameStartable() {
        return gameStartable;
    }

    /**
     * Checks if a game has started.
     *
     * @return true if a game has started, false otherwise.
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Retrieves the error encountered during the last login attempt, if any.
     *
     * @return The ErrorsDictionary object representing the login error.
     */
    public ErrorsDictionary getLogInError() {
        ErrorsDictionary error = logInError;
        logInError = null;
        return error;
    }

    /**
     * Retrieves the error encountered during the last sign-up attempt, if any.
     *
     * @return The ErrorsDictionary object representing the sign-up error.
     */
    public ErrorsDictionary getSignUpError() {
        ErrorsDictionary error = signUpError;
        signUpError = null;
        return error;
    }

    /**
     * Retrieves the error encountered during the last attempt to join a lobby, if any.
     *
     * @return The ErrorsDictionary object representing the join lobby error.
     */
    public ErrorsDictionary getJoinLobbyError() {
        ErrorsDictionary error = joinLobbyError;
        joinLobbyError = null;
        return error;
    }

    /**
     * Retrieves the error encountered during the last attempt to start a lobby, if any.
     *
     * @return The ErrorsDictionary object representing the start lobby error.
     */
    public ErrorsDictionary getStartLobbyError() {
        ErrorsDictionary error = startLobbyError;
        startLobbyError = null;
        return error;
    }

    /**
     * Retrieves the current view state of the client application.
     *
     * @return The current ViewState object representing the view state.
     */
    public ViewState getView() {
        logger.info("getView method called in ClientModel, current value of view is: "+view.getClass().getSimpleName());
        return view;
    }

    public boolean getFancyGraphics() {
        return fancyGraphics;
    }





    // SETTERS
    /**
     * Sets the connection status of the client.
     *
     * @param connected true if the client is connected, false otherwise.
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
        super.updateObservers();
    }

    /**
     * Sets the client's login state to successful.
     */
    public void setLogInSuccess() {
        this.loggedIn = true;
        super.updateObservers();
    }

    /**
     * Sets the client's login state to failed and stores the login error encountered.
     *
     * @param logInError The ErrorsDictionary object representing the login error.
     */
    public void setLogInFailed(ErrorsDictionary logInError) {
        this.loggedIn = false;
        this.logInError = logInError;
        super.updateObservers();
    }

    /**
     * Sets the client's sign-up state to successful.
     */
    public void setSignUpSuccess(){
        this.loggedIn = true;
        super.updateObservers();
    }

    /**
     * Sets the client's sign-up state to failed and stores the sign-up error encountered.
     *
     * @param signUpError The ErrorsDictionary object representing the sign-up error.
     */
    public void setSignUpFailed(ErrorsDictionary signUpError) {
        this.loggedIn = false;
        this.signUpError = signUpError;
        super.updateObservers();
    }

    /**
     * Sets the client's lobby join state to successful.
     */
    public void setJoinLobbySuccess() {
        this.inLobby = true;
        super.updateObservers();
    }

    /**
     * Sets the client's lobby join state to failed and stores the error encountered.
     *
     * @param error The ErrorsDictionary object representing the join lobby error.
     */
    public void setJoinLobbyFailed(ErrorsDictionary error) {
        this.inLobby = false;
        this.joinLobbyError = error;
        super.updateObservers();
    }

    /**
     * Sets the client's network connector.
     *
     * @param clientConnector The ClientConnector object to set.
     */
    public void setClientConnector(ClientConnector clientConnector) {
        this.clientConnector = clientConnector;
    }

    /**
     * Sets whether a game can be started.
     *
     * @param gameStartable true if a game can be started, false otherwise.
     */
    public void setGameStartable(boolean gameStartable) {
        this.gameStartable = gameStartable;
        super.updateObservers();
    }

    /**
     * Sets the client's game state to started and updates relevant game-related data.
     *
     * @param players List of PlayerRecord objects representing the players in the game.
     * @param cardMaps Map of String to CardMapRecord representing card maps of players.
     * @param secret PlayerSecretInfoRecord representing the secret information of the player.
     * @param table TableRecord representing the current state of the game table.
     * @param game GameRecord representing the overall state of the game.
     */
    public void setGameStarted(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game) {
        Players.getInstance().setPlayers(players);
        CardMaps.getInstance().setCardMaps(cardMaps);
        CardsHeld.getInstance().setCardsHeld(secret.cardsHeld(), secret.cardPlayability());
        SecretObjective.getInstance().setSecretObjective(secret.objectiveRecord());
        SharedObjectives.getInstance().setSharedObjectives(table.sharedObjectives());
        CardPools.getInstance().setCardPools(table.cardPools());
        Game.getInstance().updateGame(game);

        gameStartable = false;
        gameStarted = true;
        super.updateObservers();
    }

    /**
     * Sets whether the game is over.
     *
     * @param gameOver true if the game is over, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        logger.info("setGameOver method called with value: "+gameOver);
        this.gameOver = gameOver;
        super.updateObservers();
    }

    /**
     * Sets the view state of the client application.
     *
     * @param view The ViewState object representing the new view state to set.
     */
    public synchronized void setView(ViewState view) {
        this.view = view;
        view.update();
    }

    /**
     * Sets the error encountered during the last attempt to start a lobby.
     *
     * @param startLobbyError The ErrorsDictionary object representing the start lobby error.
     */
    public void setStartLobbyError(ErrorsDictionary startLobbyError) {
        this.startLobbyError = startLobbyError;
        super.updateObservers();
    }

    public void setFancyGraphics(boolean fancyGraphics){
        this.fancyGraphics = fancyGraphics;
    }





    //METHODS
    /**
     * Resets the connection status and login state of the client. Additionally,
     * calls {@link #quitLobby()} to ensure the client is no longer in any lobby.
     */
    public void resetConnection() {
        connected = false;
        loggedIn = false;

        quitLobby();
    }

    /**
     * Quits the current lobby. Sets {@code inLobby} to false and resets the game state
     * by calling {@link #resetGame()}.
     */
    public void quitLobby() {
        logger.info("Quitting lobby in model");
        inLobby = false;
        resetGame();
    }

    /**
     * Resets the game state variables. Sets {@code gameStartable}, {@code gameStarted},
     * and {@code gameOver} to false. Notifies observers of the state change.
     * Logs the current state of {@code gameOver} after resetting.
     */
    public void resetGame() {
        logger.info("Quitting game in model");
        gameStartable = false;
        gameStarted = false;
        gameOver = false;

        super.updateObservers();
        logger.fine("gameOver value after quitting game is: "+gameOver);
    }

    /**
     * Prints the current view state of the client application.
     * This method delegates the printing task to the associated {@link ViewState} object.
     */
    public void printView(){
        view.print();
    }
}
