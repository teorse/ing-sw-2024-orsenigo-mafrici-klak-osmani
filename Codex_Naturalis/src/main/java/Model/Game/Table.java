package Model.Game;

import Client.Model.Records.CardRecord;
import Client.Model.Records.ObjectiveRecord;
import Client.Model.Records.TableRecord;
import Model.Cards.Card;
import Model.Objectives.Objective;
import Model.Utility.Artifacts;
import Network.ServerClient.Packets.SCPUpdateTable;
import Server.Model.Lobby.ObserverRelay;

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
        this.cardPools = new HashMap<>(){{
            put(CardPoolTypes.RESOURCE, new CardPool(resourceCards));
            put(CardPoolTypes.GOLDEN, new CardPool(goldenCards));
        }};
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

        //todo
        if(gameObserverRelay != null)
            gameObserverRelay.update(new SCPUpdateTable(this.toRecord()));

        return cardDrawn;
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

    /**
     * Draws two objectives from the list and reveals them as the shared objectives.
     */
    public void revealSharedObjectives(){
        sharedObjectives.add(objectives.getLast());
        sharedObjectives.add(objectives.getLast());
    }





    //DATA TRANSFER OBJECT
    public TableRecord toRecord(){
        Artifacts topDeckResource;
        Artifacts topDeckGolden;
        List<CardRecord> visibleCardRecordResource;
        List<CardRecord> visibleCardRecordGolden;
        List<ObjectiveRecord> sharedObjectives;


        //Handle the resource deck
        CardPool cardPoolResource = cardPools.get(CardPoolTypes.RESOURCE);
        visibleCardRecordResource = cardPoolResource.toRecord();

        if(cardPoolResource.getAmountLeftInDeck() == 0)
            topDeckResource = Artifacts.NULL;
        else
            topDeckResource = visibleCardRecordResource.removeFirst().cardColor();



        //Handle the golden deck
        CardPool cardPoolGolden = cardPools.get(CardPoolTypes.GOLDEN);
        visibleCardRecordGolden = cardPoolGolden.toRecord();

        if(cardPoolGolden.getAmountLeftInDeck() == 0)
            topDeckGolden = Artifacts.NULL;
        else
            topDeckGolden = visibleCardRecordGolden.removeFirst().cardColor();



        //Handle objectives
        sharedObjectives = new ArrayList<>();
        for(Objective objective : this.sharedObjectives)
            sharedObjectives.add(objective.toRecord());


        return new TableRecord(topDeckResource, topDeckGolden, visibleCardRecordResource, visibleCardRecordGolden, sharedObjectives);
    }
}
