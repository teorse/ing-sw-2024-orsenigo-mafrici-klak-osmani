package Client.Model;

import Model.Player.CardPlayability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecretPlayer {
    private final Map<CardView,Boolean> cardsHeld;
    private final ObjectiveView secretObjectives;

    public SecretPlayer(List<CardPlayability> cardsHeld, ObjectiveView secretObjectives) {
        this.cardsHeld = new HashMap<>();
        for (CardPlayability cardPlayability : cardsHeld) {
            this.cardsHeld.put(cardPlayability.getCard().toCardView(), cardPlayability.getPlayability());
        }
        this.secretObjectives = secretObjectives;
    }

    public Map<CardView, Boolean> getCardsHeld() {
        return cardsHeld;
    }

    public ObjectiveView getSecretObjectives() {
        return secretObjectives;
    }
}