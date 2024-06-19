package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Lobby;
import it.polimi.ingsw.Client.Model.RefreshManager;

public class GameStartingStatus extends LiveComponent{

    public GameStartingStatus() {
        super();
        refreshObserved();
    }

    @Override
    public void print() {
        Lobby lobby = Lobby.getInstance();

        if(lobby.getLobbyUsers().size() == lobby.getTargetNumberUsers()) {
            System.out.println("\nTarget number of users reached. Game will start in 20 seconds.");
        }
        else {
            System.out.println("\nWaiting for players to join the lobby.");
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Lobby.getInstance());
    }

    @Override
    public void refreshObserved() {RefreshManager.getInstance().addObserved(this, Lobby.getInstance());}
}
