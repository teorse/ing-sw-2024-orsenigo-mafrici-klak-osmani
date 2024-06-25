package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

import java.io.PrintStream;
import java.util.List;

/**
 * The abstract base class for all components in the text-based user interface.
 * Each component is responsible for rendering itself to the output stream.
 */
public abstract class Component {
    final PrintStream out;

    /**
     * Constructs a new Component and initializes the output stream.
     * The output stream is set to the standard output (System.out).
     */
    public Component() {
        out = new PrintStream(System.out, true);
    }

    /**
     * Abstract method that must be implemented by subclasses to print the component.
     * The specific implementation of this method will define how the component is rendered.
     */
    public abstract void print();
}
