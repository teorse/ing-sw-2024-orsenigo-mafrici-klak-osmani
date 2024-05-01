package Server.Controller;

import Server.Model.Lobby.GameDemo.Game;
import Server.Model.Lobby.GameDemo.Player;
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
    /**
     * Appends personal data to the specified player in the game.
     *
     * @param player The player to which the data should be appended.
     * @param data   The personal data to append.
     */
    public void appendPersonalData(Player player, String data){
        model.appendPersonalData(player, data);
    }

    /**
     * Appends public data to the specified player in the game.
     *
     * @param player The player to which the data should be appended.
     * @param data   The public data to append.
     */
    public void appendPublicData(Player player, String data){
        model.appendPublicData(player, data);
    }

    /**
     * Appends personal and public data to the specified player in the game.
     *
     * @param player       The player to which the data should be appended.
     * @param personalData The personal data to append.
     * @param publicData   The public data to append.
     */
    public void appendPersonalPublicData(Player player, String personalData, String publicData){
        model.appendPersonalPublicData(player, personalData, publicData);
    }

    /**
     * Returns the player object associated to the provided username in this game.
     * @param lobbyUser
     * @return
     */
    public Player getPlayerByLobbyUser(LobbyUser lobbyUser){
        return model.getPlayerByLobbyUser(lobbyUser);
    }

    public void quitGame(LobbyUser lobbyUser){
        model.quitLayer(lobbyUser);
    }
}
