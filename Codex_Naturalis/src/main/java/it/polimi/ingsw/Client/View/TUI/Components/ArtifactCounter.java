package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.CardMaps;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Server.Model.Game.Artifacts;

import java.util.Map;

/**
 * The ArtifactCounter class represents a TUI component that displays the count of artifacts owned by the current player.
 * It extends the LiveComponent class and observes changes in the CardMaps for updates.
 */
public class ArtifactCounter extends LiveComponent {

    /**
     * Constructs an ArtifactCounter object.
     * Initializes the component and sets up observation of changes in the CardMaps.
     */
    public ArtifactCounter() {
        super();
        refreshObserved();
    }

    /**
     * Cleans up the observation of CardMaps updates when the component is no longer needed.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, CardMaps.getInstance());
    }

    /**
     * Sets up the observation of CardMaps updates for the ArtifactCounter component.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardMaps.getInstance());
    }

    /**
     * Prints the artifact count for the current player.
     * Retrieves the artifact counts from CardMaps and displays them in the console.
     */
    @Override
    public void print() {
        CardMaps map = CardMaps.getInstance();
        Map<Artifacts, Integer> artifactsCounter = map.getCardMaps().get(MyPlayer.getInstance().getUsername()).artifactsCounter();
        System.out.println("\nARTIFACT COUNTER:");
        for (Artifacts artifacts : artifactsCounter.keySet()) {
            System.out.println(artifacts.name() + ": " + artifactsCounter.get(artifacts));
        }
        System.out.println();
    }
}
