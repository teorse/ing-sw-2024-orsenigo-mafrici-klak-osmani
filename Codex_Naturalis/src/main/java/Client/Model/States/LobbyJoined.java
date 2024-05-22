package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.LobbyUserRecord;
import Client.View.TextUI;
import Model.Player.PlayerStates;
import Network.ClientServer.Packets.CSPStartGame;
import Server.Model.Lobby.LobbyRoles;

import java.util.Scanner;

/**
 * Represents the state of a client after joining a lobby.
 * This state displays the list of users currently in the lobby along with their details.
 * If the current user is identified as the admin, they are prompted to enter "START" to start the game.
 * Handles user input to initiate the game if the current user is the admin.
 * Moves the client to the next state based on the success of the previous operation and the player's current state.
 * If the previous operation was successful and the player's state is set to "PLACE", the client transitions to the GameStarterChoice state.
 * Otherwise, an error message is displayed, and the client remains in the LobbyJoined state, prompting the user to try again.
 */
public class LobbyJoined extends ClientState{

    /**
     * Constructs a new LobbyJoined state with the specified client model.
     *
     * @param model the client model
     */
    public LobbyJoined(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        print();
    }

    /**
     * Prints the list of users currently in the lobby along with their details.
     *
     * <p>The method iterates through the list of lobby users and prints their username, role, color, and connection status.
     * Additionally, if the current user is identified as the admin, it prompts them to enter "START" to start the game.
     */
    @Override
    public void print() {
        System.out.println("List of users in the lobby: ");
        for (LobbyUserRecord user : model.getLobbyUserRecords()) {
            System.out.println("Username: " + user.username());
            System.out.println("Role: " + user.role().name());
            System.out.println("Color: " + user.color().name());
            System.out.println("Connection Status: " + user.connectionStatus() + "\n");
        }
        if (TextUI.areYouAdmin(model))
            System.out.println("You are the Admin, enter START to start the game!");
    }

    /**
     * Handles the input provided by the user to start the game.
     *
     * <p>If the current user is identified as the admin, the method checks if the input is "START" to initiate the game.
     * If the input is not "START", it prompts the user to enter "START". If the current user is not the admin, it displays
     * a message indicating that they are not authorized to start the game and should wait until the game starts.
     *
     * @param input the input provided by the user
     */
    @Override
    public void handleInput(String input) {
        if (TextUI.areYouAdmin(model)) {
           if(input.equalsIgnoreCase("START"))
               model.getClientConnector().sendPacket(new CSPStartGame());
           else
               System.out.println("To start the game enter START!");
        } else
            System.out.println("You are not the admin. Please wait until the game starts!");
    }

    /**
     * Moves the client to the next state based on the success of the previous operation and the player's current state.
     *
     * <p>If the previous operation was successful and the player's state is set to "PLACE", the client transitions to the
     * GameStarterChoice state. Otherwise, an error message is displayed, and the client remains in the current state,
     * prompting the user to try again.
     */
    @Override
    public void nextState() {
        if (model.isOperationSuccessful() && model.getMyPlayerState() == PlayerStates.PLACE) {
            model.setClientState(new GameStarterChoice(model));
        }
        else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}
