package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardPools;
import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Network.ClientConnector;
import it.polimi.ingsw.Client.View.TUI.Components.CardPoolView;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPDrawCard;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

import java.util.Map;

//todo review code
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





    //CONSTRUCTOR
    public CardDrawer(ViewState view) {
        super(view);

        this.connection = ClientModel2.getInstance().getClientConnector();
        this.cardPools = CardPools.getInstance();
        view.addObserved(cardPools);

        resourceLB = 1 + ((cardPools.getCardPoolByType(CardPoolTypes.RESOURCE).coveredCardColor() == Artifacts.NULL) ? 1 : 0);
        resourceUB = 1 + cardPools.getCardPoolByType(CardPoolTypes.RESOURCE).visibleCards().size();
        goldenLB = 1 + ((cardPools.getCardPoolByType(CardPoolTypes.GOLDEN).coveredCardColor() == Artifacts.NULL) ? 1 : 0);
        goldenUB = 1 + cardPools.getCardPoolByType(CardPoolTypes.GOLDEN).visibleCards().size();

        isResourceOver = resourceLB > resourceUB;
        isGoldenOver = goldenLB > goldenUB;

        Map<CardPoolTypes, Boolean> cardDrawability = CardPools.getInstance().getCardPoolDrawability();
        if(!cardDrawability.get(CardPoolTypes.GOLDEN))
            isGoldenOver = true;
        if(!cardDrawability.get(CardPoolTypes.RESOURCE))
            isResourceOver = true;

        invalidBinaryChoice = false;
        invalidCardIndex = false;
    }





    //METHODS
    /**
     * Handles user input for drawing cards from specified card pools.
     *
     * @param input the user input to be processed
     * @return an InteractiveComponentReturns enum indicating whether the input processing is complete or incomplete
     * <p>
     * The method performs the following:
     * - If the input is "BACK", it delegates handling to the superclasses handleInput method.
     * - If the inputCounter is 0, it checks if the input is a valid binary choice to select the card pool type (RESOURCE or GOLDEN).
     * - If the card pool type is RESOURCE and the inputCounter is 1, it checks if the input is within bounds for RESOURCE cards,
     *   parses the card choice, and sends a packet to draw the chosen RESOURCE card.
     * - If the card pool type is GOLDEN and the inputCounter is 1, it checks if the input is within bounds for GOLDEN cards,
     *   parses the card choice, and sends a packet to draw the chosen GOLDEN card.
     * - Returns an InteractiveComponentReturns enum value indicating the state of input processing.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (inputCounter == 0) {
            // Check if the input is a binary choice
            if (InputValidator.validBinaryChoice(input)) {
                // Set card pool type based on input
                if (Integer.parseInt(input) == 1) {
                    cardPoolChoice = CardPoolTypes.RESOURCE;
                } else {
                    cardPoolChoice = CardPoolTypes.GOLDEN;
                }
                // Increment input counter
                inputCounter++;

                return InteractiveComponentReturns.INCOMPLETE;
            }
            else
                invalidBinaryChoice = true;

            // If input counter is 1 and card pool type is RESOURCE
        } else if (inputCounter == 1 && cardPoolChoice == CardPoolTypes.RESOURCE) {
            // Check if the input is within the bounds for RESOURCE cards
            if (InputValidator.checkInputBound(input, resourceLB, resourceUB)) {
                // Parse the card choice
                cardChoice = Integer.parseInt(input);
                // Send packet to draw the chosen RESOURCE card
                connection.sendPacket(new CSPDrawCard(cardPoolChoice, cardChoice - 2));

                return InteractiveComponentReturns.COMPLETE;
            }
            else
                invalidCardIndex = true;

            // If input counter is 1 and card pool type is GOLDEN
        } else if (inputCounter == 1 && cardPoolChoice == CardPoolTypes.GOLDEN) {
            // Check if the input is within the bounds for GOLDEN cards
            if (InputValidator.checkInputBound(input, goldenLB, goldenUB)) {
                // Parse the card choice
                cardChoice = Integer.parseInt(input);
                // Send packet to draw the chosen GOLDEN card
                connection.sendPacket(new CSPDrawCard(cardPoolChoice, cardChoice - 2));

                return InteractiveComponentReturns.COMPLETE;
            }
            else
                invalidCardIndex = true;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "draw";
    }

    @Override
    public void cleanObserved() {
        view.removeObserved(cardPools);
    }

    /**
     * Prints the current state of the card drawing interface, prompting the user to select from different card pools
     * or to draw a card from the selected pool. Handles different input states to guide the user through the process.
     */
    @Override
    public void print() {
        if (inputCounter == 0) {
            // Print the available cards in both RESOURCE and GOLDEN pools
            new CardPoolView(view, CardPoolTypes.RESOURCE).print();
            new CardPoolView(view, CardPoolTypes.GOLDEN).print();

            // Prompt user to choose from which pool they want to draw a card
            if (!isResourceOver && !isGoldenOver) {
                System.out.println("\n" +
                        """
                        Enter from which pool you want to draw:
                         1 - Resource
                         2 - Golden""");
            } else if (isResourceOver) {
                // If resource pool is empty, force choice to GOLDEN pool and reprint
                System.out.println("\nYou can draw only from the Golden deck");
                cardPoolChoice = CardPoolTypes.GOLDEN;
                inputCounter++;
                print();
            } else {
                // If golden pool is empty, force choice to RESOURCE pool and reprint
                System.out.println("\nYou can draw only from the Resource deck");
                cardPoolChoice = CardPoolTypes.RESOURCE;
                inputCounter++;
                print();
            }
        } else if (inputCounter == 1 && cardPoolChoice == CardPoolTypes.RESOURCE) {
            // Prompt user to enter a number to pick a card from RESOURCE pool
            System.out.println("\nEnter a number between " + resourceLB + " and " + resourceUB + " to pick a card: ");
        } else if (inputCounter == 1 && cardPoolChoice == CardPoolTypes.GOLDEN) {
            // Prompt user to enter a number to pick a card from GOLDEN pool
            System.out.println("\nEnter a number between " + goldenLB + " and " + goldenUB + " to pick a card: ");
        }

        if(invalidBinaryChoice){
            invalidBinaryChoice = false;
            System.out.println("The number provided is not a valid input.\nPlease type 1 or 2.");
        }
        else if (invalidCardIndex) {
            invalidCardIndex = false;
            System.out.println("The card index provided is not a valid input.\nPlease try again.");
        }
    }
}
