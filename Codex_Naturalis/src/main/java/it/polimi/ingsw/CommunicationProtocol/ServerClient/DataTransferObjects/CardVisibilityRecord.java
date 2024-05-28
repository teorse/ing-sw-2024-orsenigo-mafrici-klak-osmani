package it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects;

import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerDirection;
import it.polimi.ingsw.Server.Model.Game.Artifacts;

import java.io.Serializable;
import java.util.Map;

public record CardVisibilityRecord(Artifacts cardColor, Map<CornerDirection, Corner> corners, Map<CornerDirection, Boolean> cornerVisibility) implements Serializable {
}
