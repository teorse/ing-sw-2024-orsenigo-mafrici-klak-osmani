package Client.View;

import Client.Model.ClientModel;
import Client.Model.Records.*;
import Model.Cards.Corner;
import Model.Cards.CornerDirection;
import Model.Cards.CornerOrientation;
import Model.Cards.CornerType;
import Model.Game.CardPoolTypes;
import Model.Player.CardVisibility;
import Model.Utility.Artifacts;
import Model.Utility.Coordinates;
import Server.Model.Lobby.LobbyRoles;
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
    private final Scanner in;
    private final PrintStream out;
    private final ClientModel model;





    //CONSTRUCTOR
    public TextUI(ClientModel model) {
        in = new Scanner(System.in);
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
                                                                                                                                   \s
                """);
    }

    public static void displayGameOver() {
        System.out.print("""
                 ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗\s
                ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗
                ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝
                ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗
                ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝ ╚████╔╝ ███████╗██║  ██║
                 ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝   ╚═══╝  ╚══════╝╚═╝  ╚═╝
                                                                                         \s
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
     * Retrieves a {@code PlayerRecord} based on user input.
     *
     * <p>This method prompts the user to input an integer representing their choice of player.
     * It validates the input to ensure it is a valid integer within the range of available players.
     * If the input is invalid, it prints an error message and returns null.
     * If the input is valid, it returns the corresponding {@code PlayerRecord} from the list of players.
     *
     * @param input the user input string representing the player choice
     * @return the selected {@code PlayerRecord}, or {@code null} if the input is invalid
     */
    public PlayerRecord getInputPlayer(String input) {
        List<PlayerRecord> playerRecords = new ArrayList<>(model.getPlayerCardMapRecord().keySet());
        int choice;

        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            out.println("Invalid format input. Please enter an int between 1 and " + playerRecords.size());
            return null;
        }

        if (choice >= 1 && choice <= playerRecords.size()) {
            return playerRecords.get(choice - 1);
        } else {
            out.println("Invalid choice. Please enter an int between 1 and " + playerRecords.size());
            return null;
        }
    }

    /**
     * Gets the coordinates based on user input.
     *
     * <p>This method prompts the user to input coordinates in the format of a row and a column (e.g., "A B").
     * It then processes the input to determine the corresponding coordinates on the game board. The method
     * validates the input to ensure it contains exactly two characters, both of which must be within the
     * allowable range of rows and columns. If the input is invalid, it prints an error message and returns null.
     * If the coordinates are valid and a card is placed at the given coordinates, it returns the coordinates.
     * Otherwise, it prints an error message and returns null.
     *
     * @param input the user input string representing the coordinates
     * @return a {@code Coordinates} object representing the coordinates on the game board, or {@code null} if the input is invalid or no card is placed at the coordinates
     */
    public Coordinates getInputCoordinates(String input) {
        Coordinates coordinatesView;
        int maxCoordinate = this.maxCoordinate();
        int maxBoardSide = (maxCoordinate * 2) + 3;

        input = in.nextLine();
        String[] readCoords;
        readCoords = input.split("\\s+");

        if (readCoords.length != 2 || readCoords[0].length() != 1 || readCoords[1].length() != 1) {
            out.println("Insert a row [A - " + (char)(maxBoardSide + 'A') + "]" +
                    " and a column [A - " + (char)(maxBoardSide + 'A') + "]");
            return  null;
        }

        //Insert before the row than the column but in the map the x axis is the one of the column
        int y = (int) (readCoords[0].toUpperCase().charAt(0)) - 'A' - maxCoordinate;
        int x = (int) (readCoords[1].toUpperCase().charAt(1)) - 'A' - maxCoordinate;
        coordinatesView = new Coordinates(x,y);

        if (model.getPlayerCardMapRecord().get(myPlayerRecord(model)).cardsPlaced().get(coordinatesView) != null) {
            return coordinatesView;
        } else {
            out.println("Insert a row [A - " + (char) (maxBoardSide + 'A') + "]" +
                    " and a column [A - " + (char) (maxBoardSide + 'A') + "]");
            return null;
        }
    }

    public boolean isAvaiableCoordinates(Coordinates coordinates) {
        return model.getPlayerCardMapRecord().get(myPlayerRecord(model)).availablePlacements().contains(coordinates);
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
    public void zoomCardMap(String input, PlayerRecord playerRecord) {
        //The cards are previously set with only visible attribute
        Coordinates coordinates = getInputCoordinates(input);

        //Get the card from the map of card visualized, checked in getInputCoordinates
        CardVisibilityRecord card = model.getPlayerCardMapRecord().get(playerRecord).getCardVisibilityRecord(coordinates);

        //Show all the textual details for the specific card
        showCardPlacedDetails(playerRecord, card, coordinates);
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
        Map<CardRecord,Boolean> cardsHeld = model.getPlayerSecretInfoRecord().cardsHeld();

        //Loop through the map of cards held by the player
        for (CardRecord cardRecord : cardsHeld.keySet()) {
            int i = 1;
            //Print if the card can be placed on both sides
            out.println(i + " - This card can be placed on both sides: " + cardsHeld.get(cardRecord));

            //Show the details of the card
            showCardDetails(cardRecord);
        }
        displayArtifact(UserInterface.myPlayerRecord(model));
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
        for (PlayerRecord playerRecord : model.getPlayerRecords()) {
            showCardMap(playerRecord);
        }
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
     * @param playerRecord  The `PlayerView` representing the player whose card map contains the placed card.
     * @param cardVisibilityRecord The `CardView` representing the card whose details are to be displayed.
     * @param coordinates The `Coordinates` indicating where the card is placed on the card map.
     */
    public void showCardPlacedDetails(PlayerRecord playerRecord, CardVisibilityRecord cardVisibilityRecord, Coordinates coordinates) {
        //Prints the color of the card
        out.println("Artifact Type: " + cardVisibilityRecord.cardColor());

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
            out.println(TerminalColor.RESET);
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
     * for each player along with their nickname.
     */
    public void showPoints() {
        // TODO fixare il metodo aggiungendo myPR
        out.println("POINTS TABLE (You are the player in green, while the ones in red are disconnected)");
        for(PlayerRecord playerRecord : model.getPlayerCardMapRecord().keySet()) {
            for (LobbyUserRecord lobbyUserRecord : model.getLobbyUserRecords()) {
                if (playerRecord.nickname().equals(lobbyUserRecord.username())) {
                    if (playerRecord.nickname().equals(model.getMyUsername())) {
                        out.println(TerminalColor.GREEN_BRIGHT + playerRecord.nickname() + TerminalColor.RESET + ": " + playerRecord.points());
                    } else if (lobbyUserRecord.connectionStatus() == LobbyUserConnectionStates.OFFLINE) {
                        out.println(TerminalColor.RED_BRIGHT + playerRecord.nickname() + TerminalColor.RESET + ": " + playerRecord.points());
                    }
                }
            }
            out.println(playerRecord.nickname() + ": " + playerRecord.points());
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
    public void showCard(PlayerRecord playerRecord, Coordinates coordinates) {
        CardMapRecord cardMapRecord = model.getPlayerCardMapRecord().get(playerRecord);

        //Check if there is a card placed at the specified coordinates
        if (cardMapRecord.cardsPlaced().containsKey(coordinates)) {
            //Get the color of the card at the given coordinates
            Artifacts cardColor = cardMapRecord.getCardVisibilityRecord(coordinates).cardColor();
            //Display the background color based on the card's artifact type
            switch (cardColor) {
                case ANIMAL -> out.print(TerminalColor.BLUE_BACKGROUND_BRIGHT + "   ");
                case FUNGI -> out.print(TerminalColor.RED_BACKGROUND_BRIGHT + "   ");
                case INSECT -> out.print(TerminalColor.PURPLE_BACKGROUND_BRIGHT + "   ");
                case PLANT -> out.print(TerminalColor.GREEN_BACKGROUND_BRIGHT + "   ");
                case NULL -> out.print(TerminalColor.YELLOW_BACKGROUND_BRIGHT + "   ");
            }
        }
        //Check if the given coordinates are available for placement
        else if (cardMapRecord.availablePlacements().contains(coordinates)) {
            out.print(TerminalColor.WHITE_BACKGROUND_BRIGHT + " x "); // Indicate available placement
        }
        //Check if the given coordinates are part of a checkerboard pattern (odd sum of x and y)
        else if (((Math.abs(coordinates.getCoordX()) + (Math.abs(coordinates.getCoordY()))) % 2) != 0) {
            out.print(TerminalColor.BLACK_BACKGROUND + "   "); //Black background for odd squares
        } else {
            out.print(TerminalColor.RESET + "   "); //Reset for even squares
        }
    }


    /**
     * Displays the card map, showing the current state of the game's map with placed cards and available placements.
     * <p>
     * The output represents a square grid with coordinates labeled from A onward, both for rows and columns.
     * The grid uses specific colors to represent the following:
     * - Placed cards:
     *   - BLUE for Animal cards
     *   - RED for Fungi cards
     *   - PURPLE for Insect cards
     *   - GREEN for Plant cards
     *   - YELLOW for Starter cards
     * - Available placements are shown with a WHITE square containing an "x".
     * - Grid areas without a card placement alternate between BLACK and default color for visual separation.
     * <p>
     * The display includes a header indicating "CARD MAP" and provides a grid structure with corresponding row and column labels.
     */
    public void showCardMap(PlayerRecord playerRecord) {
        int maxCoordinate = this.maxCoordinate();
        int maxBoardSide = (maxCoordinate * 2) + 3;

        //Print spacing for aligning the title
        for (int i = 0; i < maxCoordinate; i++) {
            out.print("   ");
        }

        //Print the title "CARD MAP"
        out.println(playerRecord.nickname() + "'s Card Map");

        //Print the column headers (A, B, C, ...)
        out.print(" ");
        for (int i = 0; i < maxBoardSide; i++) {
            out.print("  " + (char) (i + 65)); //'A' corresponds to ASCII value 65
        }
        out.println();

        //Print the board row by row
        for (int j = 0; j < maxBoardSide; j++) { //Iterate through each row
            out.print((char) (j + 65) + " "); //Print the row header

            for (int k = 0; k < maxBoardSide; k++) { //Iterate through each column
                //Display the card at the specified coordinates
                showCard(playerRecord, new Coordinates(k - maxCoordinate, j - maxCoordinate));
                //Reset the terminal color after displaying each card
                out.print(TerminalColor.RESET);
            }

            out.println(); //New line after each row
        }

        out.println(); //Extra space at the end of the board
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
    public void displayArtifact(PlayerRecord playerRecord) {
        //Get the artifact counter from the card map view
        Map<Artifacts, Integer> artifactsCounter = model.getPlayerCardMapRecord().get(playerRecord).artifactsCounter();

        //Display the artifact counter header
        out.println("ARTIFACT COUNTER");

        //Iterate through the artifact counter and display the artifact name and its count
        for (Artifacts artifact : artifactsCounter.keySet()) {
            out.println(artifact.name() + ": " + artifactsCounter.get(artifact)); //Display artifact name and count
        }

        //Add a new line at the end for spacing
        out.println();
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
        for (PlayerRecord playerRecord : model.getPlayerCardMapRecord().keySet()) {
            for (Coordinates coordinates : model.getPlayerCardMapRecord().get(playerRecord).cardsPlaced().keySet()) {
                int max = Math.max(Math.abs(coordinates.getCoordY()), Math.abs(coordinates.getCoordY()));
                if (max > mbs) {
                    mbs = max;
                }
            }
        }
        return mbs;
    }
}