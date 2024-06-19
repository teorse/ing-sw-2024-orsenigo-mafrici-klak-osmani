package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.CardPools;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardPoolRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.util.List;

public class CardPoolView extends LiveComponent{

    private final CardPoolTypes cardPoolType;

    public CardPoolView(CardPoolTypes cardPoolType) {
        super();
        this.cardPoolType = cardPoolType;
        refreshObserved();
    }


    @Override
    public void print() {
        CardPools cardPools = CardPools.getInstance();
        CardPoolRecord cardPool = cardPools.getCardPoolByType(cardPoolType);
        Artifacts topDeckCardColor = cardPool.coveredCardColor();
        List<CardRecord> visibleCards = cardPool.visibleCards();

        switch (cardPoolType){
            case RESOURCE -> {
                out.println("\nRESOURCE POOL:");
                if (topDeckCardColor != null) {
                    out.println("1 - Artifact Type: " + topDeckCardColor.name() + " (covered card)");
                } else
                    out.println("1 - The covered resource deck is empty");
                for (int i = 0; i < visibleCards.size(); i++) {
                    CardRecord card = visibleCards.get(i);
                    out.print((i + 2) + " - ");
                    new CardView(card).print();
                }
            }
            case GOLDEN -> {
                out.println("\nGOLDEN POOL:");
                if (topDeckCardColor != null) {
                    out.println("1 - Artifact Type: " + topDeckCardColor.name() + " (covered card)");
                } else
                    out.println("1 - The covered golden deck is empty");
                for (int i = 0; i < visibleCards.size(); i++) {
                    CardRecord card = visibleCards.get(i);
                    out.print((i + 2) + " - ");
                    new CardView(card).print();
                }
            }
        }
    }

    @Override
    public void cleanObserved() {RefreshManager.getInstance().removeObserved(this, CardPools.getInstance());}

    @Override
    public void refreshObserved() {RefreshManager.getInstance().addObserved(this, CardPools.getInstance());}
}
