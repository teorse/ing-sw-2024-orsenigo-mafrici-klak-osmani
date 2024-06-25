package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;

import java.util.List;

/**
 * Singleton class representing the game over state, including information
 * about the winners of the game.
 */
public class GameOver extends Observable {

    // Singleton instance
    private static GameOver INSTANCE;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private GameOver(){}

    /**
     * Retrieves the singleton instance of the GameOver class.
     *
     * @return The singleton instance of GameOver.
     */
    public synchronized static GameOver getInstance(){
        if(INSTANCE == null){
            INSTANCE = new GameOver();
        }
        return INSTANCE;
    }

    // GameOver attributes
    private List<PlayerRecord> winners;

    /**
     * Retrieves the list of player records representing the winners of the game.
     *
     * @return The list of winners as {@link PlayerRecord}.
     */
    public List<PlayerRecord> getWinners() {
        return winners;
    }

    /**
     * Retrieves the winner at the specified index from the list of winners.
     *
     * @param index The index of the winner to retrieve.
     * @return The {@link PlayerRecord} of the winner at the specified index.
     */
    public PlayerRecord getWinnerByIndex(int index) {
        return winners.get(index);
    }

    /**
     * Retrieves the specific winner by their username.
     *
     * @param username The username of the winner to retrieve.
     * @return The {@link PlayerRecord} of the winner with the specified username,
     *         or {@code null} if no such winner exists.
     */
    public PlayerRecord getSpecificWinner(String username) {
        for (PlayerRecord playerRecord : winners) {
            if (playerRecord.username().equals(username)) {
                return playerRecord;
            }
        }
        return null;
    }

    /**
     * Sets the list of winners and notifies observers of the change.
     *
     * @param winners The list of {@link PlayerRecord} representing the winners.
     */
    public void setWinners(List<PlayerRecord> winners) {
        this.winners = winners;
        super.updateObservers();
    }
}
