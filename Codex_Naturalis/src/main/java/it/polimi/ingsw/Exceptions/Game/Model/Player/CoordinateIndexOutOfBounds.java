package it.polimi.ingsw.Exceptions.Game.Model.Player;

import it.polimi.ingsw.Exceptions.Game.Model.InvalidGameInputException;

/**
 * Exception thrown when the coordinateIndex specified as parameter in the place method in the cardMap
 * is not a valid index as it was a negative value or greater than the size of the List of available Coordinates.
 */
public class CoordinateIndexOutOfBounds extends InvalidGameInputException {
}
