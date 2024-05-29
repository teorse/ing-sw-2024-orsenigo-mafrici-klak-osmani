package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardPoolRecord;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.util.Map;

public class CardPools extends Observable {
    //SINGLETON PATTERN
    private static CardPools INSTANCE;
    private CardPools(){};
    public static CardPools getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CardPools();
        }

        return INSTANCE;
    }

    private Map<CardPoolTypes, CardPoolRecord> cardPools;
    private Map<CardPoolTypes, Boolean> cardPoolDrawability;

    public void updateCardPool(CardPoolTypes type, CardPoolRecord cardPool){
        cardPools.put(type, cardPool);
        super.updateObservers();
    }

    public void setCardPools(Map<CardPoolTypes, CardPoolRecord> cardPools) {
        this.cardPools = cardPools;
    }

    public void setCardPoolDrawability(Map<CardPoolTypes, Boolean> cardPoolDrawability) {
        this.cardPoolDrawability = cardPoolDrawability;
        super.updateObservers();
    }

    public CardPoolRecord getCardPoolByType(CardPoolTypes type){
        return cardPools.get(type);
    }
}
