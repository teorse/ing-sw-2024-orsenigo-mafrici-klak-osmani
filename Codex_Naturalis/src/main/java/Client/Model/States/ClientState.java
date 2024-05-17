package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.LobbyUserRecord;
import Client.Model.Records.PlayerRecord;
import Client.View.TextUI;
import Client.View.UserInterface;
import Model.Player.PlayerStates;

public abstract class ClientState {
    protected ClientModel model;
    protected TextUI textUI;
    protected PlayerRecord myPR;
    protected LobbyUserRecord myLUR;
    protected int inputCounter;


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

    //Used by GamePickObjetive and GameWaitState to move from a wait state
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

