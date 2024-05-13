package Client.Network;

import Client.Controller.ClientController;
import Network.ClientServer.Packets.ClientServerPacket;
import Network.NetworkConstants;
import Network.ServerClient.Packets.ServerClientPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The ClientConnectorSocket class is an implementation of the ClientConnector interface using sockets.<br>
 * It is responsible for establishing a connection with the server, listening for packets from the server,
 * and sending packets to the server.
 */
public class ClientConnectorSocket implements ClientConnector {
    //ATTRIBUTES
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private final ClientController controller;





    //CONSTRUCTOR
    /**
     * Constructs a ClientConnectorSocket object with the specified server IP and client controller.
     *
     * @param serverIp    The IP address of the server to connect to.
     * @param controller  The ClientController to interact with this client's local model.
     */
    public ClientConnectorSocket(String serverIp, ClientController controller){
        System.out.println("Initializing server handler");
        this.controller = controller;
        try {
            int serverPort = NetworkConstants.ServerSocketListenerPort;
            socket = new Socket(serverIp, serverPort);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException e){
            System.out.println(e);
            closeSocket();
        }
    }





    //INTERFACE METHODS
    /**
     * {@inheritDoc}
     * @param packet The packet to be sent to the server.
     */
    @Override
    public void sendPacket(ClientServerPacket packet) {
        try{
            oos.writeObject(packet);
            oos.flush();
            oos.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Listens for packets from the server and executes them using the client controller.
     */
    @Override
    public void run() {
        ServerClientPacket packet;
        //Listen for messages from the server
        try {
            while (!socket.isClosed()) {
                try {
                    packet = (ServerClientPacket) ois.readObject();
                    //When a packet is found, execute the packet
                    packet.execute(controller);
                } catch (ClassNotFoundException e) {
                    System.out.println("Received an unsupported packet class.");
                } catch (RuntimeException e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            System.out.println("The server just died, RIP.");
        }
        finally {
            closeSocket();
        }
    }

    /**
     * Closes the socket.
     */
    public void closeSocket(){
        try{
            if(socket != null)
                socket.close();
        }
        catch (IOException e){
            System.out.println("Problem with closing the socket");
            if(!socket.isClosed())
                System.out.println("Socket is still open");
            else if(socket.isClosed())
                System.out.println("Socket is closed");
        }
    }
}
