package Server.Controller.InputHandler;

import Client.Model.Records.ChatMessageRecord;
import Exceptions.Game.*;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.GameRequiredException;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.LobbyRequiredException;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.LogInRequiredException;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.MissingRequirementException;
import Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions.MultipleLobbiesException;
import Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions.MultipleLoginViolationException;
import Exceptions.Server.LobbyExceptions.*;
import Exceptions.Server.LobbyNameAlreadyTakenException;
import Exceptions.Server.LobbyNotFoundException;
import Exceptions.Server.LogInExceptions.AccountAlreadyExistsException;
import Exceptions.Server.LogInExceptions.AccountAlreadyLoggedInException;
import Exceptions.Server.LogInExceptions.AccountNotFoundException;
import Exceptions.Server.LogInExceptions.IncorrectPasswordException;
import Exceptions.Server.PermissionExceptions.AdminRoleRequiredException;
import Server.Model.Game.Game.CardPoolTypes;
import Network.ClientServer.Packets.ClientServerPacket;
import Network.ServerClient.Packets.*;
import Server.Controller.GameController;
import Server.Controller.LobbyController;
import Server.Controller.ServerController;
import Server.Model.Lobby.LobbyUserColors;
import Server.Network.ClientHandler.ClientHandler;
import Utils.Utilities;

import java.util.logging.Logger;

public class ServerInputHandler implements ServerInputExecutor, InputHandler, GameControllerObserver{
    //ATTRIBUTES
    private final ClientHandler connection;
    private final ServerController serverController;
    private LobbyController lobbyController;
    private GameController gameController;
    private String username;
    private final Logger logger;

    public ServerInputHandler(ClientHandler connection, ServerController serverController){
        logger = Logger.getLogger(ServerInputHandler.class.getName());
        logger.fine("Initializing ServerInputHandler");


        this.connection = connection;
        this.serverController = serverController;
        username = null;

        logger.fine("ServerInputHandler initialized");
    }





    //INPUT HANDLER METHODS
    @Override
    public void handleInput(ClientServerPacket packet) {
        packet.execute(this);
    }

    @Override
    public void clientDisconnectionProcedure() {
        logger.info("Client disconnection procedure initiated.");
        if(username != null){
            logger.info("User disconnected: " + username);
            serverController.userDisconnectionProcedure(username);
            }
        if(lobbyController != null) {
            logger.info("Disconnecting from lobby: " + lobbyController.getLobbyName());
            lobbyController.userDisconnectionProcedure(username);
        }
        if(gameController != null) {
            gameController = null;
            logger.info("Game controller cleared.");
        }
    }





