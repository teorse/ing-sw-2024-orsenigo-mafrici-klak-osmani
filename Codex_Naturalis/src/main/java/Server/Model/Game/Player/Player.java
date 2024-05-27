package Server.Model.Game.Player;

import Client.Model.Records.*;
import Server.Model.Game.Cards.Card;
import Server.Model.Game.Objectives.Objective;
import Network.ServerClient.Packets.SCPUpdateClientGameState;
import Network.ServerClient.Packets.SCPUpdateSecret;
import Network.ServerClient.Packets.SCPUpdateSpecificPlayer;
import Server.Interfaces.LayerUser;
import Server.Model.Lobby.LobbyUser;
import Server.Model.Lobby.LobbyUserConnectionStates;
import Server.Model.Lobby.ObserverRelay;

import java.util.*;
import java.util.logging.Logger;

/**
 * The Model.Player.Player Class contains all the information about the player, and it stores contextual information about the
 * current game that relates individually to players like what cards have been placed, what cards are being held etc.
 */
public class Player implements LayerUser {

    //ATTRIBUTES
    private final LobbyUser user;
    private int roundsCompleted;
    private final List<Card> cardsHeld;
    private final Map<Card, Boolean> cardPlayability;
    private final List<Objective> secretObjectives;
    private final CardMap cardMap;
    private PlayerStates playerState;
    private int points;
    private int objectivesCompleted;
    private boolean winner;

    private final ObserverRelay observer;

    private final Logger logger;





    //CONSTRUCTOR
    public Player(LobbyUser user, ObserverRelay gameObserverRelay) {
        logger = Logger.getLogger(Player.class.getName());
        logger.info("Initializing player: "+user.getUsername());

        this.user = user;
        this.observer = gameObserverRelay;
        this.cardMap = new CardMap(gameObserverRelay, user.getUsername());
        this.cardsHeld = new ArrayList<>();
        this.cardPlayability = new HashMap<>();
        this.secretObjectives = new ArrayList<>();
        this.playerState = PlayerStates.WAIT;
        this.roundsCompleted = 0;
        this.points = 0;
        this.objectivesCompleted = 0;
        this.winner = false;

        logger.fine("Player: "+user.getUsername()+" initialized");
    }





    //GETTERS
    @Override
    public String getUsername() {
        return user.getUsername();
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
        observer.update(new SCPUpdateSpecificPlayer(this.toTransferableDataObject()));

        if(user.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE))
            observer.update(user.getUsername(), new SCPUpdateClientGameState(playerState));
    }
    public void setWinner(){
        winner = true;
    }





    //METHODS
    //todo update javadoc
    /**
     * Method allows to select an objective from the list of secret objective candidates and adds it to
     * the player's actually confirmed objectives that will be used to count points at the end of the game.
     * @param selectedObjective Index of the objective candidate the player wants to select.
     */
    public void addSecretObjective(Objective selectedObjective){
        secretObjectives.add(selectedObjective);

        observer.update(user.getUsername(), new SCPUpdateSecret(this.toSecretPlayer()));
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
        logger.info("Adding card to player: "+getUsername());
        logger.fine("Card being added is:\n"+card.toRecord());
        //Creates a CardPlayability object which immediately reflects the card's playability given the player's
        //current cardMap.
        cardsHeld.add(card);
        updatePlayableSides();

        logger.finest("Printing all cards held");
        for(int i = 0; i < cardsHeld.size(); i++){
            CardRecord cardHeldRecord = cardsHeld.get(i).toRecord();
            logger.finest("Card in position "+i+":\n"+cardHeldRecord);
        }

        observer.update(user.getUsername(), new SCPUpdateSecret(this.toSecretPlayer()));
    }

    /**
     * Updates playable sides attribute of all cards in cardsHeld List.
     */
    private void updatePlayableSides() {
        for(Card cardHeld : cardsHeld){
            cardPlayability.put(cardHeld, cardHeld.isPlaceable(cardMap));
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
     */
    public void playCard(int cardIndex, int coordinateIndex, boolean faceUp) {
        Card playedCard;

        try {
            playedCard = cardsHeld.remove(cardIndex);
        }
        catch (IndexOutOfBoundsException i){
            //todo add better exception handling
            throw new RuntimeException("Index provided is not a valid index");
        }


        boolean cardCanBeFaceUp = cardPlayability.remove(playedCard);

        //If the card can't be played faceUp but the player provided faceUp = true, throws an exception
        //Otherwise the card is played as expected.
        if(!cardCanBeFaceUp && faceUp)
            //todo add better exception handling
            throw new RuntimeException("You can't play this card faceUp!");

        //Updates player's points after playing the card.
        points = points + cardMap.place(playedCard ,coordinateIndex,faceUp);

        //Updates the playable sides of the remaining cards held after placing the card in the card map.
        updatePlayableSides();

        observer.update(new SCPUpdateSpecificPlayer(this.toTransferableDataObject()));
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
        List<CardRecord> cardsHeldRecord = new ArrayList<>();
        Map<CardRecord, Boolean> cardPlayabilityRecord = new HashMap<>();

        for(Card cardHeld : cardsHeld){
            cardsHeldRecord.add(cardHeld.toRecord());
            cardPlayabilityRecord.put(cardHeld.toRecord(), cardPlayability.get(cardHeld));
        }

        if(!secretObjectives.isEmpty())
            return new PlayerSecretInfoRecord(cardsHeldRecord, cardPlayabilityRecord, secretObjectives.getFirst().toRecord());
        else
            return new PlayerSecretInfoRecord(cardsHeldRecord, cardPlayabilityRecord, null);
    }

    public PlayerRecord toTransferableDataObject() {return new PlayerRecord(user.getUsername(), playerState, roundsCompleted, points, objectivesCompleted, winner);}

    public Map.Entry<String, CardMapRecord> toCardMapRecord(){
        return new AbstractMap.SimpleEntry<String, CardMapRecord>(user.getUsername(), cardMap.toTransferableDataObject());
    }
}


