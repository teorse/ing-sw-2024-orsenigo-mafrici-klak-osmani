package Client.Model.Records;

import Model.Game.CardPoolTypes;

import java.io.Serializable;
import java.util.Map;

public record CardPoolDrawabilityRecord(Map<CardPoolTypes, Boolean> cardDrawability) implements Serializable {
}