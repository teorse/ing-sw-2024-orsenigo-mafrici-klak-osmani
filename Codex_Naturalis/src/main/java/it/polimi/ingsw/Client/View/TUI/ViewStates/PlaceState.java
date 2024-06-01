package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.CardDrawer;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ChatMessageSender;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PlaceState extends LobbyStates {
    List<Component> passiveComponents;
    InteractiveComponent mainComponent;
    InteractiveComponent secondaryComponent;
    InteractiveComponent activeComponent;
    Map<String, InteractiveComponent> keywordToComponentMap;
    boolean commandNotFund;
    boolean attemptToExitMainComponent;

    private final Logger logger;

    public PlaceState(ClientModel2 model) {
        super(model);
        logger = Logger.getLogger(PlaceState.class.getName());

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new ChatNotification(this));
        passiveComponents.add(new SharedObjectiveView(this));
        passiveComponents.add(new SecretObjectiveView(this));
        passiveComponents.add(new PointTableView(this));
        passiveComponents.add(new CardMapView(this));
        passiveComponents.add(new TurnShower(this));

        secondaryComponent = new ChatMessageSender(this);

        mainComponent = new CardDrawer();

        keywordToComponentMap = new HashMap<>();
        keywordToComponentMap.put(secondaryComponent.getKeyword(), secondaryComponent);

        boolean commandNotFund = false;
        boolean attemptToExitMainComponent = false;
    }

    @Override
    public void print() {
        //todo add class game and logic to switch between game title and last round
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        //todo add display of all available commands

        for (Component component : passiveComponents) {
            component.print();
        }
        mainComponent.print();
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
        if(!nextState())
            print();
    }

    boolean nextState() {
        if(model.isGameOver()) {
            model.setView(new GameOverState(model));
            return true;
        }
        else if (MyPlayer.getInstance().getMyPlayerGameState() == PlayerStates.DRAW) {
            model.setView(new DrawState(model));
            return true;
        }
        else if (MyPlayer.getInstance().getMyPlayerGameState() == PlayerStates.WAIT) {
            model.setView(new WaitState(model));
            return true;
        }
        else
            return false;
    }
}
