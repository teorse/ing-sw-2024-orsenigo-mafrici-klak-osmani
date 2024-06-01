package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPStartGame;

public class GameManualStarter extends InteractiveComponent {

    public GameManualStarter() {

    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (MyPlayer.getInstance().isAdmin()) {
            if (LobbyUsers.getInstance().size() >= 2) {
                // If input is to start the game
                if (input.equalsIgnoreCase("START")) {
                    // Send packet to start the game
                    ClientModel.getInstance().getClientConnector().sendPacket(new CSPStartGame());
                    return InteractiveComponentReturns.COMPLETE;
                }
                else {
                    // Prompt to start the game
                    //TODO system out 1 in GameManualStarter
                    System.out.println("\nWrong command, to start the game type START!");
                    return InteractiveComponentReturns.INCOMPLETE;
                }
            }
            else {
                //TODO system out 2 in GameManualStarter
                System.out.println("\nYou can't start a game on your own!");
                return InteractiveComponentReturns.INCOMPLETE;
            }
        }
        return InteractiveComponentReturns.COMPLETE;
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

    @Override
    public void cleanUp() {

    }
}
