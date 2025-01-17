package it.polimi.ingsw.Server.Model.Game.Table;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardPoolRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.TableRecord;
import it.polimi.ingsw.Server.Model.Game.Cards.Card;
import it.polimi.ingsw.Server.Model.Game.Objectives.Objective;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateTable;
import it.polimi.ingsw.Server.Model.Lobby.ObserverRelay;

import java.util.*;

/**
 * Class storing all common resources between players in the same game: Stores the card pools, the shared objectives etc.<br>
 * Can be interpreted as the "table" on top of which the game is being played.
 */
public class Table {
    //ATTRIBUTES
    private final Map<CardPoolTypes, CardPool> cardPools;
    private final Deck starterCards;
    private final List<Objective> objectives;
    private final List<Objective> sharedObjectives;

    private final ObserverRelay gameObserverRelay;





    //CONSTRUCTOR
    /**
     * Default constructor.
     * @param goldenCards   List of Golden Cards to use during this game.
     * @param resourceCards List of Resource Cards to use during this game.
     * @param starterCards  List of Starter Cards to use during this game.
     * @param objectives    List of Objectives to use during this game.
     */
    public Table(List<Card> goldenCards, List<Card> resourceCards, List<Card> starterCards, List<Objective> objectives, ObserverRelay gameObserverRelay){
        this.cardPools = new HashMap<>();
        this.cardPools.put(CardPoolTypes.RESOURCE, new CardPool(resourceCards));
        this.cardPools.put(CardPoolTypes.GOLDEN, new CardPool(goldenCards));

        this.starterCards = new Deck(starterCards);
        this.starterCards.shuffleDeck();

        this.objectives = objectives;
        Collections.shuffle(objectives);

        sharedObjectives = new ArrayList<>();

        this.gameObserverRelay = gameObserverRelay;
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
        Card cardDrawn =  cardPools.get(cardPoolType).getCard(cardIndex);

        gameObserverRelay.update(new SCPUpdateTable(this.toRecord()));

        return cardDrawn;
    }

    /**
     * @return  Objective from list of non-shared objectives.
     */
    public Objective drawObjective(){
        return objectives.removeLast();
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

    /**
     * Checks if all card pools are empty.
     * @return true if all card pools are empty, false otherwise.
     */
    public boolean areAllCardPoolsEmpty(){
        for(CardPool cardPool : cardPools.values()){
            if (cardPool.getAmountLeftInDeck() > 0)
                return false;
            if(cardPool.getAmountOfVisibleCards() > 0)
                return false;
        }
        return true;
    }

    /**
     * Checks if the specified card pool is empty.
     * @param cardPoolType the type of card pool to check.
     * @return true if the specified card pool is empty, false otherwise.
     */
    public boolean isCardPoolEmpty(CardPoolTypes cardPoolType){
        CardPool cardPool = cardPools.get(cardPoolType);
        if (cardPool.getAmountLeftInDeck() > 0)
            return false;
        if(cardPool.getAmountOfVisibleCards() > 0)
            return false;
        else
            return true;
    }

    /**
     * Draws two objectives from the list and reveals them as the shared objectives.
     */
    public void revealSharedObjectives(){
        sharedObjectives.add(objectives.removeLast());
        sharedObjectives.add(objectives.removeLast());

        gameObserverRelay.update(new SCPUpdateTable(this.toRecord()));
    }





    //DATA TRANSFER OBJECT
    /**
     * Converts the Table object to a TableRecord.
     * @return TableRecord representation of the Table object.
     */
    public TableRecord toRecord(){
        List<ObjectiveRecord> sharedObjectives = new ArrayList<>();
        for(Objective objective : this.sharedObjectives)
            sharedObjectives.add(objective.toRecord());

        Map<CardPoolTypes, CardPoolRecord> cardPools = new HashMap<>();
        for(Map.Entry<CardPoolTypes, CardPool> entry : this.cardPools.entrySet()){
            cardPools.put(entry.getKey(), entry.getValue().toRecord());
        }

        return new TableRecord(cardPools, sharedObjectives);
    }
}
