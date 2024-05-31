package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardsHeld;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.CardStarterView;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPlayCard;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;

public class CardStarterChoice extends InteractiveComponent {
    private final ClientModel model;
    private final CardRecord cardStarter;

    public CardStarterChoice() {
        this.model = ClientModel.getInstance();
        this.cardStarter = CardsHeld.getInstance().getCardsHeld().getFirst();
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (TextUI.validBinaryChoice(input)) {
            boolean faceUp = (Integer.parseInt(input) == 1);
            model.getClientConnector().sendPacket(new CSPPlayCard(0, 0, faceUp));
            return InteractiveComponentReturns.COMPLETE;
        }
        return InteractiveComponentReturns.COMPLETE;
    }

    @Override
    public String getKeyword() {
        return "";
    }

    @Override
    public void print() {
        new CardStarterView(cardStarter).print();
        System.out.println("\n" +
                """
                On which side do you want to place the card? Enter your choice:
                 1 - Front
                 2 - Back""");
    }

    private void nextState() {

    }
}
