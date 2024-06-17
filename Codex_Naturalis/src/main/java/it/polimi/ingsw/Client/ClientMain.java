package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.UserInputListener;
import it.polimi.ingsw.Client.Model.ClientModel2;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ClientMain {
    public static void main(String[] args) {

        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String LogFileName = "Client:_"+timestamp+".log";

        //Setting up the logger
        //Give each client a different file to write to
        try(InputStream inputStream = ClientMain.class.getClassLoader().getResourceAsStream("config/loggingClient.properties"))
        {
            LogManager.getLogManager().readConfiguration(inputStream);

            LogManager.getLogManager().updateConfiguration((k) -> (o , n) ->{

                if(k.equals("java.util.logging.FileHandler.pattern"))
                    return LogFileName;
                else
                    return o;
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger logger = Logger.getLogger(ClientMain.class.getName());
        logger.info("Client started");


        ClientModel2 model = new ClientModel2();
        ClientController controller = new ClientController(model);
        UserInputListener inputListener = new UserInputListener(controller);

        Thread inputListenerThread = new Thread(inputListener);
        inputListenerThread.start();
    }
}
