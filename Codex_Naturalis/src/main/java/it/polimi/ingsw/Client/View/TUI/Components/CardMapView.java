package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.CardMaps;
import it.polimi.ingsw.Client.Model.Players;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a component responsible for displaying the card maps of players in a terminal-based user interface (TUI).
 * It extends the {@link LiveComponent} class and implements methods to print and manage the visual representation of card maps.
 * <p>
 * The {@code CardMapView} class interacts with {@link CardMaps} and {@link Players} instances to retrieve and display
 * card placement information and status on a board-like grid. It provides methods to print the card maps for multiple players
 * side by side, showing card placements, availability for placement, and background colors based on the card's artifact type
 * or checkerboard pattern.
 * <p>
 * It includes methods to observe changes in the card maps and players' information through {@link RefreshManager},
 * ensuring the displayed information remains synchronized with the underlying model.
 */
public class CardMapView extends LiveComponent {

    //CONSTRUCTOR
    /**
     * Constructs a {@code CardMapView} object, initializing it to observe changes in {@link CardMaps} and {@link Players}.
     * It calls {@link LiveComponent}'s constructor and immediately refreshes observed data.
     */
    public CardMapView(){
        super();
        refreshObserved();
    }

    /**
     * Prints the card maps for all players currently in the game.
     * This method retrieves the list of players from {@link Players} and displays their card maps using {@link #showCardMaps(List)}.
     */
    @Override
    public void print() {
        showCardMaps(Players.getInstance().getPlayers());
    }

    /**
     * Removes this component from the list of observed objects in {@link RefreshManager} for both {@link CardMaps} and {@link Players}.
     * This method is called to stop observing changes in card maps and player information when the component is no longer needed.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, CardMaps.getInstance());
        RefreshManager.getInstance().removeObserved(this, Players.getInstance());
    }

    /**
     * Adds this component to the list of observed objects in {@link RefreshManager} for both {@link CardMaps} and {@link Players}.
     * This method is called to start observing changes in card maps and player information when the component is instantiated.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardMaps.getInstance());
        RefreshManager.getInstance().addObserved(this, Players.getInstance());
    }

    /**
     * Displays the card or status at the specified coordinates for a given player.
     * <p>
     * This method returns a string representing the visual representation of a card or
     * placement status at the specified coordinates on the card map of the given player.
     * The display includes the background color based on the card's artifact type or
     * other indicators for available placements or checkerboard pattern.
     *
     * @param username the username of the player whose card map is being displayed.
     * @param coordinates the coordinates on the card map to be displayed.
     * @return a string representing the visual representation of the card or status at the specified coordinates.
     * <p>
     * The method performs the following steps:
     * 1. Retrieves the card map record for the specified player.
     * 2. Checks if a card is placed at the given coordinates and returns the corresponding background color.
     * 3. If no card is placed, checks if the coordinates are available for placement and returns an indicator.
     * 4. If neither card nor placement, checks for a checkerboard pattern and returns the appropriate background color.
     * 5. If none of the above, returns a reset background.
     */
    private String showCard(String username, Coordinates coordinates) {
        CardMapRecord cardMapRecord = CardMaps.getInstance().getCardMaps().get(username);

        //Check if there is a card placed at the specified coordinates
        if (cardMapRecord.cardsPlaced().containsKey(coordinates)) {
            //Get the color of the card at the given coordinates
            Artifacts cardColor = cardMapRecord.getCardVisibilityRecord(coordinates).cardColor();
            //Display the background color based on the card's artifact type
            switch (cardColor) {
                case ANIMAL -> {
                    return (TerminalColor.BLUE_BACKGROUND_BRIGHT + "   ");
                }
                case FUNGI -> {
                    return(TerminalColor.RED_BACKGROUND_BRIGHT + "   ");
                }
                case INSECT -> {
                    return(TerminalColor.PURPLE_BACKGROUND_BRIGHT + "   ");
                }
                case PLANT -> {
                    return(TerminalColor.GREEN_BACKGROUND_BRIGHT + "   ");
                }
                case NULL -> {
                    return(TerminalColor.YELLOW_BACKGROUND_BRIGHT + "   ");
                }
            }
        }
        //Check if the given coordinates are available for placement
        else if (cardMapRecord.availablePlacements().contains(coordinates)) {
            return (TerminalColor.WHITE_BACKGROUND_BRIGHT + " x "); // Indicate available placement
        }
        //Check if the given coordinates are part of a checkerboard pattern (odd sum of x and y)
        else if (((Math.abs(coordinates.getCoordX()) + (Math.abs(coordinates.getCoordY()))) % 2) != 0) {
            return (TerminalColor.BLACK_BACKGROUND + "   "); //Black background for odd squares
        } else {
            return (TerminalColor.RESET + "   "); //Reset for even squares
        }
        return null;
    }

    /**
     * Displays the card maps for a list of players.
     * <p>
     * This method constructs and prints the card maps for each player side by side. Each card map
     * includes a title with the player's username, column headers, and the card placements on the board.
     *
     * @param playerRecords a list of PlayerRecord objects representing the players whose card maps will be shown.
     * <p>
     * This method performs the following steps:
     * 1. Calculates the maximum coordinate and board side dimensions based on the card maps.
     * 2. Initializes a list to hold the rows for each player's card map.
     * 3. Iterates over each player record to prepare the individual card maps:
     *    - Adds a title row with the player's username.
     *    - Adds a row of column headers (A, B, C, ...).
     *    - Adds rows representing the card placements on the board, including row headers.
     * 4. Prints the card maps side by side, ensuring alignment across multiple players.
     * 5. Adds extra space at the end for better visual separation.
     */
    public void showCardMaps(List<PlayerRecord> playerRecords) {
        int maxCoordinate = CardMaps.getInstance().maxCoordinate();
        int maxBoardSide = (maxCoordinate * 2) + 3;

        // List to hold rows for each player's map
        List<List<String>> maps = new ArrayList<>();

        // Prepare the card maps for each player
        for (PlayerRecord playerRecord : playerRecords) {
            List<String> mapRows = new ArrayList<>();

            // Title
            String title = playerRecord.username() + "'s Card Map";
            int padding = (maxBoardSide * 3 + 2 - title.length()) / 2;
            String titleRow = " ".repeat(padding) + title + " ".repeat(padding) + "   ";
            mapRows.add(titleRow);

            // Column headers (A, B, C, ...)
            StringBuilder headerRow = new StringBuilder(" ");
            for (int i = 0; i < maxBoardSide; i++) {
                headerRow.append("  ").append((char) (i + 65)); //'A' corresponds to ASCII value 65
            }
            headerRow.append(" ");
            mapRows.add(headerRow.toString());

            // Card map rows
            for (int j = 0; j < maxBoardSide; j++) {
                StringBuilder row = new StringBuilder();
                row.append((char) (j + 65)).append(" "); // Row header

                for (int k = 0; k < maxBoardSide; k++) {
                    row.append(showCard(playerRecord.username(), new Coordinates(k - maxCoordinate - 1, -j + maxCoordinate + 1)));
                    row.append(TerminalColor.RESET);
                }

                mapRows.add(row.toString());
            }

            maps.add(mapRows);
        }

        // Print the maps side by side
        int totalRows = maps.getFirst().size(); // Assumes all maps have the same number of rows
        for (int i = 0; i < totalRows; i++) {
            for (List<String> map : maps) {
                out.print(map.get(i));
                out.print("   "); // Add some space between maps
            }
            out.println();
        }

        out.println(); // Extra space at the end
    }
}