    //EXECUTOR METHODS
    //SERVER LAYER
    @Override
    public void logIn(String username, String password) {
        logger.info("LogIn message received.");
        logger.fine("LogIn message contents:\n"+"Username: "+username+"\nPassword: "+password);

        try{
            if(this.username != null){
                //todo
                throw new MultipleLoginViolationException(connection, "placeholder", username, "");
            }
            this.username = serverController.login(connection, username, password);
            connection.sendPacket(new SCPLogInSuccess(this.username));
        }
        catch(MultipleLoginViolationException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("MultipleLoginViolationException by: "+username+".\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPLogInFailed(ErrorsDictionary.YOU_ARE_ALREADY_LOGGED_IN));
        }
        catch (IncorrectPasswordException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("IncorrectPasswordException for: "+username+".\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPLogInFailed(ErrorsDictionary.WRONG_PASSWORD));
        }
        catch (AccountNotFoundException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("AccountNotFoundException for: "+username+".\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPLogInFailed(ErrorsDictionary.USERNAME_NOT_FOUND));
        }
        catch (AccountAlreadyLoggedInException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("AccountAlreadyLoggedInException for: "+username+".\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPLogInFailed(ErrorsDictionary.ACCOUNT_ALREADY_LOGGED_IN_BY_SOMEONE_ELSE));
        }
    }

    @Override
    public void signUp(String username, String password) {
        logger.info("SignUp message received.");
        logger.fine("SignUp message contents:\n"+"Username: "+username+"\nPassword: "+password);

        try {
            if(this.username != null){
                //todo
                throw new MultipleLoginViolationException(connection, "placeholder", username, "");
            }

            this.username = serverController.signUp(connection, username, password);
            connection.sendPacket(new SCPSignUpSuccess(this.username));
        }
        catch (AccountAlreadyExistsException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("AccountAlreadyExistsException by: "+username+".\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPSignUpFailed(ErrorsDictionary.USERNAME_ALREADY_TAKEN));
        }
        catch(MultipleLoginViolationException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("MultipleLoginViolationException by: "+username+".\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPSignUpFailed(ErrorsDictionary.YOU_ARE_ALREADY_LOGGED_IN));
        }
    }

    @Override
    public void logOut() {
        logger.info("LogOut message received.");
        if(username == null)
            return;
        if(lobbyController != null){
            lobbyController.quitLobby(username);
        }
        serverController.quitLayer(username);
        lobbyController = null;
        gameController = null;
        username = null;
        logger.info("LogOut procedure completed.");
    }

    @Override
    public void viewLobbyPreviews() {
        logger.info("ViewLobbyPreviews message received");

        try {
            if (this.username == null) {
                throw new LogInRequiredException("You need to be logged in to perform this action");
            }
            serverController.addLobbyPreviewObserver(username, connection);
        } catch (LogInRequiredException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LogInRequiredException\nStacktrace:\n"+stackTraceString);
        }
    }

    @Override
    public void stopViewingLobbyPreviews() {
        logger.info("StopViewingLobbyPreviews Message received");

        try {
            if (username == null) {
                throw new LogInRequiredException("You need to be logged in to perform this action");
            }
            serverController.removeLobbyPreviewObserver(username);
        }
        catch (LogInRequiredException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LogInRequiredException\nStacktrace:\n"+stackTraceString);
        }
    }

    @Override
    public void startLobby(String lobbyName, int lobbySize) {
        logger.info("StartLobby message received");
        logger.fine("Message contents:\nLobby name: "+lobbyName+"\nLobby Size: "+lobbySize);
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
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LogInRequiredException\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPStartLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        }
        catch (MultipleLobbiesException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("MultipleLobbiesException\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPStartLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        }
        catch (LobbyNameAlreadyTakenException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyNameAlreadyTakenException\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPStartLobbyFailed(ErrorsDictionary.LOBBY_NAME_ALREADY_TAKEN));
        }
        catch (InvalidLobbySettingsException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("InvalidLobbySettingsException\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPStartLobbyFailed(ErrorsDictionary.INVALID_LOBBY_SIZE));
        }
    }

    @Override
    public void joinLobby(String lobbyName) {
        logger.info("Received join lobby message");
        logger.fine("Message content:\nLobby Name: "+lobbyName);

        try {
            if (username == null) {
                throw new LogInRequiredException("");
            } else if (lobbyController != null) {
                throw new MultipleLobbiesException("");
            } else {
                lobbyController = serverController.joinLobby(lobbyName, username, connection);
                gameController = lobbyController.getGameController();
                lobbyController.addGameControllerObserver(username, this);
            }
        }
        catch (LogInRequiredException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LogInRequiredException\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        }
        catch (MultipleLobbiesException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("MultipleLobbiesException\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        }
        catch (LobbyNotFoundException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyNotFoundException\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.LOBBY_NAME_NOT_FOUND));
        }
        catch (LobbyUserAlreadyConnectedException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyUserAlreadyConnectedException\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        }
        catch (LobbyClosedException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyClosedException\nStacktrace:\n"+stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.LOBBY_IS_CLOSED));
        }
    }



    //LOBBY LAYER
    @Override
    public void startGame() {
        logger.info("Start Game message received");
        try {
            if (lobbyController != null) {
                lobbyController.startGame(username);
            } else
                throw new LobbyRequiredException("You need to be in a Lobby to start a game");
        }
        catch (LobbyRequiredException e){
            //todo
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyRequiredException\nStacktrace:\n"+stackTraceString);
        }
        catch (AdminRoleRequiredException e) {
            //todo
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("AdminRoleRequiredException, "+username+" attempted to start a game while not admin\nStacktrace:\n"+stackTraceString);
        }
        catch (InvalidLobbySizeToStartGameException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("InvalidLobbySizeToStartGameException\nStacktrace:\n"+stackTraceString);
        }
    }

    @Override
    public void quitLobby() {
        logger.info("Quit Lobby message received");

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
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyRequiredException, User is trying to quit lobby but he is not in any lobby." +
                    "\nStacktrace:\n"+stackTraceString);
            //todo
            //connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    @Override
    public void changeColor(LobbyUserColors newColor) {
        logger.info("Change color message received");
        logger.fine("Message contents:\nNew Color: "+newColor);
        try{
            if(lobbyController == null)
                throw new LobbyRequiredException("You need to be in a Lobby to change your color");

            lobbyController.changeColor(username, newColor);
        }
        catch (LobbyRequiredException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyRequiredException\nStacktrace:\n"+stackTraceString);
        }
        catch (UnavailableLobbyUserColorException e){
            //todo add change color failed message
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("UnavailableLobbyUserColorException\nStacktrace:\n"+stackTraceString);
        }
    }

    @Override
    public void sendChatMessage(ChatMessageRecord chatMessage) {
        logger.info("sendChatMessage message received from user "+username);
        logger.fine("Message contents:\nContent: "+chatMessage.getMessage()+
                "\nPrivate: "+chatMessage.isMessagePrivate()+
                "\nRecipient: "+chatMessage.getRecipient());

        try {
            if(username == null)
                throw new LogInRequiredException("You need to be logged in to perform this action");

            if(lobbyController == null)
                throw new LobbyRequiredException("You need to be in a Lobby to start a game");

            chatMessage.setSender(username);
            lobbyController.sendChatMessage(chatMessage);
        } catch (LogInRequiredException e) {
            String stackTrace = Utilities.StackTraceToString(e);
            logger.info("Client Handler "+this+" attempted to send a chat message before they were logged in.\nStackTrace:\n"+stackTrace);
        } catch (LobbyRequiredException e) {
            String stackTrace = Utilities.StackTraceToString(e);
            logger.info("User "+username+" attempted to send a chat message before joining a lobby.\nStackTrace:\n"+stackTrace);
        } catch (NoSuchRecipientException | InvalidRecipientException e) {
            String stackTrace = Utilities.StackTraceToString(e);
            logger.info("Exception caught by Server Input Handler while sending a chat message from user "+username+".\nMessage:\n"+e.getMessage()+"\nStackTrace:\n"+stackTrace);
        }
    }


    //GAME LAYER
    @Override
    public void playCard(int cardIndex, int coordinateIndex, boolean faceUp) {
        logger.info("Play Card message received");
        logger.fine("Message contents:\nCardIndex: "+cardIndex+"\nCoordinateIndex: "+coordinateIndex+"\nFaceUp: "+faceUp);
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
            //todo
            //connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    @Override
    public void drawCard(CardPoolTypes cardPoolType, int cardIndex) {
        logger.info("Draw Card message received");
        logger.fine("Message Contents:\nCardPoolType: "+cardPoolType+"\nCard Index: "+cardIndex);
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
            //todo
            //connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
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
            //todo
            //connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }





    //OBSERVER
    @Override
    public void updateGameController(GameController gameController) {
        this.gameController = gameController;
    }

}
