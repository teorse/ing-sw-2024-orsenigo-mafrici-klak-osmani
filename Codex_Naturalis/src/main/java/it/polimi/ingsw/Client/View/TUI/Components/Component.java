package it.polimi.ingsw.Client.View.TUI.Components;

import java.io.PrintStream;

public abstract class Component {
    final PrintStream out;

    public Component(){
        out = new PrintStream(System.out, true);
    }

    public abstract void print();
}
