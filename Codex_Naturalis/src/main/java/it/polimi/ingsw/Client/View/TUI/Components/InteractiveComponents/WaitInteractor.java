package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;

public class WaitInteractor extends InteractiveComponent {
    private final ClientModel model = ClientModel.getInstance();
    private InteractiveComponent zoom;

    public WaitInteractor(){
        zoom = new Zoomer();
    }



    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (model.isSetUpFinished() && !model.isWaitingForReconnections()) {
            zoom.handleInput(input);
        }
        return false;
    }

    @Override
    public String getKeyword() {
        return "wait";
    }

    @Override
    public void print() {
        if (model.isSetUpFinished() && !model.isWaitingForReconnections()) {
            zoom.print();
        }
    }
}
