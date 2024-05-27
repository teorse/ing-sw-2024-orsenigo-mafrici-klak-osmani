package Model.Game.States.PlayerSwitchers;

import Model.Player.Player;

import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of {@link PlayerSwitcher} that does not consider whether players are online or offline.<br>
 * This player switcher simply iterates over the list of players in order.
 */
public class PlayerSwitcherNonResilient extends PlayerSwitcher{


    public PlayerSwitcherNonResilient(List<Player> players) {
        super(players);
        logger = Logger.getLogger(PlayerSwitcherNonResilient.class.getName());
    }

    /**
     * Determines the index of the first player to take a turn.<br>
     * Returns 0 if there are players in the game, -1 otherwise.
     *
     * @return The index of the first player, or -1 if there are no players.
     */
    public int firstPlayerIndex(){
        if(players.isEmpty())
            return -1;
        else
            return 0;
    }

    /**
     * Determines the index of the next player to take a turn.<br>
     * Returns -1 if the current player is the last in the list, otherwise returns the next index.
     *
     * @param currentPlayerIndex The index of the current player.
     * @return The index of the next player, or -1 if the current player is the last in the list.
     */
    public int nextPlayerIndex(int currentPlayerIndex){
        if(currentPlayerIndex == players.size()-1)
            return -1;
        else
            return currentPlayerIndex+1;
    }
}
