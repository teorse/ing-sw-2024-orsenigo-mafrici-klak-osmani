package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPStartGame;

public class GameManualStarter extends InteractiveComponent {
    private boolean wrongCommand;
    private boolean notEnoughPlayers;

    public GameManualStarter() {
        super();
        refreshObserved();
        wrongCommand = false;
        notEnoughPlayers = false;
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        InteractiveComponentReturns superReturn = super.handleInput(input);
        if(superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

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
                    wrongCommand = true;
                    return InteractiveComponentReturns.INCOMPLETE;
                }
            }
            else {
                notEnoughPlayers = true;
                return InteractiveComponentReturns.INCOMPLETE;
            }
        }
        return InteractiveComponentReturns.COMPLETE;
    }

    @Override
    public String getKeyword() {
        return "gamestarter";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void print() {
        if(!MyPlayer.getInstance().isAdmin()) {
            if(LobbyUsers.getInstance().size() >= 2)
                System.out.println("\nTarget number of players reached!\nThe game will start soon.");
        }
        else {
            if (LobbyUsers.getInstance().size() >= 2)
                System.out.println("If you don't want to wait anymore there are already enough players to start the game." +
                        "\nType start to start the game.");
        }

        if(wrongCommand){
            wrongCommand = false;
            System.out.println("\nWrong command, to start the game type START!");
        }

        else if (notEnoughPlayers){
            notEnoughPlayers = false;
            System.out.println("\nYou can't start a game on your own!");
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().addObserved(this, LobbyUsers.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, LobbyUsers.getInstance());
    }
}
