package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPQuitLobby;

public abstract class LobbyStates extends ViewState {
    public LobbyStates(ClientModel2 model) {
        super(model);
    }

    @Override
    public abstract void print();

    @Override
    public void handleInput(String input) {
        if (input.equalsIgnoreCase("CHAT")) {
            model.setView(new ChatState(model,this));
        }
        else if(input.equalsIgnoreCase("QUITLOBBY")) {
            model.quitLobby();
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            model.setView(new LobbySelectionState(model));
        }
        else if (input.equalsIgnoreCase("EXIT")) {
            //TODO reset the input counter of the interactive component
        } else if (input.equalsIgnoreCase("BACK")) {

        }
    }

    boolean nextState() {

    }
}
