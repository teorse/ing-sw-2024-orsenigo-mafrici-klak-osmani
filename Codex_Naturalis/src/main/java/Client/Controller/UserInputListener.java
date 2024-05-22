package Client.Controller;

import Client.Model.ClientModel;
import Client.Model.States.ClientState;
import Client.Network.ClientConnector;

import java.util.Scanner;

public class UserInputListener implements Runnable{
    private final ClientController clientController;
    private final Scanner scanner = new Scanner(System.in);

    public  UserInputListener(ClientController clientController){
        this.clientController = clientController;
    }

    @Override
    public void run() {
        while(true){
            String input = scanner.nextLine();
            clientController.handleInput(input);
        }
    }
}
