package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;

import java.util.List;
import java.util.Map;

public class CardsHeld extends Observable{
    //SINGLETON PATTERN
    private static CardsHeld INSTANCE;
    private CardsHeld(){};
    public synchronized static CardsHeld getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CardsHeld();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private List<CardRecord> cardsHeld;
    private Map<CardRecord, Boolean> cardPlayability;





    //GETTERS
    public List<CardRecord> getCardsHeld() {
        return cardsHeld;
    }
    public Map<CardRecord, Boolean> getCardPlayability() {
        return cardPlayability;
    }
    public CardRecord getCard(int index){
        return cardsHeld.get(index);
    }
    public boolean getCardPlayability(int cardIndex){
        CardRecord card = cardsHeld.get(cardIndex);
        return cardPlayability.get(card);
    }
    public int getAmountHeld(){
        return cardsHeld.size();
    }





    //SETTER
    public void setCardsHeld(List<CardRecord> cardsHeld, Map<CardRecord, Boolean> cardPlayability) {
        this.cardsHeld = cardsHeld;
        this.cardPlayability = cardPlayability;
        super.updateObservers();
    }
}
