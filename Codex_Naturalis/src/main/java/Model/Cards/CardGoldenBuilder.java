package Model.Cards;

import Model.Utility.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CardGoldenBuilder {
    //ATTRIBUTES
    private Artifacts cardColor;
    private int points;
    private Map<CornerOrientation, Corner> corners = new HashMap<>();
    private boolean requiresCorner;
    private Artifacts requiredArtifact;
    private Map<Artifacts, Integer> constraint;





    //SETTERS
    public CardGoldenBuilder withCardColor(Artifacts cardColor) {
        this.cardColor = cardColor;
        return this;
    }

    public CardGoldenBuilder withPoints(int points) {
        this.points = points;
        return this;
    }

    public CardGoldenBuilder withCorners(Map<CornerOrientation, Corner> corners) {
        this.corners = corners;
        return this;
    }
    public CardGoldenBuilder withRequiresCorner(boolean requiresCorner) {
        this.requiresCorner = requiresCorner;
        return this;
    }

    public CardGoldenBuilder withRequiredArtifact(Artifacts requiredArtifact) {
        this.requiredArtifact = requiredArtifact;
        return this;
    }

    public CardGoldenBuilder withConstraint(Map<Artifacts, Integer> constraint) {
        this.constraint = constraint;
        return this;
    }





    //CARD BUILDER
    public Card build(){
        CardGolden card = (CardGolden) CardGoldenFactory.createCard();

        return ((CardGolden)
                (card
                .setCardColor(cardColor)
                .setPoints(points)
                .setCorners(corners)))
                .setRequiresCorner(requiresCorner)
                .setRequiredArtifact(requiredArtifact)
                .setConstraint(constraint);
    }
}
