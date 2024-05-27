package Client.Network;

import Client.Controller.ClientController;
import Client.Model.ClientModel;
import Client.Model.States.ConnectionState;
import Network.ClientServer.Packets.ClientServerPacket;
import Network.NetworkConstants;
import Network.ServerClient.Packets.ServerClientPacket;
import Utils.Utilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

/**
 * The ClientConnectorSocket class is an implementation of the ClientConnector interface using sockets.<br>
 * It is responsible for establishing a connection with the server, listening for packets from the server,
 * and sending packets to the server.
 */
public class ClientConnectorSocket implements ClientConnector, Runnable {
    //ATTRIBUTES
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private final ClientController controller;
    private final Logger logger;
    private final ClientModel model;





    //CONSTRUCTOR
    /**
     * Constructs a ClientConnectorSocket object with the specified server IP and client controller.
     *
     * @param serverIp    The IP address of the server to connect to.
     * @param controller  The ClientController to interact with this client's local model.
     */
    public ClientConnectorSocket(String serverIp, ClientController controller, ClientModel model) throws SocketTimeoutException {
        this.controller = controller;
        this.model = model;
        logger = Logger.getLogger(ConnectionState.class.getName());

        try {
            int serverPort = NetworkConstants.ServerSocketListenerPort;
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverIp, serverPort), NetworkConstants.ServerSocketTimeOut);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            //Setting this clientConnector to listen.
            Thread thread = new Thread(this);
            thread.start();
        }
        catch(SocketTimeoutException e){
            //Thrown when the server does not respond, possibly wrong ip address.
            throw new SocketTimeoutException(e.getMessage());
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
            String stackTrace = Utilities.StackTraceToString(e);
            logger.warning("The server just died, RIP.\n" + "Stack Trace:\n" + stackTrace);
            model.resetConnection();
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
