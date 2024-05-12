package Model.Player;

import Client.Model.Records.CardMapRecord;
import Client.Model.Records.CardRecord;
import Client.Model.Records.PlayerRecord;
import Client.Model.Records.PlayerSecretInfoRecord;
import Model.Cards.Card;
import Model.Objectives.Objective;
import Network.ServerClientPacket.SCPUpdateSecret;
import Network.ServerClientPacket.SCPUpdateSpecificPlayer;
import Server.Interfaces.LayerUser;
import Server.Model.Lobby.LobbyUser;
import Server.Model.Lobby.LobbyUserConnectionStates;
import Server.Model.Lobby.ObserverRelay;

import java.util.*;

/**
 * The Model.Player.Player Class contains all the information about the player, and it stores contextual information about the
 * current game that relates individually to players like what cards have been placed, what cards are being held etc.
 */
public class Player implements LayerUser {

    //ATTRIBUTES
    private final LobbyUser user;
    private int roundsCompleted;
    private final List<CardPlayability> cardsHeld;
    private final List<Objective> secretObjectiveCandidates;
    private final List<Objective> secretObjectives;
    private final CardMap cardMap;
    private PlayerStates playerState;
    private int points;
    private int objectivesCompleted;

    private final ObserverRelay sender;





    //CONSTRUCTOR
    public Player(LobbyUser user, ObserverRelay gameObserverRelay) {
        this.user = user;
        this.sender = gameObserverRelay;
        this.cardMap = new CardMap(gameObserverRelay, user.getUsername());
        this.cardsHeld = new ArrayList<>();
        this.secretObjectiveCandidates = new ArrayList<>();
        this.secretObjectives = new ArrayList<>();
        this.playerState = PlayerStates.WAIT;
        this.roundsCompleted = 0;
        this.points = 0;
        this.objectivesCompleted = 0;
    }





    //GETTERS
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    public LobbyUser getLobbyUser(){
        return user;
    }
    public int getRoundsCompleted() {
        return roundsCompleted;
    }
    public LobbyUserConnectionStates getConnectionStatus() {
        return user.getConnectionStatus();
    }
    public PlayerStates getPlayerState() {
        return playerState;
    }
    public int getPoints(){
        return this.points;
    }
    public int getObjectivesCompleted() {
        return objectivesCompleted;
    }





    //SETTERS
    public void setPlayerState(PlayerStates playerState){
        this.playerState = playerState;

        //todo
        if(sender != null)
            sender.update(new SCPUpdateSpecificPlayer(this.toTransferableDataObject()));
    }





    //METHODS

    public void addSecretObjectiveCandidate(Objective objective){
        secretObjectiveCandidates.add(objective);

        //todo
        if(sender != null)
            sender.update(user.getUsername(), new SCPUpdateSecret(this.toSecretPlayer()));
    }

    /**
     * Method allows to select an objective from the list of secret objective candidates and adds it to
     * the player's actually confirmed objectives that will be used to count points at the end of the game.
     * @param index Index of the objective candidate the player wants to select.
     */
    public void selectSecretObjective(int index){
        Objective selectedObjective = secretObjectiveCandidates.get(index);
        secretObjectives.add(selectedObjective);

        //todo
        if(sender != null)
            sender.update(user.getUsername(), new SCPUpdateSecret(this.toSecretPlayer()));
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
        //Creates a CardPlayability object which immediately reflects the card's playability given the player's
        //current cardMap.
        CardPlayability CP = new CardPlayability(card,false);
        CP.updatePlayability(cardMap);
        cardsHeld.add(CP);

        sender.update(user.getUsername(), new SCPUpdateSecret(this.toSecretPlayer()));
    }

    /**
     * Updates playable sides attribute of all cards in cardsHeld List.
     */
    private void updatePlayableSides() {
        for (CardPlayability cardPlayability : cardsHeld) {
            cardPlayability.updatePlayability(cardMap);
        }
    }

    /**
     * Method calls the place method in this player's CardMap to place the card, and returns the points gained by the player for
     * playing the card.<br>
     * Checks if the provided value for faceUp is compatible with the card's playability.
     * If it is then it furthers the request to CardMap.
     *
     * @param cardIndex
     * @param coordinateIndex
     * @param faceUp
     * @return returns the points gained by the player for playing the card.
     */
    public void playCard(int cardIndex, int coordinateIndex, boolean faceUp) {
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

        //Updates player's points after playing the card.
        points = points + cardMap.place(cardPlayability.getCard(),coordinateIndex,faceUp);

        //Updates the playable sides of the remaining cards held after placing the card in the card map.
        updatePlayableSides();

        sender.update(new SCPUpdateSpecificPlayer(this.toTransferableDataObject()));
    }

    /**
     * Method counts all the points a player will score with the shared and secret objectives given their current cardMap.<br>
     * Updates both the number of points a player has and the number of individual objectives the player has accomplished.
     */
    public void countAllPoints(List<Objective> sharedObjectives){
        //count points from secret objectives
        for(Objective objective : secretObjectives) {
            int pointsToAdd = objective.countPoints(cardMap);
            if(pointsToAdd != 0)
                objectivesCompleted++;
            points = points + pointsToAdd;
        }

        //count points from shared objectives
        for(Objective objective : sharedObjectives) {
            int pointsToAdd = objective.countPoints(cardMap);
            if(pointsToAdd != 0)
                objectivesCompleted++;
            points = points + pointsToAdd;
        }
    }





    //EQUALS HASH
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(user, player.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }

    public PlayerSecretInfoRecord toSecretPlayer() {
        Map<CardRecord, Boolean> cardsHeld = new HashMap<>();
        for (CardPlayability cardPlayability : this.cardsHeld) {
            cardsHeld.put(cardPlayability.getCard().toRecord(), cardPlayability.getPlayability());
        }

        return new PlayerSecretInfoRecord(cardsHeld, secretObjectives.getFirst().toRecord());
    }

    public PlayerRecord toTransferableDataObject() {return new PlayerRecord(user.getUsername(), playerState, roundsCompleted, points, objectivesCompleted);}

    public Map.Entry<PlayerRecord, CardMapRecord> toPlayerCardMapView(){
        return new AbstractMap.SimpleEntry<PlayerRecord, CardMapRecord>(toTransferableDataObject(), cardMap.toTransferableDataObject());
    }
}


