package Client.Model.Records;


import java.io.Serializable;

/**
 * Represents a record containing information about a game.
 *
 * <p>A {@code GameRecord} consists of the number of rounds completed, a flag indicating if it's the last round,
 * a flag indicating if the game is over, and the current state of the game.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record GameRecord(int roundsCompleted, boolean lastRoundFlag, boolean setupFinished, boolean waitingForReconnections)
        implements Serializable {

    /**
     * Constructs a new {@code GameRecord} with the specified parameters.
     *
     * @param roundsCompleted the number of rounds completed in the game
     * @param lastRoundFlag indicates if it's the last round of the game
     */
    public GameRecord {
        // No additional implementation needed as records automatically generate a constructor
    }
}