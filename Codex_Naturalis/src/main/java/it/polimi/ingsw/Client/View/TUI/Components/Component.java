package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

import java.io.PrintStream;

public abstract class Component {
    final PrintStream out;

    public Component(){
        out = new PrintStream(System.out, true);
    }

    public abstract void print();

    public abstract void cleanUp();
}
