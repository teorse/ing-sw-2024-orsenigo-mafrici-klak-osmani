package Client.Model.Records;


import Model.Game.States.GameState;

import java.util.List;

/**
 * Represents a view of a game, providing a comprehensive overview of the game's state, players, and table.
 * <p>
 * The `GameView` class serves as a snapshot of a `Game` object, encapsulating various attributes that represent
 * the state of the game, such as:
 * - A list of `PlayerView` objects representing the players in the game.
 * - The number of rounds completed.
 * - A `TableView` object depicting the current state of the game table.
 * - Flags indicating whether the last round has been triggered and whether the game has ended.
 * - The current `GameState`.
 * <p>
 * This class is useful for generating a high-level view of a game for display purposes in a user interface
 * or other forms of game output.
 *
 * @param playerRecords ATTRIBUTES
 */
public record GameRecord(List<PlayerRecord> playerRecords, int roundsCompleted, TableRecord tableRecord, boolean lastRoundFlag,
                         boolean gameOver, GameState currentState) {
    /**
     * Constructs a `GameView` instance with the given game attributes.
     * <p>
     * This constructor initializes the following attributes to represent the state of a game:
     * - The `playerViews`, representing a list of player views.
     * - The `roundsCompleted`, indicating the number of rounds completed in the game.
     * - The `tableView`, representing the current state of the game table.
     * - The `lastRoundFlag`, indicating whether the game has reached the last round.
     * - The `gameOver`, indicating whether the game has ended.
     * - The `currentState`, representing the current game state.
     *
     * @param playerRecords     The list of `PlayerView` objects representing the players in the game.
     * @param roundsCompleted The number of rounds completed in the game.
     * @param tableRecord       The `TableView` representing the visual representation of the game table.
     * @param lastRoundFlag   Boolean flag indicating whether the game has reached the last round.
     * @param gameOver        Boolean flag indicating whether the game has ended.
     * @param currentState    The current `GameState` of the game.
     */
    public GameRecord {
    }
}