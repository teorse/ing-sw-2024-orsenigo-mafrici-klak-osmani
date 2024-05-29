package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.Components.CardPoolView;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

public class CardPoolZoom extends InteractiveComponent{
    //METHODS
    public boolean handleInput(String input) {
        if (TextUI.validBinaryChoice(input)) {
            if (Integer.parseInt(input) == 1)
                new CardPoolView(CardPoolTypes.RESOURCE).print();
            else
                new CardPoolView(CardPoolTypes.GOLDEN).print();
        }
        else
            System.out.println("Invalid input. Please enter 1 or 2 to select the pool type!");

        return false;
    }

    @Override
    public String getKeyword() {
        return "CardPool";
    }

    @Override
    public void print() {
        System.out.println("\n" + """
                            Select the pool type to zoom:
                             1 - Resource Pool
                             2 - Golden Pool""");
    }
}
