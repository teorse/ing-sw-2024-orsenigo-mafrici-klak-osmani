package Server.Controller;

import Exceptions.Game.*;
import Model.Game.CardPoolTypes;
import Model.Game.Game;

/**
 * The GameController class serves as the controller for the game.<br>
 * It handles operations related to managing game data and interactions between players.
 */
public class GameController {
    //ATTRIBUTES
    private final Game model;





    //CONSTRUCTOR
    /**
     * Constructs a GameController object with the specified game model.
     *
     * @param model The Game model associated with this controller.
     */
    public GameController(Game model){
        this.model = model;
    }





    //CONTROLLER METHODS
    public void playCard(String username, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        model.playCard(username, cardIndex, coordinateIndex, faceUp);
    }
    public void drawCard(String username, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, MoveAttemptOnWaitStateException, MaxResourceCardsDrawnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, MaxGoldenCardsDrawnException {
        model.drawCard(username, cardPoolType, index);
    }
    public void pickPlayerObjective(String username, int objectiveIndex) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        model.pickPlayerObjective(username, objectiveIndex);
    }
}
