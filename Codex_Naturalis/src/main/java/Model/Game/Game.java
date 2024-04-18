package Model.Game;

import Model.Cards.Card;
import Model.Objectives.Objective;
import Model.Player.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This Class stores all the information about the current game, including the list of players, the current game phase,
 * the current round etc.
 */
public class Game {
    //ATTRIBUTES
    private List<Player> players;
    private Table table;
    private GamePhases gamePhase;

    /**
     * Stores number of rounds completed by the table.<br>
     * A round is deemed completed if all the currently online players have completed their turn for the current round.
     */
    private int roundsCompleted;
    private Map<Player, Integer> playerPoints;





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

        //Sets all players' points to zero.
        this.playerPoints = new HashMap<>()
        {{
            for(Player player : players)
                put(player, 0);
        }};
        this.gamePhase = GamePhases.SETUP;
    }





    //GETTERS
    public GamePhases getGamePhase() {
        return gamePhase;
    }





    //METHODS
    public void incrementRoundsCompleted(){
        this.roundsCompleted++;
    }

    public void addPointsToPlayer(Player player, int points){
        playerPoints.compute(player, (k, oldPoints) -> oldPoints + points);
    }

    /**
     * Method verifies whether the conditions for the game-end are met, if so it updates the game phase accordingly.
     */
    public void checkGameEnding(){

        if(table.areDecksEmpty()){
            this.gamePhase = GamePhases.ENDING;
            return;
        }

        for(Map.Entry<Player, Integer> entry : playerPoints.entrySet()){
            if(entry.getValue() >= 20){
                this.gamePhase = GamePhases.ENDING;
                return;
            }
        }
        return;
    }
}
