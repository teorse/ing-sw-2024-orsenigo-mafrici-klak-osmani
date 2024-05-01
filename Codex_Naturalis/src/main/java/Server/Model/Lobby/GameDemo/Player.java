package Server.Model.Lobby.GameDemo;

import Server.Interfaces.LayerUser;
import Server.Model.Lobby.LobbyUser;

public class Player implements LayerUser {
    private LobbyUser user;
    private PlayerData playerData;

    public Player(LobbyUser user){
        this.user = user;
        playerData = new PlayerData();
    }

    public void appendPersonalData(String data){
        playerData.appendPersonalData(data);
    }

    public void appendPublicData(String data){
        playerData.appendPublicData(data);
    }

    public void appendPersonalPublicData(String personalData, String publicData){
        playerData.appendPersonalPublicData(personalData, publicData);
    }

    public void addListener(PlayerDataListener listener){
        listener.setObservedPlayer(this);
        playerData.addListener(listener);
    }

    public LobbyUser getUser(){
        return user;
    }

    @Override
    public String getUsername(){
        return user.getUsername();
    }
}
