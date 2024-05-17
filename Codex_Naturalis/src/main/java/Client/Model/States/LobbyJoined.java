package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.LobbyUserRecord;
import Client.View.TextUI;
import Model.Player.PlayerStates;
import Network.ClientServer.Packets.CSPStartGame;
import Server.Model.Lobby.LobbyRoles;

import java.util.Scanner;

public class LobbyJoined extends ClientState{
    public LobbyJoined(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        print();
    }

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

    @Override
    public void nextState() {
        if (model.isOperationSuccesful() && model.getMyPlayerState() == PlayerStates.PLACE) {
            model.setClientState(new GameStarterChoice(model));
        }
        else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}
