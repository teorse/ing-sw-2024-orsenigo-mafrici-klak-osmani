package Model.Player;

import Model.Cards.Card;
import Model.Objectives.Objective;

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
    private PlayerColors playerColor;
    private int roundsCompleted;
    private List<CardPlayability> cardsHeld;
    private List<Objective> secretObjectiveCandidates;
    private List<Objective> secretObjectives;
    /**
     * CardMap storing all the cards placed by the player and the relative counters.
     */
    private CardMap cardMap;
    private PlayerConnectionStatus connectionStatus;
    private PlayerStates playerState;





    //CONSTRUCTOR
    public Player(String nickname) {
        this.nickname = nickname;
        this.cardMap = new CardMap();
        this.cardsHeld = new ArrayList<>();
        this.secretObjectiveCandidates = new ArrayList<>();
        this.secretObjectives = new ArrayList<>();
        this.playerState = PlayerStates.WAIT;
        this.roundsCompleted = 0;
    }





    //GETTERS
    public String getNickname() {
        return nickname;
    }
    public PlayerColors getPlayerColor() {
        return playerColor;
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
    public CardMap getCardMap() {
        return cardMap;
    }
    public PlayerConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }
    public PlayerStates getPlayerState() {
        return playerState;
    }





    //SETTERS
    public void setPlayerColor(PlayerColors playerColors) {
        this.playerColor = playerColors;
    }
    public void setConnectionStatus(PlayerConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    public void setPlayerState(PlayerStates playerState){
        this.playerState = playerState;
    }





    //METHODS

    public void addSecretObjectiveCandidate(Objective objective){
        secretObjectiveCandidates.add(objective);
    }

    /**
     * Method allows to select an objective from the list of secret objective candidates and adds it to
     * the player's actually confirmed objectives that will be used to count points at the end of the game.
     * @param index Index of the objective candidate the player wants to select.
     */
    public void selectSecretObjective(int index){
        Objective selectedObjective = secretObjectiveCandidates.get(index);
        secretObjectives.add(selectedObjective);
    }

    public void incrementRoundsCompleted() {
        this.roundsCompleted++;
    }

    /**
     * Transform Card to CardPlayability by adding a boolean which represent if that card is playable on both sides, set
     * as false at the beginning.
     *
     * @param card chosen either from the four cards faceUp in the center of the table (a new card is then revealed to
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
     * Method to gather inputs from user and call the Place method in cardMap.<br>
     * Checks if the provided value for faceUp is compatible with the card's playability.
     * If it is then it furthers the request to CardMap.
     *
     * @param cardIndex
     * @param coordinateIndex
     * @param faceUp
     * @return returns the points gained by the player for playing the card.
     */
    public int playCard(int cardIndex, int coordinateIndex, boolean faceUp) {
        CardPlayability cardPlayability;

        try {
             cardPlayability = cardsHeld.get(cardIndex);
        }
        catch (IndexOutOfBoundsException i){
            throw new RuntimeException("Index provided is not a valid index");
        }


        boolean cardCanBeFaceUp = cardPlayability.getPlayability();

        //If the card can't be played faceUp but the player provided faceUp = true, throws an exception
        //Otherwise the card is played as expected.
        if(!cardCanBeFaceUp && faceUp)
            throw new RuntimeException("You can't play this card faceUp!");

        //returns the points awarded to the player for playing the card.
        return cardMap.place(cardPlayability.getCard(),coordinateIndex,faceUp);
    }
}


