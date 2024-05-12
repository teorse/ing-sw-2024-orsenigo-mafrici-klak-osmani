package Client.Controller;

import Client.Network.ClientConnector;

import java.util.Scanner;

public class UserInputListener implements Runnable{
    private ClientConnector clientConnector;
    private final Scanner scanner = new Scanner(System.in);

    public  UserInputListener(ClientConnector clientConnector){
        this.clientConnector = clientConnector;
    }

    @Override
    public void run() {
        while(true){
            String input = scanner.nextLine();
            UserInputInterpreter interpreter = new UserInputInterpreter(input, clientConnector);
            Thread thread = new Thread(interpreter);
            thread.start();
        }
    }
}
