package Server.Controller.InputHandler;

import Model.Game.CardPoolTypes;
import Exceptions.Game.GameException;
import Network.ClientServerPacket.ClientServerPacket;
import Network.ClientServerPacket.CommandTypes;
import Network.ClientServerPacket.GameCommands;
import Network.ServerClientPacket.SCPPrintPlaceholder;
import Server.Controller.GameController;
import Model.Player.Player;
import Server.Model.Lobby.LobbyUser;
import Server.Network.ClientHandler.ClientHandler;

import java.util.List;

/**
 * The GameInputHandler class implements the InputHandler interface for handling input messages at the game level.
 * It interprets the messages received from the client and executes appropriate actions within the game.
 */
public class GameInputHandler implements InputHandler{
    //ATTRIBUTES
    private final ClientHandler connection;
    private final GameController controller;
    private final Player player;





    //CONSTRUCTOR
    /**
     * Constructs a GameInputHandler object.
     *
     * @param lobbyUser  The LobbyUser associated with the player at the lobby level.
     * @param controller The GameController managing the game.
     */
    public GameInputHandler(ClientHandler connection, LobbyUser lobbyUser, GameController controller){
        this.connection = connection;
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
                    case PLAY_CARD -> playCard(payload);
                    case DRAW_CARD -> drawCard(payload);
                    case PICK_OBJECTIVE -> pickObjective(payload);
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
    private void playCard(List<String> payload){

        int cardIndex = Integer.parseInt(payload.getFirst());
        int coordinateIndex = Integer.parseInt(payload.get(1));
        boolean faceUp = Boolean.parseBoolean(payload.get(2));

        try {
            controller.playCard(player, cardIndex, coordinateIndex, faceUp);
        }
        catch (GameException e){
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }


    }

    private void drawCard(List<String> payload){

        CardPoolTypes cardPoolType = CardPoolTypes.valueOf(payload.getFirst());
        int index = Integer.parseInt(payload.get(1));

        try {
            controller.drawCard(player, cardPoolType, index);
        }
        catch (GameException e){
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }

    private void pickObjective(List<String> payload){

        int objectiveIndex = Integer.parseInt(payload.getFirst());

        try {
            controller.pickPlayerObjective(player, objectiveIndex);
        }
        catch (GameException e){
            connection.sendPacket(new SCPPrintPlaceholder(e.getMessage()));
        }
    }
}
