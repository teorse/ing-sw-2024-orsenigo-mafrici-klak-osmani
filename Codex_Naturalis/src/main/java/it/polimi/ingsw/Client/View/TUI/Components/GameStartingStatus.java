package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Lobby;

public class GameStartingStatus extends Component{

    Lobby lobby;

    public GameStartingStatus() {this.lobby = Lobby.getInstance();}

    @Override
    public void print() {


        if(lobby.getLobbyUsers().size() == lobby.getTargetNumberUsers()) {
            System.out.println("\nTarget number of users reached. Game will start in 20 seconds.");
        }
        else {
            System.out.println("\nWaiting for players to join the lobby.");
        }
    }
}
