package Client.Model.Records;

import Model.Player.PlayerStates;
import Server.Model.Lobby.LobbyUserColors;
import Server.Model.Lobby.LobbyUserConnectionStates;

import java.io.Serializable;

/**
 * Represents a view of a player's state for display in the user interface.
 * <p>
 * The `PlayerView` class provides a representation of a `Player` object, focusing on information relevant for UI display.
 * It captures key details such as:
 * - The player's nickname and color.
 * - Connection status and current state.
 * - The number of rounds completed by the player.
 * - The player's total points and objectives completed.
 * - The player's list of held cards with their playability status.
 * - The player's secret objectives.
 * <p>
 * This class serves as a bridge between the game logic and the user interface, allowing a clear presentation
 * of a player's state during the game.
 * <p>
 * Additionally, the class includes setter methods for updating specific player attributes, and simple utility methods
 * to increment rounds completed or add points to the player's total.
 *
 * @param nickname ATTRIBUTES
 */
public record PlayerRecord(String nickname, LobbyUserColors color, LobbyUserConnectionStates connectionStatus,
                           PlayerStates playerState, int roundsCompleted, int points, int objectivesCompleted) implements Serializable {
    //CONSTRUCTOR
}