package it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects;

import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.io.Serializable;
import java.util.List;

/**
 * A record representing a pool of cards in the game.
 * It contains information about the covered card color and a list of visible cards.
 */
public record CardPoolRecord(Artifacts coveredCardColor, List<CardRecord> visibleCards) implements Serializable {
}
