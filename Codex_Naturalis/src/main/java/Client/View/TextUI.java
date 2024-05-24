package Client.View;

import Client.Model.ClientModel;
import Client.Model.Records.*;
import Model.Cards.Corner;
import Model.Cards.CornerDirection;
import Model.Cards.CornerOrientation;
import Model.Cards.CornerType;
import Model.Game.CardPoolTypes;
import Model.Utility.Artifacts;
import Model.Utility.Coordinates;
import Server.Model.Lobby.LobbyUserConnectionStates;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * Text-based User Interface for interacting with the game.
 * <p>
 * The `TextUI` class is designed for command-line interactions with the user. It provides various methods to display
 * game information, get user input, and perform actions like clearing the terminal.
 * <p>
 * The primary functionalities of this class include:
 * - Displaying game titles, logos, and other introductory information.
 * - Interacting with the game through a text-based interface, allowing players to choose options and make game decisions.
 * - Providing detailed views of game components, such as card maps, card pools, and players' cards.
 * - Showing specific game-related information like shared objectives, points, and artifact counters.
 * - Utility functions for clearing the terminal and other miscellaneous operations.
 */
public class TextUI extends UserInterface {
    //ATTRIBUTES
    private final PrintStream out;
    private final ClientModel model;





    //CONSTRUCTOR
    public TextUI(ClientModel model) {
        out = new PrintStream(System.out, true);
        this.model = model;
    }





    //STATIC METHODS
    public static void displayGameTitle() {
        System.out.print("""
                 ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗
                ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝
                ██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗
                ██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║
                ╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║
                 ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝
                """);
    }

    public static void displayGameOver() {
        System.out.print("""
                 ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗
                ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗
                ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝
                ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗
                ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝ ╚████╔╝ ███████╗██║  ██║
                 ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝   ╚═══╝  ╚══════╝╚═╝  ╚═╝
                """);
    }

