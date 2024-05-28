package it.polimi.ingsw.Server.Model.Game.Logic.States;

import it.polimi.ingsw.Server.Model.Game.Logic.Game;
import it.polimi.ingsw.Server.Model.Game.Logic.States.PlayerSwitchers.PlayerSwitcher;
import it.polimi.ingsw.Server.Model.Game.Player.Player;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

/**
 * Represents a synchronous state in the game where actions are performed by players in a turn-based manner.<br>
 * This abstract class extends {@link GameState} and provides functionality for managing player turns.
 * Concrete states that require synchronous behavior should extend this class.
 */
public abstract class SynchronousGameState extends GameState {
    //ATTRIBUTES
    protected int currentPlayerIndex;
    protected final PlayerSwitcher playerSwitcher;





    //CONSTRUCTOR
    /**
     * Constructs a new SynchronousGameState.
     *
     * @param game           The game instance associated with this state.
     * @param playerSwitcher The player switcher that determines the order of player turns.
     */
    protected SynchronousGameState(Game game, PlayerSwitcher playerSwitcher) {
        super(game);
        this.playerSwitcher = playerSwitcher;

    }





    //METHODS
    /**
     * Removes a player from the game and updates the current player index accordingly.<br>
     * If the removed player is the current player, it finds the next player to take the turn.
     *
     * @param player The player to remove.
     */
    @Override
    public void removePlayer(Player player){
        logger.info("Removing player "+player.getUsername()+" from game.");
        int indexPlayerToRemove = players.indexOf(player);
        logger.fine("Player "+player.getUsername()+" is number "+indexPlayerToRemove+" in the current order of the game.");

        //Remove the player from the player list
        if(indexPlayerToRemove == currentPlayerIndex){
            logger.fine("The player to remove is the current player");
            players.remove(player);
            logger.fine("Player removed");
            currentPlayerIndex--;
            logger.fine("Current player index decremented");
            findNextPlayer();
            logger.fine("Looking for the next player");
        }

        else if(indexPlayerToRemove < currentPlayerIndex){
            logger.fine("The player to remove is before the current player");
            players.remove(player);
            logger.fine("Player removed");
            currentPlayerIndex--;
            logger.fine("Current player index decremented");
        }

        else{
            logger.fine("The player to remove is after the current player");
            players.remove(player);
            logger.fine("Player removed");
        }

        if(players.size() <= 1)
            game.gameOver();
    }

    /**
     * Determines the next player to take a turn.<br>
     * If no players are available, it transitions to the next game state.
     */
    protected void findNextPlayer(){
        currentPlayerIndex = playerSwitcher.nextPlayerIndex(currentPlayerIndex);

        if(currentPlayerIndex == -1){
            nextState();
        }
        else {
            Player nextPlayer = players.get(currentPlayerIndex);
            nextPlayer.setPlayerState(determinePlayerState(nextPlayer));
        }
    }

    /**
     * Determines the first player to take a turn.<br>
     * If no players are available, the game is ended.
     */
    protected void findFirstPlayer(){
        currentPlayerIndex = playerSwitcher.firstPlayerIndex();

        if(currentPlayerIndex == -1){
            //todo add exception
            game.gameOver();
        }
        else {
            Player firstPlayer = players.get(currentPlayerIndex);
            firstPlayer.setPlayerState(determinePlayerState(firstPlayer));
        }
    }

    /**
     * Determines the player state for a given player.<br>
     * Subclasses must implement this method to define the specific player state transitions.
     *
     * @param player The player whose state is to be determined.
     * @return The state of the player.
     */
    protected abstract PlayerStates determinePlayerState(Player player);
}
