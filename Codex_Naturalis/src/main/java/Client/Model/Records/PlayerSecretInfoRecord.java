package Client.Model.Records;

import java.util.HashMap;
import java.util.Map;

public record PlayerSecretInfoRecord(Map<CardRecord, Boolean> cardsHeld, ObjectiveRecord secretObjectives) {
    public PlayerSecretInfoRecord(Map<CardRecord, Boolean> cardsHeld, ObjectiveRecord secretObjectives) {
        this.cardsHeld = new HashMap<>();
        this.secretObjectives = secretObjectives;
    }
}