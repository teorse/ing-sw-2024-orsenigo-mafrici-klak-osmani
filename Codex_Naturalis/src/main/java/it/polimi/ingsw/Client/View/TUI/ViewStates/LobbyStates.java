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

/**
 * Abstract class that all classes AFTER joining a lobby have to implement
 * to allow for the "quit" and "chat" functionality.
 */
public abstract class LobbyStates extends ComplexState {
    private final ClientModel model;
    private final Logger logger;

    /**
     * Constructs a LobbyStates with the specified main component and secondary components.
     *
     * @param mainComponent the main interactive component
     * @param secondaryComponents the list of secondary interactive components
     */
    public LobbyStates(InteractiveComponent mainComponent, List<InteractiveComponent> secondaryComponents) {
        super(mainComponent, secondaryComponents);
        logger = Logger.getLogger(LobbyStates.class.getName());
        logger.info("Initializing LobbyState abstract class");

        model = ClientModel.getInstance();
    }

    /**
     * Constructs a LobbyStates with the specified main component.
     *
     * @param mainComponent the main interactive component
     */
    public LobbyStates(InteractiveComponent mainComponent) {
        super(mainComponent);
        logger = Logger.getLogger(LobbyStates.class.getName());
        logger.info("Initializing LobbyState abstract class");

        model = ClientModel.getInstance();
    }

    /**
     * Handles input from the user and processes it. Supports commands to enter chat or quit the lobby.
     *
     * @param input the user input
     * @return true if the input was handled, false otherwise
     */
    @Override
    public boolean handleInput(String input) {
        if (input.equalsIgnoreCase("/CHAT")) {
            model.setView(new ChatState(this));
            model.printView();
            return true;
        } else if (input.equalsIgnoreCase("/QUITLOBBY")) {
            logger.info("Quitting lobby command in Lobby State");
            model.quitLobby();
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            model.setView(new LobbySelectionState());
            model.printView();
            logger.fine("Game over value after quit lobby command is: " + ClientModel.getInstance().isGameOver());
            return true;
        } else {
            return super.handleInput(input);
        }
    }

    /**
     * Determines the next state based on the current state of the model.
     *
     * @return true if the state has been changed, false otherwise
     */
    @Override
    boolean nextState() {
        synchronized (nextStateLock) {
            if (model.getView().equals(this)) {
                if (super.nextState())
                    return true;
                else {
                    logger.info("Evaluating next state in LobbyState");
                    logger.fine("GameOver value in Lobby States is: " + ClientModel.getInstance().isGameOver());

                    if (!ClientModel.getInstance().isInLobby()) {
                        logger.fine("User is no longer in the Lobby, setting next state as LobbyStateSelection.");
                        RefreshManager.getInstance().resetObservables();
                        ClientModel.getInstance().setView(new LobbySelectionState());
                        ClientModel.getInstance().printView();
                        return true;
                    } else if (MyPlayer.getInstance().isNewState() && !ClientModel.getInstance().isGameOver()) {
                        logger.fine("User is still in the Lobby and the game is not over and a new player game state has been delivered.");

                        RefreshManager.getInstance().resetObservables();

                        PlayerStates playerState = MyPlayer.getInstance().getMyPlayerGameState();
                        logger.fine("The new player game state is: " + playerState);

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
                        logger.fine("New view state is: " + ClientModel.getInstance().getView().getClass().getSimpleName());
                        ClientModel.getInstance().printView();
                        return true;
                    }
                }
                logger.fine("No eligible state was found, returning false");
                // Returns false because could not match conditions for next state
                return false;
            }
            logger.fine("State was already changed before this call, returning true");
            // Returns true because the initial if statement was false and therefore this state is already not the current state.
            return true;
        }
    }
}