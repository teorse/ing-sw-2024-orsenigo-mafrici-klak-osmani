package it.polimi.ingsw.Server.Controller.InputHandler;

import it.polimi.ingsw.Server.Controller.GameController;

/**
 * The GameControllerObserver interface provides a contract for observers that monitor changes in a GameController instance.
 * Classes implementing this interface can register themselves to be notified of updates in the game state.
 */
public interface GameControllerObserver {

    /**
     * Updates the observer with the latest GameController instance.
     *
     * @param gameController The updated GameController instance.
     */
    void updateGameController(GameController gameController);
}
