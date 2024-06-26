package it.polimi.ingsw.Client.Network;

import it.polimi.ingsw.CommunicationProtocol.NetworkConstants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The RMIClientSocketFactory class is responsible for creating client sockets
 * for RMI (Remote Method Invocation) communication.
 */
public class RMIClientSocketFactory implements java.rmi.server.RMIClientSocketFactory {

    /**
     * Creates a new client socket and connects it to the specified IP address and port.
     *
     * @param ip the IP address of the server
     * @param port the port number of the server
     * @return the connected socket
     * @throws IOException if an I/O error occurs when creating the socket or connecting it
     */
    @Override
    public Socket createSocket(String ip, int port) throws IOException {
        // Create a new socket
        Socket socket = new Socket();

        // Connect the socket to the specified IP address and port with a timeout
        socket.connect(new InetSocketAddress(ip, port), NetworkConstants.ServerSocketTimeOut);

        return socket;
    }
}
