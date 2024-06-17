package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPQuitLobby;

//Abstract class that all classes AFTER joining a lobby have to implement
//To allow for the "quit" and "chat" functionality.
public abstract class LobbyStates extends ComplexState {
    public LobbyStates(ClientModel model) {
        super(model);
    }

    @Override
    public boolean handleInput(String input) {
        if (input.equalsIgnoreCase("CHAT")) {
            model.setView(new ChatState(model,this));
            return true;
        }
        else if(input.equalsIgnoreCase("QUITLOBBY")) {
            model.quitLobby();
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            model.setView(new LobbySelectionState(model));
            return true;
        }
        else
            return super.handleInput(input);
    }
}
