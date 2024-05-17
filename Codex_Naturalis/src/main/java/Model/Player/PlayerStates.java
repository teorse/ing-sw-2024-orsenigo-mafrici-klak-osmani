package Model.Player;

/**
 * Enum representing the possible states of a player during the game.
 * Each state corresponds to a specific phase or action that the player can take.
 */
public enum PlayerStates {
    /**
     * State representing the phase where the player is placing cards on the game board.
     */
    PLACE,

    /**
     * State representing the phase where the player is drawing any cards from the deck.
     */
    DRAW,

    /**
     * State representing the phase where the player is drawing golden cards from the deck (setup phase only).
     */
    DRAW_GOLDEN,

    /**
     * State representing the phase where the player is drawing resource cards from the deck(setup phase only).
     */
    DRAW_RESOURCE,

    /**
     * State representing the phase where the player is waiting for a signal from the server.
     */
    WAIT,

    /**
     * State representing the phase where the player is picking an objective card.
     */
    PICK_OBJECTIVE}
