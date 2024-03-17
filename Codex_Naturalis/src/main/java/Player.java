import java.util.ArrayList;
import java.util.Map;

public class Player {
    private String nickname;
    private String color; //May be better to use enum class, only 4 color
    private int points;
    private int roundsCompleted;
    private ArrayList<ResourceCard> cardsHeld;
    private DoubleHashMap cardsPlaced;
    //Double HashMap with first key as coordX of card being placed
    //second key is coordY of the card being placed
    private ArrayList<Coords> availablePlacements;
    private Map<Resource, Integer> playerResources;
    private Map<Item, Integer> playerItems;
    private ObjectiveCard secretObjective;
    private String connectionStatus;

    //Constructor (nickname, cardsHeld, playerResources, playerItems)

    public Player(String nickname, ArrayList<ResourceCard> cardsHeld, Map<Resource, Integer> playerResources, Map<Item, Integer> playerItems) {
        this.nickname = nickname;
        this.cardsHeld = cardsHeld;
        this.playerResources = playerResources;
        this.playerItems = playerItems;
    }


    //get for all of them

    public String getNickname() {
        return nickname;
    }

    public String getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public int getRoundsCompleted() {
        return roundsCompleted;
    }

    public ArrayList<ResourceCard> getCardsHeld() {
        return cardsHeld;
    }

    public DoubleHashMap getCardsPlaced() {
        return cardsPlaced;
    }

    public ArrayList<Coords> getAvailablePlacements() {
        return availablePlacements;
    }

    public Map<Resource, Integer> getPlayerResources() {
        return playerResources;
    }

    public Map<Item, Integer> getPlayerItems() {
        return playerItems;
    }

    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }


    //set (color, points, roundsCompleted, connectionStatus, secretObjective)

    public void setColor(String color) {
        this.color = color;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setRoundsCompleted(int roundsCompleted) {
        this.roundsCompleted = roundsCompleted;
    }

    public void setSecretObjective(ObjectiveCard secretObjective) {
        this.secretObjective = secretObjective;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }


    //TODO: add-remove (cardsHeld, playerResources, playerItems, availablePlacements)
    //all methods to add/remove/modify cardsPlaced should use DoubleHasMap methods
}
