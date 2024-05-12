package Client;

import Client.Controller.ClientController;
import Client.Controller.UserInputListener;
import Client.Model.ClientModel;
import Client.Network.ClientConnector;
import Client.Network.ClientConnectorRMI;
import Client.Network.ClientConnectorSocket;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        ClientModel model = new ClientModel();
        ClientController controller = new ClientController();

        Scanner scanner = new Scanner(System.in);
        String input = "";

        System.out.println("Socket or RMI?");
        input = scanner.nextLine();

        ClientConnector clientConnector = null;

        if(input.equals("Socket"))
            clientConnector = new ClientConnectorSocket("127.0.0.1", controller);

        if(input.equals("RMI"))
            clientConnector = new ClientConnectorRMI("127.0.0.1", controller);

        Thread handler = new Thread(clientConnector);
        handler.start();

        Thread inputListener = new Thread(new UserInputListener(clientConnector));
        inputListener.start();
    }
}
