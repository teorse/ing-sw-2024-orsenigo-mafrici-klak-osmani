package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.TextUI;

/**
 * Abstract class representing the state of the client in the game.
 * <p>
 * This class serves as a blueprint for different states that the client can be in during the game.
 * It contains methods to print information, handle user input, and transition to the next state.
 * Each concrete subclass of ViewState represents a specific state in the game, such as login, lobby, or gameplay.
 * The class also provides a method to determine the next game state based on the current game conditions.
 */
public abstract class ViewState {
    //TODO fix the QUIT command!

    public abstract void print();

    public abstract void handleInput(String input);

    public abstract void nextState();

}

