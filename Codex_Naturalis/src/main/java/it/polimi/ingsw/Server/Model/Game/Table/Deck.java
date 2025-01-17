package it.polimi.ingsw.Server.Model.Game.Table;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Cards.Card;

import java.util.Collections;
import java.util.List;

/**
 * Class wraps around an ArrayList of Cards and adds to it a shuffle method to treat the ArrayList as if it were a
 * deck of cards.
 */
public class Deck {

    //ATTRIBUTES
    private final List<Card> deck;





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





    //MODEL CLIENT CONVERSION
    /**
     * Converts the first card in the deck to a CardRecord.
     * @return CardRecord of the first card in the deck, or null if the deck is empty.
     */
    protected CardRecord toRecord(){
        if(!deck.isEmpty())
            return deck.getFirst().toRecord();
        return null;
    }
}
