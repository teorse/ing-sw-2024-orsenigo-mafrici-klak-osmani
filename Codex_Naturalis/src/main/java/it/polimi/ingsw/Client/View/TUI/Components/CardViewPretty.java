package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerDirection;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerOrientation;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CardViewPretty extends Component{
    private final CardRecord card;
    private final String cardColor;
    private final String cardBackgroundColor;
    private final boolean printFront;
    private final boolean printRear;

    public CardViewPretty(CardRecord card, boolean printFront, boolean printRear) {
        this.card = card;
        this.printFront = printFront;
        this.printRear = printRear;

        switch (card.cardColor()) {
            case FUNGI -> {
                if (card.constraint() != null && !card.constraint().isEmpty())
                    cardColor = TerminalColor.YELLOW;
                else
                    cardColor = TerminalColor.RED;

                cardBackgroundColor = TerminalColor.RED_BACKGROUND;
            }
            case ANIMAL -> {
                if (card.constraint() != null && !card.constraint().isEmpty())
                    cardColor = TerminalColor.YELLOW;
                else
                    cardColor = TerminalColor.BLUE;

                cardBackgroundColor = TerminalColor.BLUE_BACKGROUND;
            }
            case PLANT -> {
                if (card.constraint() != null && !card.constraint().isEmpty())
                    cardColor = TerminalColor.YELLOW;
                else
                    cardColor = TerminalColor.GREEN;

                cardBackgroundColor = TerminalColor.GREEN_BACKGROUND;
            }
            case INSECT -> {
                if (card.constraint() != null && !card.constraint().isEmpty())
                    cardColor = TerminalColor.YELLOW;
                else
                    cardColor = TerminalColor.PURPLE;

                cardBackgroundColor = TerminalColor.PURPLE_BACKGROUND;
            }
            default -> {
                cardColor = TerminalColor.YELLOW;
                cardBackgroundColor = TerminalColor.YELLOW_BACKGROUND;
            }
        }
    }

    public void print(){
        if(printFront && printRear){
            StringBuilder cardToPrint = new StringBuilder();
            System.out.print("          ");//Prints 10 spaces
            System.out.print("   FRONT  ");//Prints 10 spaces
            System.out.print("          ");//Prints 10 spaces

            System.out.print("     ");

            System.out.print("          ");//Prints 10 spaces
            System.out.print("   BACK   ");//Prints 10 spaces
            System.out.print("          ");//Prints 10 spaces

            System.out.println();

            for (int i = 0; i < 7; i++) {
                cardToPrint.append(drawSide(true, i, true)).append(drawCenter(i, true)).append(drawSide(false, i, true))
                        .append("     ")
                        .append(drawSide(true, i, false)).append(drawCenter(i, false)).append(drawSide(false, i, false))
                        .append("\n");
            }

            System.out.println(cardToPrint);
        }
        else if(printFront){
            StringBuilder cardToPrint = new StringBuilder();
            System.out.print("          ");//Prints 10 spaces
            System.out.print("   FRONT  ");//Prints 10 spaces
            System.out.print("          ");//Prints 10 spaces

            System.out.println();

            for(int i = 0; i < 7; i++){
                cardToPrint.append(drawSide(true, i, true)).append(drawCenter(i, true)).append(drawSide(false, i, true))
                        .append("\n");
            }
            System.out.println(cardToPrint);
        }
        else if (printRear) {
            StringBuilder cardToPrint = new StringBuilder();
            System.out.print("          ");//Prints 10 spaces
            System.out.print("   BACK   ");//Prints 10 spaces
            System.out.print("          ");//Prints 10 spaces

            System.out.println();

            for(int i = 0; i < 7; i++){
                cardToPrint.append(drawSide(true, i, false)).append(drawCenter(i, false)).append(drawSide(false, i, false))
                        .append("\n");
            }
            System.out.println(cardToPrint);
        }
    }

    //true = right, false = left
    private String drawSide(boolean leftSide, int row, boolean faceUp) {
        Corner corner;
        if (leftSide) {
            switch (row) {
                case 0 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.NW, faceUp));
                    if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardColor + "┌────┬──" + TerminalColor.RESET;
                    else
                        return cardColor + "┌───────" + TerminalColor.RESET;
                }
                case 1 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.NW, faceUp));
                    if (corner.getCornerType().equals(CornerType.EMPTY))
                        return cardColor + "│" + TerminalColor.RESET + "    " + cardColor + "│" + TerminalColor.RESET + cardBackgroundColor + "  " + TerminalColor.RESET;
                    else if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardColor + "│" + TerminalColor.RESET + " " + corner.getArtifact().getUnicodeIcon() + " " + cardColor + "│" + cardBackgroundColor + "  " + TerminalColor.RESET;
                    else
                        return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                }
                case 2 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.NW, faceUp));
                    if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardColor + "├────┘" + cardBackgroundColor + "  " + TerminalColor.RESET;
                    else
                        return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                }
                case 3 -> {
                    return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                }
                case 4 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.SW, faceUp));
                    if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardColor + "├────┐" + cardBackgroundColor + "  " + TerminalColor.RESET;
                    else
                        return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                }
                case 5 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.SW, faceUp));
                    if (corner.getCornerType().equals(CornerType.EMPTY))
                        return cardColor + "│    │" + cardBackgroundColor + "  " + TerminalColor.RESET;
                    else if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardColor + "│ " + corner.getArtifact().getUnicodeIcon() + " │" + cardBackgroundColor + "  " + TerminalColor.RESET;
                    else
                        return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                }
                case 6 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.SW, faceUp));
                    if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardColor + "└────┴──" + TerminalColor.RESET;
                    else
                        return cardColor + "└───────" + TerminalColor.RESET;
                }
            }
        } else {
            switch (row) {
                case 0 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.NE, faceUp));
                    if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardColor + "──┬────┐" + TerminalColor.RESET;
                    else
                        return cardColor + "───────┐" + TerminalColor.RESET;
                }
                case 1 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.NE, faceUp));
                    if (corner.getCornerType().equals(CornerType.EMPTY))
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│    │" + TerminalColor.RESET;
                    else if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│ " + corner.getArtifact().getUnicodeIcon() + " │" + TerminalColor.RESET;
                    else
                        return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                }
                case 2 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.NE, faceUp));
                    if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "└────┤" + TerminalColor.RESET;
                    else
                        return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                }
                case 3 -> {
                    return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                }
                case 4 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.SE, faceUp));
                    if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "┌────┤" + TerminalColor.RESET;
                    else
                        return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                }
                case 5 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.SE, faceUp));
                    if (corner.getCornerType().equals(CornerType.EMPTY))
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│    │" + TerminalColor.RESET;
                    else if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│ " + corner.getArtifact().getUnicodeIcon() + " │" + TerminalColor.RESET;
                    else
                        return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                }
                case 6 -> {
                    corner = card.corners().get(new CornerOrientation(CornerDirection.SE, faceUp));
                    if (!corner.getCornerType().equals(CornerType.NULL))
                        return cardColor + "──┴────┘" + TerminalColor.RESET;
                    else
                        return cardColor + "───────┘" + TerminalColor.RESET;
                }
            }
        }
        return "";
    }

    private String drawCenter(int row, boolean faceUp) {
        if (faceUp) {
            switch (row) {
                case 0 -> {
                    if (card.points() == 0)
                        return cardColor + "──────────────" + TerminalColor.RESET;
                    else {
                        if (card.requiresCorner() || !card.requiredArtifact().equals(Artifacts.NULL))
                            return cardColor + "┬────────────┬" + TerminalColor.RESET;
                        else
                            return cardColor + "───┬──────┬───" + TerminalColor.RESET;
                    }
                }
                case 1 -> {
                    if (card.points() == 0)
                        return cardBackgroundColor + "              " + TerminalColor.RESET;//14 spaces
                    else {
                        String points;
                        if (card.points() / 10 > 1)
                            points = String.valueOf(card.points());
                        else
                            points = card.points() + " ";

                        if (card.requiresCorner()) {
                            return cardColor + "│  " + points + " |  " + "\uD83D\uDCD0" + "  │" + TerminalColor.RESET;
                        } else if (card.requiredArtifact() != null && !card.requiredArtifact().equals(Artifacts.NULL)) {
                            return cardColor + "│  " + points + " |  " + card.requiredArtifact().getUnicodeIcon() + "  │" + TerminalColor.RESET;
                        } else
                            return cardBackgroundColor + "   " + TerminalColor.RESET + cardColor + "│  " + points + "  │" + TerminalColor.RESET + cardBackgroundColor + "   " + TerminalColor.RESET;
                    }
                }
                case 2 -> {
                    if (card.points() == 0)
                        return cardBackgroundColor + "              " + TerminalColor.RESET;
                    else {
                        if (card.requiresCorner() || !card.requiredArtifact().equals(Artifacts.NULL))
                            return cardColor + "└────────────┘" + TerminalColor.RESET;
                        else
                            return cardBackgroundColor + "   " + TerminalColor.RESET + cardColor + "└──────┘" + TerminalColor.RESET + cardBackgroundColor + "   " + TerminalColor.RESET;
                    }
                }
                case 3 -> {
                    return cardBackgroundColor + "              " + TerminalColor.RESET;
                }
                case 4 -> {
                    if (card.constraint() == null || card.constraint().isEmpty())
                        return cardBackgroundColor + "              " + TerminalColor.RESET;
                    else
                        return cardColor + "┌────────────┐" + TerminalColor.RESET;
                }
                case 5 -> {
                    if (card.constraint() == null || card.constraint().isEmpty())
                        return cardBackgroundColor + "              " + TerminalColor.RESET;

                    List<Artifacts> constraint = card.constraint().entrySet()
                            .stream()
                            .flatMap(entry -> Stream.generate(entry::getKey).limit(entry.getValue()))
                            .toList();

                    switch (constraint.size()) {
                        case 1 -> {
                            return cardColor + "│     " + constraint.get(0).getUnicodeIcon() + "     │" + TerminalColor.RESET;
                        }
                        case 2 -> {
                            return cardColor + "│    " + constraint.get(0).getUnicodeIcon() + constraint.get(1).getUnicodeIcon() + "    │" + TerminalColor.RESET;
                        }
                        case 3 -> {
                            return cardColor + "│   " + constraint.get(0).getUnicodeIcon() + constraint.get(1).getUnicodeIcon() + constraint.get(2).getUnicodeIcon() + "   │" + TerminalColor.RESET;
                        }
                        case 4 -> {
                            return cardColor + "│  " + constraint.get(0).getUnicodeIcon() + constraint.get(1).getUnicodeIcon() + constraint.get(2).getUnicodeIcon() + constraint.get(3).getUnicodeIcon() + "  │" + TerminalColor.RESET;
                        }
                        case 5 -> {
                            return cardColor + "│ " + constraint.get(0).getUnicodeIcon() + constraint.get(1).getUnicodeIcon() + constraint.get(2).getUnicodeIcon() + constraint.get(3).getUnicodeIcon() + constraint.get(4).getUnicodeIcon() + " │" + TerminalColor.RESET;
                        }
                    }
                }
                case 6 -> {
                    if (card.constraint() == null || card.constraint().isEmpty())
                        return cardColor + "──────────────" + TerminalColor.RESET;
                    else
                        return cardColor + "┴────────────┴" + TerminalColor.RESET;
                }
            }
        } else {
            List<Artifacts> centralArtifacts = new ArrayList<>();
            if (card.centralArtifacts() != null) {
                centralArtifacts = card.centralArtifacts().entrySet()
                        .stream()
                        .flatMap(entry -> Stream.generate(entry::getKey).limit(entry.getValue()))
                        .toList();
            }
            switch (row) {
                case 0, 6 -> {
                    return cardColor + "──────────────" + TerminalColor.RESET;
                }
                case 1 -> {
                    if (centralArtifacts.size() == 3 || centralArtifacts.size() == 2)
                        return cardBackgroundColor + "    " + TerminalColor.RESET + cardColor + "┌────┐" + TerminalColor.RESET + cardBackgroundColor + "    " + TerminalColor.RESET;
                    else
                        return cardBackgroundColor + "              " + TerminalColor.RESET;
                }
                case 2 -> {
                    if (centralArtifacts.size() == 2 || centralArtifacts.size() == 3)
                        return cardBackgroundColor + "    " + TerminalColor.RESET + cardColor + "│ " + centralArtifacts.get(0).getUnicodeIcon() + " │" + TerminalColor.RESET + cardBackgroundColor + "    " + TerminalColor.RESET;
                    if (centralArtifacts.size() == 1)
                        return cardBackgroundColor + "    " + TerminalColor.RESET + cardColor + "┌────┐" + TerminalColor.RESET + cardBackgroundColor + "    " + TerminalColor.RESET;
                    else
                        return cardBackgroundColor + "              " + TerminalColor.RESET;
                }
                case 3 -> {
                    if (centralArtifacts.size() == 2 || centralArtifacts.size() == 3)
                        return cardBackgroundColor + "    " + TerminalColor.RESET + cardColor + "│ " + centralArtifacts.get(1).getUnicodeIcon() + " │" + TerminalColor.RESET + cardBackgroundColor + "    " + TerminalColor.RESET;
                    if (centralArtifacts.size() == 1)
                        return cardBackgroundColor + "    " + TerminalColor.RESET + cardColor + "│ " + centralArtifacts.get(0).getUnicodeIcon() + " │" + TerminalColor.RESET + cardBackgroundColor + "    " + TerminalColor.RESET;
                    else
                        return cardBackgroundColor + "              " + TerminalColor.RESET;
                }
                case 4 -> {
                    if (centralArtifacts.size() == 3)
                        return cardBackgroundColor + "    " + TerminalColor.RESET + cardColor + "│ " + centralArtifacts.get(2).getUnicodeIcon() + " │" + TerminalColor.RESET + cardBackgroundColor + "    " + TerminalColor.RESET;
                    if (centralArtifacts.size() == 2 || centralArtifacts.size() == 1)
                        return cardBackgroundColor + "    " + TerminalColor.RESET + cardColor + "└────┘" + TerminalColor.RESET + cardBackgroundColor + "    " + TerminalColor.RESET;
                    else
                        return cardBackgroundColor + "              " + TerminalColor.RESET;
                }
                case 5 -> {
                    if (centralArtifacts.size() == 3)
                        return cardBackgroundColor + "    " + TerminalColor.RESET + cardColor + "└────┘" + TerminalColor.RESET + cardBackgroundColor + "    " + TerminalColor.RESET;
                    else
                        return cardBackgroundColor + "              " + TerminalColor.RESET;
                }
            }
        }
        return "";
    }
}
