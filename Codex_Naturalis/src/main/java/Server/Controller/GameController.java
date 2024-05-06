package Server.Controller;

import Exceptions.Game.*;
import Model.Game.CardPoolTypes;
import Model.Game.Game;
import Model.Player.Player;
import Server.Model.Lobby.LobbyUserColors;
import Server.Model.Lobby.LobbyUser;

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
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        model.playCard(player, cardIndex, coordinateIndex, faceUp);
    }
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, MoveAttemptOnWaitStateException, MaxResourceCardsDrawnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, MaxGoldenCardsDrawnException {
        model.drawCard(player, cardPoolType, index);
    }
    public void pickPlayerObjective(Player player, int objectiveIndex) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        model.pickPlayerObjective(player, objectiveIndex);
    }


    /**
     * Returns the player object associated to the provided username in this game.
     * @param lobbyUser
     * @return
     */
    public Player getPlayerByLobbyUser(LobbyUser lobbyUser){
        return model.getPlayerByLobbyUser(lobbyUser);
    }
}
