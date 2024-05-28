package it.polimi.ingsw.Server.Controller.InputHandler;

import it.polimi.ingsw.Server.Controller.GameController;

public interface GameControllerObserver {
    void updateGameController(GameController gameController);
}
