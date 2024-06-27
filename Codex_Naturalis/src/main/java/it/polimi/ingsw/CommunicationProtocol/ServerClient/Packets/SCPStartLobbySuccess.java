package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.io.Serial;
import java.util.List;

/**
 * Packet sent from the server to the client indicating a successful start of a lobby.
 * Contains the {@code LobbyRecord} and a list of {@code LobbyUserRecord} representing the lobby's state.
 * Upon receiving this packet, the client invokes {@code startLobbySuccess} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPStartLobbySuccess implements ServerClientPacket{
    @Serial
    private static final long serialVersionUID = -7181004148327909726L;
    private final LobbyRecord lobbyRecord;
    private final List<LobbyUserRecord> lobbyUsers;

    /**
     * Constructs a {@code SCPStartLobbySuccess} instance with the specified lobby record and list of lobby users.
     *
     * @param lobbyRecord The {@code LobbyRecord} representing the details of the started lobby.
     * @param lobbyUsers  The list of {@code LobbyUserRecord} representing the users in the started lobby.
     */
    public SCPStartLobbySuccess(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {
        this.lobbyRecord = lobbyRecord;
        this.lobbyUsers = lobbyUsers;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code startLobbySuccess} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method informs the client about the successful start of a lobby,
     *                         providing the lobby record and the list of lobby users.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.startLobbySuccess(lobbyRecord, lobbyUsers);
    }
}
