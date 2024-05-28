package it.polimi.ingsw.Server;

import it.polimi.ingsw.Server.Controller.ServerController;
import it.polimi.ingsw.Server.Model.Server.ServerModel;
import it.polimi.ingsw.Server.Network.Listener.ListenerRMI;
import it.polimi.ingsw.Server.Network.Listener.ListenerSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The main class to start the server application.<br>
 * This class initializes the server model, controller, and starts the server socket listener.
 */
public class ServerMain {
    private static ServerModel serverModel;
    private static ServerController serverController;
    private static ListenerSocket listenerSocket;
    private static ListenerRMI listenerRMI;


    /**
     * The main method to start the server application.<br>
     * It initializes the server model, controller, initializes the local RMI registry, and starts the server socket and RMI listeners.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        //Setting up the logger
        try(InputStream inputStream = ServerMain.class.getClassLoader().getResourceAsStream("config/loggingServer.properties"))
        {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger logger = Logger.getLogger(ServerMain.class.getName());
        logger.info("Server application started");

        ServerCentralManager serverCentralManager = new ServerCentralManager();

        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            System.out.println("Enter command (start, stop, status, exit): ");
            command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "start":
                    if (!serverCentralManager.isRunning()) {
                        serverCentralManager.startServer();
                    } else {
                        System.out.println("Server is already running.");
                    }
                    break;

                case "stop":
                    if (serverCentralManager.isRunning()) {
                        serverCentralManager.stopServer();
                    } else {
                        System.out.println("Server is not running.");
                    }
                    break;

                case "status":
                    if (serverCentralManager.isRunning()) {
                        System.out.println("Server is running.");
                    } else {
                        System.out.println("Server is stopped.");
                    }
                    break;

                case "exit":
                    if (serverCentralManager.isRunning()) {
                        serverCentralManager.stopServer();
                    }
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Unknown command. Please enter 'start', 'stop', 'status', or 'exit'.");
            }
        }
    }
}
