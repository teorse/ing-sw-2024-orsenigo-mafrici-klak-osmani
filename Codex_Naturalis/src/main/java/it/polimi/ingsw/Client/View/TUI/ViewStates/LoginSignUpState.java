package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LogInSignUp;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

public class LoginSignUpState extends ViewState {

    InteractiveComponent mainComponent;
    ErrorsDictionary logInError = null;
    ErrorsDictionary signUpError = null;

    public LoginSignUpState(ClientModel model) {
        super(model);
        mainComponent = new LogInSignUp(this);
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        //reset the log-in/sign-up procedure if an error occurs
        if(logInError != null || signUpError != null){
            mainComponent.cleanObserved();
            mainComponent = new LogInSignUp(this);
        }

        mainComponent.print();

        if(logInError != null) {
            System.out.println("The following error occurred while logging in:");
            switch (logInError) {
                case GENERIC_ERROR -> System.out.println("Generic error.");
                case YOU_ARE_ALREADY_LOGGED_IN -> System.out.println("You are already logged in!");
                case WRONG_PASSWORD -> System.out.println("Wrong password.");
                case USERNAME_NOT_FOUND -> System.out.println("Username not found.");
                case ACCOUNT_ALREADY_LOGGED_IN_BY_SOMEONE_ELSE ->
                        System.out.println("Account already logged in!");
            }
            logInError = null;
        }

        if(signUpError != null) {
            System.out.println("The following error occurred while signing up:");
            switch (signUpError) {
                case GENERIC_ERROR -> System.out.println("Generic error.");
                case USERNAME_ALREADY_TAKEN -> System.out.println("Username already taken.");
            }
            signUpError = null;
        }
    }

    @Override
    public boolean handleInput(String input) {
        mainComponent.handleInput(input);

        return true;
    }

    @Override
    public void update() {
        if(!nextState()){
            logInError = model.getLogInError();
            signUpError = model.getSignUpError();
            print();
        }
    }

    private boolean nextState(){
        if(model.isLoggedIn()){
            model.unsubscribe(this);
            sleepOnObservables();
            model.setView(new LobbyJoinedState(model));
            return true;
        }
        return false;
    }
}
