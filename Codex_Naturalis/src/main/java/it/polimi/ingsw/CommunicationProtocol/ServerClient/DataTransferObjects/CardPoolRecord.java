package it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects;

import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.util.List;

public record CardPoolRecord(CardPoolTypes cardPoolType, Artifacts coveredCardColor, List<CardRecord> visibleCards) {
}
