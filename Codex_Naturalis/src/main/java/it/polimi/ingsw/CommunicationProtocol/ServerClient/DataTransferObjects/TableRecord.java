package it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects;

import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents a record containing information about the table in a game.
 *
 * <p>A {@code TableRecord} contains details such as the top deck resource and golden cards, visible card records for resource and golden decks, and shared objectives.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record TableRecord(Map<CardPoolTypes, CardPoolRecord> cardPools,
                          List<ObjectiveRecord> sharedObjectives) implements Serializable {
}