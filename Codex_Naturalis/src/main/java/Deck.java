/**
 * Class wraps around an ArrayList of Cards and adds to it a shuffle method to treat the ArrayList as if it were a
 * deck of cards.
 */

import java.util.ArrayList;
import java.util.List;

public class Deck {
    /**
     * It's the ArrayList that stores the card part of the deck.
     */
    private List<Card> deck;

    /**
     * Default Constructor.<br>
     * Defines a deck by taking as parameter the ArrayList which is to be turned into the deck.
     * @param deck  ArrayList of Cards to be turned into a deck.
     */
    public Deck(List<Card> deck) {
        this.deck = deck;
    }

    /**
     * Shuffle method switches randomly the positions of the elements inside the ArrayList.
     */
    public void shuffleDeck(){

        //code for logic to shuffle the deck of cards
        /* Example logic
         for (int i = deck.size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1)); // Generate a random index between 0 and i (inclusive)
            Collections.swap(deck, i, j); // Swap elements at i and j indices
        }
        */


    }

    /**
     *Method removes and returns the card in the ArrayList in position [0].
     * @return Card held in deck[0].
     */
    public Card pop(){
        return deck.removeFirst();
    }

    /**
     * Method returns the card in the Arraylist in position [0] without removing it.
     * @return Card held in deck[0]
     */
    public Card peek(){
        return deck.getFirst();
    }

    /**
     * Method used to return the size (number of elements) currently in the ArrayList.
     * @return Int with current size of ArrayList.
     */
    public int cardsLeft(){
        return deck.size();
    }
}
