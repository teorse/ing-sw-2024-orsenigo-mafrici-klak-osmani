package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Lobby;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public class GameStartingStatus extends LiveComponent{

    public GameStartingStatus(ViewState viewState) {
        super(viewState);
        view.addObserved(Lobby.getInstance());
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
        view.removeObserved(Lobby.getInstance());
    }
}
