package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public abstract class LiveComponent extends Component{

    public LiveComponent(){
        //todo remove duplicate attribute view from subclasses
        super();
    }

    public abstract void cleanObserved();

    public abstract void refreshObserved();
}
