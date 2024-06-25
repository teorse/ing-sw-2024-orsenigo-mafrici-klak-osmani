package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class representing the collection of players in the client-side model.
 */
public class Players extends Observable {
    // Singleton instance
    private static Players INSTANCE;

    // Attributes
    private final List<PlayerRecord> players;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private Players() {
        players = new ArrayList<>();
    }

    /**
     * Retrieves the singleton instance of the Players class.
     *
     * @return The singleton instance of Players.
     */
    public synchronized static Players getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Players();
        }
        return INSTANCE;
    }

    /**
     * Retrieves the list of players.
     *
     * @return A new list containing all player records.
     */
    public List<PlayerRecord> getPlayers() {
        synchronized (players) {
            return new ArrayList<>(players);
        }
    }

    /**
     * Retrieves the number of players currently stored.
     *
     * @return The number of players.
     */
    public int getPlayersSize() {
        synchronized (players) {
            return players.size();
        }
    }

    /**
     * Retrieves the username of a player at a specified index.
     *
     * @param index The index of the player whose username is to be retrieved.
     * @return The username of the player at the specified index.
     */
    public String getUsernameByIndex(int index) {
        return players.get(index).username();
    }

    /**
     * Sets the list of players to the provided list.
     *
     * @param players The list of players to set.
     */
    public void setPlayers(List<PlayerRecord> players) {
        synchronized (this.players) {
            this.players.clear();
            this.players.addAll(players);
        }
        super.updateObservers();
    }

    /**
     * Updates a specific player's record in the collection.
     *
     * @param player The updated player record.
     */
    public void setSpecificPlayer(PlayerRecord player) {
        for (int i = 0; i < players.size(); i++) {
            PlayerRecord currentPlayer = players.get(i);
            if (currentPlayer.username().equals(player.username())) {
                players.set(i, player);
                break;
            }
        }
        super.updateObservers();
    }
}
