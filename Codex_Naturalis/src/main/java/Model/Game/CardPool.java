package Model.Game;

import Model.Cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing all the shared cards of a specific type (specifically resource or golden).<br>
 * The list stores all the visible cards for the given type, and the deck stores the covered cards.
 */
public class CardPool {

    //ATTRIBUTES
    private Deck deck;
    private List<Card> visibleCards;





    //CONSTRUCTOR
    /**
     * Default constructor
     * @param cards List of cards to store in this card collection.
     */
    public CardPool(List<Card> cards){
        deck = new Deck(cards);
        deck.shuffleDeck();
        visibleCards = new ArrayList<>(){{
            add(deck.pop());
            add(deck.pop());
        }};
    }





    //GETTER

    /**
     * Method to retrieve cards from the collection.
     * @param index Specifies the card to be retrieved. If index = -1 then pops a card from the deck,
     *              If -1< index < number of visible cards then returns the specified visible card.
     * @return      Card object specified by the index.
     */
    public Card getCard(int index){
        if(index < -1 || index >= visibleCards.size())
            throw new IndexOutOfBoundsException();
        else if(index == -1)
            return deck.pop();
        else
        {
            Card card = visibleCards.remove(index);
            visibleCards.add(index, deck.pop());
            return card;
        }
    }

    /**
     * @return Number of cards left in this pool's deck.
     */
    public int getAmountLeftInDeck(){
        return deck.cardsLeft();
    }
}