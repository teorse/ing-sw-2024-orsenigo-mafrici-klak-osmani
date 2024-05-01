package Server.Model.Lobby.GameDemo;

import Server.Model.Lobby.Lobby;
import Server.Model.Lobby.LobbyUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final List<Player> players;
    private final Map<LobbyUser, Player> LobbyUserToPlayerMap;
    private final Lobby lobby;

    public Game(List<LobbyUser> lobbyUsers, Lobby lobby){
        this.lobby = lobby;

        LobbyUserToPlayerMap = new HashMap<>();
        players = new ArrayList<>();

        for(LobbyUser lobbyUser : lobbyUsers){
            Player player = new Player(lobbyUser);
            player.addListener(new PlayerDataListener(lobby));

            LobbyUserToPlayerMap.put(lobbyUser, player);
            players.add(player);
        }
    }

    public void appendPersonalData(Player player, String data){
        player.appendPersonalData(data);
    }

    public void appendPublicData(Player player, String data){
        player.appendPublicData(data);
    }

    public void appendPersonalPublicData(Player player, String personalData, String publicData){
        player.appendPersonalPublicData(personalData, publicData);
    }

    public Player getPlayerByLobbyUser(LobbyUser lobbyUser){
        return LobbyUserToPlayerMap.get(lobbyUser);
    }

    public void quitLayer(LobbyUser lobbyUser){
        Player player = LobbyUserToPlayerMap.remove(lobbyUser);
        players.remove(player);
    }
}
