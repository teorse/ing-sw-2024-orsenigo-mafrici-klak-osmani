package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardPools;
import it.polimi.ingsw.Client.View.TUI.Components.CardPoolView;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

public class CardPoolZoom extends InteractiveComponent{
    private CardPoolTypes choice;
    boolean invalidInput;


    public CardPoolZoom(ViewState view) {
        super(view);
        invalidInput = false;

        view.addObserved(CardPools.getInstance());
    }

    //METHODS
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (InputValidator.validBinaryChoice(input)) {
            if (Integer.parseInt(input) == 1)
                choice = CardPoolTypes.RESOURCE;
            else
                choice = CardPoolTypes.GOLDEN;
            inputCounter++;
        }
        else
            invalidInput = true;

        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "CardPool";
    }

    @Override
    public void cleanObserved() {
        view.removeObserved(CardPools.getInstance());
    }

    @Override
    public void print() {
        if(inputCounter == 0) {

            if(invalidInput)
                System.out.println("Invalid input. Please enter 1 or 2 to select the pool type!");

            System.out.println("\n" + """
                    Select the pool type to zoom:
                     1 - Resource Pool
                     2 - Golden Pool""");
        }
        else if(inputCounter == 1){
            if(choice == CardPoolTypes.RESOURCE)
                new CardPoolView(view, CardPoolTypes.RESOURCE).print();
            else
                new CardPoolView(view, CardPoolTypes.GOLDEN).print();
        }
    }
}
