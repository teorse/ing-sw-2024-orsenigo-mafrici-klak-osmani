package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.UserInputListener;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.CommunicationProtocol.LanIpFinder;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The ClientMain class is the entry point for the client application.
 * It initializes the client model, controller, and starts the user input listener.
 */
public class ClientMain {
    public static void main(String[] args) {

        // Generate a timestamp for the log file name
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String LogFileName = "Client:_"+timestamp+".log";

        // Setting up the logger
        // Give each client a different file to write to
        try (InputStream inputStream = ClientMain.class.getClassLoader().getResourceAsStream("config/loggingClient.properties")) {
            LogManager.getLogManager().readConfiguration(inputStream);

            LogManager.getLogManager().updateConfiguration((k) -> (o, n) -> {
                if (k.equals("java.util.logging.FileHandler.pattern"))
                    return LogFileName;
                else
                    return o;
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a logger instance for this class
        Logger logger = Logger.getLogger(ClientMain.class.getName());
        logger.info("Client started");


        //Parsing arguments entered by user from TUI
        //Each argument should have a tag beforehand.

        //Given that there are few parameters in the current implementation expected to be passed as arguments (2),
        //no dedicated enum class has been created yet, if in the future the number of keywords will grow
        //it would be a good idea to create the enum.

        if(args != null && args.length != 0){
            List<String> arguments = Arrays.stream(args).toList();

            int CLIGraphicsIndex = arguments.indexOf("graphics");
            int ipIndex = arguments.indexOf("ip");

            if(CLIGraphicsIndex != -1){
                logger.fine("detected keyword graphics in the args");
                int graphics = Integer.parseInt(args[CLIGraphicsIndex+1]);
                if(graphics > 1 || graphics < 0) {
                    logger.warning("Invalid value for parameter graphics, value is: " + graphics + ", but expected 1 or 0.");
                    System.exit(1234);
                }
                else{
                    if(graphics == 0)
                        ClientModel.getInstance().setFancyGraphics(false);
                    else {
                        ClientModel.getInstance().setFancyGraphics(true);
                    }
                }
            }

            if(ipIndex != -1){
                String ipAddress = args[ipIndex+1];

                boolean validIp = LanIpFinder.isValidIPAddress(ipAddress);

                if(validIp)
                    LanIpFinder.getInstance().setIp(ipAddress);
                else{
                    logger.warning("The ip passed as parameter is not correctly formatted, shutting down the server");
                    System.exit(1234);
                }
            }
        }
        else
            logger.info("No parameters detected in args, proceeding normally with the default execution");



        // Initialize the client model
        ClientModel model = ClientModel.getInstance();

        // Initialize the refresh manager
        RefreshManager.getInstance();

        // Initialize the client controller
        ClientController controller = new ClientController(model);

        // Initialize and start the user input listener
        UserInputListener inputListener = new UserInputListener(controller);
        Thread inputListenerThread = new Thread(inputListener);
        inputListenerThread.start();
    }
}
