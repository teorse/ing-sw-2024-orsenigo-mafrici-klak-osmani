package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardsHeld;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.CardStarterView;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPlayCard;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;

public class CardStarterChoice extends InteractiveComponent {
    private final ClientModel model;
    private final CardRecord cardStarter;
    private boolean invalidBinaryChoice;

    public CardStarterChoice() {
        super();
        this.model = ClientModel.getInstance();
        //todo check if there is no scenario where .getFirst() will cause null pointer.
        this.cardStarter = CardsHeld.getInstance().getCardsHeld().getFirst();
        invalidBinaryChoice = false;

        refreshObserved();
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        InteractiveComponentReturns superReturn = super.handleInput(input);
        if(superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        if (InputValidator.validBinaryChoice(input)) {
            boolean faceUp = (Integer.parseInt(input) == 1);
            model.getClientConnector().sendPacket(new CSPPlayCard(0, 0, faceUp));
            return InteractiveComponentReturns.COMPLETE;
        }
        else
            invalidBinaryChoice = true;

        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "PlaceStarter";
    }


    @Override
    public void print() {
        new CardStarterView(cardStarter).print();
        System.out.println("\n" +
                """
                On which side do you want to place the card? Enter your choice:
                 1 - Front
                 2 - Back""");

        if(invalidBinaryChoice){
            invalidBinaryChoice = false;
            System.out.println("The number you provided is not a valid input, please type 1 or 2.");
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().addObserved(this, CardsHeld.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardsHeld.getInstance());
    }
}
