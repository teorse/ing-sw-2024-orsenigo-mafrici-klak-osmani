package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;

public class LobbyChooser extends InteractiveComponent{

    private final ViewState view;
    private InteractiveComponent subComponent;

    private boolean wrongBinaryChoice;

    public LobbyChooser(ViewState view) {
        super(view);
        this.view = view;
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {
        if (inputCounter == 0) {
            if(input.equalsIgnoreCase("BACK"))
                return super.handleInput(input);

            if (InputValidator.validBinaryChoice(input)) {
                if (Integer.parseInt(input) == 1) {
                    subComponent = new LobbyCreator(view);
                }
                else if (Integer.parseInt(input) == 2)
                    subComponent = new LobbyJoiner(view);

                inputCounter++;
            } else
                wrongBinaryChoice = true;

            return InteractiveComponentReturns.INCOMPLETE;
        }
        if(inputCounter == 1){
            InteractiveComponentReturns result = subComponent.handleInput(input);

            if(result.equals(InteractiveComponentReturns.QUIT)) {
                inputCounter--;
                subComponent.cleanObserved();
                return InteractiveComponentReturns.INCOMPLETE;
            }
            else if (result.equals(InteractiveComponentReturns.COMPLETE)) {
                subComponent.cleanObserved();
            }

            return result;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        //todo
        return "chooseLobby";
    }

    @Override
    public void print() {
        if (wrongBinaryChoice) {
            System.out.println("\nWrong binary choice. Please type 1 or 2.");
        }

        if(inputCounter == 0) {
            System.out.println("\n" +
                    """
                            Enter your choice:
                             1 - Create a lobby
                             2 - Join a lobby""");
        }

        else if(inputCounter == 1){
            subComponent.print();
        }
    }

    @Override
    public void cleanObserved() {}
}
