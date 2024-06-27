package it.polimi.ingsw.Server.Network.Listener;

import it.polimi.ingsw.CommunicationProtocol.NetworkConstants;
import it.polimi.ingsw.Server.Controller.InputHandler.ClientInputHandler;
import it.polimi.ingsw.Server.Controller.ServerController;
import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandler;
import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandlerSocket;
import it.polimi.ingsw.Utils.Utilities;

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
     * Constructs a ListenerSocket object.
     */
    public ListenerSocket(){
        logger = Logger.getLogger(ListenerSocket.class.getName());
        logger.fine("Initializing Socket Listener");

        serverController = ServerController.getInstance();
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
