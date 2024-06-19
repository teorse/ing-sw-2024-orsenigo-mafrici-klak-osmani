package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPickObjective;

public class PickSecretObjective extends InteractiveComponent {

    public PickSecretObjective() {
        super();
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {
        if (InputValidator.validBinaryChoice(input)) {
            ClientModel.getInstance().getClientConnector().sendPacket(new CSPPickObjective(Integer.parseInt(input) - 1));
            return InteractiveComponentReturns.COMPLETE;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "pickSecretObjective";
    }

    @Override
    public void print() {
        System.out.println("\nChoose a secret objective: \n");
    }

    @Override
    public void cleanObserved() {

    }

    @Override
    public void refreshObserved() {

    }
}
