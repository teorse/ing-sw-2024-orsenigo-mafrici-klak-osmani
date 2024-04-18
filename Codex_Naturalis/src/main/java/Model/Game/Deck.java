package Model.Game;

import Model.Cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class wraps around an ArrayList of Cards and adds to it a shuffle method to treat the ArrayList as if it were a
 * deck of cards.
 */
public class Deck {

    //ATTRIBUTES
    private List<Card> deck;





    //CONSTRUCTOR
    /**
     * Default Constructor.<br>
     * @param deck  ArrayList of Cards to be turned into a deck.
     */
    public Deck(List<Card> deck) {
        this.deck = deck;
    }





    //METHODS
    /**
     * Shuffle method switches randomly the positions of the elements inside the ArrayList.
     */
    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    /**
     *Method removes and returns the card in the ArrayList in position [0].
     * @return Card held in deck[0].
     */
    public Card draw(){
        return deck.removeLast();
    }

    /**
     * Method used to return the size (number of elements) currently in the ArrayList.
     * @return Int with current size of ArrayList.
     */
    public int cardsLeft(){
        return deck.size();
    }
}
