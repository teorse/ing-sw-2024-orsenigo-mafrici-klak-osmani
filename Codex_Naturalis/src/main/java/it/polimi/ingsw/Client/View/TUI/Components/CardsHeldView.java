package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.CardsHeld;

import java.io.PrintStream;

public class CardsHeldView extends Component{
    private final CardsHeld cardsHeld;

    public CardsHeldView(){
        cardsHeld = CardsHeld.getInstance();
    }


    @Override
    public void print() {
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
}
