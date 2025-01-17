package it.polimi.ingsw.Server.Model.Game.Table;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardPoolRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing all the shared cards of a specific type (specifically resource or golden).<br>
 * The list stores all the visible cards for the given type, and the deck stores the covered cards.
 */
public class CardPool {

    //ATTRIBUTES
    private final Deck deck;
    private final List<Card> visibleCards;





    //CONSTRUCTOR
    /**
     * Default constructor
     * @param cards List of cards to store in this card collection.
     */
    public CardPool(List<Card> cards){
        deck = new Deck(cards);
        deck.shuffleDeck();
        visibleCards = new ArrayList<>();
        visibleCards.add(deck.draw());
        visibleCards.add(deck.draw());
    }





    //GETTER

    /**
     * Method to retrieve cards from the collection.
     * @param index Specifies the card to be retrieved. If index = -1 then pops a card from the deck,
     *              If -1 lt index lt number of visible cards then returns the specified visible card.
     * @return      Card object specified by the index.
     */
    public Card getCard(int index){
        if(index < -1 || index >= visibleCards.size())
            throw new IndexOutOfBoundsException();
        else if(index == -1)
            return deck.draw();
        else
        {
            Card card = visibleCards.remove(index);
            visibleCards.add(index, deck.draw());
            return card;
        }
    }

    /**
     * @return Number of cards left in this pool's deck.
     */
    public int getAmountLeftInDeck(){
        return deck.cardsLeft();
    }

    public int getAmountOfVisibleCards(){
        return visibleCards.size();
    }





    //MODEL CLIENT CONVERSION
    /**
     * Converts this CardPool instance into a CardPoolRecord object for serialization.
     * The CardPoolRecord contains information about the deck and visible cards in this CardPool.
     *
     * @return The CardPoolRecord representing this CardPool instance.
     */
    public CardPoolRecord toRecord(){
        List<CardRecord> visibleCards = new ArrayList<>();
        for (Card visibleCard : this.visibleCards) {
            visibleCards.add(visibleCard.toRecord());
        }

        return new CardPoolRecord(deck.toRecord().cardColor(), visibleCards);
    }
}
