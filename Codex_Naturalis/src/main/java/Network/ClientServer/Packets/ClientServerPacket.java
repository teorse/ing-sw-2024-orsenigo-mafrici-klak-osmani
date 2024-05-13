package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

import java.io.Serializable;

/**
 * The ClientServerPacket class represents a packet sent from the client to the server.
 * It encapsulates communication between the client and the server.
 */
public interface ClientServerPacket extends Serializable{
    void execute(ServerInputExecutor executor);
}