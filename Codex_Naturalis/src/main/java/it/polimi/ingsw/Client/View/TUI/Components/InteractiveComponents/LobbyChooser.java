package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.TextUI;

public class LobbyChooser extends InteractiveComponent{

    private InteractiveComponent subComponent;

    public LobbyChooser() {

    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {
        if (inputCounter == 0) {
            if(input.equalsIgnoreCase("BACK"))
                return super.handleInput(input);

            if (TextUI.validBinaryChoice(input)) {
                if (Integer.parseInt(input) == 1) {
                    subComponent = new LobbyCreator();
                }
                else if (Integer.parseInt(input) == 2)
                    subComponent = new LobbyJoiner();

                inputCounter++;
            }
            return InteractiveComponentReturns.INCOMPLETE;
        }
        if(inputCounter == 1){
            InteractiveComponentReturns result = subComponent.handleInput(input);

            if(result.equals(InteractiveComponentReturns.QUIT)) {
                inputCounter--;
                return InteractiveComponentReturns.INCOMPLETE;
            }

            return result;
        }
        //todo add logger to check if input counter behaves strangely
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        //todo
        return "chooseLobby";
    }

    @Override
    public void print() {
        System.out.println("\n" +
                """
                Enter your choice:
                 1 - Create a lobby
                 2 - Join a lobby""");
    }

    @Override
    public void cleanUp() {

    }
}
