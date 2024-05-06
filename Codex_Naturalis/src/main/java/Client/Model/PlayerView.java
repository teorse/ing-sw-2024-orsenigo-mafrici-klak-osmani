package Client.Model;

import Model.Player.PlayerColors;
import Model.Player.PlayerStates;
import Server.Model.Lobby.LobbyUserConnectionStates;

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
 */
public class PlayerView {
    // ATTRIBUTES
    private final String nickname;
    private final PlayerColors color;
    private LobbyUserConnectionStates connectionStatus;
    private PlayerStates playerState;
    private int roundsCompleted;
    private int points;
    private final int objectivesCompleted;
    private final CardMapView cardMapView;


    //CONSTRUCTOR
    public PlayerView(String nickname, PlayerColors color, LobbyUserConnectionStates connectionStatus, PlayerStates playerState, int roundsCompleted, int points, int objectivesCompleted, CardMapView cardMapView) {
        this.nickname = nickname;
        this.color = color;
        this.connectionStatus = connectionStatus;
        this.playerState = playerState;
        this.roundsCompleted = roundsCompleted;
        this.points = points;
        this.objectivesCompleted = objectivesCompleted;
        this.cardMapView = cardMapView;
    }

    // GETTERS
    public String getNickname() {
        return nickname;
    }

    public PlayerColors getColor() {
        return color;
    }

    public LobbyUserConnectionStates getConnectionStatus() {
        return connectionStatus;
    }

    public PlayerStates getPlayerState() {
        return playerState;
    }

    public int getRoundsCompleted() {
        return roundsCompleted;
    }

    public int getPoints() {
        return points;
    }

    public int getObjectivesCompleted() {
        return objectivesCompleted;
    }

    public CardMapView getCardMapView() {
        return cardMapView;
    }


    // SETTERS
    public void setConnectionStatus(LobbyUserConnectionStates connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public void setPlayerState(PlayerStates playerState) {
        this.playerState = playerState;
    }

    // METHODS
    public void incrementRoundsCompleted() {
        this.roundsCompleted++;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}