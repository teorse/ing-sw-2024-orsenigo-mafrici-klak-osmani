package Model.Player;

import Model.Cards.Card;
import Model.Objectives.Objective;

import java.util.ArrayList;
import java.util.List;

/**
 * The Model.Player.Player Class contains all the information about the player, and it stores contextual information about the
 * current game that relates individually to players like what cards have been placed, what cards are being held etc.
 */
public class Player {

    //ATTRIBUTE
    private String nickname;
    private Color color;
    private int points;
    private int roundsCompleted;
    private List<CardPlayability> cardsHeld;
    private Objective secretObjective;
    /**
     * Model.Model.Player.Player.CardMap storing all the cards placed by the player and the relative counters.
     */
    private CardMap cardMap;
    private String connectionStatus;





    //CONSTRUCTOR
    public Player(String nickname) {
        this.nickname = nickname;
        this.cardMap = new CardMap();
        this.cardsHeld = new ArrayList<>();
        this.roundsCompleted = 0;
        this.points = 0;
    }





    //GETTER
    public String getNickname() {
        return nickname;
    }
    public Color getColor() {
        return color;
    }
    public int getPoints() {
        return points;
    }
    public int getRoundsCompleted() {
        return roundsCompleted;
    }
    public List<CardPlayability> getCardsHeld() {
        return cardsHeld;
    }
    public Objective getSecretObjective() {
        return secretObjective;
    }
    public CardMap getCardMap() {
        return cardMap;
    }
    public String getConnectionStatus() {
        return connectionStatus;
    }





    //SETTER
    public void setColor(Color color) {
        this.color = color;
    }
    public void setSecretObjective(Objective secretObjective) {
        this.secretObjective = secretObjective;
    }
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }





    //METHODS
    public void addPoints(int add) {
        this.points = this.points + add;
    }

    public void incrementRoundsCompleted() {
        this.roundsCompleted++;
    }

    /**
     * Trasform Card to CardPlayability by adding a boolean which represent if that card is playable on both sides.
     *
     * @param card choosen either from the four cards faceup in the center of the table (a new card is then revealed to
     *             replace the one just taken), or the first card from one of the two decks.
     */
    public void addCardHeld(Card card) {
        //Set by defualt the playability of card and then update when all the 3 card are held by the player
        CardPlayability CP = new CardPlayability(card,false);
        cardsHeld.add(CP);
    }

    /**
     * @param index
     * @return card from cardsHeld list at specified index.
     */
    public CardPlayability getCardsHeld(int index) {
        return cardsHeld.get(index);
    }

    /**
     * Updates playable sides attribute of all cards in cardsHeld List.
     */
    public void updatePlayableSides() {
        CardMap CM = this.cardMap;
        for (CardPlayability cardPlayability : cardsHeld) {
            cardPlayability.setPlayability(CM);
        }
    }
}

//public void playCard(int cardIndex, int coordinateIndex, boolean faceUp)
//method to gather inputs from user and call the Place method in cardMap

