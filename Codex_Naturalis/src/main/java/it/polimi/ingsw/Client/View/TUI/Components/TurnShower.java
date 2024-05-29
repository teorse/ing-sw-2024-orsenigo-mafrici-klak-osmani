package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.Model.Players;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.awt.*;
import java.util.List;

public class TurnShower extends Component {
    private final List<PlayerRecord> playerRecords;

    public TurnShower(ClientModel model) {
        this.playerRecords = Players.getInstance().getPlayers();
    }

    @Override
    public void print() {
        for (PlayerRecord playerRecord : playerRecords) {
            if (playerRecord.playerState() == PlayerStates.PLACE || playerRecord.playerState() == PlayerStates.DRAW) {
                System.out.println("\nIt's " + playerRecord.username() + " turn.");
                break;
            }
        }
    }
}