    /**
     * Clears the terminal screen to remove any existing text.
     * <p>
     * The method first checks the operating system (OS) type. If the OS is Windows, it uses a Windows-specific
     * command to clear the terminal. For non-Windows systems (like macOS and Linux), it uses ANSI escape sequences
     * to clear the screen.
     * <p>
     * If an error occurs during the process, an exception stack trace is printed.
     */
    public static void clearCMD() {
        try {
            //Detects if the OS is Windows
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                //Command for Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                //Command for macOS/Linux
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }





    //GET METHODS (Verify if the input is valid otherwise return null)
    /**
     * Converts input row and column strings into a Coordinates object.
     * The method interprets the row and column as letters (e.g., "A", "B", "C") and converts them
     * to numerical coordinates based on their ASCII values, adjusted by a maximum coordinate offset.
     *
     * @param row The row input as a string, representing a letter (e.g., "A", "B", "C").
     * @param column The column input as a string, representing a letter (e.g., "A", "B", "C").
     * @return A Coordinates object representing the converted x and y coordinates.
     */
    public Coordinates inputToCoordinates(String row, String column) {
        int maxBoardSide = this.maxCoordinate() + 1;

        //Insert before the row than the column but in the map the x-axis is the one of the column
        int x = (int) (column.toUpperCase().charAt(0)) - 'A' - maxBoardSide;
        int y = (int) (row.toUpperCase().charAt(0)) - 'A' - maxBoardSide;

        return new Coordinates(x,-y);
    }





    //ZOOM METHODS
    /**
     * Zooms into a specific card on the card map and displays its details.
     * <p>
     * This method allows the user to zoom into a specific card by first selecting a player and then providing coordinates
     * for the card on the player's card map. Once the card is identified, it displays detailed information about the card,
     * including its color, central artifacts, and corners, indicating if they are covered or visible.
     * <p>
     * It retrieves the player using the `getInputPlayer` method, then obtains the coordinates with `getInputCoordinates`,
     * and finally retrieves the card from the player's `CardMapView`. It uses `showCardPlacedDetails` to print out the
     * card's specific details.
     */
    public void zoomCardMap(String row, String column, String username) {
        //The cards are previously set with only visible attribute
        Coordinates coordinates = inputToCoordinates(row, column);

        //Get the card from the map of card visualized, checked in getInputCoordinates
        CardVisibilityRecord card = model.getCardMaps().get(username).getCardVisibilityRecord(coordinates);

        //Show all the textual details for the specific card
        showCardPlacedDetails(card);
    }

    /**
     * Displays detailed information about the cards held by a player.
     * <p>
     * This method provides:
     * - An index for each card held by the player.
     * - Whether the card can be placed on both sides.
     * - Detailed view of the card properties.
     * <p>
     * For each card held by the player, the following information is displayed:
     * - Index number.
     * - Playability on both sides.
     * - Card details.
     * <p>
     * The method calls `showCardDetails(CardView)` to display the specifics of each card.
     */
    public void zoomCardsHeld() {
        //Retrieve the list of cards held by the player
        List<CardRecord> cardsHeld = model.getPlayerSecretInfoRecord().cardsHeld();
        Map<CardRecord,Boolean> cardPlayability = model.getPlayerSecretInfoRecord().cardPlayability();

        int i = 1;

        //Loop through the map of cards held by the player
        for (CardRecord cardRecord : cardsHeld) {

            //Print if the card can be placed on both sides
            out.println(i++ + " - This card can be placed on both sides: " + cardPlayability.get(cardRecord));

            //Show the details of the card
            showCardDetails(cardRecord);
        }
        System.out.println();
        displayArtifact(model.getMyUsername());
    }

    /**
     * Displays the detailed view of the card pools in the game, including both resource and golden pools.
     * <p>
     * This method shows:
     * - The artifact type of the top deck card for both resource and golden pools.
     * - The list of visible cards for each pool along with their details.
     * <p>
     * The method outputs the following information for each card:
     * - Index and artifact type.
     * - Detailed view of card properties.
     */
    public void zoomCardPool(CardPoolTypes cardPoolTypes) {
        if (cardPoolTypes == CardPoolTypes.RESOURCE) {
            Artifacts tdr = model.getTableRecord().topDeckResource();
            List<CardRecord> vcvr = model.getTableRecord().visibleCardRecordResource();
            out.println("\nRESOURCE POOL:");
            out.println("1 - Artifact Type: " + tdr.name() + " (covered card)");
            for (int i = 0; i < vcvr.size(); i++) {
                CardRecord card = vcvr.get(i);
                out.print((i + 2) + " - ");
                showCardDetails(card);
            }
        } else {
            Artifacts tdg = model.getTableRecord().topDeckGolden();
            List<CardRecord> vcvg = model.getTableRecord().visibleCardRecordGolden();
            out.println("\nGOLDEN POOL:");
            out.println("1 - Artifact Type: " + tdg.name() + " (covered card)");
            for (int i = 0; i < vcvg.size(); i++) {
                CardRecord card = vcvg.get(i);
                out.print((i + 2) + " - ");
                showCardDetails(card);
            }
        }
    }





    //SHOW METHODS
    /**
     * Displays the current state of the game board.
     *
     * <p>This method shows shared objectives, the player's secret objectives,
     * and the points of each player. It iterates through all player records
     * and displays each player's card map on the game board.
     */
    public void showGameBoard() {
        showSharedObjectives(model.getGameRecord());
        showObjective(model.getPlayerSecretInfoRecord().secretObjectives());
        showPoints();
        showCardMaps(model.getPlayers());
    }

    /**
     * Displays detailed information about a given card.
     * <p>
     * This method prints the following details about the specified card:
     * - The points associated with the card.
     * - Information about the card's front corners, including corner orientation and artifact type.
     * - Details about the back corners (assumed to be empty for all cards).
     * - Whether the card requires covering certain corners to gain points.
     * - Any specific artifact that the card might require.
     * - Constraints on the card, if any, with detailed information about each constraint.
     *
     * @param card The CardView object representing the card whose details are to be displayed.
     */
    public void showCardDetails(CardRecord card) { //Used only for resource and golden card, not already placed
        //Prints the artifact type of the card
        out.println("Artifact Type: " + card.cardColor());

        //Prints the points of the card
        out.println("Points: " + card.points());

        //Prints the corners of the card their details
        out.println("Front Corners:");
        for (CornerOrientation co : card.corners().keySet()) {
            Corner corner = card.corners().get(co);

            //Details about the corners
            out.print(" - " + co.getCornerDirection().name() + ":");
            if (corner.getCornerType() == CornerType.ARTIFACT)
                out.println(" " + corner.getArtifact());
            else
                out.println(" " + corner.getCornerType());
        }
        out.println("Back Corners: all empty");

        //Prints if the card needs to cover corners in order to gain points for the player
        if(card.requiresCorner()) {
            out.println("Requires Corner");
        }

        //Prints the artifact needed by the card
        if(card.requiredArtifact() != Artifacts.NULL && card.requiredArtifact() != null) {
            out.println("Required Object: " + card.requiredArtifact());
        }

        //Prints the existing constraints
        if (card.constraint() != null) {
            out.println("Constraints:");
            for (Artifacts artifacts : card.constraint().keySet()) {
                out.println(" - " + artifacts + ": " + card.constraint().get(artifacts));
            }
        }
    }

    /**
     * Displays the detailed information of a card that has been placed on a player's card map.
     * <p>
     * This method prints the following details:
     * - The artifact type of the card.
     * - The central artifacts (if the card is a `CardStarter` and placed face down).
     * - The corners of the card with a note indicating if they are covered.
     * <p>
     * If a corner is covered, it will be displayed in red. The method retrieves the corner details and checks
     * whether it is visible or covered to determine the color coding.
     *
     * @param cardVisibilityRecord The `CardView` representing the card whose details are to be displayed.
     */
    public void showCardPlacedDetails(CardVisibilityRecord cardVisibilityRecord) {
        //Prints the color of the card
        out.println("\nArtifact Type: " + cardVisibilityRecord.cardColor());

        //Prints the corners of the card their details
        out.println("Corners: (Those covered will be printed in red)");
        for (CornerDirection cd : cardVisibilityRecord.corners().keySet()) {
            // Retrieve the corner based on its orientation
            Corner corner = cardVisibilityRecord.corners().get(cd);

            // Check if the corner is covered, indicated by the color red
            if (!cardVisibilityRecord.cornerVisibility().get(cd)) {
                out.print(TerminalColor.RED);
            }

            // Display the corner details
            out.print(" - " + cd.name() + ":");

            // Print the artifact or corner type based on the corner's contents
            if (corner.getCornerType() == CornerType.ARTIFACT) {
                out.println(" " + corner.getArtifact());
            } else {
                out.println(" " + corner.getCornerType());
            }

            // Reset the terminal color to avoid affecting other output
            out.print(TerminalColor.RESET);
        }
    }


    /**
     * Displays the shared objectives in the game, along with their detailed information.
     *
     * @param gameRecord The GameView object containing the current state of the game, including shared objectives.
     * <p>
     * This method retrieves the list of shared objectives from the given GameView and displays their
     * description, points, and additional details depending on the type of objective.
     * <p>
     * If the objective has a geometric pattern, the coordinates and associated artifacts are displayed.
     * If the objective has numeric requirements, the artifact and its required quantity are shown.
     *
     * If there are no shared objectives, a message is displayed indicating that no objectives are available.
     */
    public void showSharedObjectives(GameRecord gameRecord) {
        List<ObjectiveRecord> sharedObjectives = model.getTableRecord().sharedObjectives();

        //If the shardObjectives list is empty, we inform the user.
        if (sharedObjectives.isEmpty()) {
            out.println("No available shared objectives.");
            return;
        }

        //If the sharedObjectives list isn't empty, we print them one by one.
        out.println("Shared objectives:");
        for (ObjectiveRecord objective : sharedObjectives) {
            out.println("Description: " + objective.description());
            out.println("Points: " + objective.points());

            out.println(); //Space between the objectives
        }
    }

    public void showObjective(ObjectiveRecord objectiveRecord) {
        System.out.println("Description: " + objectiveRecord.description());
        System.out.println("Points: " + objectiveRecord.points());
    }

    /**
     * Displays the current points for all players in the game.
     *
     * <p>
     * This method retrieves the list of PlayerViews from the given GameView and displays the points
     * for each player along with their username.
     */
    public void showPoints() {
        out.println("POINTS TABLE (You are the player in green, while the ones in red are disconnected)");
        for (PlayerRecord playerRecord : model.getPlayers()) {
            if (playerRecord.username().equals(model.getMyUsername()))
                out.println(TerminalColor.GREEN_BRIGHT + playerRecord.username() + TerminalColor.RESET + ": " + playerRecord.points());
            else if (usernameToLobbyUserRecord(model, playerRecord.username()).connectionStatus() == LobbyUserConnectionStates.OFFLINE)
                out.println(TerminalColor.RED_BRIGHT + playerRecord.username() + TerminalColor.RESET + ": " + playerRecord.points());
            else
                out.println(playerRecord.username() + ": " + playerRecord.points());
        }
    }

    /**
     * Displays a visual representation of a card at a specific coordinate on the game map.
     *
     * @param coordinates The coordinates of the card to display.
     * <p>
     * This method checks if the given coordinates contain a placed card, an available placement, or if they fall within an
     * unused grid area. It then outputs a colored representation:
     * - If there's a placed card, it shows a color based on the card's type:
     *   - BLUE for Animal cards
     *   - RED for Fungi cards
     *   - PURPLE for Insect cards
     *   - GREEN for Plant cards
     *   - YELLOW for NULL cards (generic empty card)
     * - If there's an available placement, it shows a WHITE square with an "x".
     * - If it's an empty grid area with an odd coordinate sum, it shows a BLACK square.
     * - Otherwise, it resets to default color.
     */
    public String showCard(String username, Coordinates coordinates) {
        CardMapRecord cardMapRecord = model.getCardMaps().get(username);

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
     * Displays the card maps of multiple players side by side on the terminal.
     *
     * <p>This method prints the card maps of each player horizontally aligned,
     * allowing for easy comparison and viewing of multiple players' card maps simultaneously.
     *
     * @param playerRecords a list of PlayerRecord objects representing the players whose card maps are to be displayed
     */
    public void showCardMaps(List<PlayerRecord> playerRecords) {
        int maxCoordinate = this.maxCoordinate();
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

    /**
     * Displays the current artifact counter, listing the types of artifacts and their corresponding quantities.
     * <p>
     * The output shows a summary of the player's artifacts with a label "ARTIFACT COUNTER."
     * <p>
     * Each artifact type is listed along with the number of that artifact currently held by the player.
     * The artifact types and their associated quantities are retrieved from the cardMapView's artifact counter.
     * <p>
     * After listing all artifact types and their counts, an additional blank line is printed for separation.
     */
    public void displayArtifact(String username) {
        //Get the artifact counter from the card map view
        Map<Artifacts, Integer> artifactsCounter = model.getCardMaps().get(username).artifactsCounter();

        //Display the artifact counter header
        out.println("ARTIFACT COUNTER");

        //Iterate through the artifact counter and display the artifact name and its count
        for (Artifacts artifact : artifactsCounter.keySet()) {
            out.println(artifact.name() + ": " + artifactsCounter.get(artifact)); //Display artifact name and count
        }
    }

    /**
     * Determines the maximum coordinate value among all coordinates of placed cards in the player records.
     * <p>
     * This method iterates through all player records and their corresponding placed cards.
     * For each card's coordinates, it calculates the maximum absolute value between the x and y coordinates.
     * The method then returns the maximum coordinate value found.
     *
     * @return The maximum coordinate value among all placed cards in the player records.
     */
    public int maxCoordinate() {
        int mbs = 0;
        for (String username : model.getCardMaps().keySet())
            for (Coordinates coordinates : model.getCardMaps().get(username).cardsPlaced().keySet()) {
                int max = Math.max(Math.abs(coordinates.getCoordY()), Math.abs(coordinates.getCoordY()));
                if (max > mbs) {
                    mbs = max;
                }
        }
        return Math.max(mbs, 5);
    }
}