package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPQuitLobby;

import java.util.List;

//Abstract class that all classes AFTER joining a lobby have to implement
//To allow for the "quit" and "chat" functionality.
public abstract class LobbyStates extends ComplexState {
    private final ClientModel model;


    public LobbyStates(InteractiveComponent mainComponent, List<InteractiveComponent> secondaryComponents) {
        super(mainComponent, secondaryComponents);
        model = ClientModel.getInstance();
    }

    public LobbyStates(InteractiveComponent mainComponent) {
        super(mainComponent);
        model = ClientModel.getInstance();
    }


    @Override
    public boolean handleInput(String input) {
        if (input.equalsIgnoreCase("CHAT")) {
            model.setView(new ChatState(this));
            return true;
        }
        else if(input.equalsIgnoreCase("QUITLOBBY")) {
            model.quitLobby();
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            model.setView(new LobbySelectionState());
            return true;
        }
        else
            return super.handleInput(input);
    }

    @Override
    boolean nextState(){
        if(super.nextState())
            return true;
        else {
            if (!ClientModel.getInstance().isInLobby()) {
                RefreshManager.getInstance().resetObservables();
                ClientModel.getInstance().setView(new LobbySelectionState());
                ClientModel.getInstance().getView().print();

                return true;
            }
            else if(MyPlayer.getInstance().isNewState() && !ClientModel.getInstance().isGameOver()) {

                RefreshManager.getInstance().resetObservables();

                switch (MyPlayer.getInstance().getMyPlayerGameState()) {
                    case PLACE -> {
                        if (!Game.getInstance().isSetupFinished())
                            model.setView(new StarterPlaceState());
                        else
                            model.setView(new PlaceState());
                    }
                    case DRAW -> model.setView(new DrawState());
                    case PICK_OBJECTIVE -> model.setView(new GamePickObjectiveState());
                    case WAIT -> model.setView(new WaitState());
                }

                ClientModel.getInstance().getView().print();
                return true;
            }
        }
        return false;
    }
}
