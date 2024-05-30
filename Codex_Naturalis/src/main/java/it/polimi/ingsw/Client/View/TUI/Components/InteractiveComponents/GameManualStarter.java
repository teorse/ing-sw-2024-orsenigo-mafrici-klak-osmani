package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPStartGame;

public class GameManualStarter extends InteractiveComponent {

    @Override
    public boolean handleInput(String input) {
        if (MyPlayer.getInstance().isAdmin()) {
            if (LobbyUsers.getInstance().size() >= 2) {
                // If input is to start the game
                if (input.equalsIgnoreCase("START")) {
                    // Send packet to start the game
                    ClientModel.getInstance().getClientConnector().sendPacket(new CSPStartGame());
                    return true;
                }
                else {
                    // Prompt to start the game
                    System.out.println("\nWrong command, to start the game type START!");
                    return false;
                }
            }
            else {
                System.out.println("\nYou can't start a game on your own!");
                return false;
            }
        }
        return true;
    }

    @Override
    public String getKeyword() {
        return "gameStarter";
    }

    @Override
    public void print() {
        if(!MyPlayer.getInstance().isAdmin())
            return;

        if(LobbyUsers.getInstance().size() >= 2)
            System.out.println("If you don't want to wait anymore there are already enough players to start the game." +
                    "\nType start to start the game.");
    }
}
