package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public abstract class LiveComponent extends Component{
    protected final ViewState view;

    public LiveComponent(ViewState view){
        //todo remove duplicate attribute view from subclasses
        super();
        this.view = view;
    }

    public abstract void cleanObserved();
}
