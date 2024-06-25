package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;

import java.util.List;

/**
 * Singleton class representing the collection of lobby previews in the client-side model.
 */
public class LobbyPreviews extends Observable {
    // Singleton instance
    private static LobbyPreviews INSTANCE;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private LobbyPreviews() {}

    /**
     * Retrieves the singleton instance of the LobbyPreviews class.
     *
     * @return The singleton instance of LobbyPreviews.
     */
    public synchronized static LobbyPreviews getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LobbyPreviews();
        }
        return INSTANCE;
    }

    // Lobby previews attributes
    private List<LobbyPreviewRecord> lobbyPreviews;

    /**
     * Retrieves the list of lobby preview records.
     *
     * @return The list of lobby preview records.
     */
    public List<LobbyPreviewRecord> getLobbyPreviews() {
        return lobbyPreviews;
    }

    /**
     * Retrieves a specific lobby preview record by its index in the list.
     *
     * @param index The index of the lobby preview record to retrieve.
     * @return The lobby preview record at the specified index.
     */
    public LobbyPreviewRecord getLobbyPreviewByIndex(int index) {
        return lobbyPreviews.get(index);
    }

    /**
     * Sets the list of lobby preview records and notifies observers of the change.
     *
     * @param lobbyPreviews The updated list of lobby preview records.
     */
    public void setLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviews) {
        this.lobbyPreviews = lobbyPreviews;
        super.updateObservers();
    }
}
