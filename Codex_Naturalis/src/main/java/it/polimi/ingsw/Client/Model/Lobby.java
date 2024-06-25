package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.util.List;

/**
 * Singleton class representing the lobby state in the client-side model.
 */
public class Lobby extends Observable {

    // Singleton instance
    private static Lobby INSTANCE;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private Lobby(){}

    /**
     * Retrieves the singleton instance of the Lobby class.
     *
     * @return The singleton instance of Lobby.
     */
    public synchronized static Lobby getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Lobby();
        }
        return INSTANCE;
    }

    // Lobby attributes
    private String lobbyName;
    private int targetNumberUsers;
    private List<LobbyUserColors> availableUserColors;
    private boolean gameStartable;
    private LobbyUsers lobbyUsers;

    /**
     * Retrieves the name of the lobby.
     *
     * @return The name of the lobby.
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Retrieves the target number of users that the lobby aims to accommodate.
     *
     * @return The target number of users.
     */
    public int getTargetNumberUsers() {
        return targetNumberUsers;
    }

    /**
     * Retrieves the list of available user colors that users can choose from in the lobby.
     *
     * @return The list of available user colors as {@link LobbyUserColors}.
     */
    public List<LobbyUserColors> getAvailableUserColors() {
        return availableUserColors;
    }

    /**
     * Checks if the lobby is currently in a startable state for the game.
     *
     * @return {@code true} if the game is startable, {@code false} otherwise.
     */
    public boolean isGameStartable() {
        return gameStartable;
    }

    /**
     * Retrieves the instance of LobbyUsers associated with the lobby.
     *
     * @return The instance of LobbyUsers.
     */
    public LobbyUsers getLobbyUsers() {
        return lobbyUsers;
    }

    /**
     * Sets the attributes of the lobby based on the provided {@link LobbyRecord} and notifies observers.
     *
     * @param lobby The {@link LobbyRecord} containing updated lobby information.
     */
    public void setLobby(LobbyRecord lobby){
        setLobbyName(lobby.lobbyName());
        setTargetNumberUsers(lobby.targetNumberUsers());
        setAvailableUserColors(lobby.availableUserColors());
        setGameStartable(lobby.gameStartable());
        super.updateObservers();
    }

    /**
     * Sets the name of the lobby and notifies observers of the change.
     *
     * @param lobbyName The new name of the lobby.
     */
    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
        super.updateObservers();
    }

    /**
     * Sets the target number of users for the lobby and notifies observers of the change.
     *
     * @param targetNumberUsers The new target number of users for the lobby.
     */
    public void setTargetNumberUsers(int targetNumberUsers) {
        this.targetNumberUsers = targetNumberUsers;
        super.updateObservers();
    }

    /**
     * Sets the available user colors for the lobby and notifies observers of the change.
     *
     * @param availableUserColors The updated list of available user colors.
     */
    public void setAvailableUserColors(List<LobbyUserColors> availableUserColors) {
        this.availableUserColors = availableUserColors;
        super.updateObservers();
    }

    /**
     * Sets the game startable status of the lobby and notifies observers of the change.
     *
     * @param gameStartable The new game startable status of the lobby.
     */
    public void setGameStartable(boolean gameStartable) {
        this.gameStartable = gameStartable;
        super.updateObservers();
    }
}
