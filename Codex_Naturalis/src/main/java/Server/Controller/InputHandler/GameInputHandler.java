package Server.Controller.InputHandler;

import Network.ClientServerPacket.ClientServerPacket;
import Network.ClientServerPacket.CommandTypes;
import Network.ClientServerPacket.GameCommands;
import Server.Controller.GameController;
import Server.Model.Lobby.GameDemo.Player;
import Server.Model.Lobby.LobbyUser;

import java.util.List;

/**
 * The GameInputHandler class implements the InputHandler interface for handling input messages at the game level.
 * It interprets the messages received from the client and executes appropriate actions within the game.
 */
public class GameInputHandler implements InputHandler{
    //ATTRIBUTES
    private final GameController controller;
    private final Player player;





    //CONSTRUCTOR
    /**
     * Constructs a GameInputHandler object.
     *
     * @param lobbyUser  The LobbyUser associated with the player at the lobby level.
     * @param controller The GameController managing the game.
     */
    public GameInputHandler(LobbyUser lobbyUser, GameController controller){
        this.controller = controller;
        player = controller.getPlayerByLobbyUser(lobbyUser);
    }





    //INTERFACE METHODS

    /**
     * {@inheritDoc}
     * @param packet The ClientServerPacket representing the input from the client.
     */
    @Override
    public void handleInput(ClientServerPacket packet) {
        List<String> header = packet.header();
        String commandType = header.getFirst();

        //todo implement code to check if enum values are valid before the switch case or add try catch to switch cases.

        switch (CommandTypes.valueOf(commandType)){
            case SERVER , LOBBY -> throw new RuntimeException("Error, server or lobby command reached GameInputHandler. ServerMain and Lobby commands are" +
                        "not handled at this level.");

            case GAME -> {
                String command = header.get(1);
                List<String> payload = packet.payload();

                switch (GameCommands.valueOf(command)){
                    case PUBLIC -> appendPublicData(payload);
                    case PERSONAL -> appendPersonalData(payload);
                    case PERSONAL_PUBLIC -> appendPublicPersonalData(payload);
                }
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clientDisconnectionProcedure() {
        //todo implement disconnection procedure
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logOut() {
    }

    /**
     * {@inheritDoc}
     * @return true if the input handler is bound to the controller, false if the handler is not bound to anything.
     */
    @Override
    public boolean isBound() {
        return true;
    }





    //PACKET HANDLING METHODS
    private void appendPublicData(List<String> payload){

        String publicData = payload.getFirst();

        controller.appendPublicData(player, publicData);
    }

    private void appendPersonalData(List<String> payload){

        String personalData = payload.getFirst();

        controller.appendPersonalData(player, personalData);
    }

    private void appendPublicPersonalData(List<String> payload){

        String personalData = payload.getFirst();
        String publicData = payload.get(1);

        controller.appendPersonalPublicData(player, personalData, publicData);
    }
}
