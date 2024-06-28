package it.polimi.ingsw.Server.Model.Game.Player;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerSecretInfoRecord;
import it.polimi.ingsw.Exceptions.Game.Model.InvalidGameInputException;
import it.polimi.ingsw.Exceptions.Game.Model.Player.CoordinateIndexOutOfBounds;
import it.polimi.ingsw.Server.Model.Game.Cards.Card;
import it.polimi.ingsw.Server.Model.Game.Objectives.Objective;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateClientGameState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateSecret;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateSpecificPlayer;
import it.polimi.ingsw.Server.Interfaces.LayerUser;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUser;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;
import it.polimi.ingsw.Server.Model.Lobby.ObserverRelay;

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
    /**
     * Constructs a new Player with the specified user and observer relay.
     * Initializes the player with default values and sets up logging.
     *
     * @param user the LobbyUser representing the player
     * @param gameObserverRelay the ObserverRelay to update game state
     */
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
    /**
     * Sets the player's state and updates the observer with the player's new state.
     * If the player is online, it also sends an update to the client's game state.
     *
     * @param playerState the new state to set for the player
     */
    public void setPlayerState(PlayerStates playerState){
        this.playerState = playerState;
        logger.info("PlayerState of player: "+getUsername()+" has been set to: "+playerState+"\n" +
                "Proceeding to updated the clients.");
        if(observer != null) {
            observer.update(new SCPUpdateSpecificPlayer(this.toTransferableDataObject()));

            if (user.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE))
                observer.update(user.getUsername(), new SCPUpdateClientGameState(playerState));
        }
    }

    public void setWinner(){
        winner = true;
    }





    //METHODS
    /**
     * Adds a selected objective to the player's list of secret objectives.
     * Updates the observer with the player's updated secret information.
     *
     * @param selectedObjective the objective to add to the player's secret objectives
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
    public void playCard(int cardIndex, int coordinateIndex, boolean faceUp) throws InvalidGameInputException {
        Card playedCard;

        try {
            playedCard = cardsHeld.get(cardIndex);
        }
        catch (IndexOutOfBoundsException i){
            throw new InvalidGameInputException();
        }


        boolean cardCanBeFaceUp = cardPlayability.remove(playedCard);

        //If the card can't be played faceUp but the player provided faceUp = true, throws an exception
        //Otherwise the card is played as expected.
        if(!cardCanBeFaceUp && faceUp)
            throw new InvalidGameInputException();

        //Updates player's points after playing the card.
        points = points + cardMap.place(playedCard ,coordinateIndex,faceUp);

        cardsHeld.remove(cardIndex);

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
    /**
     * Indicates whether some other object is "equal to" this one.
     * Compares this Player object with the specified object for equality.
     *
     * @param o the reference object with which to compare
     * @return {@code true} if this object is the same as the o argument; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(user, player.user);
    }

    /**
     * Returns a hash code value for the player based on the user's attributes.
     *
     * @return a hash code value for this player
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }

    /**
     * Converts the player's secret information to a transferable data object.
     * This includes the cards held by the player, their playability status, and the first secret objective.
     *
     * @return a PlayerSecretInfoRecord containing the player's secret information
     */
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


