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
    //get tutto
    //set (color, points, roundsCompleted, connectionStatus, secretObjective)
    //add-remove (cardsHeld, playerResources, playerItems, availablePlacements)
    //all methods to add/remove/modify cardsPlaced should use DoubleHasMap methods
}
