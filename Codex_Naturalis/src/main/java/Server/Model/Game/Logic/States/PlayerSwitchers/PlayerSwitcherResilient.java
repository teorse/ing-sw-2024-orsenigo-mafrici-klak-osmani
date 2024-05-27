package Server.Model.Game.Logic.States.PlayerSwitchers;

import Server.Model.Game.Player.Player;
import Server.Model.Lobby.LobbyUserConnectionStates;

import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of {@link PlayerSwitcher} that considers the connection status of players.<br>
 * This player switcher ensures that only online players are selected for turns.
 */
public class PlayerSwitcherResilient extends PlayerSwitcher{



    public PlayerSwitcherResilient(List<Player> players) {
        super(players);
        logger = Logger.getLogger(PlayerSwitcherNonResilient.class.getName());
    }

    /**
     * Determines the index of the first player to take a turn, considering only online players.<br>
     * Returns the index of the first online player, or -1 if no players are online.
     *
     * @return The index of the first online player, or -1 if no players are online.
     */
    @Override
    public int firstPlayerIndex() {
        logger.info("Looking for first player of this round");
        int currentPlayerIndex = -1;

        //Look for the first(in the list's order) online player in the list and set them as the current player
        for(int i = 0; i < players.size(); i++){
            logger.fine("Checking player: "+players.get(i).getUsername()+"\nConnection status: "+players.get(i).getConnectionStatus());
            if(players.get(i).getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                logger.fine("Setting player "+players.get(i)+" as current player");
                currentPlayerIndex = i;
                break;
            }
        }

        if(currentPlayerIndex == -1)
            logger.warning("No players found online in game");

        return currentPlayerIndex;
    }

    /**
     * Determines the index of the next player to take a turn, considering only online players.<br>
     * Returns -1 if the current player is the last in the list or if no subsequent players are online.
     *
     * @param currentPlayerIndex The index of the current player.
     * @return The index of the next online player, or -1 if the current player is the last or no subsequent players are online.
     */
    @Override
    public int nextPlayerIndex(int currentPlayerIndex) {
        logger.info("Looking for the next player");

        int playersSize = players.size();

        logger.fine("Current player was: "+players.get(currentPlayerIndex).getUsername()+
                "\nCurrent player index was: "+currentPlayerIndex+", real life number is: "+currentPlayerIndex+1+
                "\nTotal players in this game: "+playersSize);

        //If current player is the last players call next state.
        if(currentPlayerIndex +1 == playersSize) {
            logger.fine("Player index+1 equals player size (the current player was the last player for the round), proceeding to next state");
            return -1;
        }

        else {
            logger.fine("Current player was not the last player");
            //For all the remaining players
            for (int i = currentPlayerIndex + 1; i < playersSize; i++) {
                logger.fine("Checking player number "+i);
                Player nextPlayer = players.get(i);
                logger.fine("Player number "+i+" is "+nextPlayer.getUsername()+"\nTheir connection status is: "+nextPlayer.getConnectionStatus());
                //Set as current player the first online player found among the remaining ones.
                if(nextPlayer.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                    logger.fine("Setting current player index to: "+i);
                    return i;
                }
            }
            logger.fine("No eligible players were found in this round, proceeding to next round.");
            //If no eligible players were found after the current player then advance to next round.
            return -1;
        }
    }
}
