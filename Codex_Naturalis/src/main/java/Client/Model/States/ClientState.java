package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.LobbyUserRecord;
import Client.Model.Records.PlayerRecord;
import Client.View.TextUI;
import Client.View.UserInterface;
import Model.Player.PlayerStates;

/**
 * Abstract class representing the state of the client in the game.
 * <p>
 * This class serves as a blueprint for different states that the client can be in during the game.
 * It contains methods to print information, handle user input, and transition to the next state.
 * Each concrete subclass of ClientState represents a specific state in the game, such as login, lobby, or gameplay.
 * The class also provides a method to determine the next game state based on the current game conditions.
 */
public abstract class ClientState {

    protected ClientModel model;
    protected TextUI textUI;
    protected PlayerRecord myPR;
    protected LobbyUserRecord myLUR;
    protected int inputCounter;

    /**
     * Constructs a new ClientState with the specified client model.
     * <p>
     * Initializes the ClientState with the provided client model, initializes the TextUI for interaction,
     * retrieves the player record of the current user, retrieves the lobby user record of the current user,
     * sets the input counter to 0, and initializes the operation success flag to false in the client model.
     *
     * @param model the client model
     */
    public ClientState(ClientModel model) {
        this.model = model;
        this.textUI = new TextUI(model);
        this.myPR = TextUI.myPlayerRecord(model);
        this.myLUR = TextUI.myLobbyUserRecord(model);
        this.inputCounter = 0;
        this.model.setOperationSuccesful(false);
    }


    public abstract void print();

    public abstract void handleInput(String input);

    public abstract void nextState();

    /**
     * Determines the next state of the client based on the current game state and operation success.
     * <p>
     * If the previous operation was successful and the game is ongoing:
     * - If the player is not the last online player and their state is "WAIT", transitions to the GameWaitState.
     * - If the player's state is "PLACE", transitions to the GamePlaceState to allow the player to place a card.
     * If the game is over (all players finished), transitions to the GameOverState.
     * If the operation fails, prints an error message and remains in the current state, prompting the user to try again.
     */
    //Used by GamePickObjective and GameWaitState to move from a wait state
    void nextGameState() {
        //TODO il server manda un pacchetto quando finisce il game
        if (model.isOperationSuccesful()) {
            if (!UserInterface.isLastOnlinePlayer(model)) {
                myPR = UserInterface.myPlayerRecord(model);
                if (myPR.playerState() == PlayerStates.WAIT) {
                    model.setClientState(new GameWaitState(model));
                } else if (myPR.playerState() == PlayerStates.PLACE) {
                    model.setClientState(new GamePlaceState(model));
                }
            } else
                model.setClientState(new GameOverState(model));
        } else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}

