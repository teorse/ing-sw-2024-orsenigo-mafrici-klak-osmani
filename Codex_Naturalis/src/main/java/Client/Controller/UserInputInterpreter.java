package Client.Controller;

import Client.Network.ClientConnector;
import Network.ClientServerPacket.ClientServerPacket;

import java.util.ArrayList;
import java.util.List;

public class UserInputInterpreter implements Runnable{
    private String input;
    private ClientConnector clientConnector;

    public UserInputInterpreter(String input, ClientConnector clientConnector){
        this.input = input;
        this.clientConnector = clientConnector;
    }

    @Override
    public void run() {
        List<String> header = new ArrayList<>();
        List<String> payload = new ArrayList<>();

        String[] inputSplit = input.split(" ");

        header.add(inputSplit[0]);
        header.add(inputSplit[1]);

        for(int i = 2; i < inputSplit.length; i++)
            payload.add(inputSplit[i]);

        ClientServerPacket message = new ClientServerPacket(header, payload);
        clientConnector.sendPacket(message);
    }
}
