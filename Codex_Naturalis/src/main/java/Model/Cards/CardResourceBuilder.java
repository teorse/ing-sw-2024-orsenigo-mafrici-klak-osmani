package Model.Cards;

import Model.Other.*;
import Model.Utility.*;

import java.util.Map;

/**
 * 
 */
public class CardResourceBuilder {
    //ATTRIBUTES
    private Artifacts cardColor;
    private int points;
    private Map<CornerOrientation, Corner> corners;





    /**
     * Default constructor
     */
    public CardResourceBuilder() {
    }





    //SETTERS
    public CardResourceBuilder withCardColor(Artifacts cardColor) {
        this.cardColor = cardColor;
        return this;
    }

    public CardResourceBuilder withPoints(int points) {
        this.points = points;
        return this;
    }

    public CardResourceBuilder withCorners(Map<CornerOrientation, Corner> corners) {
        this.corners = corners;
        return this;
    }





    //CARD BUILDER
    public Card build(){
        CardResource card = (CardResource) CardResourceFactory.createCard();

        return card
                .setCardColor(cardColor)
                .setPoints(points)
                .setCorners(corners);
    }
}