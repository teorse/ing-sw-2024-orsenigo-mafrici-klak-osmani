package Model.Game;

import Model.Cards.Card;
import Model.Objectives.Objective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class storing all common resources between players in the same game: Stores the card pools, the shared objectives etc.<br>
 * Can be interpreted as the "table" on top of which the game is being played.
 */
public class Table {
    //ATTRIBUTES
    private List<CardPool> cardPools;
    private Deck starterCards;
    private List<Objective> objectives;
    private List<Objective> objectivesShared;





    //CONSTRUCTOR
    /**
     * Default constructor.
     * @param goldenCards   List of Golden Cards to use during this game.
     * @param resourceCards List of Resource Cards to use during this game.
     * @param starterCards  List of Starter Cards to use during this game.
     * @param objectives    List of Objectives to use during this game.
     */
    public Table(List<Card> goldenCards, List<Card> resourceCards, List<Card> starterCards, List<Objective> objectives){
        this.cardPools = new ArrayList<>(){{
            add(new CardPool(goldenCards));
            add(new CardPool(resourceCards));
        }};
        this.starterCards = new Deck(starterCards);
        this.starterCards.shuffleDeck();

        this.objectives = objectives;
        Collections.shuffle(objectives);
    }





    //GETTERS
    /**
     * Method to pick a card from the available pools.
     * @param poolIndex Index specifying the pool to pick from.
     * @param cardIndex Index specifying the card to pick.
     * @return  Card corresponding to the indexes.
     */
    public Card getCard(int poolIndex, int cardIndex){
        return cardPools.get(poolIndex).getCard(cardIndex);
    }

    /**
     * @return  Objective from list of non-shared objectives.
     */
    public Objective getRandomObjective(){
        return objectives.getLast();
    }





    //METHODS

    /**
     * @return  true if all decks are empty, false if at least one deck is not empty.
     */
    public boolean areDecksEmpty(){

        int counter = 0;
        for(CardPool cardPool : cardPools) {
            counter = counter + cardPool.getAmountLeftInDeck();
        }
        if(counter == 0)
            return true;
        else
            return false;
    }

}
