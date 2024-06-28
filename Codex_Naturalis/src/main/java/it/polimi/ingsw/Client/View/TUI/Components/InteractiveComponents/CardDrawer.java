package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardPools;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.Network.ClientConnector;
import it.polimi.ingsw.Client.View.TUI.Components.CardPoolView;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPDrawCard;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.util.Map;
import java.util.logging.Logger;

/**
 * The CardDrawer class represents an interactive component in a text-based user interface (TUI)
 * that allows the user to choose and draw cards from either the RESOURCE or GOLDEN card pools.
 * It extends InteractiveComponent and interacts with the client-side model and network connector
 * to handle user inputs, validate choices, and send corresponding requests to the server.
 * The class manages input stages, validates user selections, and displays prompts and messages
 * related to card drawing operations, including error handling for invalid inputs.
 */
public class CardDrawer extends InteractiveComponent{
    //ATTRIBUTES
    private CardPoolTypes cardPoolChoice;
    private CardPools cardPools;
    private int cardChoice;
    private ClientConnector connection;

    private int resourceLB;
    private int resourceUB;
    private int goldenLB;
    private int goldenUB;
    private boolean isResourceOver;
    private boolean isGoldenOver;

    private boolean invalidBinaryChoice;
    private boolean invalidCardIndex;

    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Constructs a CardDrawer object, initializing it with specific attributes
     * and settings related to card pools and drawing mechanics.
     * <p>
     * Initializes the object by calling the superclass constructor with a value of 1.
     * Sets up the connection to the client model's client connector.
     * Retrieves and refreshes the observed card pools.
     * Determines the lower and upper bounds for drawing cards from both RESOURCE and GOLDEN pools,
     * considering the visibility and availability of cards.
     * Checks if either RESOURCE or GOLDEN pools are depleted based on their availability.
     * Initializes flags for tracking invalid binary choices and card index inputs.
     */
    public CardDrawer() {
        super(1);
        logger = Logger.getLogger(CardDrawer.class.getName());
        logger.info("Initializing CardDrawer Component.");

        // Initialize connection to client connector
        this.connection = ClientModel.getInstance().getClientConnector();

        // Refresh observed card pools
        this.cardPools = CardPools.getInstance();
        refreshObserved();

        // Determine lower and upper bounds for RESOURCE pool cards
        resourceLB = 1 + ((cardPools.getCardPoolByType(CardPoolTypes.RESOURCE).coveredCardColor() == Artifacts.NULL) ? 1 : 0);
        resourceUB = 1 + cardPools.getCardPoolByType(CardPoolTypes.RESOURCE).visibleCards().size();

        // Determine lower and upper bounds for GOLDEN pool cards
        goldenLB = 1 + ((cardPools.getCardPoolByType(CardPoolTypes.GOLDEN).coveredCardColor() == Artifacts.NULL) ? 1 : 0);
        goldenUB = 1 + cardPools.getCardPoolByType(CardPoolTypes.GOLDEN).visibleCards().size();

        // Check if RESOURCE pool is depleted
        isResourceOver = resourceLB > resourceUB;

        // Check if GOLDEN pool is depleted
        isGoldenOver = goldenLB > goldenUB;

        // Check drawability of card pools and set flags accordingly
        Map<CardPoolTypes, Boolean> cardDrawability = CardPools.getInstance().getCardPoolDrawability();
        if (!cardDrawability.get(CardPoolTypes.GOLDEN))
            isGoldenOver = true;
        if (!cardDrawability.get(CardPoolTypes.RESOURCE))
            isResourceOver = true;

        // Initialize flags for tracking input validation
        invalidBinaryChoice = false;
        invalidCardIndex = false;
    }





