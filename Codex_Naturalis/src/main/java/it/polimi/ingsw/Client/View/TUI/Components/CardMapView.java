package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.CardMaps;
import it.polimi.ingsw.Client.Model.Players;
import it.polimi.ingsw.Client.Model.States.ClientState;
import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CardMapView extends Component implements Observer {
    //ATTRIBUTES
    private final CardMaps cardMaps;
    private final Players players;

    private final ClientState clientState;




    //CONSTRUCTOR
    public CardMapView(CardMaps cardMaps, Players players, ClientState clientState){
        this.cardMaps = cardMaps;
        this.clientState = clientState;
        this.cardMaps.subscribe(this);
        this.players = players;
        this.players.subscribe(this);
    }

    @Override
    public void print() {
        showCardMaps(players.getPlayers());
    }

    //Helper methods to print
    private String showCard(String username, Coordinates coordinates) {
        CardMapRecord cardMapRecord = cardMaps.getCardMaps().get(username);

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
    public void showCardMaps(List<PlayerRecord> playerRecords) {
        int maxCoordinate = cardMaps.maxCoordinate();
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


    @Override
    public void update() {
        clientState.print();
    }
}
