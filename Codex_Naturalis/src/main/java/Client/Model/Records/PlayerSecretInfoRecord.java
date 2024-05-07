package Client.Model.Records;

import java.io.Serializable;
import java.util.Map;

public record PlayerSecretInfoRecord(Map<CardRecord, Boolean> cardsHeld, ObjectiveRecord secretObjectives) implements Serializable {

}