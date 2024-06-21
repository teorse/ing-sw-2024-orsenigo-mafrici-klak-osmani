package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardPools;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.CardPoolView;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

public class CardPoolZoom extends InteractiveComponent{
    private CardPoolTypes choice;
    boolean invalidInput;


    public CardPoolZoom() {
        super(1);
        invalidInput = false;

        refreshObserved();
    }

    //METHODS
    public InteractiveComponentReturns handleInput(String input) {

        InteractiveComponentReturns superReturn = super.handleInput(input);

        if(getInputCounter() == 0) {
            if (superReturn == InteractiveComponentReturns.QUIT)
                return superReturn;
            else if (superReturn == InteractiveComponentReturns.COMPLETE) {
                return InteractiveComponentReturns.INCOMPLETE;
            }

            if (InputValidator.validBinaryChoice(input)) {
                if (Integer.parseInt(input) == 1)
                    choice = CardPoolTypes.RESOURCE;
                else
                    choice = CardPoolTypes.GOLDEN;
                incrementInputCounter();
            } else
                invalidInput = true;
        }

        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "cardpoolzoom";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, CardPools.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardPools.getInstance());
    }

    @Override
    public void print() {
        int inputCounter = getInputCounter();

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
                new CardPoolView(CardPoolTypes.RESOURCE).print();
            else
                new CardPoolView(CardPoolTypes.GOLDEN).print();
        }

        super.print();
    }
}