    //METHODS
    /**
     * Handles user input for choosing and drawing cards from either the RESOURCE or GOLDEN pool.
     * Manages different stages of input based on the current state of card pool selection and validates user inputs.
     * Sends appropriate packets to the server based on user choices to draw cards.
     *
     * @param input The user input provided during interaction.
     * @return An {@link InteractiveComponentReturns} indicating the status of the interaction.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        logger.info("Handling in put in CardDrawer");

        // Process input through superclass method
        InteractiveComponentReturns superReturn = super.handleInput(input);
        if (superReturn == InteractiveComponentReturns.QUIT) {
            return superReturn;
        } else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        int inputCounter = getInputCounter();
        if (inputCounter == 0) {
            // Stage 1: Choose between RESOURCE and GOLDEN pool
            if (InputValidator.validBinaryChoice(input)) {
                // Set card pool type based on input
                if (Integer.parseInt(input) == 1) {
                    cardPoolChoice = CardPoolTypes.RESOURCE;
                } else {
                    cardPoolChoice = CardPoolTypes.GOLDEN;
                }
                // Increment input counter to proceed to next stage
                incrementInputCounter();
                return InteractiveComponentReturns.INCOMPLETE;
            } else {
                // Invalid input for binary choice
                invalidBinaryChoice = true;
            }
        } else if (inputCounter == 1) {
            // Stage 2: Choose a specific card from the selected pool
            if (cardPoolChoice == CardPoolTypes.RESOURCE) {
                // Check if the input is within the bounds for RESOURCE cards
                if (InputValidator.checkInputBound(input, resourceLB, resourceUB)) {
                    // Parse the card choice
                    cardChoice = Integer.parseInt(input);
                    // Send packet to draw the chosen RESOURCE card
                    connection.sendPacket(new CSPDrawCard(cardPoolChoice, cardChoice - 2));
                    return InteractiveComponentReturns.COMPLETE;
                } else {
                    // Invalid card index for RESOURCE pool
                    invalidCardIndex = true;
                }
            } else if (cardPoolChoice == CardPoolTypes.GOLDEN) {
                // Check if the input is within the bounds for GOLDEN cards
                if (InputValidator.checkInputBound(input, goldenLB, goldenUB)) {
                    // Parse the card choice
                    cardChoice = Integer.parseInt(input);
                    // Send packet to draw the chosen GOLDEN card
                    connection.sendPacket(new CSPDrawCard(cardPoolChoice, cardChoice - 2));
                    return InteractiveComponentReturns.COMPLETE;
                } else {
                    // Invalid card index for GOLDEN pool
                    invalidCardIndex = true;
                }
            }
        }

        // Default return if the input does not complete the interaction
        return InteractiveComponentReturns.INCOMPLETE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKeyword() {
        return "draw";
    }

    /**
     * Retrieves the description associated with the card drawing action.
     *
     * @return An empty string (no specific description provided).
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * Removes this component from the list of observed components managed by RefreshManager,
     * specifically targeting the cardPools instance.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, cardPools);
    }

    /**
     * Adds this component to the list of observed components managed by RefreshManager,
     * specifically targeting the cardPools instance.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, cardPools);
    }

    /**
     * Prints prompts and messages related to the process of choosing a card from either the RESOURCE or GOLDEN pool.
     * Manages different stages of input based on the current state of card availability in each pool.
     * Handles prompts for pool selection and card index input, and manages error messages for invalid inputs.
     */
    @Override
    public void print() {
        if(getInputCounter() == 0)
            cardPoolChoice = null;

        if(!isResourceOver && !isGoldenOver && cardPoolChoice == null) {
            new CardPoolView(CardPoolTypes.RESOURCE).print();
            new CardPoolView(CardPoolTypes.GOLDEN).print();
        }
        else if(isGoldenOver || cardPoolChoice == CardPoolTypes.RESOURCE) {
            new CardPoolView(CardPoolTypes.RESOURCE).print();
        }
        else {
            new CardPoolView(CardPoolTypes.GOLDEN).print();
        }

        if (getInputCounter() == 0) {
            //Resetting the cardPool choice in case of a back to print again both pools

            // Stage 1: Prompt user to choose from which pool they want to draw a card
            if (!isResourceOver && !isGoldenOver) {
                // Print the available cards in both RESOURCE and GOLDEN pools
                System.out.println("\n" +
                        """
                        Enter from which pool you want to draw:
                         1 - Resource
                         2 - Golden""");
            } else if (isResourceOver) {
                // If resource pool is empty, force choice to GOLDEN pool and reprint
                cardPoolChoice = CardPoolTypes.GOLDEN;
                incrementInputCounter();
            } else {
                // If golden pool is empty, force choice to RESOURCE pool and reprint
                cardPoolChoice = CardPoolTypes.RESOURCE;
                incrementInputCounter();
            }
        }

        if (getInputCounter() == 1 && cardPoolChoice == CardPoolTypes.RESOURCE) {
            if(isGoldenOver)
                System.out.println("\nYou can draw only from the Resource deck");
            // Stage 2: Prompt user to enter a number to pick a card from RESOURCE pool
            System.out.println("\nEnter a number between " + resourceLB + " and " + resourceUB + " to pick a card: ");
        } else if (getInputCounter() == 1 && cardPoolChoice == CardPoolTypes.GOLDEN) {
            if(isResourceOver)
                System.out.println("\nYou can draw only from the Golden deck");
            // Stage 3: Prompt user to enter a number to pick a card from GOLDEN pool
            System.out.println("\nEnter a number between " + goldenLB + " and " + goldenUB + " to pick a card: ");
        }

        // Print error messages for invalid inputs
        if (invalidBinaryChoice) {
            invalidBinaryChoice = false;
            System.out.println("The number provided is not a valid input.\nPlease type 1 or 2.");
        } else if (invalidCardIndex) {
            invalidCardIndex = false;
            System.out.println("The card index provided is not a valid input.\nPlease try again.");
        }

        if(!isResourceOver && !isGoldenOver)
            super.print();
    }
}
