package Server.Network.Listener;

import CommunicationProtocol.NetworkConstants;
import Server.Controller.InputHandler.ClientInputHandler;
import Server.Controller.ServerController;
import Server.Network.ClientHandler.ClientHandler;
import Server.Network.ClientHandler.ClientHandlerSocket;
import Utils.Utilities;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * ListenerSocket class is responsible for listening to incoming TCP connection requests from clients.<br>
 * When a request is received, it creates a ClientHandler to further handle the client's connection.
 */
public class ListenerSocket implements Runnable{
    //ATTRIBUTES
    private ServerSocket serverSocket;
    private final ServerController serverController;
    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Constructs a ListenerSocket object with the specified ServerController.
     *
     * @param serverController The ServerController object to interact with the server.
     */
    public ListenerSocket(ServerController serverController){
        logger = Logger.getLogger(ListenerSocket.class.getName());
        logger.fine("Initializing Socket Listener");

        this.serverController = serverController;
        try {
            serverSocket = new ServerSocket(NetworkConstants.ServerSocketListenerPort);
            logger.fine("Socket Listener initialized");
        }
        catch (IOException e){
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("IOException caught in Listener Socket constructor:\n"+ stackTraceString);
            closeServerSocket();
        }
    }






    //LISTENER THREAD
    /**
     * Starts thread to listen for incoming connection requests.<br>
     * When a request is received, creates a ClientHandler to handle the client's connection.<br>
     * Each ClientHandler will have its own thread.
     */
    @Override
    public void run() {
        logger.fine("Socket Server Listener Started");
        try {

            //Running infinite loop to accept client requests.
            while (!serverSocket.isClosed()) {
                logger.info("Server is Listening on socket");
                System.out.println("Server is Listening on socket...");

                // Accept incoming request
                Socket socket = serverSocket.accept();
                logger.info("New client request received on socket from: " + socket);
                System.out.println("New client request received on socket: " + socket);

                System.out.println("Creating new socket handler...");
                logger.info("Creating new socket client handler for client: " + socket);
                ClientHandler handler = new ClientHandlerSocket(socket);

                //Giving the ClientHandler an Input interpreter to interact with the server
                logger.fine("Setting client Handler's input handler");
                handler.setInputHandler(new ClientInputHandler(handler, serverController));

                // Start the thread for the new clientHandler.
                Thread thread = new Thread(handler);
                thread.start();
                System.out.println("Client Handler started");
            }
        }
        catch (IOException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("IOException caught in Listener Socket thread:\n"+ stackTraceString);
        }
       finally {
            logger.info("Listener Socket Thread in finally block");
           closeServerSocket();
        }
    }

    /**
     * Closes the server socket.
     */
    private void closeServerSocket() {
        try {
            logger.fine("Closing server socket");
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            String stackTraceString = Utilities.StackTraceToString(e);
            logger.warning("IOException caught while closing server socket:\n"+ stackTraceString);
        }
    }
}
