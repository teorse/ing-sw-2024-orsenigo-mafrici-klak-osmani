package Client.Model.Records;

import Model.Cards.Corner;
import Model.Cards.CornerDirection;
import Model.Utility.Artifacts;

import java.io.Serializable;
import java.util.Map;

public record CardVisibilityRecord(Artifacts cardColor, Map<CornerDirection, Corner> corners, Map<CornerDirection, Boolean> cornerVisibility) implements Serializable {
}
