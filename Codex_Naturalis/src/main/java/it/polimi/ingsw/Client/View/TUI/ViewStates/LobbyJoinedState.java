package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ColorPicker;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.GameManualStarter;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;
import it.polimi.ingsw.Client.View.TUI.Components.LobbyView;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.util.HashMap;
import java.util.Map;

public class LobbyJoinedState extends ViewState {
    Component lobbyView;
    InteractiveComponent mainComponent;
    Map<String, InteractiveComponent> keywordToComponentMap;
    InteractiveComponent secondaryComponent;
    InteractiveComponent activeComponent;

    PlayerStates myPlayerGameState;

    ErrorsDictionary joinLobbyError;
    ErrorsDictionary startLobbyError;

    boolean attemptToExitMainComponent;
    boolean commandNotFund;

    public LobbyJoinedState(ClientModel2 model) {
        super(model);

        lobbyView = new LobbyView();
        mainComponent = new GameManualStarter();
        secondaryComponent = new ColorPicker();

        keywordToComponentMap = new HashMap<>();
        keywordToComponentMap.put(secondaryComponent.getKeyword(), secondaryComponent);

        myPlayerGameState = MyPlayer.getInstance().getMyPlayerGameState();

        attemptToExitMainComponent = false;
        commandNotFund = false;
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        lobbyView.print();
        mainComponent.print();

        if(attemptToExitMainComponent)
            System.out.println("You can't exit from the main component.");
        else if (commandNotFund) {
            System.out.println("Command not found");
        }

        attemptToExitMainComponent = false;
        commandNotFund = false;
    }

    @Override
    public void handleInput(String input) {
        if(input == null){
            //todo add logger
            return;
        }
        if(input.isEmpty()){
            //todo add logger
            return;
        }
        if (input.charAt(0) == '/') {
            String keyword;
            //Removes char '/' and cuts the string based on spaces
            String[] parts = input.substring(1).split(" ");
            //Picks the first word after '/' character
            keyword = parts[0];

            if(!keywordToComponentMap.containsKey(keyword)){
                commandNotFund = true;
            }
            else{
                activeComponent = keywordToComponentMap.get(keyword);
            }
        }

        InteractiveComponentReturns result = activeComponent.handleInput(input);
        if(result == InteractiveComponentReturns.COMPLETE){
            if(!activeComponent.equals(mainComponent)) {
                activeComponent = mainComponent;
            }
        }
        else if(result == InteractiveComponentReturns.QUIT) {
            if (!activeComponent.equals(mainComponent))
                activeComponent = mainComponent;
            else
                attemptToExitMainComponent = true;
        }

        print();
    }

    @Override
    public void update() {
        if (model.isGameStarted()) {
            switch (myPlayerGameState) {
                case PLACE -> {
                    if (!model.isSetUpFinished())
                        model.setView(new StarterPlaceState(model));
                    else
                        model.setView(new PlaceState(model));
                }
                case DRAW -> model.setView(new DrawState(model));
                case PICK_OBJECTIVE -> model.setView(new GamePickObjectiveState(model));
                case WAIT -> model.setView(new WaitState(model));
                case null, default -> print();
            }
        }
    }
}
