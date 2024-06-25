package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.CardsHeld;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

/**
 * The CardsHeldView class is responsible for displaying the cards currently held by the player.
 * It observes changes in the CardsHeld model to update the view accordingly.
 */
public class CardsHeldView extends LiveComponent {

    /**
     * Constructs a new CardsHeldView and refreshes the observed objects.
     */
    public CardsHeldView() {
        super();
        refreshObserved();
    }

    /**
     * Prints the cards held by the player.
     * For each card, it prints whether the card can be placed on both sides,
     * shows the details of the card, and prints a spacer line between cards.
     */
    @Override
    public void print() {
        CardsHeld cardsHeld = CardsHeld.getInstance();

        // Loop through the map of cards held by the player
        for (int i = 0; i < cardsHeld.getAmountHeld(); i++) {

            // Print if the card can be placed on both sides
            out.println((i+1) + " - This card can be placed on both sides: " + cardsHeld.getCardPlayability(i));

            // Show the details of the card
            new CardView(cardsHeld.getCard(i)).print();

            // Print a spacer line between cards
            System.out.println();
        }
    }

    /**
     * Cleans the observed objects by removing this view from the observers of CardsHeld.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, CardsHeld.getInstance());
    }

    /**
     * Refreshes the observed objects by adding this view to the observers of CardsHeld.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardsHeld.getInstance());
    }
}
