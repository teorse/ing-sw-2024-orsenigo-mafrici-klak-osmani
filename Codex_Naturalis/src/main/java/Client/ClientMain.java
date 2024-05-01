package Client;

import Client.Controller.ClientController;
import Client.Controller.UserInputListener;
import Client.Model.ClientModel;
import Client.Network.ServerHandler;
import Client.Network.ServerHandlerSocket;

public class ClientMain {
    public static void main(String[] args) {
        ClientModel model = new ClientModel();
        ClientController controller = new ClientController();

        ServerHandler serverHandler = new ServerHandlerSocket("127.0.0.1", controller);
        Thread handler = new Thread(serverHandler);
        handler.start();

        Thread inputListener = new Thread(new UserInputListener(serverHandler));
        inputListener.start();
    }
}
