package Model.Player; /**
 * The Model.Player.Player Class contains all the information about the player, and it stores contextual information about the
 * current game that relates individually to players like what cards have been placed, what cards are being held etc.
 */

import Model.Objectives.Objective;

import java.util.ArrayList;
import java.util.List;

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
     * The secret objective held by the player.
     */
    private Objective secretObjective;
    /**
     * Model.Player.CardMap storing all the cards placed by the player and the relative counters.
     */
    private CardMap cardMap;
    /**
     * A String containing the current connection status of the player to the server.
     */
    private String connectionStatus;

    /**
     * Default Constructor, defines player by their nickname and initializes to 0 the various counters
     * and the cardsHeld List.
     * @param nickname  String with player nickname.
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.cardMap = new CardMap();
        this.cardsHeld = new ArrayList<>();
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
     * Method to get player's secret objective.
     * @return Model.Objectives.Objective player's secret objective.
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
     * @param secretObjective   Model.Objectives.Objective containing the player's secret objective.
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

    //public void addCardHeld(Card card){}
    //adds card given as parameter to the List of cards held

    //public Card getCardHeld(int index){}
    //returns card in from cardsHeld list at specified index.

    //public void updatePlayableSides(){}
    //method updates playable sides attribute of all cards in cardsHeld List.

    //public void playCard(int cardIndex, int coordinateIndex, boolean faceUp)
    //method to gather inputs from user and call the Place method in cardMap
}
