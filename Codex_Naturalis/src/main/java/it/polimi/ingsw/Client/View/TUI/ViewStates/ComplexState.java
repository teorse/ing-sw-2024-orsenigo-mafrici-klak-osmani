package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class ComplexState extends InteractiveState{
    private final Map<String, InteractiveComponent> keywordToComponentMap;
    private InteractiveComponent activeComponent;
    boolean commandNotFund;
    boolean attemptToExitMainComponent;

    private final Logger logger;

    public ComplexState(InteractiveComponent mainComponent, List<InteractiveComponent> secondaryComponents) {
        super(mainComponent);
        logger = Logger.getLogger(ComplexState.class.getName());
        logger.info("Initializing ComplexState abstract class");

        keywordToComponentMap = new HashMap<>();
        activeComponent = mainComponent;
        commandNotFund = false;

        for(InteractiveComponent secondaryComponent : secondaryComponents) {
            keywordToComponentMap.put(secondaryComponent.getKeyword(), secondaryComponent);
        }
    }

    public ComplexState(InteractiveComponent mainComponent) {
        super(mainComponent);
        logger = Logger.getLogger(ComplexState.class.getName());
        logger.info("Initializing ComplexState abstract class");

        keywordToComponentMap = new HashMap<>();
        activeComponent = mainComponent;
        commandNotFund = false;
    }

    public InteractiveComponent getActiveComponent() {
        return activeComponent;
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

        if(activeComponent.equals(getMainComponent())){
            return super.handleInput(input);
        }

        InteractiveComponentReturns result = activeComponent.handleInput(input);
        if(result.equals(InteractiveComponentReturns.COMPLETE)){
            if(!activeComponent.equals(getMainComponent())) {
                activeComponent.cleanObserved();
                activeComponent = getMainComponent();
            }
        }
        else if(result == InteractiveComponentReturns.QUIT) {
            if (!activeComponent.equals(getMainComponent())) {
                activeComponent.cleanObserved();
                activeComponent = mainComponent;
            }
        }
        print();
        return true;
    }

    @Override
    public void refreshObservables(){
        super.refreshObservables();

        for(InteractiveComponent component : keywordToComponentMap.values())
            component.refreshObserved();
    }

    @Override
    public void print(){
        logger.info("Printing active component in abstract class ComplexState");

        if(activeComponent == null)
            logger.fine("active component is null");
        else
            logger.fine("active component is not null");

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
