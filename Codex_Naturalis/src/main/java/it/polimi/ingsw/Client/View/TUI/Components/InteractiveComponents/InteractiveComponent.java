package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.Components.Component;

public abstract class InteractiveComponent extends Component {
    int inputCounter = 0;


    public abstract void handleInput(String input);
    public abstract String getKeyword();
}
