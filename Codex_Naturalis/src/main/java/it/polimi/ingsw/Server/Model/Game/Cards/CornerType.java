package it.polimi.ingsw.Server.Model.Game.Cards;

/**
 * Enum representing the types that a corner on a game card can have.
 * <p>
 * The possible corner types are:
 * - EMPTY: Represents an empty corner on the card.
 * - NULL: Represents a null corner, typically indicating an invalid or uninitialized state.
 * - ARTIFACT: Represents a corner with an artifact on the card.
 */
public enum CornerType {
    EMPTY,
    NULL,
    ARTIFACT
}