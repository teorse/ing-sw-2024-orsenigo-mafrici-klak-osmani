package Server.Controller.InputHandler;

import Exceptions.Server.LobbyNameAlreadyTakenException;
import Exceptions.Server.LobbyNotFoundException;
import Network.ClientServerPacket.ClientServerPacket;
import Network.ClientServerPacket.CommandTypes;
import Network.ClientServerPacket.ServerCommands;
import Network.ServerClientPacket.Demo.SCPPrintPlaceholder;
import Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions.MultipleLobbiesException;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.LogInRequiredException;
import Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions.MultipleLoginViolationException;
import Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions.LobbyRequiredException;
import Server.Controller.LobbyController;
import Server.Controller.ServerController;
import Exceptions.Server.LogInExceptions.AccountAlreadyExistsException;
import Exceptions.Server.LogInExceptions.AccountAlreadyLoggedInException;
import Exceptions.Server.LogInExceptions.AccountNotFoundException;
import Exceptions.Server.LogInExceptions.IncorrectPasswordException;
import Exceptions.Server.LobbyExceptions.InvalidLobbySettingsException;
import Exceptions.Server.LobbyExceptions.LobbyClosedException;
import Exceptions.Server.LobbyExceptions.LobbyUserAlreadyConnectedException;
import Server.Network.ClientHandler.ClientHandler;

import java.util.List;

/**
 * The ServerInputHandler class implements the InputHandler interface for handling input messages at the server layer.
 * It interprets the messages received from the client and executes appropriate actions at the server level.
 */
public class ServerInputHandler implements InputHandler{
    //ATTRIBUTES
    private final ClientHandler connection;
    private String username;
    private final ServerController serverController;
    private InputHandler lobbyInputHandler;
    private boolean loggedIn;





    //CONSTRUCTOR
    /**
     * Constructs a ServerInputHandler object.
     *
     * @param serverController The ServerController managing the server.
     * @param connection       The ClientHandler associated with the client.
     */
    public ServerInputHandler(ServerController serverController, ClientHandler connection){
        this.serverController = serverController;
        this.connection = connection;
        lobbyInputHandler = null;
        loggedIn = false;
    }





    //INTERFACE METHODS

    /**
     * {@inheritDoc}
     * @param message The ClientServerPacket representing the input from the client.
     */
    @Override
    public void handleInput(ClientServerPacket message) {
        List<String> header = message.header();
        String commandType = header.getFirst();

        switch (CommandTypes.valueOf(commandType)){
            case LOBBY, GAME -> {
                try {
                    if (!loggedIn) {
                        throw new LogInRequiredException("You need to be logged in to perform this action");
                    }
                    else if (lobbyInputHandler == null || !lobbyInputHandler.isBound()) {
                        throw new LobbyRequiredException("You need to be in a Lobby to perform this action");
                    }
                    else
                        lobbyInputHandler.handleInput(message);
                }
                catch (LogInRequiredException | LobbyRequiredException e){
                    connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
                }
            }

            case SERVER -> {
                String command = header.get(1);
                List<String> payload = message.payload();

                switch (ServerCommands.valueOf(command)){
                    case START_LOBBY -> {
                        String lobbyName = payload.getFirst();
                        int targetNumberUsers = Integer.parseInt(payload.get(1));
                        createNewLobby(lobbyName, targetNumberUsers);
                    }

                    case JOIN_LOBBY -> {
                        String lobbyName = payload.getFirst();
                        joinLobby(lobbyName);
                    }

                    case SIGN_UP -> {
                        String username = payload.getFirst();
                        String password = payload.get(1);
                        signUp(username, password);
                    }

                    case LOG_IN -> {
                        String username = payload.getFirst();
                        String password = payload.get(1);
                        login(username, password);
                    }

                    case LOG_OUT -> logOut();

                    case VIEW_LOBBY_PREVIEWS -> viewLobbyPreviews();

                    case STOP_VIEWING_LOBBY_PREVIEWS -> stopViewingLobbyPreviews();
                }
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clientDisconnectionProcedure() {
        if(!loggedIn)
            return;

        if(lobbyInputHandler != null){
            lobbyInputHandler.clientDisconnectionProcedure();
        }
        serverController.userDisconnectionProcedure(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logOut(){
        if(!loggedIn)
            return;
        if(lobbyInputHandler != null){
            lobbyInputHandler.logOut();
        }
        serverController.quitLayer(username);
        lobbyInputHandler = null;
        loggedIn = false;
        username = null;
    }

    /**
     * {@inheritDoc}
     * @return true if the input handler is bound to the controller, false if the handler is not bound to anything.
     */
    @Override
    public boolean isBound() {
        return false;
    }





    //PACKET HANDLING METHODS
    public void signUp(String username, String password){
        if(loggedIn){
            connection.sendPacket(new SCPPrintPlaceholder("You are already logged in"));
            return;
        }
        try {
             this.username = serverController.signUp(connection, username, password);
             loggedIn = true;
        }
        catch (AccountAlreadyExistsException e){
            connection.sendPacket(new SCPPrintPlaceholder("The username you provided is already taken"));
            loggedIn = false;
        }
    }

    public void login(String username, String password){
        try{
            if(loggedIn){
                //todo
                throw new MultipleLoginViolationException(connection, "placeholder", username, "");
            }
            this.username = serverController.login(connection, username, password);
            loggedIn = true;
        }
        catch(MultipleLoginViolationException e){
            connection.sendPacket(new SCPPrintPlaceholder("You are already logged in"));
            loggedIn = false;
        }
        catch (IncorrectPasswordException e){
            connection.sendPacket(new SCPPrintPlaceholder("Wrong password"));
            loggedIn = false;
        }
        catch (AccountNotFoundException e){
            connection.sendPacket(new SCPPrintPlaceholder("Username not found"));
            loggedIn = false;
        }
        catch (AccountAlreadyLoggedInException e){
            connection.sendPacket(new SCPPrintPlaceholder("Someone has already logged into this account"));
            loggedIn = false;
        }
    }

    private void createNewLobby(String lobbyName, int targetNumberUsers){
        try {
            if (!loggedIn) {
                throw new LogInRequiredException("");
            } else if (lobbyInputHandler != null && lobbyInputHandler.isBound()) {
                throw new MultipleLobbiesException("");
            } else {
                LobbyController lobbyController = serverController.createNewLobby(lobbyName, username, targetNumberUsers, connection);
                lobbyInputHandler = new LobbyInputHandler(connection, username, lobbyController);
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

    private void joinLobby(String lobbyName){
        try {
            if (!loggedIn) {
                throw new LogInRequiredException("");
            } else if (lobbyInputHandler != null && lobbyInputHandler.isBound()) {
                throw new MultipleLobbiesException("");
            } else {
                LobbyController lobbyController = serverController.joinLobby(lobbyName, username, connection);
                lobbyInputHandler = new LobbyInputHandler(connection, username, lobbyController);
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

    private void viewLobbyPreviews() {
        try {
            if (!loggedIn) {
                throw new LogInRequiredException("You need to be logged in to perform this action");
            }
            serverController.addLobbyPreviewObserver(username, connection);
        } catch (LogInRequiredException e) {
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    private void stopViewingLobbyPreviews() {
        try {
            if (!loggedIn) {
                throw new LogInRequiredException("You need to be logged in to perform this action");
            }
            serverController.removeLobbyPreviewObserver(username);
        }
        catch (LogInRequiredException e) {
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }
}
