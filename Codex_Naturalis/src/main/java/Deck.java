import java.util.ArrayList;

public class Deck {
    private ArrayList<ResourceCard> deck;

    //Constructor

    public Deck(ArrayList<ResourceCard> deck) {
        this.deck = deck;
    }


    //Shuffle

    public void shuffleDeck(ArrayList<ResourceCard> deck){

        //code for logic to shuffle the deck of cards
        /* Example logic
         for (int i = deck.size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1)); // Generate a random index between 0 and i (inclusive)
            Collections.swap(deck, i, j); // Swap elements at i and j indices
        }
        */


    }

    //Pop card

    public ResourceCard popCard(ArrayList<ResourceCard> deck){

        ResourceCard resourceCard = new ResourceCard();

        // 1. Pop the first card from the deck
        //TODO: Check the validity of the remove method.
        resourceCard = deck.removeFirst();


        return resourceCard;
    }


    //Check amount
    //Returns the size of the deck.
    public int cardsLeft(ArrayList<ResourceCard> deck){
        return deck.size();
    }
}
