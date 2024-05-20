package Client;

import Client.Controller.ClientController;
import Client.Controller.UserInputListener;
import Client.Model.ClientModel;
import Server.ServerMain;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
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


        ClientModel model = new ClientModel();
        ClientController controller = new ClientController(model);
        UserInputListener inputListener = new UserInputListener(controller);

        Thread inputListenerThread = new Thread(inputListener);
        inputListenerThread.start();
    }
}
