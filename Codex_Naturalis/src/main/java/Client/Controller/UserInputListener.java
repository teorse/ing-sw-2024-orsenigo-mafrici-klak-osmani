package Client.Controller;

import Client.Network.ServerHandler;

import java.util.Scanner;

public class UserInputListener implements Runnable{
    private ServerHandler serverHandler;
    private final Scanner scanner = new Scanner(System.in);

    public  UserInputListener(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
    }

    @Override
    public void run() {
        while(true){
            String input = scanner.nextLine();
            UserInputInterpreter interpreter = new UserInputInterpreter(input, serverHandler);
            Thread thread = new Thread(interpreter);
            thread.start();
        }
    }
}
