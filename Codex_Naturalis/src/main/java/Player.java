/**
 * The Player Class contains all the information about the player, and it stores contextual information about the
 * current game that relates individually to players like what cards have been placed, what cards are being held etc.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    /**
     * String containing player's nickname.
     */
    private String nickname;
    /**
     * String containing player's hex color code.
     */
    private String color; //May be better to use enum class, only 4 color
    /**
     * Field storing the amount of points currently being held by the player.
     */
    private int points;
    /**
     * Field storing the number of rounds completed by the player. Only updates AFTER a player commits his moves to a round.
     */
    private int roundsCompleted;
    /**
     * ArrayList containing the cards being held by the player.
     */
    private List<CardPlayability> cardsHeld;
    /**
     * HashMap storing the information about the cards previously placed during the game by the player.
     */
    private NestedMap cardsPlaced;
    /**
     * An ArrayList containing all the allowed coordinates where the player can place his cards.
     */
    private List<Coordinates> availablePlacements;
    /**
     * A map that is functionally a counter for the Resources currently held by the player.
     */
    private Map<Resource, Integer> playerResourcesCounter;
    /**
     * A map that is functionally a counter for the Items currently held by the player.
     */
    private Map<Item, Integer> playerItemsCounter;
    /**
     * The secret objective held by the player.
     */
    private Objective secretObjective;
    /**
     * A String containing the current connection status of the player to the server.
     */
    private String connectionStatus;

    /**
     * Default Constructor, defines player by their nickname.
     * @param nickname  String with player nickname.
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.cardsHeld = new ArrayList<>();
        this.playerResourcesCounter = new HashMap<>();
        this.playerItemsCounter = new HashMap<>();
        this.availablePlacements = new ArrayList<>();
        this.roundsCompleted = 0;
        this.points = 0;
    }

    /**
     * Method to get String with player's nickname.
     * @return String with player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Method to get player's color.
     * @return String with player's color.
     */
    public String getColor() {
        return color;
    }

    /**
     * Method to get player's points.
     * @return Int with player's points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Method to get amount of rounds completed by player.
     * @return Int with player's completed rounds.
     */
    public int getRoundsCompleted() {
        return roundsCompleted;
    }

    /**
     * Method to get the ArrayList with the cards currently held by the player.
     * @return ArrayList with cards held by player.
     */
    public List<CardPlayability> getCardsHeld() {
        return cardsHeld;
    }

    /**
     * Method to get the Hashmap with the placement of the cards played.
     * @return  DoubleHashMap with player's card placement.
     */
    public NestedMap getCardsPlaced() {
        return cardsPlaced;
    }

    /**
     * Method to get ArrayList with coordinates currently available for placement.
     * @return ArrayList of Coordinates available for placement.
     */
    public List<Coordinates> getAvailablePlacements() {
        return availablePlacements;
    }

    /**
     * Method to get player's Resources counter.
     * @return  Map<Resource, Integer> with Key Resource and value number of units held.
     */
    public Map<Resource, Integer> getPlayerResourcesCounter() {
        return playerResourcesCounter;
    }

    /**
     * Method to get player's Items counter.
     * @return  Map<Item, Integer> with Key Item and value number of units held.
     */
    public Map<Item, Integer> getPlayerItemsCounter() {
        return playerItemsCounter;
    }

    /**
     * Method to get player's secret objective.
     * @return Objective player's secret objective.
     */
    public Objective getSecretObjective() {
        return secretObjective;
    }

    /**
     * Method to get player's current connection status.
     * @return  String with player's current connection status.
     */
    public String getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * Method to set player's color.
     * @param color String containing player's color.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Adds specified amount of points to the player.
     * @param add   Int points to add to the player.
     */
    public void addPoints(int add) {
        this.points = this.points+add;
    }

    /**
     * Increments the rounds completed by the player by 1.
     */
    public void incrementRoundsCompleted() {
        this.roundsCompleted++;
    }

    /**
     * Method to set the player's secret objective.
     * @param secretObjective   Objective containing the player's secret objective.
     */
    public void setSecretObjective(Objective secretObjective) {
        this.secretObjective = secretObjective;
    }

    /**
     * Method to update player's connection status.
     * @param connectionStatus  String containing player's updated connection status.
     */
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
}
