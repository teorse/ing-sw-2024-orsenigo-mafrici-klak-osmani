package Client;

import Client.Controller.ClientController;
import Client.Controller.UserInputListener;
import Client.Model.ClientModel;
import Client.Network.ClientConnector;
import Client.Network.ClientConnectorRMI;
import Client.Network.ClientConnectorSocket;

import java.net.ConnectException;
import java.util.Scanner;
import java.util.Set;

public class ClientMain {
    public static void main(String[] args) {
        ClientModel model = new ClientModel();
        ClientController controller = new ClientController();
        String serverIp;

        Scanner scanner = new Scanner(System.in);
        String input = "";

        System.out.println("Type the ip address of the server, leave empty for localhost");
        input = scanner.nextLine();
        if(input.equals(""))
            serverIp = "127.0.0.1";
        else
            serverIp = input;

        System.out.println("Socket or RMI?");
        input = scanner.nextLine();

        ClientConnector clientConnector = null;

        try {
            if (input.equals("Socket")) {
                clientConnector = new ClientConnectorSocket(serverIp, controller);
            }

            if (input.equals("RMI"))
                clientConnector = new ClientConnectorRMI(serverIp, controller);

            Thread handler = new Thread(clientConnector);
            handler.start();

            Thread inputListener = new Thread(new UserInputListener(clientConnector));
            inputListener.start();
        }
        catch (ConnectException | java.rmi.ConnectException e ){
            System.out.println("Couldn't connect to the server");
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            System.out.println(threadSet);
        }
    }
}
