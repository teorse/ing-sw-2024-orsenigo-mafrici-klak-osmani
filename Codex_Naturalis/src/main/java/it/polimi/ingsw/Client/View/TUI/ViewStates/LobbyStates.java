package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPQuitLobby;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.util.List;
import java.util.logging.Logger;

//Abstract class that all classes AFTER joining a lobby have to implement
//To allow for the "quit" and "chat" functionality.
public abstract class LobbyStates extends ComplexState {
    private final ClientModel model;
    private final Logger logger;


    public LobbyStates(InteractiveComponent mainComponent, List<InteractiveComponent> secondaryComponents) {
        super(mainComponent, secondaryComponents);
        logger = Logger.getLogger(LobbyStates.class.getName());
        logger.info("Initializing LobbyState abstract class");

        model = ClientModel.getInstance();
    }

    public LobbyStates(InteractiveComponent mainComponent) {
        super(mainComponent);
        logger = Logger.getLogger(LobbyStates.class.getName());
        logger.info("Initializing LobbyState abstract class");

        model = ClientModel.getInstance();
    }


    @Override
    public boolean handleInput(String input) {
        if (input.equalsIgnoreCase("/CHAT")) {
            model.setView(new ChatState(this));
            return true;
        }
        else if(input.equalsIgnoreCase("/QUITLOBBY")) {
            model.quitLobby();
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            model.setView(new LobbySelectionState());
            return true;
        }
        else
            return super.handleInput(input);
    }

    @Override
    synchronized boolean nextState(){
        if (model.getView().equals(this)) {
            if(super.nextState())
                return true;
            else {
                logger.info("Evaluating next state in LobbyState");

                if (!ClientModel.getInstance().isInLobby()) {
                    logger.fine("User is no longer in the Lobby, setting next state as LobbyStateSelection.");
                    RefreshManager.getInstance().resetObservables();
                    ClientModel.getInstance().setView(new LobbySelectionState());
                    ClientModel.getInstance().getView().print();

                    return true;
                }
                else if(MyPlayer.getInstance().isNewState() && !ClientModel.getInstance().isGameOver()) {
                    logger.fine("User is still in the Lobby and the game is not over and a new player game state has been delivered.");

                    RefreshManager.getInstance().resetObservables();

                    PlayerStates playerState = MyPlayer.getInstance().getMyPlayerGameState();
                    logger.fine("The new player game state is: "+ playerState);

                    switch (playerState) {
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
                    logger.fine("New view state is: "+ClientModel.getInstance().getView().getClass().getSimpleName());
                    ClientModel.getInstance().getView().print();
                    return true;
                }
            }
            //Returns false because could not match conditions for next state
            return false;
        }
        //Returns true because the initial if statement was false and therefore this state is already not the current state.
        return true;
    }
}
