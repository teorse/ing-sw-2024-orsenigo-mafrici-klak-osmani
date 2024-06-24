package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents a complex state that can switch between different interactive components based on user input.
 */
public abstract class ComplexState extends InteractiveState {
    private final Map<String, InteractiveComponent> keywordToComponentMap;
    private InteractiveComponent activeComponent;
    boolean printHelp;
    boolean commandNotFound;

    private final Logger logger;

    /**
     * Constructs a ComplexState with a main interactive component and a list of secondary components.
     * @param mainComponent the main interactive component
     * @param secondaryComponents a list of secondary interactive components
     */
    public ComplexState(InteractiveComponent mainComponent, List<InteractiveComponent> secondaryComponents) {
        super(mainComponent);
        logger = Logger.getLogger(ComplexState.class.getName());
        logger.info("Initializing ComplexState abstract class");

        keywordToComponentMap = new HashMap<>();
        activeComponent = mainComponent;
        printHelp = false;
        commandNotFound = false;

        // Populate the keyword to component map
        for (InteractiveComponent secondaryComponent : secondaryComponents) {
            keywordToComponentMap.put(secondaryComponent.getKeyword().toLowerCase(), secondaryComponent);
        }
    }

    /**
     * Constructs a ComplexState with only a main interactive component.
     * @param mainComponent the main interactive component
     */
    public ComplexState(InteractiveComponent mainComponent) {
        super(mainComponent);
        logger = Logger.getLogger(ComplexState.class.getName());
        logger.info("Initializing ComplexState abstract class");

        keywordToComponentMap = new HashMap<>();
        activeComponent = mainComponent;
        commandNotFound = false;
    }

    /**
     * Returns the currently active interactive component.
     * @return the active interactive component
     */
    public InteractiveComponent getActiveComponent() {
        return activeComponent;
    }

    /**
     * Handles user input, switching between components based on commands.
     * @param input the user input
     * @return true if the input was handled
     */
    @Override
    public boolean handleInput(String input) {
        logger.info("Handling input in Client State with main component: " + getMainComponent().getClass().getSimpleName());
        attemptToExitMainComponent = false;
        commandNotFound = false;
        printHelp = false;

        String keyword = null;
        if ((input != null && !input.isEmpty()) && input.charAt(0) == '/') {
            if (keywordToComponentMap.isEmpty() && !input.equalsIgnoreCase("/back")) {
                return false;
            }

            // Removes char '/' and splits the string based on spaces
            String[] parts = input.substring(1).toLowerCase().split(" ");
            // Picks the first word after '/' character
            keyword = parts[0].toLowerCase();
        }

        if (keyword != null && !keyword.equalsIgnoreCase("back")) {
            if (keyword.equalsIgnoreCase("h") || keyword.equalsIgnoreCase("help")) {
                printHelp = true;
            } else if (!keywordToComponentMap.containsKey(keyword)) {
                commandNotFound = true;
            } else {
                activeComponent = keywordToComponentMap.get(keyword);
            }
            ClientModel.getInstance().printView();
            return true;
        }

        if (activeComponent.equals(getMainComponent())) {
            return super.handleInput(input);
        }

        InteractiveComponentReturns result = activeComponent.handleInput(input);
        if (result.equals(InteractiveComponentReturns.COMPLETE)) {
            if (!activeComponent.equals(getMainComponent())) {
                activeComponent.cleanObserved();
                activeComponent = getMainComponent();
            }
        } else if (result == InteractiveComponentReturns.QUIT) {
            if (!activeComponent.equals(getMainComponent())) {
                activeComponent.cleanObserved();
                activeComponent = getMainComponent();
            }
        }
        ClientModel.getInstance().printView();
        return true;
    }

    /**
     * Refreshes the observables for all components.
     */
    @Override
    public void refreshObservables() {
        super.refreshObservables();

        for (InteractiveComponent component : keywordToComponentMap.values()) {
            component.refreshObserved();
        }
    }

    /**
     * Prints the current state, including help messages if requested.
     */
    @Override
    public void print() {
        synchronized (printLock) {
            logger.info("Printing active component in abstract class ComplexState");

            if (activeComponent == null) {
                logger.fine("active component is null");
            } else {
                logger.fine("active component is not null");
            }

            if (!keywordToComponentMap.isEmpty()) {
                System.out.println("\nTo display the available commands type /help or /h");
            }

            super.print();

            if (commandNotFound) {
                System.out.println("\nCommand not found");
            } else if (printHelp) {
                if (!keywordToComponentMap.isEmpty()) {
                    System.out.println("\nThese are the available commands:");
                    for (String keyword : keywordToComponentMap.keySet()) {
                        System.out.println(keywordToComponentMap.get(keyword).getDescription());
                    }
                    if (ClientModel.getInstance().isInLobby()) {
                        System.out.println("/chat -> to enter the chat state");
                        System.out.println("/quitlobby -> to quit the lobby");
                    }
                } else {
                    System.out.println("\nThere are no commands available!");
                }
            }
        }
    }
}