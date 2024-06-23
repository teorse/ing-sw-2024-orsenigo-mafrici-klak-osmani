package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardPoolRecord;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.util.Map;

public class CardPools extends Observable {
    //SINGLETON PATTERN
    private static CardPools INSTANCE;
    private CardPools(){};
    public synchronized static CardPools getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CardPools();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private Map<CardPoolTypes, CardPoolRecord> cardPools;
    private Map<CardPoolTypes, Boolean> cardPoolDrawability;





    //GETTERS
    public Map<CardPoolTypes, CardPoolRecord> getCardPools() {
        return cardPools;
    }
    public Map<CardPoolTypes, Boolean> getCardPoolDrawability() {
        return cardPoolDrawability;
    }
    public CardPoolRecord getCardPoolByType(CardPoolTypes type){
        return cardPools.get(type);
    }
    public boolean getCardPoolDrawability(CardPoolTypes type) {
        return cardPoolDrawability.get(type);
    }





    //SETTERS
    public void setCardPools(Map<CardPoolTypes, CardPoolRecord> cardPools) {
        this.cardPools = cardPools;
        super.updateObservers();
    }
    public void setCardPoolDrawability(Map<CardPoolTypes, Boolean> cardPoolDrawability) {
        this.cardPoolDrawability = cardPoolDrawability;
        super.updateObservers();
    }
    public void setSpecificCardPool(CardPoolTypes type, CardPoolRecord cardPool){
        cardPools.put(type, cardPool);
        super.updateObservers();
    }
}
