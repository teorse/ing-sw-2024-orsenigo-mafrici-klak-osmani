package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.LobbyUserRecord;
import Client.Model.Records.PlayerRecord;
import Client.View.TextUI;
import Client.View.UserInterface;
import Model.Player.PlayerStates;

import java.util.logging.Logger;

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
        this.myPR = TextUI.usernameToPlayerRecord(model,model.getMyUsername());
        this.inputCounter = 0;
        this.model.setOperationSuccesful(false);
    }


    public abstract void print();

    public abstract void handleInput(String input);

    public abstract void nextState();

}

