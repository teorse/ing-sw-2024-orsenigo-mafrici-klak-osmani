package Model.Cards;

import Model.Cards.*;
import Model.Other.*;
import Model.Utility.*;

import java.util.*;

/**
 *
 */
public class CardStarterBuilder {
    //ATTRIBUTES
    private Map<Artifacts, Integer> centralArtifacts = new HashMap<>();
    private Map<CornerOrientation, Corner> corners = new HashMap<>();

    /**
     * Default constructor
     */
    public CardStarterBuilder() {
    }

    //SETTERS
    public CardStarterBuilder withCentralArtifacts(Map<Artifacts, Integer> centralArtifacts) {
        this.centralArtifacts = centralArtifacts;
        return this;
    }

    public CardStarterBuilder withCorners(Map<CornerOrientation, Corner> corners) {
        this.corners = corners;
        return this;
    }

    //CARD BUILDER
    public CardStarter build() {
        CardStarter card = (CardStarter) CardFactory.createCard();
        return card
                .setCentralArtifacts(centralArtifacts)
                .setCorners(corners);
    }
}
