/**
 * This Class stores all the information about the resources shared by all players during the game and other shared metrics.<br>
 */

import Model.Cards.Card;
import Model.Objectives.Objective;

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
    private int lastRound;
    /**
     * Stores the current deck of Golden Cards.
     */
    private Deck goldenDeck;
    /**
     * Stores the current deck of Resource Cards.
     */
    private Deck resourcesDeck;
    /**
     * Stores the deck of Starter Cards,
     */
    private Deck starterDeck;
    /**
     * Array that initially stores all possible objectives.
     */
    private List<Objective> objectives;
    /**
     * ArrayList containing the visible golden cards.
     */
    private List<Card> cardGoldenVisible = new ArrayList<>();
    /**
     * ArrayList containing the visible resource cards.
     */
    private List<Card> cardResourceVisible = new ArrayList<>();
    /**
     * List containing shared objectives.
     */
    private List<Objective> objectivesShared = new ArrayList<>();

    /**
     * Default Constructor.<br>
     * Initializes a Game object by taking Players ArrayList, Golden and Resource Deck, and ArrayList of Objectives.<br>
     * Automatically
     * @param players       ArrayList of Players.
     * @param visibleCards  Int number of cards for each type that will be visible to the player next to the decks.
     * @param goldenDeck    Deck of Golden Cards.
     * @param resourcesDeck Deck of Resource Cards.
     * @param objectives    ArrayList of Objectives.
     */
    public Game(List<Player> players, int visibleCards, Deck goldenDeck, Deck resourcesDeck, Deck starterDeck, List<Objective> objectives) {
        this.players = players;
        this.goldenDeck = goldenDeck;
        this.resourcesDeck = resourcesDeck;
        this.starterDeck = starterDeck;
        this.objectives = objectives;

        this.goldenDeck.shuffleDeck();
        this.resourcesDeck.shuffleDeck();
        this.starterDeck.shuffleDeck();

        for(int i=0; i<visibleCards; i++);{
            this.cardGoldenVisible.add(goldenDeck.pop());
            this.cardResourceVisible.add(resourcesDeck.pop());
        }
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
     * Method to access the starterDeck.
     * @return  Deck starterDeck.
     */
    public Deck getStarterDeck() {
        return starterDeck;
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
    public Card getCardGoldenVisible(int index) {
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
    public Card getCardResourceVisible(int index) {
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
     * @return  Model.Objectives.Objective shared objective at position "index" in List.
     */
    public Objective getObjectivesShared(int index) {
        return objectivesShared.get(index);
    }

    /**
     * Method to set shared objective at position "index" in the List.
     * @param objective     Model.Objectives.Objective to be set in List at position "index".
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

    //public boolean isGameEnding()
    //checks if cards are over to initiate the GAME ENDING part of the gameplay.
}
