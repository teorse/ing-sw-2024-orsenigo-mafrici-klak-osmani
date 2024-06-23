package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;

import java.util.List;

public class LobbyPreviews extends Observable {
    //SINGLETON PATTERN
    private static LobbyPreviews INSTANCE;
    private LobbyPreviews() {}
    public synchronized static LobbyPreviews getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LobbyPreviews();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private List<LobbyPreviewRecord> lobbyPreviews;





    //GETTERS
    public List<LobbyPreviewRecord> getLobbyPreviews() {
        return lobbyPreviews;
    }
    public LobbyPreviewRecord getLobbyPreviewByIndex(int index) {
        return lobbyPreviews.get(index);
    }





    //SETTERS
    public void setLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviews) {
        this.lobbyPreviews = lobbyPreviews;
        super.updateObservers();
    }
}
