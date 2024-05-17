package Client;

import Client.Controller.ClientController;
import Client.Controller.UserInputListener;
import Client.Model.ClientModel;

public class ClientMain {
    public static void main(String[] args) {
        ClientModel model = new ClientModel();
        ClientController controller = new ClientController(model);
        UserInputListener inputListener = new UserInputListener(controller);

        Thread inputListenerThread = new Thread(inputListener);
        inputListenerThread.start();
    }
}
