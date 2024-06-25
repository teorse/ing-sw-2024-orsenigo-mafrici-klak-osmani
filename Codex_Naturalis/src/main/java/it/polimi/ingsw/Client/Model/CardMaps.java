package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardVisibilityRecord;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;

import java.util.Map;

/**
 * The singleton class `CardMaps` represents the collection of card maps for all players in the game.
 * It manages the card maps and provides methods to retrieve, set, and manipulate card placements.
 * This class extends `Observable`, allowing observers to be notified of changes to the card maps.
 */
public class CardMaps extends Observable {

    // Singleton pattern
    private static CardMaps INSTANCE;

    private CardMaps() {}

    /**
     * Retrieves the instance of `CardMaps`, creating it if it does not exist.
     *
     * @return The instance of `CardMaps`.
     */
    public synchronized static CardMaps getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardMaps();
        }
        return INSTANCE;
    }

    // Attributes
    private Map<String, CardMapRecord> cardMaps;

    // Getters

    /**
     * Retrieves the map of card maps for all players.
     *
     * @return The map of card maps.
     */
    public Map<String, CardMapRecord> getCardMaps() {
        return cardMaps;
    }

    /**
     * Retrieves the visibility record of a card at a specific coordinate for a given player.
     *
     * @param coordinate The coordinates of the card.
     * @param owner      The owner of the card map.
     * @return The visibility record of the card.
     */
    public CardVisibilityRecord getCardByCoordinate(Coordinates coordinate, String owner) {
        return cardMaps.get(owner).getCardVisibilityRecord(coordinate);
    }

    // Setters

    /**
     * Sets the map of card maps for all players.
     * Notifies observers after updating the card maps.
     *
     * @param cardMaps The map of card maps to set.
     */
    public void setCardMaps(Map<String, CardMapRecord> cardMaps) {
        this.cardMaps = cardMaps;
        super.updateObservers();
    }

    /**
     * Sets a specific card map for a player.
     * Notifies observers after updating the card maps.
     *
     * @param username      The username of the player.
     * @param cardMapRecord The card map record to set.
     */
    public void setSpecificCardMap(String username, CardMapRecord cardMapRecord) {
        this.cardMaps.put(username, cardMapRecord);
        super.updateObservers();
    }

    // Methods

    /**
     * Computes the maximum coordinate value across all card placements.
     *
     * @return The maximum coordinate value.
     */
    public int maxCoordinate() {
        int mbs = 0;
        for (String username : cardMaps.keySet())
            for (Coordinates coordinates : cardMaps.get(username).cardsPlaced().keySet()) {
                int max = Math.max(Math.abs(coordinates.getCoordY()), Math.abs(coordinates.getCoordY()));
                if (max > mbs) {
                    mbs = max;
                }
            }
        return Math.max(mbs, 5);
    }

    /**
     * Computes the maximum board side size based on the maximum coordinate.
     *
     * @return The maximum board side size.
     */
    public int maxBoardSide() {
        return maxCoordinate() * 2 + 3;
    }

    /**
     * Converts character row and column indices to coordinates on the board.
     *
     * @param row    The character representing the row index.
     * @param column The character representing the column index.
     * @return The corresponding coordinates on the board.
     */
    public Coordinates charsToCoordinates(char row, char column) {
        int maxBoardSide = this.maxCoordinate() + 1;

        // Convert characters to coordinates
        int x = (int) (column) - 'A' - maxBoardSide;
        int y = (int) (row) - 'A' - maxBoardSide;

        return new Coordinates(x, -y); // Negate y-coordinate to align with board conventions
    }

    /**
     * Retrieves the index of a coordinate in the available placements for a given player's card map.
     *
     * @param row    The character representing the row index.
     * @param column The character representing the column index.
     * @param owner  The owner of the card map.
     * @return The index of the coordinate in the available placements, or -1 if not found.
     */
    public int coordinateIndexByCharIndexes(char row, char column, String owner) {
        Coordinates coordinate = charsToCoordinates(row, column);
        CardMapRecord cardMapRecord = cardMaps.get(owner);

        if (cardMapRecord.availablePlacements().contains(coordinate))
            return cardMapRecord.availablePlacements().indexOf(coordinate);
        else
            return -1;
    }
}
