package Client.Model;

import Model.Cards.Card;
import Model.Game.CardPool;
import Model.Game.CardPoolTypes;
import Model.Objectives.Objective;
import Model.Utility.Artifacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a view of the game table, encapsulating the visual representation of card pools and shared objectives.
 * <p>
 * The TableView class provides an overview of the game's table, including:
 * - The top deck cards for resource and golden card pools.
 * - The lists of visible cards for resource and golden pools.
 * - The list of shared objectives.
 * <p>
 * It is constructed based on a given Table instance and can be used to display information
 * about the game's card pools and shared objectives in a human-readable format.
 * <p>
 * The attributes and methods in this class are designed to be used in a view or display layer
 * to present game-related information to users.
 */
public class TableView {

    //ATTRIBUTES
    private Artifacts topDeckResource;
    private Artifacts topDeckGolden;
    private final List<CardView> visibleCardViewResource;
    private final List<CardView> visibleCardViewGolden;
    private final List<ObjectiveView> sharedObjectives;

    //CONSTRUCTOR
    /**
     * Constructs a `TableView` object with the specified card pools and shared objectives.
     * <p>
     * This constructor initializes:
     * - The list of visible card views for resource and golden card pools.
     * - The top deck cards for the resource and golden card pools.
     * - The list of shared objectives.
     * <p>
     * It iterates over the given card pools to populate the resource and golden card views.
     * For each card pool, it sets the top deck card and creates a list of visible card views.
     * It also populates the shared objectives from the given list of objectives.
     *
     * @param cardPools        A map of `CardPoolTypes` to `CardPool` representing the card pools to be visualized.
     * @param sharedObjectives A list of `Objective` objects representing the shared objectives in the game.
     */
    public TableView(Map<CardPoolTypes, CardPool> cardPools, List<Objective> sharedObjectives) {
        visibleCardViewResource = new ArrayList<>();
        visibleCardViewGolden = new ArrayList<>();
        this.sharedObjectives = new ArrayList<>();

        // Loop through the card pools to set up the resource and golden card views
        for (CardPoolTypes cardPoolType : cardPools.keySet()) {
            if (cardPoolType == CardPoolTypes.RESOURCE) {
                CardPool cpr = cardPools.get(cardPoolType);
                // Set the top resource deck card if the deck has cards
                if (cpr.getAmountLeftInDeck() > 0) {
                    this.topDeckResource = cpr.getDeck().getCard(0).getCardColor();
                } else {
                    this.topDeckResource = null;
                }
                //Populate the visible card views for resources
                for (Card card : cpr.getVisibleCards()) {
                    visibleCardViewResource.add(card.toCardView());
                }
            } else if (cardPoolType == CardPoolTypes.GOLDEN) {
                CardPool cpg = cardPools.get(cardPoolType);
                //Set the top golden deck card if the deck has cards
                if (cpg.getAmountLeftInDeck() > 0) {
                    this.topDeckGolden = cpg.getDeck().getCard(0).getCardColor();
                } else {
                    this.topDeckGolden = null;
                }
                //Populate the visible card views for golden
                for (Card card : cpg.getVisibleCards()) {
                    visibleCardViewGolden.add(card.toCardView());
                }
            }
        }
        for (Objective objective : sharedObjectives) {
            this.sharedObjectives.add(objective.toObjectiveView());
        }
    }

    //GETTERS
    public Artifacts getTopDeckResource() {
        return topDeckResource;
    }

    public Artifacts getTopDeckGolden() {
        return topDeckGolden;
    }

    public List<CardView> getVisibleCardViewResource() {
        return visibleCardViewResource;
    }

    public List<CardView> getVisibleCardViewGolden() {
        return visibleCardViewGolden;
    }

    public List<ObjectiveView> getSharedObjectives() {
        return sharedObjectives;
    }
}