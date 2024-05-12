package Server;

import Network.NetworkConstants;
import Server.Controller.ServerController;
import Server.Model.ServerModel;
import Server.Network.Listener.ListenerRMI;
import Server.Network.Listener.ListenerSocket;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The main class to start the server application.<br>
 * This class initializes the server model, controller, and starts the server socket listener.
 */
public class ServerMain {
    /**
     * The main method to start the server application.<br>
     * It initializes the server model, controller, initializes the local RMI registry, and starts the server socket and RMI listeners.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {

        // Creating and initializing the server model
        System.out.println("Creating Server Model");
        ServerModel serverModel = new ServerModel();

        // Creating the server controller and passing the server model
        System.out.println("Creating Server Controller");
        ServerController serverController = new ServerController(serverModel);

        // Creating the server socket listener and passing the server controller
        System.out.println("Creating Server socket Listener");
        ListenerSocket listenerSocket = new ListenerSocket(serverController);

        //Creating the server RMI listener and passing the server controller
        System.out.println("Creating Server RMI Listener");
        ListenerRMI listenerRMI = new ListenerRMI(serverController);

        // Starting the server listener threads
        Thread socketThread = new Thread(listenerSocket);
        Thread RMIThread = new Thread(listenerRMI);
        socketThread.start();
        RMIThread.start();
    }
}
