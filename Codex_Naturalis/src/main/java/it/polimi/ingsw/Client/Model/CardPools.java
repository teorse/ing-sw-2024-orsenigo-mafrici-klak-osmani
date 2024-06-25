package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardPoolRecord;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.util.Map;

/**
 * The CardPools class represents the collection of different card pools in the game.
 * It is observable, allowing observers to be notified of changes to the card pools.
 */
public class CardPools extends Observable {
    // Singleton instance of CardPools
    private static CardPools INSTANCE;

    // Private constructor for singleton pattern
    private CardPools() {}

    /**
     * Returns the singleton instance of CardPools.
     *
     * @return The singleton instance of CardPools
     */
    public synchronized static CardPools getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CardPools();
        }
        return INSTANCE;
    }

    // Attributes
    private Map<CardPoolTypes, CardPoolRecord> cardPools;
    private Map<CardPoolTypes, Boolean> cardPoolDrawability;

    // Getters
    public Map<CardPoolTypes, CardPoolRecord> getCardPools() {
        return cardPools;
    }

    public Map<CardPoolTypes, Boolean> getCardPoolDrawability() {
        return cardPoolDrawability;
    }

    /**
     * Retrieves a specific card pool record by its type.
     *
     * @param type The type of the card pool
     * @return The CardPoolRecord associated with the specified type
     */
    public CardPoolRecord getCardPoolByType(CardPoolTypes type){
        return cardPools.get(type);
    }

    /**
     * Retrieves the drawability status of a specific card pool type.
     *
     * @param type The type of the card pool
     * @return True if the card pool is drawable; false otherwise
     */
    public boolean getCardPoolDrawability(CardPoolTypes type) {
        return cardPoolDrawability.get(type);
    }

    // Setters

    /**
     * Sets the entire collection of card pools.
     *
     * @param cardPools The map of card pool types to their respective records
     */
    public void setCardPools(Map<CardPoolTypes, CardPoolRecord> cardPools) {
        this.cardPools = cardPools;
        super.updateObservers();
    }

    /**
     * Sets the drawability status of each card pool type.
     *
     * @param cardPoolDrawability The map of card pool types to their drawability status
     */
    public void setCardPoolDrawability(Map<CardPoolTypes, Boolean> cardPoolDrawability) {
        this.cardPoolDrawability = cardPoolDrawability;
        super.updateObservers();
    }

    /**
     * Sets a specific card pool record for a given type.
     *
     * @param type The type of the card pool
     * @param cardPool The CardPoolRecord to set
     */
    public void setSpecificCardPool(CardPoolTypes type, CardPoolRecord cardPool){
        cardPools.put(type, cardPool);
        super.updateObservers();
    }
}
