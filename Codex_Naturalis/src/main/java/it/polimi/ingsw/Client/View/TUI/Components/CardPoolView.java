package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.CardPools;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardPoolRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.util.List;

/**
 * The CardPoolView class is a live component that displays the cards in a specified card pool.
 * It observes changes in the card pools and updates its display accordingly.
 */
public class CardPoolView extends LiveComponent {

    private final CardPoolTypes cardPoolType;

    /**
     * Constructs a new CardPoolView for the specified card pool type.
     *
     * @param cardPoolType the type of card pool to display (e.g., RESOURCE, GOLDEN).
     */
    public CardPoolView(CardPoolTypes cardPoolType) {
        super();
        this.cardPoolType = cardPoolType;
        refreshObserved();
    }

    /**
     * Prints the current state of the card pool.
     * It shows the top card of the deck (if any) and the visible cards in the pool.
     */
    @Override
    public void print() {
        CardPools cardPools = CardPools.getInstance();
        CardPoolRecord cardPool = cardPools.getCardPoolByType(cardPoolType);
        Artifacts topDeckCardColor = cardPool.coveredCardColor();
        List<CardRecord> visibleCards = cardPool.visibleCards();

        // Print the card pool based on its type
        switch (cardPoolType) {
            case RESOURCE -> {
                out.println("\nRESOURCE POOL:");
                if (topDeckCardColor != null) {
                    out.println("1 - Artifact Type: " + topDeckCardColor.name() + " (covered card)");
                } else {
                    out.println("1 - The covered resource deck is empty");
                }
                // Print visible cards
                for (int i = 0; i < visibleCards.size(); i++) {
                    CardRecord card = visibleCards.get(i);
                    out.print((i + 2) + " - ");
                    new CardView(card).print();
                }
            }
            case GOLDEN -> {
                out.println("\nGOLDEN POOL:");
                if (topDeckCardColor != null) {
                    out.println("1 - Artifact Type: " + topDeckCardColor.name() + " (covered card)");
                } else {
                    out.println("1 - The covered golden deck is empty");
                }
                // Print visible cards
                for (int i = 0; i < visibleCards.size(); i++) {
                    CardRecord card = visibleCards.get(i);
                    out.print((i + 2) + " - ");
                    new CardView(card).print();
                }
            }
        }
    }

    /**
     * Removes this component from the list of observed objects in the RefreshManager.
     * This stops the component from receiving updates.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, CardPools.getInstance());
    }

    /**
     * Adds this component to the list of observed objects in the RefreshManager.
     * This allows the component to receive updates when the card pools change.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardPools.getInstance());
    }
}
