package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyRoles;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Singleton class representing the collection of lobby users in the client-side model.
 */
public class LobbyUsers extends Observable {
    // Singleton instance
    private static LobbyUsers INSTANCE;

    // Attributes
    private List<LobbyUserRecord> lobbyUserRecords;
    private final Map<String, LobbyUserColors> lobbyUserColors;
    private final Logger logger;

    /**
     * Private constructor to enforce singleton pattern and initialize attributes.
     */
    private LobbyUsers() {
        lobbyUserRecords = new ArrayList<>();
        lobbyUserColors = new HashMap<>();
        logger = Logger.getLogger(LobbyUsers.class.getName());
        logger.info("Initializing LobbyUsers Class");
    }

    /**
     * Retrieves the singleton instance of the LobbyUsers class.
     *
     * @return The singleton instance of LobbyUsers.
     */
    public synchronized static LobbyUsers getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LobbyUsers();
        }
        return INSTANCE;
    }

    /**
     * Retrieves a copy of the list of lobby user records.
     *
     * @return A list containing lobby user records.
     */
    public List<LobbyUserRecord> getLobbyUserRecords() {
        logger.info("getLobbyUserRecords method called");
        logger.fine("LobbyUserRecords contents are: " + lobbyUserRecords);

        return new ArrayList<>(lobbyUserRecords);
    }

    /**
     * Retrieves the username of a lobby user record by its index in the list.
     *
     * @param index The index of the lobby user record.
     * @return The username of the lobby user at the specified index.
     */
    public String getLobbyUserNameByIndex(int index) {
        return lobbyUserRecords.get(index).username();
    }

    /**
     * Retrieves the number of lobby user records.
     *
     * @return The number of lobby user records.
     */
    public int size() {
        return lobbyUserRecords != null ? lobbyUserRecords.size() : 0;
    }

    /**
     * Retrieves the lobby user colors associated with a specific username.
     *
     * @param username The username of the lobby user.
     * @return The lobby user colors associated with the username.
     */
    public LobbyUserColors getLobbyUserColors(String username) {
        return lobbyUserColors.get(username);
    }

    /**
     * Retrieves a copy of the map containing lobby user colors.
     *
     * @return A map containing lobby user colors.
     */
    public Map<String, LobbyUserColors> getLobbyUserColorsMap() {
        return new HashMap<>(lobbyUserColors);
    }

    /**
     * Retrieves the connection state of a lobby user by their username.
     *
     * @param username The username of the lobby user.
     * @return The connection state of the lobby user.
     */
    public LobbyUserConnectionStates getLobbyUsersConnectionState(String username) {
        for (LobbyUserRecord lobbyUserRecord : lobbyUserRecords) {
            if (lobbyUserRecord.username().equals(username)) {
                return lobbyUserRecord.connectionStatus();
            }
        }
        return null;
    }

    /**
     * Sets the list of lobby user records, updates associated colors, and checks admin status for MyPlayer.
     *
     * @param lobbyUserRecords The updated list of lobby user records.
     */
    public void setLobbyUserRecords(List<LobbyUserRecord> lobbyUserRecords) {
        logger.info("setLobbyUserRecords method called");
        logger.fine("Parameter lobbyUserRecords are: " + lobbyUserRecords);

        this.lobbyUserRecords = new ArrayList<>(lobbyUserRecords);

        for (LobbyUserRecord user : lobbyUserRecords) {
            lobbyUserColors.put(user.username(), user.color());
        }

        for (LobbyUserRecord user : lobbyUserRecords) {
            if (user.username().equals(MyPlayer.getInstance().getUsername())) {
                MyPlayer.getInstance().setIsAdmin(user.role().equals(LobbyRoles.ADMIN));
                break;
            }
        }

        logger.fine("Object state of lobbyUserRecords variable: " + this.lobbyUserRecords);
        super.updateObservers();
    }
}
