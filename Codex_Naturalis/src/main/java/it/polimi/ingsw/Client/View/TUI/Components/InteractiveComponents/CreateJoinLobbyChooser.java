package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.TextUI;

public class CreateJoinLobbyChooser extends InteractiveComponent{

    private InteractiveComponent subComponent;

    @Override
    public boolean handleInput(String input) {
        if (inputCounter == 0) {
            if (TextUI.validBinaryChoice(input)) {
                if (Integer.parseInt(input) == 1) {
                    subComponent = new LobbyCreator();
                }
                else if (Integer.parseInt(input) == 2)
                    subComponent = new LobbyChooser();

                inputCounter++;
            }
            return false;
        }
        if(inputCounter == 1){
            boolean result = subComponent.handleInput(input);

            if(result)
                inputCounter = 0;

            return result;
        }
        //todo add logger to check if input counter behaves strangely
        return false;
    }

    @Override
    public String getKeyword() {
        //todo
        return "temp";
    }

    @Override
    public void print() {
        System.out.println("\n" +
                """
                Enter your choice:
                 1 - Create a lobby
                 2 - Join a lobby""");
    }
}
