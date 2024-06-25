package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

/**
 * The abstract base class for all live components in the text-based user interface.
 * Live components are those that can observe and react to changes in the game state.
 */
public abstract class LiveComponent extends Component {

    /**
     * Constructs a new LiveComponent and initializes the output stream.
     */
    public LiveComponent() {
        //todo remove duplicate attribute view from subclasses
        super();
    }

    /**
     * Cleans the observed data of the component.
     * This method should be implemented to clear any data or state that the component is observing.
     */
    public abstract void cleanObserved();

    /**
     * Refreshes the observed data of the component.
     * This method should be implemented to update the data or state that the component is observing.
     */
    public abstract void refreshObserved();
}
