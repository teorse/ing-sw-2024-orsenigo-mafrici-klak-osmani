package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

/**
 * Packet sent from the server to the client to indicate a successful join operation to a lobby.
 * Contains details about the lobby and its users.
 * Upon receiving this packet, the client invokes {@code joinLobbySuccessful} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPJoinLobbySuccessful implements ServerClientPacket {
    private final LobbyRecord lobbyRecord;
    private final List<LobbyUserRecord> lobbyUsers;

    /**
     * Constructs a {@code SCPJoinLobbySuccessful} instance with the specified lobby record and list of lobby users.
     *
     * @param lobbyRecord The {@code LobbyRecord} object containing details about the joined lobby.
     * @param lobbyUsers  The list of {@code LobbyUserRecord} objects representing users in the lobby.
     */
    public SCPJoinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers){
        this.lobbyRecord = lobbyRecord;
        this.lobbyUsers = lobbyUsers;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code joinLobbySuccessful} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method notifies the client about the successful join operation,
     *                         providing details about the lobby and its users.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.joinLobbySuccessful(lobbyRecord, lobbyUsers);
    }
}
