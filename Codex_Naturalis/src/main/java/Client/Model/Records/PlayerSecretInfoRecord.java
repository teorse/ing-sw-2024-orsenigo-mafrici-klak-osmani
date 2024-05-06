package Client.Model.Records;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public record PlayerSecretInfoRecord(Map<CardRecord, Boolean> cardsHeld, ObjectiveRecord secretObjectives) implements Serializable {
    public PlayerSecretInfoRecord(Map<CardRecord, Boolean> cardsHeld, ObjectiveRecord secretObjectives)  {
        this.cardsHeld = new HashMap<>();
        this.secretObjectives = secretObjectives;
    }
}