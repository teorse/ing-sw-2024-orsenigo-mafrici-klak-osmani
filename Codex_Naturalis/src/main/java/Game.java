import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private int currentRound;
    private Deck goldenDeck;
    private Deck resourcesDeck;
    private ArrayList<ObjectiveCard> objectives;
    private GoldenCard goldenCardVisible1;
    private GoldenCard goldenCardVisible2;
    private ResourceCard resourceCardVisible1;
    private ResourceCard resourceCardVisible2;
    private ObjectiveCard objectiveVisible1;
    private ObjectiveCard objectiveVisible2;

    //Constructor (Players[], Deck golden, Deck resources, ArrayList<ObjectiveCard> objectives)

    public Game(ArrayList<Player> players, Deck goldenDeck, Deck resourcesDeck, ArrayList<ObjectiveCard> objectives) {
        this.players = players;
        this.goldenDeck = goldenDeck;
        this.resourcesDeck = resourcesDeck;
        this.objectives = objectives;
    }


    //get Deck golden, Deck resources, objectives
    public Deck getGoldenDeck() {
        return goldenDeck;
    }

    public Deck getResourcesDeck() {
        return resourcesDeck;
    }

    public ArrayList<ObjectiveCard> getObjectives() {
        return objectives;
    }


    //get-set resourceCardVisible1/2, goldenCardVisible1/2, objectiveVisible1, objectiveVisible2


    public GoldenCard getGoldenCardVisible1() {
        return goldenCardVisible1;
    }

    public void setGoldenCardVisible1(GoldenCard goldenCardVisible1) {
        this.goldenCardVisible1 = goldenCardVisible1;
    }

    public GoldenCard getGoldenCardVisible2() {
        return goldenCardVisible2;
    }

    public void setGoldenCardVisible2(GoldenCard goldenCardVisible2) {
        this.goldenCardVisible2 = goldenCardVisible2;
    }

    public ResourceCard getResourceCardVisible1() {
        return resourceCardVisible1;
    }

    public void setResourceCardVisible1(ResourceCard resourceCardVisible1) {
        this.resourceCardVisible1 = resourceCardVisible1;
    }

    public ResourceCard getResourceCardVisible2() {
        return resourceCardVisible2;
    }

    public void setResourceCardVisible2(ResourceCard resourceCardVisible2) {
        this.resourceCardVisible2 = resourceCardVisible2;
    }

    public ObjectiveCard getObjectiveVisible1() {
        return objectiveVisible1;
    }

    public void setObjectiveVisible1(ObjectiveCard objectiveVisible1) {
        this.objectiveVisible1 = objectiveVisible1;
    }

    public ObjectiveCard getObjectiveVisible2() {
        return objectiveVisible2;
    }

    public void setObjectiveVisible2(ObjectiveCard objectiveVisible2) {
        this.objectiveVisible2 = objectiveVisible2;
    }
}
