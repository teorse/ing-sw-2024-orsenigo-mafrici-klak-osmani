package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

import java.io.PrintStream;
import java.util.List;

public abstract class Component {
    final PrintStream out;
    protected final ViewState view;

    public Component(ViewState view){
        out = new PrintStream(System.out, true);
        this.view = view;
    }

    public abstract void print();
}
