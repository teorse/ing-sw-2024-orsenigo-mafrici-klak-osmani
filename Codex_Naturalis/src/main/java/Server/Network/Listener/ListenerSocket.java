package Server.Network.Listener;

import Network.NetworkConstants;
import Server.Controller.InputHandler.ServerInputHandler;
import Server.Controller.ServerController;
import Server.Network.ClientHandler.ClientHandler;
import Server.Network.ClientHandler.ClientHandlerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ListenerSocket class is responsible for listening to incoming TCP connection requests from clients.<br>
 * When a request is received, it creates a ClientHandler to further handle the client's connection.
 */
public class ListenerSocket implements Runnable{
    //ATTRIBUTES
    private ServerSocket serverSocket;
    private final ServerController serverController;





    //CONSTRUCTOR
    /**
     * Constructs a ListenerSocket object with the specified ServerController.
     *
     * @param serverController The ServerController object to interact with the server.
     */
    public ListenerSocket(ServerController serverController){
        this.serverController = serverController;
        try {
            serverSocket = new ServerSocket(NetworkConstants.ServerSocketListenerPort);
        }
        catch (IOException e){
            e.printStackTrace();
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
        try {

            //Running infinite loop to accept client requests.
            while (!serverSocket.isClosed()) {
                System.out.println("Server is Listening on socket...");

                // Accept incoming request
                Socket socket = serverSocket.accept();
                System.out.println("New client request received on socket: " + socket);

                System.out.println("Creating a new socket handler for this client...");
                ClientHandler handler = new ClientHandlerSocket(socket);

                //Giving the ClientHandler an Input interpreter to interact with the server
                handler.setInputHandler(new ServerInputHandler(handler, serverController));
                System.out.println("Socket Handler Created");

                // Start the thread for the new clientHandler.
                Thread thread = new Thread(handler);
                thread.start();
                System.out.println("Socket Handler Thread started");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
       finally {
           closeServerSocket();
        }
    }

    /**
     * Closes the server socket.
     */
    private void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
