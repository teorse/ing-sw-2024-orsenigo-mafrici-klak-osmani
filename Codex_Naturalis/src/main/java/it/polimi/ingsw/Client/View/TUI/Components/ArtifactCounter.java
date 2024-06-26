package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.CardMaps;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Server.Model.Game.Artifacts;

import java.util.Map;

public class ArtifactCounter extends LiveComponent {

    public ArtifactCounter() {
        super();
        refreshObserved();
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, CardMaps.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardMaps.getInstance());
    }

    @Override
    public void print() {
        CardMaps map = CardMaps.getInstance();
        Map<Artifacts, Integer> artifactsCounter = map.getCardMaps().get(MyPlayer.getInstance().getUsername()).artifactsCounter();
        System.out.println("ARTIFACT COUNTER:");
        for (Artifacts artifacts : artifactsCounter.keySet()) {
            System.out.println(artifacts.name() + ": " + artifactsCounter.get(artifacts));
        }
    }
}
