package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.Network.ClientConnector;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

public class ClientModel2 extends Observable {
    //Internal Logic
    boolean connected;
    boolean loggedIn;
    boolean inLobby;
    boolean gameStartable;
    boolean gameStarted;
    boolean setUpFinished;
    boolean waitingForReconnections;
    boolean gameOver;
    ViewState view;


    //Error Managing
    //TODO when get an error delete it
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

    public boolean isGameStartable() {
        return gameStartable;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isSetUpFinished() {
        return setUpFinished;
    }

    public boolean isWaitingForReconnections() {
        return waitingForReconnections;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public ErrorsDictionary getLogInError() {
        return logInError;
    }

    public ErrorsDictionary getSignUpError() {
        return signUpError;
    }

    public ErrorsDictionary getJoinLobbyError() {
        return joinLobbyError;
    }

    public ErrorsDictionary getStartLobbyError() {
        return startLobbyError;
    }

    public ViewState getView() {
        return view;
    }



    //SETTERS
    public void setConnected(boolean connected) {
        this.connected = connected;
        super.updateObservers();
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        super.updateObservers();
    }

    public void setInLobby(boolean inLobby) {
        this.inLobby = inLobby;
        super.updateObservers();
    }

    public void setGameStartable(boolean gameStartable) {
        this.gameStartable = gameStartable;
        super.updateObservers();
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        super.updateObservers();
    }

    public void setSetUpFinished(boolean setUpFinished) {
        this.setUpFinished = setUpFinished;
        super.updateObservers();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setView(ViewState view) {
        this.view = view;
    }

    public void setWaitingForReconnections(boolean waitingForReconnections) {
        this.waitingForReconnections = waitingForReconnections;
        super.updateObservers();
    }

    public void setLogInError(ErrorsDictionary logInError) {
        this.logInError = logInError;
        super.updateObservers();
    }

    public void setSignUpError(ErrorsDictionary signUpError) {
        this.signUpError = signUpError;
        super.updateObservers();
    }

    public void setJoinLobbyError(ErrorsDictionary joinLobbyError) {
        this.joinLobbyError = joinLobbyError;
        super.updateObservers();
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

        gameOver();
    }

    public void gameOver() {
        gameStartable = false;
        gameStarted = false;
        setUpFinished = false;
        waitingForReconnections = false;
        gameOver = false;

        super.updateObservers();
    }
}
