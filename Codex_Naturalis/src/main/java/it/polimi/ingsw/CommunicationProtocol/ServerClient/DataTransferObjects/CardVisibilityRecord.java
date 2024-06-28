package it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects;

import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerDirection;
import it.polimi.ingsw.Server.Model.Game.Artifacts;

import java.io.Serializable;
import java.util.Map;

/**
 * A record representing the visibility of a card in the game.
 * It contains information about the card color, corners, and the visibility of each corner.
 */
public record CardVisibilityRecord(Artifacts cardColor, Map<CornerDirection, Corner> corners, Map<CornerDirection, Boolean> cornerVisibility, CardRecord card, Boolean faceUp) implements Serializable {
}
