package Server.Controller.InputHandler;

import Network.ClientServerPacket.ClientServerPacket;
import Network.ClientServerPacket.CommandTypes;
import Network.ClientServerPacket.LobbyCommands;
import Network.ServerClientPacket.SCPPrintPlaceholder;
import Server.Controller.GameController;
import Server.Controller.InputHandler.Exceptions.NotInGameException;
import Server.Controller.InputHandler.Exceptions.WrongServerLayerException;
import Server.Controller.LobbyController;
import Server.Model.Lobby.LobbyUser;
import Server.Model.ServerUser;
import Server.Network.ClientHandler.ClientHandler;

import java.util.List;

/**
 * The LobbyInputHandler class implements the InputHandler interface for handling input messages at the lobby level.
 * It interprets the messages received from the client and executes appropriate actions within the lobby.
 */
public class LobbyInputHandler implements InputHandler{
    //ATTRIBUTES
    private final ClientHandler connection;
    private LobbyController lobbyController;
    private LobbyUser lobbyUser;
    private InputHandler gameInputHandler;





    //CONSTRUCTOR
    /**
     * Constructs a LobbyInputHandler object.
     *
     * @param connection    The ClientHandler associated with the client.
     * @param lobbyController The LobbyController managing the lobby.
     * @param serverUser    The ServerUser associated with the client.
     */
    public LobbyInputHandler(ClientHandler connection, LobbyController lobbyController, ServerUser serverUser){
        this.connection = connection;
        this.lobbyController = lobbyController;

        lobbyUser = lobbyController.getLobbyUserByServerUser(serverUser);

        GameController gameController = lobbyController.getGameController();
        lobbyController.addGameControllerObserver(this);

        if(gameController != null)
            gameInputHandler = new GameInputHandler(this.connection, lobbyUser, gameController);

        else
            gameInputHandler = null;
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
            case GAME -> {
                try {
                    if (gameInputHandler == null || !gameInputHandler.isBound()) {
                        throw new NotInGameException("You have to be in a game to perform this action");
                    } else {
                        gameInputHandler.handleInput(message);
                    }
                }
                catch (NotInGameException e){
                    connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
                }
            }

            case SERVER -> {
                try {
                    throw new WrongServerLayerException("Lobby", "Server", "This input was not supposed to reach this level" +
                            "of input handler");
                }
                catch (WrongServerLayerException e){
                    connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
                }
            }

            case LOBBY -> {
                String command = header.get(1);
                List<String> payload = message.payload();

                switch (LobbyCommands.valueOf(command)){
                    case START_GAME -> startGame();
                    case QUIT -> logOut();

                    case COMMAND1 -> Command1();
                    case COMMAND2 -> Command2();
                    case COMMAND3 -> Command3();
                }
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clientDisconnectionProcedure(){
        lobbyController.removeGameControllerObserver(this);
        lobbyController.userDisconnectionProcedure(lobbyUser);
        if(gameInputHandler != null){
            gameInputHandler.clientDisconnectionProcedure();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logOut(){
        if(lobbyController != null) {
            lobbyController.removeGameControllerObserver(this);
            lobbyController.quitLobby(lobbyUser);
            lobbyController = null;
        }
        if(gameInputHandler != null)
            gameInputHandler = null;

        if(lobbyUser != null)
            lobbyUser = null;
    }

    /**
     * {@inheritDoc}
     *
     * @return true if the input handler is bound to the controller, false if the handler is not bound to anything.
     */
    @Override
    public boolean isBound() {
        return lobbyController != null || lobbyUser != null;
    }





    //PACKET HANDLING METHODS
    private void startGame(){
        lobbyController.startGame();
    }

    private void Command1(){
        lobbyController.Command1();
    }

    private void Command2(){
        lobbyController.Command2();
    }

    private void Command3(){
        lobbyController.Command3();
    }





    //OBSERVER PATTERN
    /**
     * Updates the game controller associated with the lobby input handler.<br>
     * Updating the game controller means creating a new game input handler
     *
     * @param gameController The new GameController instance.
     */
    public void updateGameController(GameController gameController){
        this.gameInputHandler = new GameInputHandler(connection, lobbyUser, gameController);
    }
}
