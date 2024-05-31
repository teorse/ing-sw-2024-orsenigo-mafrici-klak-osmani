package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LogInSignUp;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

public class LoginSignUpState extends ViewState {

    InteractiveComponent mainComponent;
    ErrorsDictionary logInError = null;
    ErrorsDictionary signUpError = null;

    public LoginSignUpState(ClientModel2 model) {
        super(model);
        mainComponent = new LogInSignUp();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        mainComponent.print();

        switch (logInError) {
            case GENERIC_ERROR -> System.out.println("Generic error. Try again!");
            case YOU_ARE_ALREADY_LOGGED_IN -> System.out.println("You are already logged in. Try again!");
            case WRONG_PASSWORD -> System.out.println("Wrong password. Try again!");
            case USERNAME_NOT_FOUND -> System.out.println("Username not found. Try again!");
            case ACCOUNT_ALREADY_LOGGED_IN_BY_SOMEONE_ELSE -> System.out.println("Account already logged in. Try again!");
        }

        switch (signUpError) {
            case GENERIC_ERROR -> System.out.println("Generic error. Try again!");
            case USERNAME_ALREADY_TAKEN -> System.out.println("Username already taken. Try again!");
        }
    }

    @Override
    public void handleInput(String input) {
        mainComponent.handleInput(input);
    }

    @Override
    public void update() {
        if(model.isLoggedIn()) {
            model.unsubscribe(this);
            nextState();
        }
        else {
            logInError = model.getLogInError();
            signUpError = model.getSignUpError();
            print();
        }

    }

    private void nextState(){
        model.setView(new LobbyJoinedState(model));
    }
}
