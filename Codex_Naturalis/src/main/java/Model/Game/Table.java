package Model.Game;

import Model.Cards.Card;
import Model.Objectives.Objective;

import java.util.*;

/**
 * Class storing all common resources between players in the same game: Stores the card pools, the shared objectives etc.<br>
 * Can be interpreted as the "table" on top of which the game is being played.
 */
public class Table {
    //ATTRIBUTES
    private Map<CardPoolTypes, CardPool> cardPools;
    private Deck starterCards;
    private List<Objective> objectives;
    private List<Objective> sharedObjectives;





    //CONSTRUCTOR
    /**
     * Default constructor.
     * @param goldenCards   List of Golden Cards to use during this game.
     * @param resourceCards List of Resource Cards to use during this game.
     * @param starterCards  List of Starter Cards to use during this game.
     * @param objectives    List of Objectives to use during this game.
     */
    public Table(List<Card> goldenCards, List<Card> resourceCards, List<Card> starterCards, List<Objective> objectives){
        this.cardPools = new HashMap<>(){{
            put(CardPoolTypes.RESOURCE, new CardPool(resourceCards));
            put(CardPoolTypes.GOLDEN, new CardPool(goldenCards));
        }};
        this.starterCards = new Deck(starterCards);
        this.starterCards.shuffleDeck();

        this.objectives = objectives;
        Collections.shuffle(objectives);
    }





    //GETTERS
    public List<Objective> getSharedObjectives(){
        return this.sharedObjectives;
    }
    /**
     * Method to pick a card from the available pools.
     * @param cardPoolType  Parameter specifying the pool from which to pick the card.
     * @param cardIndex     Index specifying the card to pick.
     * @return  Card corresponding to the indexes.
     */
    public Card drawCard(CardPoolTypes cardPoolType, int cardIndex){
        return cardPools.get(cardPoolType).getCard(cardIndex);
    }

    /**
     * @return  Objective from list of non-shared objectives.
     */
    public Objective drawObjective(){
        return objectives.getLast();
    }

    /**
     * @return Card from list of starter cards.
     */
    public Card drawStarterCard(){
        return starterCards.draw();
    }





    //METHODS

    /**
     * @return  true if all decks are empty, false if at least one deck is not empty.
     */
    public boolean areDecksEmpty(){

        int counter = 0;
        for(Map.Entry<CardPoolTypes, CardPool> entry : cardPools.entrySet()){
            counter = counter + entry.getValue().getAmountLeftInDeck();
        }
        if(counter == 0)
            return true;
        else
            return false;
    }

}
