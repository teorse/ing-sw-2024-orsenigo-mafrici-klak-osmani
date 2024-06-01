package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.ObjectiveCandidatesView;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPickObjective;

public class PickSecretObjective extends InteractiveComponent {
    private ClientModel model;

    public PickSecretObjective() {
        this.model = ClientModel.getInstance();
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {
        if (TextUI.validBinaryChoice(input)) {
            model.getClientConnector().sendPacket(new CSPPickObjective(Integer.parseInt(input) - 1));
            return InteractiveComponentReturns.COMPLETE;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "";
    }

    @Override
    public void print() {
        System.out.println("\nChoose a secret objective: \n");
        new ObjectiveCandidatesView().print();
    }

    @Override
    public void cleanUp() {

    }
}
