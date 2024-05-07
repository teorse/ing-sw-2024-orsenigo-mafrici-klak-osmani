package Server.Model.Lobby.Game;

import Model.Player.Player;
import Network.ServerClientPacket.ServerClientPacket;
import Server.Model.Lobby.LobbyUser;
import Server.Network.ClientHandler.ClientHandler;

import java.util.Map;

public class GameModelUpdatesSender {

    private final Map<LobbyUser, ClientHandler> lobbyUserConnection;

    public GameModelUpdatesSender(Map<LobbyUser, ClientHandler> lobbyUserConnection){
        this.lobbyUserConnection = lobbyUserConnection;
    }

    public void updateClientGameModel(Player player, ServerClientPacket packet){
        LobbyUser user = player.getLobbyUser();
        ClientHandler connection = lobbyUserConnection.get(user);

        connection.sendPacket(packet);
    }

    public void updateClientGameModel(ServerClientPacket packet){

        ClientHandler connection;
        for(Map.Entry<LobbyUser, ClientHandler> entry : lobbyUserConnection.entrySet()) {
            connection = entry.getValue();

            connection.sendPacket(packet);
        }
    }
}
