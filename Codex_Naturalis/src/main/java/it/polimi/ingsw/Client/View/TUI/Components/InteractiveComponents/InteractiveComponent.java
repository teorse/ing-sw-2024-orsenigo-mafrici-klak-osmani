package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.Components.Component;

public abstract class InteractiveComponent extends Component {
    //ATTRIBUTES
    int inputCounter = 0;





    //METHODS
    //TODO fix return of the boolean
    public abstract boolean handleInput(String input);
    public abstract String getKeyword();
}
