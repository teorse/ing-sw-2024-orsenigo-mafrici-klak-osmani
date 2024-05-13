package Server.Controller.InputHandler;

import Exceptions.Game.*;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.GameRequiredException;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.LobbyRequiredException;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.LogInRequiredException;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.MissingRequirementException;
import Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions.MultipleLobbiesException;
import Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions.MultipleLoginViolationException;
import Exceptions.Server.LobbyExceptions.InvalidLobbySettingsException;
import Exceptions.Server.LobbyExceptions.LobbyClosedException;
import Exceptions.Server.LobbyExceptions.LobbyUserAlreadyConnectedException;
import Exceptions.Server.LobbyExceptions.UnavailableLobbyUserColorException;
import Exceptions.Server.LobbyNameAlreadyTakenException;
import Exceptions.Server.LobbyNotFoundException;
import Exceptions.Server.LogInExceptions.AccountAlreadyExistsException;
import Exceptions.Server.LogInExceptions.AccountAlreadyLoggedInException;
import Exceptions.Server.LogInExceptions.AccountNotFoundException;
import Exceptions.Server.LogInExceptions.IncorrectPasswordException;
import Model.Game.CardPoolTypes;
import Network.ClientServer.Packets.ClientServerPacket;
import Network.ServerClient.Demo.SCPPrintPlaceholder;
import Server.Controller.GameController;
import Server.Controller.LobbyController;
import Server.Controller.ServerController;
import Server.Model.Lobby.LobbyUserColors;
import Server.Network.ClientHandler.ClientHandler;

public class ServerInputHandler implements ServerInputExecutor, InputHandler, GameControllerObserver{
    //ATTRIBUTES
    private final ClientHandler connection;
    private final ServerController serverController;
    private LobbyController lobbyController;
    private GameController gameController;
    private String username;

    public ServerInputHandler(ClientHandler connection, ServerController serverController){
        this.connection = connection;
        this.serverController = serverController;
        username = null;
    }





    //INPUT HANDLER METHODS
    @Override
    public void handleInput(ClientServerPacket packet) {
        packet.execute(this);
    }

    @Override
    public void clientDisconnectionProcedure() {
        if(username != null)
            serverController.userDisconnectionProcedure(username);
        if(lobbyController != null)
            lobbyController.userDisconnectionProcedure(username);
        if(gameController != null)
            gameController = null;
    }





    //EXECUTOR METHODS
    //SERVER LAYER
    @Override
    public void logIn(String username, String password) {
        try{
            if(this.username != null){
                //todo
                throw new MultipleLoginViolationException(connection, "placeholder", username, "");
            }
            this.username = serverController.login(connection, username, password);
        }
        catch(MultipleLoginViolationException e){
            connection.sendPacket(new SCPPrintPlaceholder("You are already logged in"));
        }
        catch (IncorrectPasswordException e){
            connection.sendPacket(new SCPPrintPlaceholder("Wrong password"));
        }
        catch (AccountNotFoundException e){
            connection.sendPacket(new SCPPrintPlaceholder("Username not found"));
        }
        catch (AccountAlreadyLoggedInException e){
            connection.sendPacket(new SCPPrintPlaceholder("Someone has already logged into this account"));
        }
    }

    @Override
    public void signUp(String username, String password) {
        if(this.username != null){
            connection.sendPacket(new SCPPrintPlaceholder("You are already logged in"));
            return;
        }
        try {
            this.username = serverController.signUp(connection, username, password);
        }
        catch (AccountAlreadyExistsException e){
            connection.sendPacket(new SCPPrintPlaceholder("The username you provided is already taken"));
        }
    }

    @Override
    public void logOut() {
        if(username == null)
            return;
        if(lobbyController != null){
            lobbyController.quitLobby(username);
        }
        serverController.quitLayer(username);
        lobbyController = null;
        gameController = null;
        username = null;
    }

