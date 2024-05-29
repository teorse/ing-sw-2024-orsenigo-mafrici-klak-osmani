package it.polimi.ingsw.Client.Model.States;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Client.View.UserInterface;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPDrawCard;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPQuitLobby;

import java.util.Map;

/**
 * Represents the state where the player chooses to draw a card from the resource or golden pool during the game.
 * Upon instantiation, the command-line interface is cleared, and the initial draw state of the game is printed.
 * Players are prompted to choose from which pool they want to draw a card.
 * Once the pool selection is made, they are prompted to select a card from the chosen pool.
 * The chosen card is then drawn from the selected pool.
 */
public class GameDrawState extends ClientState{
    //TODO fixare la stampa ripetuta di "Enter a number between 1 and 3 to pick a card:" quando crashano persone e nella schermata di game over

    CardPoolTypes cardPoolTypes;
    int cardChoice;
    //Bound to draw for a specific deck
    int resourceLB = 1 + ((model.getTableRecord().topDeckResource() == Artifacts.NULL) ? 1 : 0);
    int resourceUB = 1 + model.getTableRecord().visibleCardRecordResource().size();
    int goldenLB = 1 + ((model.getTableRecord().topDeckGolden() == Artifacts.NULL) ? 1 : 0);
    int goldenUB = 1 + model.getTableRecord().visibleCardRecordResource().size();
    boolean isResourceOver = resourceLB > resourceUB;
    boolean isGoldenOver = goldenLB > goldenUB;


    /**
     * Constructs a new GameDrawState with the specified client model.
     * <p>
     * Upon instantiation, this constructor clears the command-line interface and prints the draw state of the game.
     *
     * @param model the client model
     */
    public GameDrawState(ClientModel model) {
        super(model);
        Map<CardPoolTypes, Boolean> cardDrawability = model.getCardPoolDrawability();
        if(!cardDrawability.get(CardPoolTypes.GOLDEN))
            isGoldenOver = true;
        if(!cardDrawability.get(CardPoolTypes.RESOURCE))
            isResourceOver = true;

        print();
    }

    /**
     * Prints the options for selecting a card from the resource or golden pool.
     * <p>
     * This method displays the available card pools for drawing, including the resource pool and the golden pool.
     * It prompts the user to enter a number to choose from which pool they want to draw a card.
     * If the input counter is 0, it prints the options for selecting a pool (Resource or Golden).
     * If the input counter is 1, it prints nothing, indicating that the user has made their pool selection.
     */
    @Override
    public void print() {
        if (inputCounter == 0) {
            TextUI.clearCMD();
            if (!model.getGameRecord().lastRoundFlag())
                TextUI.displayGameTitle();
            else
                TextUI.displayLastRound();

            System.out.println("\nIf you want to go back at the previous choice, type BACK. If you want to quit the lobby, type QUIT.");
            System.out.print("If you want to see the Chat State, type CHAT.");
            if (model.isNewMessage())
                System.out.println(" (NEW MESSAGE)");
            else
                System.out.println();

            textUI.zoomCardPool(CardPoolTypes.RESOURCE);
            textUI.zoomCardPool(CardPoolTypes.GOLDEN);
            if (!isResourceOver && !isGoldenOver) {
                System.out.println("\n" +
                        """
                        Enter from which pool you want to draw:
                         1 - Resource
                         2 - Golden""");
            } else if (isResourceOver) {
                System.out.println("\nYou can draw only from the Golden deck");
                cardPoolTypes = CardPoolTypes.GOLDEN;
                inputCounter++;
                print();
            } else {
                System.out.println("\nYou can draw only from the Resource deck");
                cardPoolTypes = CardPoolTypes.RESOURCE;
                inputCounter++;
                print();
            }
        }
        else if (inputCounter == 1 && cardPoolTypes == CardPoolTypes.RESOURCE) {
            System.out.println("\nEnter a number between " + resourceLB + " and " + resourceUB + " to pick a card: ");
        }
        else if (inputCounter == 1 && cardPoolTypes == CardPoolTypes.GOLDEN) {
            System.out.println("\nEnter a number between " + goldenLB + " and " + goldenUB + " to pick a card: ");
        }
    }

    /**
     * Handles user input to draw a card from the specified card pool.
     *
     * <p>This method processes input based on the current step of the input sequence:
     * <ul>
     *   <li>Step 0: Checks if the input corresponds to a binary choice (1 or 2) to select the card pool type (resource or golden).
     *       If valid, advances to the next step and stores the chosen card pool type.</li>
     *   <li>Step 1: Checks if the input corresponds to a valid card choice index within the specified card pool type.
     *       If valid, advances to the next step and stores the chosen card index.</li>
     * </ul>
     * Once both steps are completed, the method sends a packet to the client connector to draw the selected card from the specified card pool.
     *
     * @param input The user input to be processed.
     */
    @Override
    public void handleInput(String input) {
        // Check if the input is "BACK"
        if(input.equalsIgnoreCase("BACK")) {
            // Reset input counter to 0
            inputCounter = 0;
            // Print the current state
            print();
        }
        else if (input.equalsIgnoreCase("CHAT")) {
            model.setClientState(new ChatState(model,this));
        }
        else if(input.equalsIgnoreCase("QUIT")) {
            model.quitLobby();
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            model.setClientState(new LobbySelectionState(model));
        }
        else if (inputCounter == 0) {
            // Check if the input is a binary choice
            if (UserInterface.validBinaryChoice(input)) {
                // Set card pool type based on input
                if (Integer.parseInt(input) == 1) {
                    cardPoolTypes = CardPoolTypes.RESOURCE;
                } else {
                    cardPoolTypes = CardPoolTypes.GOLDEN;
                }
                // Increment input counter
                inputCounter++;
            }
            // Print the current state
            print();

            // If input counter is 1 and card pool type is RESOURCE
        } else if (inputCounter == 1 && cardPoolTypes == CardPoolTypes.RESOURCE) {
            // Check if the input is within the bounds for RESOURCE cards
            if (UserInterface.checkInputBound(input, resourceLB, resourceUB)) {
                // Parse the card choice
                cardChoice = Integer.parseInt(input);
                // Send packet to draw the chosen RESOURCE card
                model.getClientConnector().sendPacket(new CSPDrawCard(cardPoolTypes, cardChoice - 2));
            } else {
                // Print the current state
                print();
            }

            // If input counter is 1 and card pool type is GOLDEN
        } else if (inputCounter == 1 && cardPoolTypes == CardPoolTypes.GOLDEN) {
            // Check if the input is within the bounds for GOLDEN cards
            if (UserInterface.checkInputBound(input, goldenLB, goldenUB)) {
                // Parse the card choice
                cardChoice = Integer.parseInt(input);
                // Send packet to draw the chosen GOLDEN card
                model.getClientConnector().sendPacket(new CSPDrawCard(cardPoolTypes, cardChoice - 2));
            } else {
                // Print the current state
                print();
            }
        }
    }


    /**
     * Moves the client to the next state based on the success of the previous operation and the player's current state.
     * If the previous operation was successful and the player is not the last online player in the lobby,
     * and the player's state is set to "WAIT", transitions to the GameWaitState to indicate waiting for other players' actions.
     * If the player is the last online player, transitions to the GameOverState to handle the end of the game.
     * If the operation fails, prints an error message and remains in the current state, prompting the user to try again.
     */
    @Override
    public void nextState() {
        if(model.isGameOver()){
            model.setClientState(new GameOverState(model));
        }
        else if (model.getMyPlayerGameState() == PlayerStates.WAIT)
            model.setClientState(new GameWaitState(model));
        else
            print();
    }
}

