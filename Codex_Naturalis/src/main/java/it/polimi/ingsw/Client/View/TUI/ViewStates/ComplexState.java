package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;

import java.util.HashMap;
import java.util.Map;

public abstract class ComplexState extends ViewState {
    Map<String, InteractiveComponent> keywordToComponentMap;
    InteractiveComponent mainComponent;
    InteractiveComponent activeComponent;
    boolean commandNotFund;
    boolean attemptToExitMainComponent;

    public ComplexState(ClientModel model) {
        super(model);
        keywordToComponentMap = new HashMap<>();
        activeComponent = mainComponent;
        commandNotFund = false;
        attemptToExitMainComponent = false;
    }
    void addSecondaryComponent(InteractiveComponent component){
        keywordToComponentMap.put(component.getKeyword(), component);
    }

    @Override
    public boolean handleInput(String input) {
        if (input.charAt(0) == '/') {
            if(keywordToComponentMap.isEmpty())
                return false;

            String keyword;
            //Removes char '/' and cuts the string based on spaces
            String[] parts = input.substring(1).split(" ");
            //Picks the first word after '/' character
            keyword = parts[0];

            if (!keywordToComponentMap.containsKey(keyword)) {
                commandNotFund = true;
            } else {
                activeComponent = keywordToComponentMap.get(keyword);
            }
            print();
            return true;
        }

        InteractiveComponentReturns result = activeComponent.handleInput(input);
        if(result.equals(InteractiveComponentReturns.COMPLETE)){
            if(!activeComponent.equals(mainComponent)) {
                activeComponent.cleanObserved();
                activeComponent = mainComponent;
            }
        }
        else if(result == InteractiveComponentReturns.QUIT) {
            if (!activeComponent.equals(mainComponent)) {
                activeComponent.cleanObserved();
                activeComponent = mainComponent;
            } else
                attemptToExitMainComponent = true;
        }
        print();
        return true;
    }

    @Override
    public void print(){
        activeComponent.print();

        if(attemptToExitMainComponent) {
            attemptToExitMainComponent = false;
            System.out.println("You can't exit from the main component.");
        }
        else if (commandNotFund) {
            commandNotFund = false;
            System.out.println("Command not found");
        }
    }
}