    @Override
    public void viewLobbyPreviews() {
        try {
            if (this.username == null) {
                throw new LogInRequiredException("You need to be logged in to perform this action");
            }
            serverController.addLobbyPreviewObserver(username, connection);
        } catch (LogInRequiredException e) {
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    @Override
    public void stopViewingLobbyPreviews() {
        try {
            if (username == null) {
                throw new LogInRequiredException("You need to be logged in to perform this action");
            }
            serverController.removeLobbyPreviewObserver(username);
        }
        catch (LogInRequiredException e) {
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    @Override
    public void startLobby(String lobbyName, int lobbySize) {
        try {
            if (username == null) {
                throw new LogInRequiredException("");
            } else if (lobbyController != null) {
                throw new MultipleLobbiesException("");
            } else {
                lobbyController = serverController.createNewLobby(lobbyName, username, lobbySize, connection);
                lobbyController.addGameControllerObserver(username, this);
            }
        }
        catch (LogInRequiredException e){
            connection.sendPacket(new SCPPrintPlaceholder("You have to be logged in to perform this action"));
        }
        catch (MultipleLobbiesException e){
            connection.sendPacket(new SCPPrintPlaceholder("Can't create new lobby, you already are in a lobby"));
        }
        catch (LobbyNameAlreadyTakenException e){
            connection.sendPacket(new SCPPrintPlaceholder("The name provided for the lobby is already used"));
        }
        catch (InvalidLobbySettingsException e) {
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    @Override
    public void joinLobby(String lobbyName) {
        try {
            if (username == null) {
                throw new LogInRequiredException("");
            } else if (lobbyController != null) {
                throw new MultipleLobbiesException("");
            } else {
                lobbyController = serverController.joinLobby(lobbyName, username, connection);
                lobbyController.addGameControllerObserver(username, this);
            }
        }
        catch (LogInRequiredException e){
            connection.sendPacket(new SCPPrintPlaceholder("You have to be logged in to perform this action"));
        }
        catch (MultipleLobbiesException e){
            connection.sendPacket(new SCPPrintPlaceholder("Can't create new lobby, you already are in a lobby"));
        }
        catch (LobbyNotFoundException e){
            connection.sendPacket(new SCPPrintPlaceholder("No lobby found with that name"));
        }
        catch (LobbyUserAlreadyConnectedException e){
            connection.sendPacket(new SCPPrintPlaceholder("This account is already connected to the lobby"));
        }
        catch (LobbyClosedException e){
            connection.sendPacket(new SCPPrintPlaceholder("The lobby you are trying to join is closed"));
        }
    }



    //LOBBY LAYER
    @Override
    public void startGame() {
        try {
            if (lobbyController != null) {
                lobbyController.startGame();
            } else
                throw new LobbyRequiredException("You need to be in a Lobby to start a game");
        }
        catch (LobbyRequiredException e){
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    @Override
    public void quitLobby() {
        if(gameController != null)
            gameController = null;

        try {
            if (lobbyController != null) {
                //todo
                //lobbyController.removeGameControllerObserver(this);
                lobbyController.quitLobby(username);
                lobbyController = null;
            } else
                throw new LobbyRequiredException("You need to be in a Lobby to quit the Lobby");
        }
        catch (LobbyRequiredException e){
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    @Override
    public void changeColor(LobbyUserColors newColor) {
        try{
            if(lobbyController == null)
                throw new LobbyRequiredException("You need to be in a Lobby to change your color");

            lobbyController.changeColor(username, newColor);
        }
        catch (LobbyRequiredException | UnavailableLobbyUserColorException e){
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }



    //GAME LAYER
    @Override
    public void playCard(int cardIndex, int coordinateIndex, boolean faceUp) {
        try{
            if(username == null)
                throw new LogInRequiredException("You need to be logged in to perform this action");
            if(lobbyController == null)
                throw new LobbyRequiredException("You need to be in a lobby to perform this action");
            if(gameController == null)
                throw new GameRequiredException("The game has to already have started to perform this action");


            gameController.playCard(username, cardIndex, coordinateIndex, faceUp);
        }
        catch (MissingRequirementException | GameException e){
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    @Override
    public void drawCard(CardPoolTypes cardPoolType, int cardIndex) {
        try{
            if(username == null)
                throw new LogInRequiredException("You need to be logged in to perform this action");
            if(lobbyController == null)
                throw new LobbyRequiredException("You need to be in a lobby to perform this action");
            if(gameController == null)
                throw new GameRequiredException("The game has to already have started to perform this action");

            gameController.drawCard(username, cardPoolType, cardIndex);
        }
        catch (MissingRequirementException | GameException e){
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    @Override
    public void pickObjective(int objectiveIndex) {
        try{
            if(username == null)
                throw new LogInRequiredException("You need to be logged in to perform this action");
            if(lobbyController == null)
                throw new LobbyRequiredException("You need to be in a lobby to perform this action");
            if(gameController == null)
                throw new GameRequiredException("The game has to already have started to perform this action");

            gameController.pickPlayerObjective(username, objectiveIndex);
        }
        catch (MissingRequirementException | GameException e){
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }





    //OBSERVER
    @Override
    public void updateGameController(GameController gameController) {
        this.gameController = gameController;
    }

}
