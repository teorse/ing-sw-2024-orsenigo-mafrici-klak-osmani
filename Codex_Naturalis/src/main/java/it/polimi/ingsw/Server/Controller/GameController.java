package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Exceptions.Game.Model.Player.CoordinateIndexOutOfBounds;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Game.Logic.Game;
import it.polimi.ingsw.Exceptions.Game.*;

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
    /**
     * Allows a player to play a card on the game table.
     *
     * @param username        The username of the player performing the action.
     * @param cardIndex       The index of the card in the player's hand.
     * @param coordinateIndex The index of the coordinates on the game table where the card will be placed.
     * @param faceUp          Whether the card should be placed face up or face down.
     * @throws NotYourTurnException                     If it's not the player's turn to perform an action.
     * @throws MoveAttemptOnWaitStateException         If a move attempt is made while in the wait state.
     * @throws InvalidActionForPlayerStateException    If the action is invalid for the current state of the player.
     * @throws InvalidActionForGameStateException      If the action is invalid for the current state of the game.
     * @throws CoordinateIndexOutOfBounds              If the coordinate index is out of bounds.
     */
    public void playCard(String username, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, CoordinateIndexOutOfBounds {
        model.playCard(username, cardIndex, coordinateIndex, faceUp);
    }

    /**
     * Allows a player to draw a card from a specified card pool.
     *
     * @param username     The username of the player performing the action.
     * @param cardPoolType The type of card pool from which the card is drawn.
     * @param index        The index within the card pool from which the card is drawn.
     * @throws NotYourTurnException                  If it's not the player's turn to perform an action.
     * @throws MoveAttemptOnWaitStateException      If a move attempt is made while in the wait state.
     * @throws MaxResourceCardsDrawnException       If the maximum number of resource cards has already been drawn.
     * @throws InvalidActionForPlayerStateException If the action is invalid for the current state of the player.
     * @throws InvalidActionForGameStateException   If the action is invalid for the current state of the game.
     * @throws MaxGoldenCardsDrawnException         If the maximum number of golden cards has already been drawn.
     */
    public void drawCard(String username, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, MoveAttemptOnWaitStateException, MaxResourceCardsDrawnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, MaxGoldenCardsDrawnException {
        model.drawCard(username, cardPoolType, index);
    }

    /**
     * Allows a player to pick a player objective.
     *
     * @param username       The username of the player performing the action.
     * @param objectiveIndex The index of the player objective to pick.
     * @throws MoveAttemptOnWaitStateException      If a move attempt is made while in the wait state.
     * @throws InvalidActionForPlayerStateException If the action is invalid for the current state of the player.
     * @throws InvalidActionForGameStateException   If the action is invalid for the current state of the game.
     */
    public void pickPlayerObjective(String username, int objectiveIndex) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        model.pickPlayerObjective(username, objectiveIndex);
    }
}
