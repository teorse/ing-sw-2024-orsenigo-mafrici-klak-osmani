/**
 * This Class stores all the information about the resources shared by all players during the game and other shared metrics.<br>
 */

import java.util.ArrayList;
import java.util.List;

public class Game {
    /**
     * Array containing players taking part to this game session.
     */
    private List<Player> players;
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
    private List<Objective> objectives;
    /**
     * ArrayList containing the visible golden cards.
     */
    private List<CardGolden> cardGoldenVisible = new ArrayList<>(2);
    /**
     * ArrayList containing the visible resource cards.
     */
    private List<CardResource> cardResourceVisible = new ArrayList<>(2);
    /**
     * List containing shared objectives.
     */
    private List<Objective> objectivesShared = new ArrayList<>(2);

    /**
     * Default Constructor.<br>
     * Initializes a Game object by taking Players ArrayList, Golden and Resource Deck, and ArrayList of Objectives.
     * @param players       ArrayList of Players.
     * @param goldenDeck    Deck of Golden Cards.
     * @param resourcesDeck Deck of Resource Cards.
     * @param objectives    ArrayList of Objectives.
     */
    public Game(List<Player> players, Deck goldenDeck, Deck resourcesDeck, List<Objective> objectives) {
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
    public List<Objective> getObjectives() {
        return objectives;
    }

    /**
     * Returns the visible golden card at position "index" without removing it from the List.
     * @param index position in List of card to be returned.
     * @return  CardGolden cardGoldenVisible1.
     */
    public CardGolden getCardGoldenVisible(int index) {
        return cardGoldenVisible.get(index);
    }

    /**
     * Method to set the visible golden card at position "index" in the List.
     * @param card      CardGolden card to be set as new cardGoldenVisible1.
     * @param index     position in List of card to be set.
     */
    public void setCardGoldenVisible(CardGolden card, int index) {
        this.cardGoldenVisible.set(index, card);
    }

    /**
     * Returns the visible resource card at position "index" without removing it from the List.
     * @param index position in List of card to be returned.
     * @return  CardGolden cardGoldenVisible1.
     */
    public CardResource getCardResourceVisible(int index) {
        return cardResourceVisible.get(index);
    }

    /**
     * Method to set the visible resource card at position "index" in the List.
     * @param card      CardResource card to be set as new cardGoldenVisible1.
     * @param index     position in List of card to be set.
     */
    public void setCardResourceVisible(CardResource card, int index) {
        this.cardResourceVisible.set(index, card);
    }

    /**
     * Returns the shared objective at position "index" without removing it from the List.
     * @param index position in List of objective to be returned.
     * @return  Objective shared objective at position "index" in List.
     */
    public Objective getObjectivesShared(int index) {
        return objectivesShared.get(index);
    }

    /**
     * Method to set shared objective at position "index" in the List.
     * @param objective     Objective to be set in List at position "index".
     * @param index         Position in List to set.
     */
    public void setObjectivesShared(Objective objective, int index) {
        this.objectivesShared.set(index, objective);
    }

    /**
     * Method to access the ArrayList storing the Players participating in the Game.<br>
     * Can be used to then manipulate the Array by updating information about the players.
     * @return ArrayList of participating players.
     */
    public List<Player> getPlayers() {
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
    public void incrementRoundsCompleted(){
        this.roundsCompleted++;
    }
}
