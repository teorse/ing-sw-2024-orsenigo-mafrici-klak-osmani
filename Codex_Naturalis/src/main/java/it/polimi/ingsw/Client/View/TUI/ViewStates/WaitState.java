package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WaitState {
    List<Component> passiveComponenets;
    InteractiveComponent mainComponent;
    List<InteractiveComponent> secondaryInteractiveComponents;


    public WaitState(){
        passiveComponenets = new ArrayList<>();
        passiveComponenets.add(new SharedObjectiveView());
        passiveComponenets.add(new SecretObjectiveView());
        passiveComponenets.add(new PointTableView());
        passiveComponenets.add(new CardMapView());
    }

    public void print(){
        //todo add class game and logic to switch between game title and last round
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        //todo add display of all available commands

        for()

    }

}
