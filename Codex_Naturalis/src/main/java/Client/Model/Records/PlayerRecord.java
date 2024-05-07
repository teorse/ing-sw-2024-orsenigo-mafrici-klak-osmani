package Client.Model.Records;

import Model.Player.PlayerStates;
import Server.Model.Lobby.LobbyUserColors;
import Server.Model.Lobby.LobbyUserConnectionStates;

import java.io.Serializable;

/**
 * Represents a record containing information about a player in a game.
 *
 * <p>A {@code PlayerRecord} contains details such as the nickname, color, state, rounds completed, points, and objectives completed by the player.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record PlayerRecord(String nickname, LobbyUserColors color, PlayerStates playerState,
                           int roundsCompleted, int points, int objectivesCompleted) implements Serializable {

    /**
     * Constructs a new {@code PlayerRecord} with the specified parameters.
     *
     * @param nickname the nickname of the player
     * @param color the color assigned to the player
     * @param playerState the state of the player
     * @param roundsCompleted the number of rounds completed by the player
     * @param points the points earned by the player
     * @param objectivesCompleted the number of objectives completed by the player
     */
    public PlayerRecord {
        // No additional implementation needed as records automatically generate a constructor
    }
}