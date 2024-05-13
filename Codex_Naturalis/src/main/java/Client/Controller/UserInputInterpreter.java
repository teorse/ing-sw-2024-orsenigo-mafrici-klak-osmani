package Client.Controller;

import Client.Network.ClientConnector;
import Model.Game.CardPoolTypes;
import Network.ClientServer.CommandTypes;
import Network.ClientServer.GameCommands;
import Network.ClientServer.LobbyCommands;
import Network.ClientServer.Packets.*;
import Network.ClientServer.ServerCommands;
import Server.Model.Lobby.LobbyUserColors;

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


        ClientServerPacket message = null;

        CommandTypes commandType = CommandTypes.valueOf(header.getFirst());
        String command = header.get(1);

        switch (commandType){
            case SERVER -> {
                ServerCommands serverCommand = ServerCommands.valueOf(command);
                switch (serverCommand){
                    case SIGN_UP -> message = new CSPSignUp(payload.getFirst(), payload.get(1));
                    case LOG_IN -> message = new CSPLogIn(payload.getFirst(), payload.get(1));
                    case LOG_OUT -> message = new CSPLogOut();
                    case START_LOBBY -> message = new CSPStartLobby(payload.getFirst(), Integer.parseInt(payload.get(1)));
                    case JOIN_LOBBY -> message = new CSPJoinLobby(payload.getFirst());
                    case VIEW_LOBBY_PREVIEWS -> new CSPViewLobbyPreviews();
                    case STOP_VIEWING_LOBBY_PREVIEWS -> new CSPStopViewingLobbyPreviews();
                }
            }

            case LOBBY -> {
                LobbyCommands lobbyCommand = LobbyCommands.valueOf(command);
                switch (lobbyCommand){
                    case CHANGE_COLOR -> message = new CSPChangeColor(LobbyUserColors.valueOf(payload.getFirst()));
                    case START_GAME -> message = new CSPStartGame();
                    case QUIT -> message = new CSPQuitLobby();
                }
            }

            case GAME -> {
                GameCommands gameCommand = GameCommands.valueOf(command);
                switch (gameCommand){
                    case DRAW_CARD -> message = new CSPDrawCard(CardPoolTypes.valueOf(payload.getFirst()), Integer.parseInt(payload.get(1)));
                    case PLAY_CARD -> message = new CSPPlayCard(Integer.parseInt(payload.getFirst()), Integer.parseInt(payload.get(1)), Boolean.parseBoolean(payload.get(2)));
                    case PICK_OBJECTIVE -> message = new CSPPickObjective(Integer.parseInt(payload.getFirst()));
                }
            }
        }


        if(message != null)
            clientConnector.sendPacket(message);
    }
}
