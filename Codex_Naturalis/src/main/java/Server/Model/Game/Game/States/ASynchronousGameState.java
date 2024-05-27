package Server.Model.Game.Game.States;

import Server.Model.Game.Game.Game;
import Server.Model.Game.Player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an asynchronous state in the game where actions can be performed independently by different players.<br>
 * This abstract class extends {@link GameState} and provides additional functionality to track player readiness.
 * Concrete states that require asynchronous behavior should extend this class.
 */
public abstract class ASynchronousGameState extends GameState {

    /**
     * Map that keeps track of which players have completed the setups of this game phase and which haven't.<br>
     * If true, it means the player HAS completed all setups and is ready for the next phase.
     */
    protected final Map<Player, Boolean> playerReadiness;

    protected ASynchronousGameState(Game game) {
        super(game);

        playerReadiness = new HashMap<>();
        for(Player player : players){
            playerReadiness.put(player, false);
        }
    }

    /**
     * Removes a player from the game and updates the readiness map accordingly.
     *
     * @param player The player to remove.
     */
    @Override
    public void removePlayer(Player player) {
        playerReadiness.remove(player);
        players.remove(player);

        if(players.size() > 1)
            nextState();
        else
            game.gameOver();
    }
}
