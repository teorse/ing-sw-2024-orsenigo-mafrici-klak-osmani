package Server.Network.ClientHandler;

import Network.ClientServerPacket.ClientServerPacket;
import Network.ServerClientPacket.Demo.SCPPrintPlaceholder;
import Network.ServerClientPacket.ServerClientPacket;
import Server.Controller.InputHandler.InputHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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





    //CONSTRUCTOR
    /**
     * Constructs a ClientHandlerSocket object with the specified socket.
     *
     * @param socket The socket representing the connection to the client.
     */
    public ClientHandlerSocket(Socket socket){
        this.socket = socket;

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e){
            System.out.println("Problem in creating streams for: "+socket);
            e.printStackTrace();
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
        sendPacket(new SCPPrintPlaceholder("Welcome to the server, please Log in or Sign up."));

        //Listen for messages on the input stream.
        try {
            //Outer try layer catches socket exceptions to break the while's execution
            while (!socket.isClosed()) {


                try {
                    //Middle try layer catches serialization exceptions without breaking listening while loop
                    ClientServerPacket received = (ClientServerPacket) ois.readObject();

                    //Start new thread to handle the input so that this thread can go back to listening for more inputs
                    Thread thread = new Thread(() -> serverInputHandler.handleInput(received));
                    thread.start();
                }
                catch (ClassNotFoundException e) {
                    System.out.println("Exception in client Handler "+socket);
                    e.printStackTrace();
                }
            }
        }
        catch(IOException e){
            System.out.println("Lost connection to client: "+socket);
            e.printStackTrace();
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
            oos.writeObject(packet);
            oos.flush();
            oos.reset();
        }
        catch (IOException e){
            e.printStackTrace();
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
        try {
            if (socket != null && !socket.isClosed())
                socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
