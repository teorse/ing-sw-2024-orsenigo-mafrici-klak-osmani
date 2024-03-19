/**
 * This Class stores all the information about the resources shared by all players during the game and other shared metrics.<br>
 */

import java.util.ArrayList;

public class Game {
    /**
     * Array containing players taking part to this game session.
     */
    private ArrayList<Player> players;
    /**
     * Stores number of rounds completed by the table.<br>
     * Does not count the round in progress
     */
    private int roundsCompleted;
    /**
     * Stores the current deck of Golden Cards.
     */
    private Deck goldenDeck;
    /**
     * Stores the current deck of Resource Cards.
     */
    private Deck resourcesDeck;
    /**
     * Array that initially stores all possible objectives.
     */
    private ArrayList<Objective> objectives;
    /**
     * First visible Golden Card.
     */
    private GoldenCard goldenCardVisible1;
    /**
     * Second visible Golden Card.
     */
    private GoldenCard goldenCardVisible2;
    /**
     * First visible Resource Card.
     */
    private Card resourceCardVisible1;
    /**
     * Second visible Resource Card.
     */
    private Card resourceCardVisible2;
    /**
     * First shared objective.
     */
    private Objective objectiveVisible1;
    /**
     * Second shared objective.
     */
    private Objective objectiveVisible2;

    /**
     * Default Constructor.<br>
     * Initializes a Game object by taking Players ArrayList, Golden and Resource Deck, and ArrayList of Objectives.
     * @param players       ArrayList of Players.
     * @param goldenDeck    Deck of Golden Cards.
     * @param resourcesDeck Deck of Resource Cards.
     * @param objectives    ArrayList of Objectives.
     */
    public Game(ArrayList<Player> players, Deck goldenDeck, Deck resourcesDeck, ArrayList<Objective> objectives) {
        this.players = players;
        this.goldenDeck = goldenDeck;
        this.resourcesDeck = resourcesDeck;
        this.objectives = objectives;
    }

    /**
     * Method to access the goldenDeck.
     * @return  Deck goldenDeck.
     */
    public Deck getGoldenDeck() {
        return goldenDeck;
    }

    /**
     * Method to access the resourceDeck.
     * @return  Deck resourceDeck.
     */
    public Deck getResourcesDeck() {
        return resourcesDeck;
    }

    /**
     * Method to access the Objectives ArrayList.
     * @return  ArrayList of Objectives.
     */
    public ArrayList<Objective> getObjectives() {
        return objectives;
    }

    /**
     * Method to access the first visible golden card.
     * @return  GoldenCard goldenCardVisible1.
     */
    public GoldenCard getGoldenCardVisible1() {
        return goldenCardVisible1;
    }

    /**
     * Method to set the first visible golden card.
     * @param goldenCardVisible1    GoldenCard card to be set as new goldenCardVisible1.
     */
    public void setGoldenCardVisible1(GoldenCard goldenCardVisible1) {
        this.goldenCardVisible1 = goldenCardVisible1;
    }

    /**
     * Method to access the second visible golden card.
     * @return  GoldenCard goldenCardVisible2.
     */
    public GoldenCard getGoldenCardVisible2() {
        return goldenCardVisible2;
    }

    /**
     * Method to set the second visible golden card.
     * @param goldenCardVisible2    GoldenCard card to be set as new goldenCardVisible2.
     */
    public void setGoldenCardVisible2(GoldenCard goldenCardVisible2) {
        this.goldenCardVisible2 = goldenCardVisible2;
    }

    /**
     * Method to access the first visible resource card.
     * @return  ResourceCard resourceCardVisible1.
     */
    public Card getResourceCardVisible1() {
        return resourceCardVisible1;
    }

    /**
     * Method to set the first visible resource card.
     * @param resourceCardVisible1    ResourceCard card to be set as new resourceCardVisible1.
     */
    public void setResourceCardVisible1(Card resourceCardVisible1) {
        this.resourceCardVisible1 = resourceCardVisible1;
    }

    /**
     * Method to access the second visible resource card.
     * @return  ResourceCard resourceCardVisible2.
     */
    public Card getResourceCardVisible2() {
        return resourceCardVisible2;
    }

    /**
     * Method to set the second visible resource card.
     * @param resourceCardVisible2    ResourceCard card to be set as new resourceCardVisible1.
     */
    public void setResourceCardVisible2(Card resourceCardVisible2) {
        this.resourceCardVisible2 = resourceCardVisible2;
    }

    /**
     * Method to access the first visible Objective.
     * @return  Objective objectiveVisible1.
     */
    public Objective getObjectiveVisible1() {
        return objectiveVisible1;
    }

    /**
     * Method to set the first visible objective.
     * @param objectiveVisible1    Objective to be set as new objectiveVisible1.
     */
    public void setObjectiveVisible1(Objective objectiveVisible1) {
        this.objectiveVisible1 = objectiveVisible1;
    }

    /**
     * Method to access the second visible Objective.
     * @return  Objective objectiveVisible2.
     */
    public Objective getObjectiveVisible2() {
        return objectiveVisible2;
    }

    /**
     * Method to set the second visible objective.
     * @param objectiveVisible2    Objective to be set as new objectiveVisible1.
     */
    public void setObjectiveVisible2(Objective objectiveVisible2) {
        this.objectiveVisible2 = objectiveVisible2;
    }

    /**
     * Method to access the ArrayList storing the Players participating in the Game.<br>
     * Can be used to then manipulate the Array by updating information about the players.
     * @return ArrayList of participating players.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns integer with rounds already completed.
     * @return  int roundsCompleted.
     */
    public int getRoundsCompleted() {
        return roundsCompleted;
    }

    /**
     * Increments by 1 the counter for rounds played.
     */
    public void increaseRoundsCompleted(){
        this.roundsCompleted++;
    }
}
