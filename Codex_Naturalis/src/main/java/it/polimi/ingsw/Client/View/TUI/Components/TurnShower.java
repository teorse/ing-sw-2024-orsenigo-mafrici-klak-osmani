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

/**
 * The TurnShower class is a live component that displays whose turn it is in the game.
 * It observes the players' states and updates its display based on the current player turn.
 */
public class TurnShower extends LiveComponent {

    /**
     * Constructs a new TurnShower and initializes the observed data.
     */
    public TurnShower() {
        super();
        refreshObserved();
    }

    /**
     * Prints the current turn information.
     * If it is the user's turn, it prints "It's your turn."
     * If it is another player's turn, it prints "It's [player's name] turn."
     */
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

    /**
     * Cleans the observed data of this component by removing it from the RefreshManager's list of observed objects.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Players.getInstance());
    }

    /**
     * Refreshes the observed data of this component by adding it to the RefreshManager's list of observed objects.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Players.getInstance());
    }
}
