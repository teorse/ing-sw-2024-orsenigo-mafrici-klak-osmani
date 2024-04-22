package Model.Game;

import Model.Cards.Card;
import Model.Objectives.Objective;
import Model.Player.Player;
import Model.Game.States.*;
import Model.Player.PlayerColors;

import java.util.Collections;
import java.util.List;

/**
 * This Class stores all the information about the current game, including the list of players, the current game phase,
 * the current round etc.
 */
public class Game {
    //ATTRIBUTES
    private List<Player> players;
    private Table table;
    private boolean lastRoundFlag;
    private boolean gameIsOver;
    private GameState state;

    /**
     * Stores number of rounds completed by the table.<br>
     * A round is deemed completed if all the currently online players have completed their turn for the current round.
     */
    private int roundsCompleted;





    //CONSTRUCTOR
    /**
     * Default Constructor.<br>
     * Initializes a Model.Game.Game object by taking Players ArrayList, Golden and Resource Model.Game.Deck, and ArrayList of Objectives.<br>
     * Automatically
     * @param goldenCards   List of Golden Cards to use during this game.
     * @param resourceCards List of Resource Cards to use during this game.
     * @param starterCards  List of Starter Cards to use during this game.
     * @param objectives    List of Objectives to use during this game.
     * @param players       List of players taking part to this game.
     */
    public Game(List<Card> goldenCards, List<Card> resourceCards, List<Card> starterCards, List<Objective> objectives, List<Player> players) {
        this.players = players;
        Collections.shuffle(this.players);
        this.table = new Table(goldenCards, resourceCards, starterCards, objectives);
        this.lastRoundFlag = false;
        this.gameIsOver = false;
        state = new CardsSetup(this);
    }





    //GETTERS
    public List<Player> getPlayers() {
        return players;
    }
    public Table getTable() {
        return table;
    }
    public int getRoundsCompleted() {
        return roundsCompleted;
    }
    public boolean isLastRoundFlag(){
        return this.lastRoundFlag;
    }





    //STATE PATTERN METHODS
    public void setState(GameState state){
        this.state = state;
    }
    public void placeCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp){
        state.placeCard(player, cardIndex, coordinateIndex, faceUp);
    }
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index){
        state.drawCard(player, cardPoolType, index);
    }
    public void pickPlayerColor(Player player, PlayerColors color){
        state.pickPlayerColor(player, color);
    }
    public void pickPlayerObjective(Player player, int objectiveIndex){
        state.pickPlayerObjective(player, objectiveIndex);
    }





    //METHODS
    public void incrementRoundsCompleted(){
        this.roundsCompleted++;
    }

    /**
     * Method verifies whether the conditions for the game-end are met, if so sets to true the lastRound flag.
     */
    public void checkGameEndingConditions(){

        if(table.areDecksEmpty()){
            this.lastRoundFlag = true;
            return;
        }

        for(Player player : players){
            if(player.getPoints() >= 20){
                this.lastRoundFlag = true;
                return;
            }
        }
        return;
    }

    public void gameOver(){
        List<Objective> sharedObjectives = table.getSharedObjectives();

        //Count all the points for each player.
        for(Player player : players){
            player.countAllPoints(sharedObjectives);
        }

        gameIsOver = true;
        //After all players are given their points the winner is calculated.
        //ToDo Sort the winner.
    }
}
