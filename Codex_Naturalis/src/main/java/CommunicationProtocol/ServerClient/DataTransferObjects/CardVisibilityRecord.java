package CommunicationProtocol.ServerClient.DataTransferObjects;

import Server.Model.Game.Cards.Corner;
import Server.Model.Game.Cards.CornerDirection;
import Server.Model.Game.Artifacts;

import java.io.Serializable;
import java.util.Map;

public record CardVisibilityRecord(Artifacts cardColor, Map<CornerDirection, Corner> corners, Map<CornerDirection, Boolean> cornerVisibility) implements Serializable {
}
