package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.PlayerRecord;
import Client.View.TextUI;
import Client.View.UserInterface;

public class GameOverState extends ClientState{

    public GameOverState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameOver();
        print();
    }

    @Override
    public void print() {
        System.out.println("THE GAME IS OVER. Final Rankings: ");
        int i = 1;
        for (PlayerRecord winners : model.getWinners()) {
            if (winners.winner())
                System.out.println(" 1 - ");
            else
                System.out.println(" " + i + " - ");
            System.out.println(winners.nickname() + "  Points: " + winners.points() + "  ObjectivesCompleted: "
                    + winners.objectivesCompleted() + " RoundsCompleted: " + winners.roundsCompleted());
        }
        System.out.println("\n" + "To exit the Final Rankings view enter EXIT.");
    }

    @Override
    public void handleInput(String input) {
        if(input.equalsIgnoreCase("EXIT"))
            nextState();
        else
            System.out.println("To exit enter EXIT!");
    }

    @Override
    public void nextState() {
        if (UserInterface.isLastOnlinePlayer(model)) {
            model.setClientState(new LobbySelectionState(model));
        } else {
            model.setClientState(new LobbyJoined(model));
        }
    }
}
