package Server.Network.ClientHandler;

import CommunicationProtocol.ClientServer.Packets.ClientServerPacket;
import CommunicationProtocol.ServerClient.Packets.SCPConnectionAck;
import CommunicationProtocol.ServerClient.Packets.ServerClientPacket;
import Server.Controller.InputHandler.InputHandler;
import Utils.Utilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * ClientHandlerSocket class represents the socket implementation of the ClientHandler interface.
 * It handles communication with a client over a socket connection.
 */
public class ClientHandlerSocket implements ClientHandler, Runnable{
    //ATTRIBUTES
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private InputHandler serverInputHandler;
    private final Socket socket;
    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Constructs a ClientHandlerSocket object with the specified socket.
     *
     * @param socket The socket representing the connection to the client.
     */
    public ClientHandlerSocket(Socket socket){

        logger = Logger.getLogger(ClientServerPacket.class.getName());
        logger.info("Initializing Client Handler Socket");

        this.socket = socket;

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            logger.info("Client Handler socket initialized");
        }
        catch (IOException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("IOException caught in Client Handler Socket Constructor while opening socket streams.\nStacktrace:\n"+stackTraceString);
            closeSocket();
        }
    }





    //RUN
    /**
     * Listens for messages from the client and handles them accordingly.
     * Messages are parsed by the serverInputHandler and processed.
     * Exceptions are caught and sent back to the client for handling.
     * @see InputHandler
     */
    @Override
    public void run() {
        logger.info("Client Handler Socket is running");

        logger.fine("Sending SCP ConnectionAck to Client "+socket);
        sendPacket(new SCPConnectionAck("Welcome to the server, please Log in or Sign up."));

        //Listen for messages on the input stream.
        try {
            //Outer try layer catches socket exceptions to break the while's execution
            while (!socket.isClosed()) {

                try {
                    //Middle try layer catches serialization exceptions without breaking listening while loop
                    ClientServerPacket received = (ClientServerPacket) ois.readObject();
                    logger.fine("Message received in client handler socket from client "+socket);

                    //Start new thread to handle the input so that this thread can go back to listening for more inputs
                    Thread thread = new Thread(() -> serverInputHandler.handleInput(received));
                    thread.start();
                }
                catch (ClassNotFoundException e) {
                    String stackTraceString = Utilities.StackTraceToString(e);
                    logger.warning("ClassNotFoundException caught in Client Handler Socket Constructor while reading object from socket.\nStacktrace:\n"+stackTraceString);
                }
            }
        }
        //todo implement more "graceful" disconnection protocol
        catch(IOException e){
            System.out.println("Lost connection to client: "+socket);
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("Lost connection to client: "+socket+"\nStacktrace:\n"+stackTraceString);
        }
        finally {
            serverInputHandler.clientDisconnectionProcedure();
            closeSocket();
        }
    }





    //INTERFACE METHODS

    /**
     * {@inheritDoc}
     * @param packet The packet to be sent to the client.
     */
    @Override
    public void sendPacket(ServerClientPacket packet) {
        try {

            logger.fine("Sending message to client "+socket);

            oos.writeObject(packet);
            oos.flush();
            oos.reset();
        }
        catch (IOException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("IOException caught in Client Handler Socket while sending message to client"+socket+".\nStacktrace:\n"+stackTraceString);
        }
    }

    /**
     * {@inheritDoc}
     * @param inputHandler The InputHandler object to be set for the client.
     * @see InputHandler
     */
    @Override
    public void setInputHandler(InputHandler inputHandler) {
        this.serverInputHandler = inputHandler;
    }





    //CLEAN UP METHOD

    /**
     * Closes the socket.
     */
    private void closeSocket(){
        logger.info("Closing socket");
        try {
            if (socket != null && !socket.isClosed())
                socket.close();
        }
        catch (IOException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("IOException caught in Client Handler Socket while closing the socket.\nStacktrace:\n"+stackTraceString);
        }
    }
}
