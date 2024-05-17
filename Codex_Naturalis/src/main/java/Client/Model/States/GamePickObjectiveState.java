package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.ObjectiveRecord;
import Client.View.TextUI;
import Network.ClientServer.Packets.CSPPickObjective;

public class GamePickObjectiveState extends ClientState{
    public GamePickObjectiveState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        print();
    }

    @Override
    public void print() {
        int i = 1;
        System.out.println("Choose a secret objective: ");
        for (ObjectiveRecord objectiveRecord : model.getObjectiveCandidates()) {
            System.out.print(i + " - ");
            textUI.showObjective(objectiveRecord);
            System.out.println("\n");
            i++;
        }
    }

    @Override
    public void handleInput(String input) {
        if (TextUI.getBinaryChoice(input)) {
            int choice = Integer.parseInt(input);
            model.getClientConnector().sendPacket(new CSPPickObjective(choice - 1));
        } else
            print();
    }

    @Override
    public void nextState() {
        nextGameState();
    }
}
