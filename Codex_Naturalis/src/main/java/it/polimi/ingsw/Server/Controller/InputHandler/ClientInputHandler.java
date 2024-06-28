package it.polimi.ingsw.Server.Controller.InputHandler;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;
import it.polimi.ingsw.Exceptions.Game.GameException;
import it.polimi.ingsw.Exceptions.Game.Model.InvalidGameInputException;
import it.polimi.ingsw.Exceptions.Game.Model.Player.CoordinateIndexOutOfBounds;
import it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.GameRequiredException;
import it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.LobbyRequiredException;
import it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.LogInRequiredException;
import it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.MissingRequirementException;
import it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions.MultipleLobbiesException;
import it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions.MultipleLoginViolationException;
import it.polimi.ingsw.Exceptions.Server.LobbyExceptions.*;
import it.polimi.ingsw.Exceptions.Server.LobbyNameAlreadyTakenException;
import it.polimi.ingsw.Exceptions.Server.LobbyNotFoundException;
import it.polimi.ingsw.Exceptions.Server.LogInExceptions.AccountAlreadyExistsException;
import it.polimi.ingsw.Exceptions.Server.LogInExceptions.AccountAlreadyLoggedInException;
import it.polimi.ingsw.Exceptions.Server.LogInExceptions.AccountNotFoundException;
import it.polimi.ingsw.Exceptions.Server.LogInExceptions.IncorrectPasswordException;
import it.polimi.ingsw.Exceptions.Server.PermissionExceptions.AdminRoleRequiredException;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.ClientServerPacket;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Controller.LobbyController;
import it.polimi.ingsw.Server.Controller.ServerController;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;
import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandler;
import it.polimi.ingsw.Utils.Utilities;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.*;

import java.util.logging.Logger;

public class ClientInputHandler implements ClientServerMessageExecutor, InputHandler, GameControllerObserver{
    //ATTRIBUTES
    private final ClientHandler connection;
    private final ServerController serverController;
    private LobbyController lobbyController;
    private GameController gameController;
    private String username;
    private final Logger logger;

    /**
     * Constructs a ClientInputHandler with the specified connection.
     *
     * @param connection       the client handler connection
     */
    public ClientInputHandler(ClientHandler connection) {
        logger = Logger.getLogger(ClientInputHandler.class.getName());
        logger.fine("Initializing ServerInputHandler");

        this.connection = connection;
        serverController = ServerController.getInstance();
        username = null;

        logger.fine("ServerInputHandler initialized");
    }

    /**
     * Handles the input message received from the client.
     *
     * @param packet The ClientServerPacket representing the input from the client.
     */
    @Override
    public void handleInput(ClientServerPacket packet) {
        packet.execute(this);
    }

    /**
     * Handles the accidental disconnection of the client.
     */
    @Override
    public void clientDisconnectionProcedure() {
        logger.info("Client disconnection procedure initiated.");
        if (username != null) {
            logger.info("User disconnected: " + username);
            serverController.userDisconnectionProcedure(username);
        }
        if (lobbyController != null) {
            logger.info("Disconnecting from lobby: " + lobbyController.getLobbyName());
            lobbyController.userDisconnectionProcedure(username);
        }
        if (gameController != null) {
            gameController = null;
            logger.info("Game controller cleared.");
        }
    }

