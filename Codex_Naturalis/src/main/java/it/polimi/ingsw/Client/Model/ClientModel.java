package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.Network.ClientConnector;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ConnectionState;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.*;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ClientModel extends Observable {
    //SINGLETON PATTERN
    private static ClientModel INSTANCE;
    public static ClientModel getInstance(){
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

    //Internal Logic
    boolean connected;
    boolean loggedIn;
    boolean inLobby;
    boolean gameStartable;
    boolean gameStarted;
    boolean gameOver;
    ViewState view;

    //CONNECTOR
    ClientConnector clientConnector;

    //Error Managing
    ErrorsDictionary logInError;
    ErrorsDictionary signUpError;
    ErrorsDictionary joinLobbyError;
    ErrorsDictionary startLobbyError;





    //GETTERS
    public boolean isConnected() {
        return connected;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isInLobby() {
        return inLobby;
    }

    public ClientConnector getClientConnector() {
        return clientConnector;
    }

    public boolean isGameStartable() {
        return gameStartable;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public ErrorsDictionary getLogInError() {
        ErrorsDictionary error = logInError;
        logInError = null;
        return error;
    }

    public ErrorsDictionary getSignUpError() {
        ErrorsDictionary error = signUpError;
        signUpError = null;
        return error;
    }

    public ErrorsDictionary getJoinLobbyError() {
        ErrorsDictionary error = joinLobbyError;
        joinLobbyError = null;
        return error;
    }

    public ErrorsDictionary getStartLobbyError() {
        ErrorsDictionary error = startLobbyError;
        startLobbyError = null;
        return error;
    }

    public ViewState getView() {
        return view;
    }





    //SETTERS
    public void setConnected(boolean connected) {
        this.connected = connected;
        super.updateObservers();
    }

    public void setLogInSuccess() {
        this.loggedIn = true;
        super.updateObservers();
    }

    public void setLogInFailed(ErrorsDictionary logInError) {
        this.loggedIn = false;
        this.logInError = logInError;
        super.updateObservers();
    }

    public void setSignUpSuccess(){
        this.loggedIn = true;
        super.updateObservers();
    }

    public void setSignUpFailed(ErrorsDictionary signUpError) {
        this.loggedIn = false;
        this.signUpError = signUpError;
        super.updateObservers();
    }

    public void setJoinLobbySuccess() {
        this.inLobby = true;
        super.updateObservers();
    }

    public void setJoinLobbyFailed(ErrorsDictionary error) {
        this.inLobby = false;
        this.joinLobbyError = error;
        super.updateObservers();
    }

    public void setClientConnector(ClientConnector clientConnector) {
        this.clientConnector = clientConnector;
    }

    public void setGameStartable(boolean gameStartable) {
        this.gameStartable = gameStartable;
        super.updateObservers();
    }

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

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    //TODO add the Reset of the observer here
    public void setView(ViewState view) {
        this.view = view;
    }

    public void setStartLobbyError(ErrorsDictionary startLobbyError) {
        this.startLobbyError = startLobbyError;
        super.updateObservers();
    }





    //METHODS
    public void resetConnection() {
        connected = false;
        loggedIn = false;

        quitLobby();
    }

    public void quitLobby() {
        inLobby = false;

        resetGame();
    }

    public void resetGame() {
        gameStartable = false;
        gameStarted = false;
        gameOver = false;

        super.updateObservers();
    }
}
