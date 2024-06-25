package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;

import java.util.List;
import java.util.Map;

/**
 * The CardsHeld class represents the collection of cards held by a player.
 * It extends the Observable class, allowing observers to subscribe and receive updates
 * when changes occur in the cards held.
 */
public class CardsHeld extends Observable {
    // SINGLETON PATTERN
    private static CardsHeld INSTANCE;

    /**
     * Private constructor to ensure singleton pattern.
     */
    private CardsHeld() {}

    /**
     * Retrieves the singleton instance of CardsHeld.
     *
     * @return The singleton instance of CardsHeld.
     */
    public synchronized static CardsHeld getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardsHeld();
        }
        return INSTANCE;
    }

    // ATTRIBUTES
    private List<CardRecord> cardsHeld;
    private Map<CardRecord, Boolean> cardPlayability;

    // GETTERS
    /**
     * Retrieves the list of cards held by the player.
     *
     * @return The list of CardRecord objects representing the held cards.
     */
    public List<CardRecord> getCardsHeld() {
        return cardsHeld;
    }

    /**
     * Retrieves the map indicating the playability status of each held card.
     *
     * @return The map of CardRecord to Boolean indicating playability status.
     */
    public Map<CardRecord, Boolean> getCardPlayability() {
        return cardPlayability;
    }

    /**
     * Retrieves a specific card held by its index.
     *
     * @param index The index of the card to retrieve.
     * @return The CardRecord object representing the held card at the specified index.
     */
    public CardRecord getCard(int index) {
        return cardsHeld.get(index);
    }

    /**
     * Retrieves the playability status of a specific held card by its index.
     *
     * @param cardIndex The index of the card to check.
     * @return `true` if the card is playable; `false` otherwise.
     */
    public boolean getCardPlayability(int cardIndex) {
        CardRecord card = cardsHeld.get(cardIndex);
        return cardPlayability.get(card);
    }

    /**
     * Retrieves the total number of cards held by the player.
     *
     * @return The number of cards held.
     */
    public int getAmountHeld() {
        return cardsHeld.size();
    }

    // SETTER
    /**
     * Sets the list of cards held and their playability status.
     *
     * @param cardsHeld The list of CardRecord objects representing the held cards.
     * @param cardPlayability The map indicating the playability status of each held card.
     */
    public void setCardsHeld(List<CardRecord> cardsHeld, Map<CardRecord, Boolean> cardPlayability) {
        this.cardsHeld = cardsHeld;
        this.cardPlayability = cardPlayability;
        super.updateObservers();
    }
}
