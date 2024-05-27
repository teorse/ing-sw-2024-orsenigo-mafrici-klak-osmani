package Server.Model.Game.Logic.States.PlayerSwitchers;

import Server.Model.Game.Player.Player;

import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract class for managing the order of player turns in a game.<br>
 * Concrete implementations should define the logic for determining the first player and the next player in the sequence.
 */
public abstract class PlayerSwitcher {
    //ATTRIBUTES
    final List<Player> players;
    Logger logger;

    protected PlayerSwitcher(List<Player> players) {
        this.players = players;
    }

    /**
     * Determines the index of the first player to take a turn.
     *
     * @return The index of the first player.
     */
    public abstract int firstPlayerIndex();

    /**
     * Determines the index of the next player to take a turn based on the current player index.
     *
     * @param currentPlayerIndex The index of the current player.
     * @return The index of the next player.
     */
    public abstract int nextPlayerIndex(int currentPlayerIndex);
}
