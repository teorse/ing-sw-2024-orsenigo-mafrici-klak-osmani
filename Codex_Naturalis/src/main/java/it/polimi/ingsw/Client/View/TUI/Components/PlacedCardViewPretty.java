package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardVisibilityRecord;
import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerDirection;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerOrientation;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;

public class PlacedCardViewPretty extends CardViewPretty{
    private final CardVisibilityRecord cardVisibility;

    public PlacedCardViewPretty(CardVisibilityRecord card) {
        super(card.card(), true, true);
        this.cardVisibility = card;
    }


    //true = right, false = left
    @Override
    protected String renderSide(boolean leftSide, int row, boolean faceUp) {
        Corner corner;
        if (leftSide) {
            switch (row) {
                case 0 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.NW)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.NW, faceUp));
                        if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardColor + "┌────┬──" + TerminalColor.RESET;
                        else
                            return cardColor + "┌───────" + TerminalColor.RESET;
                    }
                    else{
                        return cardColor + "     ┌──" + TerminalColor.RESET;
                    }
                }
                case 1 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.NW)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.NW, faceUp));
                        if (corner.getCornerType().equals(CornerType.EMPTY))
                            return cardColor + "│" + TerminalColor.RESET + "    " + cardColor + "│" + TerminalColor.RESET + cardBackgroundColor + "  " + TerminalColor.RESET;
                        else if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardColor + "│" + TerminalColor.RESET + " " + corner.getArtifact().getUnicodeIcon() + " " + cardColor + "│" + cardBackgroundColor + "  " + TerminalColor.RESET;
                        else
                            return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                    } else {
                        return "     " + cardColor + "│" + TerminalColor.RESET + cardBackgroundColor + "  " + TerminalColor.RESET;
                    }
                }
                case 2 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.NW)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.NW, faceUp));
                        if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardColor + "├────┘" + cardBackgroundColor + "  " + TerminalColor.RESET;
                        else
                            return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                    } else {
                        return cardColor + "┌────┘" + cardBackgroundColor + "  " + TerminalColor.RESET;
                    }
                }
                case 3 -> {
                    return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                }
                case 4 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.SW)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.SW, faceUp));
                        if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardColor + "├────┐" + cardBackgroundColor + "  " + TerminalColor.RESET;
                        else
                            return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                    } else {
                        return cardColor + "└────┐"+ cardBackgroundColor + "  " + TerminalColor.RESET;
                    }
                }
                case 5 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.SW)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.SW, faceUp));
                        if (corner.getCornerType().equals(CornerType.EMPTY))
                            return cardColor + "│    │" + cardBackgroundColor + "  " + TerminalColor.RESET;
                        else if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardColor + "│ " + corner.getArtifact().getUnicodeIcon() + " │" + cardBackgroundColor + "  " + TerminalColor.RESET;
                        else
                            return cardColor + "│" + cardBackgroundColor + "       " + TerminalColor.RESET;//7 spaces
                    } else {
                        return "     " + cardColor + "│" + TerminalColor.RESET + cardBackgroundColor + "  " + TerminalColor.RESET;
                    }
                }
                case 6 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.SW)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.SW, faceUp));
                        if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardColor + "└────┴──" + TerminalColor.RESET;
                        else
                            return cardColor + "└───────" + TerminalColor.RESET;
                    } else {
                        return cardColor + "     └──" + TerminalColor.RESET;
                    }
                }
            }
        } else {
            switch (row) {
                case 0 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.NE)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.NE, faceUp));
                        if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardColor + "──┬────┐" + TerminalColor.RESET;
                        else
                            return cardColor + "───────┐" + TerminalColor.RESET;
                    } else {
                        return cardColor + "──┐     " + TerminalColor.RESET;
                    }
                }
                case 1 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.NE)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.NE, faceUp));
                        if (corner.getCornerType().equals(CornerType.EMPTY))
                            return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│    │" + TerminalColor.RESET;
                        else if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│ " + corner.getArtifact().getUnicodeIcon() + " │" + TerminalColor.RESET;
                        else
                            return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                    } else {
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│     " + TerminalColor.RESET;
                    }
                }
                case 2 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.NE)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.NE, faceUp));
                        if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "└────┤" + TerminalColor.RESET;
                        else
                            return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                    } else {
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "└────┐" + TerminalColor.RESET;
                    }
                }
                case 3 -> {
                    return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                }
                case 4 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.SE)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.SE, faceUp));
                        if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "┌────┤" + TerminalColor.RESET;
                        else
                            return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                    } else {
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "┌────┘" + TerminalColor.RESET;
                    }
                }
                case 5 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.SE)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.SE, faceUp));
                        if (corner.getCornerType().equals(CornerType.EMPTY))
                            return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│    │" + TerminalColor.RESET;
                        else if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│ " + corner.getArtifact().getUnicodeIcon() + " │" + TerminalColor.RESET;
                        else
                            return cardBackgroundColor + "       " + TerminalColor.RESET + cardColor + "│" + TerminalColor.RESET;//7 spaces
                    } else {
                        return cardBackgroundColor + "  " + TerminalColor.RESET + cardColor + "│     " + TerminalColor.RESET;
                    }
                }
                case 6 -> {
                    if (cardVisibility.cornerVisibility().get(CornerDirection.SE)) {
                        corner = card.corners().get(new CornerOrientation(CornerDirection.SE, faceUp));
                        if (!corner.getCornerType().equals(CornerType.NULL))
                            return cardColor + "──┴────┘" + TerminalColor.RESET;
                        else
                            return cardColor + "───────┘" + TerminalColor.RESET;
                    } else {
                        return cardColor + "──┘     " + TerminalColor.RESET;
                    }
                }
            }
        }
        return "";
    }

    @Override
    public void print() {
        StringBuilder cardToPrint = new StringBuilder();

        System.out.println();
        for(int i = 0; i < 7; i++){
            cardToPrint.append(renderSide(true, i, cardVisibility.faceUp())).append(renderCenter(i, cardVisibility.faceUp())).append(renderSide(false, i, cardVisibility.faceUp()))
                    .append("\n");
        }

        System.out.println(cardToPrint);
    }
}
