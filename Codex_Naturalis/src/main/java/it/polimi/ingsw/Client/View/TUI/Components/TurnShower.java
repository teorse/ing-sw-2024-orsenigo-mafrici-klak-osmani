package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.Model.Players;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.awt.*;
import java.util.List;

public class TurnShower extends Component {
    private final List<PlayerRecord> playerRecords;

    public TurnShower(ViewState viewState) {
        super(viewState);
        this.playerRecords = Players.getInstance().getPlayers();
    }

    @Override
    public void print() {
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
}
