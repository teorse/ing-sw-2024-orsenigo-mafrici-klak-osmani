package Model.Player;

import Model.Cards.Card;
import Model.Objectives.Objective;
import Model.Utility.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Model.Player.Player Class contains all the information about the player, and it stores contextual information about the
 * current game that relates individually to players like what cards have been placed, what cards are being held etc.
 */
public class Player {

    //ATTRIBUTES
    private String nickname;
    private Color color;
    private int roundsCompleted;
    private List<CardPlayability> cardsHeld;
    private Objective secretObjective;
    /**
     * CardMap storing all the cards placed by the player and the relative counters.
     */
    private CardMap cardMap;
    private String connectionStatus;
    private PlayerStates playerState;





    //CONSTRUCTOR
    public Player(String nickname) {
        this.nickname = nickname;
        this.cardMap = new CardMap();
        this.cardsHeld = new ArrayList<>();
        this.playerState = PlayerStates.WAIT;
        this.roundsCompleted = 0;
    }





    //GETTERS
    public String getNickname() {
        return nickname;
    }
    public Color getColor() {
        return color;
    }
    public int getRoundsCompleted() {
        return roundsCompleted;
    }
    public List<CardPlayability> getCardsHeld() {
        return Collections.unmodifiableList(cardsHeld);
    }
    public CardPlayability getCardsHeld(int index) {
        return cardsHeld.get(index);
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
    public PlayerStates getPlayerState() {
        return playerState;
    }





    //SETTERS
    public void setColor(Color color) {
        this.color = color;
    }
    public void setSecretObjective(Objective secretObjective) {
        this.secretObjective = secretObjective;
    }
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    public void setPlayerState(PlayerStates playerState){
        this.playerState = playerState;
    }





    //METHODS

    public void incrementRoundsCompleted() {
        this.roundsCompleted++;
    }

    /**
     * Trasform Card to CardPlayability by adding a boolean which represent if that card is playable on both sides, set
     * as false at the beginning.
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
     * Updates playable sides attribute of all cards in cardsHeld List.
     */
    public void updatePlayableSides() {
        CardMap CM = this.cardMap;
        for (CardPlayability cardPlayability : cardsHeld) {
            cardPlayability.setPlayability(CM);
        }
    }

    /**
     * Method to gather inputs from user and call the Place method in cardMap
     *
     * @param cardIndex
     * @param coordinateIndex
     * @param faceUp
     */
    public void playCard(int cardIndex, int coordinateIndex, boolean faceUp) {
        cardMap.place(getCardsHeld(cardIndex).getCard(),coordinateIndex,faceUp);
    }
}


