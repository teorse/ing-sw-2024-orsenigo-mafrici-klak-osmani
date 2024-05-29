package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;

import java.util.List;
import java.util.Map;

public class CardsHeld {
    //SINGLETON PATTERN
    private static CardsHeld INSTANCE;
    private CardsHeld(){};
    public static CardsHeld getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CardsHeld();
        }

        return INSTANCE;
    }


    private List<CardRecord> cardsHeld;
    private Map<CardRecord, Boolean> cardPlayability;

    public int getAmountHeld(){
        return cardsHeld.size();
    }

    public CardRecord getCard(int index){
        return cardsHeld.get(index);
    }
    public boolean getCardPlayability(int cardIndex){
        CardRecord card = cardsHeld.get(cardIndex);
        return cardPlayability.get(card);
    }

}