    /**
     * Processes a login request from the client.
     *
     * @param username The username provided by the client.
     * @param password The password provided by the client.
     */
    @Override
    public void logIn(String username, String password) {
        logger.info("LogIn message received.");
        logger.fine("LogIn message contents:\n" + "Username: " + username + "\nPassword: " + password);

        try {
            if (this.username != null) {
                throw new MultipleLoginViolationException(connection, "placeholder", username, "");
            }
            this.username = serverController.login(connection, username, password);
            connection.sendPacket(new SCPLogInSuccess(this.username));
        } catch (MultipleLoginViolationException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("MultipleLoginViolationException by: " + username + ".\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPLogInFailed(ErrorsDictionary.YOU_ARE_ALREADY_LOGGED_IN));
        } catch (IncorrectPasswordException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("IncorrectPasswordException for: " + username + ".\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPLogInFailed(ErrorsDictionary.WRONG_PASSWORD));
        } catch (AccountNotFoundException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("AccountNotFoundException for: " + username + ".\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPLogInFailed(ErrorsDictionary.USERNAME_NOT_FOUND));
        } catch (AccountAlreadyLoggedInException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("AccountAlreadyLoggedInException for: " + username + ".\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPLogInFailed(ErrorsDictionary.ACCOUNT_ALREADY_LOGGED_IN_BY_SOMEONE_ELSE));
        }
    }

    /**
     * Processes a sign-up request from the client.
     *
     * @param username The username provided by the client.
     * @param password The password provided by the client.
     */
    @Override
    public void signUp(String username, String password) {
        logger.info("SignUp message received.");
        logger.fine("SignUp message contents:\n" + "Username: " + username + "\nPassword: " + password);

        try {
            if (this.username != null) {
                throw new MultipleLoginViolationException(connection, "placeholder", username, "");
            }

            this.username = serverController.signUp(connection, username, password);
            connection.sendPacket(new SCPSignUpSuccess(this.username));
        } catch (AccountAlreadyExistsException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("AccountAlreadyExistsException by: " + username + ".\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPSignUpFailed(ErrorsDictionary.USERNAME_ALREADY_TAKEN));
        } catch (MultipleLoginViolationException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("MultipleLoginViolationException by: " + username + ".\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPSignUpFailed(ErrorsDictionary.YOU_ARE_ALREADY_LOGGED_IN));
        }
    }


    /**
     * Processes a logout request from the client.
     */
    @Override
    public void logOut() {
        logger.info("LogOut message received.");
        if (username == null)
            return;
        if (lobbyController != null) {
            lobbyController.quitLobby(username);
        }
        serverController.quitLayer(username);
        lobbyController = null;
        gameController = null;
        username = null;
        logger.info("LogOut procedure completed.");
    }

    /**
     * Handles the request to view lobby previews.
     */
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
            logger.warning("LogInRequiredException\nStacktrace:\n" + stackTraceString);
        }
    }

    /**
     * Handles the request to stop viewing lobby previews.
     */
    @Override
    public void stopViewingLobbyPreviews() {
        logger.info("StopViewingLobbyPreviews message received");

        try {
            if (username == null) {
                throw new LogInRequiredException("You need to be logged in to perform this action");
            }
            serverController.removeLobbyPreviewObserver(username);
        } catch (LogInRequiredException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LogInRequiredException\nStacktrace:\n" + stackTraceString);
        }
    }

    /**
     * Handles the request to start a new lobby.
     *
     * @param lobbyName The name of the lobby to be created.
     * @param lobbySize The size of the lobby to be created.
     */
    @Override
    public void startLobby(String lobbyName, int lobbySize) {
        logger.info("StartLobby message received");
        logger.fine("Message contents:\nLobby name: " + lobbyName + "\nLobby Size: " + lobbySize);
        try {
            if (username == null) {
                throw new LogInRequiredException("");
            } else if (lobbyController != null) {
                throw new MultipleLobbiesException("");
            } else {
                lobbyController = serverController.createNewLobby(lobbyName, username, lobbySize, connection);
                lobbyController.addGameControllerObserver(username, this);
            }
        } catch (LogInRequiredException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LogInRequiredException\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPStartLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        } catch (MultipleLobbiesException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("MultipleLobbiesException\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPStartLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        } catch (LobbyNameAlreadyTakenException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyNameAlreadyTakenException\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPStartLobbyFailed(ErrorsDictionary.LOBBY_NAME_ALREADY_TAKEN));
        } catch (InvalidLobbySettingsException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("InvalidLobbySettingsException\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPStartLobbyFailed(ErrorsDictionary.INVALID_LOBBY_SIZE));
        }
    }


    /**
     * Handles the request to join an existing lobby.
     *
     * @param lobbyName The name of the lobby to join.
     */
    @Override
    public void joinLobby(String lobbyName) {
        logger.info("Received join lobby message");
        logger.fine("Message content:\nLobby Name: " + lobbyName);

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
        } catch (LogInRequiredException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LogInRequiredException\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        } catch (MultipleLobbiesException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("MultipleLobbiesException\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        } catch (LobbyNotFoundException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyNotFoundException\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.LOBBY_NAME_NOT_FOUND));
        } catch (LobbyUserAlreadyConnectedException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyUserAlreadyConnectedException\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.GENERIC_ERROR));
        } catch (LobbyClosedException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyClosedException\nStacktrace:\n" + stackTraceString);
            connection.sendPacket(new SCPJoinLobbyFailed(ErrorsDictionary.LOBBY_IS_CLOSED));
        }
    }




    //LOBBY LAYER
    /**
     * Handles the request to start the game within the current lobby.
     * This operation requires the user to be in a lobby and to have admin privileges.
     */
    @Override
    public void startGame() {
        logger.info("Start Game message received");
        try {
            if(username == null ||username.isEmpty())
                throw new LogInRequiredException("You need to be logged in to perform this action");

            if (lobbyController != null) {
                lobbyController.startGame(username);
            } else {
                throw new LobbyRequiredException("You need to be in a Lobby to start a game");
            }
        }
        catch (LobbyRequiredException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyRequiredException\nStacktrace:\n"+stackTraceString);
        }
        catch (AdminRoleRequiredException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("AdminRoleRequiredException, "+username+" attempted to start a game while not admin\nStacktrace:\n"+stackTraceString);
        }
        catch (InvalidLobbySizeToStartGameException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("InvalidLobbySizeToStartGameException\nStacktrace:\n"+stackTraceString);
        } catch (GameAlreadyStartedException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("GameAlreadyStartedException\nStacktrace:\n"+stackTraceString);
        } catch (LogInRequiredException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LogInRequiredException\nStacktrace:\n"+stackTraceString);
        }
    }


    /**
     * Handles the request to quit the current lobby.
     * If the user is in a game associated with the lobby, it will be terminated.
     */
    @Override
    public void quitLobby() {
        logger.info("Quit Lobby message received");

        if(gameController != null)
            gameController = null;

        try {
            if (lobbyController != null) {
                lobbyController.quitLobby(username);
                lobbyController = null;
            } else {
                throw new LobbyRequiredException("You need to be in a Lobby to quit the Lobby");
            }
        }
        catch (LobbyRequiredException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("LobbyRequiredException, User is trying to quit lobby but he is not in any lobby." +
                    "\nStacktrace:\n"+stackTraceString);
        }
    }


    /**
     * Handles the request to change the user's color in the current lobby.
     *
     * @param newColor The new color chosen by the user.
     */
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
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("UnavailableLobbyUserColorException\nStacktrace:\n"+stackTraceString);
        }
    }


    /**
     * Handles the request to send a chat message.
     *
     * @param chatMessage The chat message to be sent.
     */
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
    /**
     * Handles the request to play a card in the game.
     *
     * @param cardIndex The index of the card to play.
     * @param coordinateIndex The index of the coordinate to play the card on.
     * @param faceUp Flag indicating whether the card should be played face up.
     */
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
        catch (MissingRequirementException | GameException | InvalidGameInputException e){
            //connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }


    /**
     * Handles the request to draw a card in the game.
     *
     * @param cardPoolType The type of card pool from which to draw the card.
     * @param cardIndex The index of the card to draw.
     */
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
            //connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }


    /**
     * Handles the request for a player to pick an objective in the game.
     *
     * @param objectiveIndex The index of the objective to be picked.
     */
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
            //connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }






    //OBSERVER
    /**
     * Updates the current game controller instance.
     *
     * @param gameController The updated game controller instance.
     */
    @Override
    public void updateGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
