package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.Players;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.awt.*;
import java.util.List;

public class TurnShower extends LiveComponent {

    public TurnShower() {
        super();
        refreshObserved();
    }

    @Override
    public void print() {
        List<PlayerRecord> playerRecords = Players.getInstance().getPlayers();
        for (PlayerRecord playerRecord : playerRecords) {
            if (playerRecord.playerState() == PlayerStates.PLACE || playerRecord.playerState() == PlayerStates.DRAW) {
                if (!playerRecord.username().equals(MyPlayer.getInstance().getUsername()))
                    System.out.println("\nIt's " + playerRecord.username() + " turn.");
                else
                    System.out.println("\nIt's your turn.");
                break;
            }
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Players.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Players.getInstance());
    }
}
