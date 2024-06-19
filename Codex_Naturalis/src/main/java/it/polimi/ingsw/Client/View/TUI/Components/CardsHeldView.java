package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.CardsHeld;
import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

import java.io.PrintStream;
import java.util.List;

public class CardsHeldView extends LiveComponent{

    public CardsHeldView(){
        super();
        refreshObserved();
    }

    @Override
    public void print() {
        CardsHeld cardsHeld = CardsHeld.getInstance();

        //Loop through the map of cards held by the player
        for (int i = 0; i < cardsHeld.getAmountHeld(); i++) {

            //Print if the card can be placed on both sides
            out.println((i+1) + " - This card can be placed on both sides: " + cardsHeld.getCardPlayability(i));

            //Show the details of the card
            new CardView(cardsHeld.getCard(i)).print();

            //Print a spacer line between cards
            System.out.println();
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, CardsHeld.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardsHeld.getInstance());
    }
}
