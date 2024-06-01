package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.*;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DrawState extends LobbyStates {
    List<Component> passiveComponenets;
    InteractiveComponent mainComponent;
    InteractiveComponent secondaryComponent;
    InteractiveComponent activeComponent;
    Map<String, InteractiveComponent> keywordToComponentMap;
    boolean commandNotFund;
    boolean attemptToExitMainComponent;

    PlayerStates myPlayerStates;

    private final Logger logger;

    public DrawState(ClientModel2 model) {
        super(model);
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponenets = new ArrayList<>();
        passiveComponenets.add(new ChatNotification(this));
        passiveComponenets.add(new SharedObjectiveView(this));
        passiveComponenets.add(new SecretObjectiveView(this));
        passiveComponenets.add(new PointTableView(this));
        passiveComponenets.add(new CardMapView(this));
        passiveComponenets.add(new TurnShower(this));

        secondaryComponent = new ChatMessageSender(this);

        mainComponent = new CardDrawer();

        keywordToComponentMap = new HashMap<>();
        keywordToComponentMap.put(secondaryComponent.getKeyword(), secondaryComponent);

        commandNotFund = false;
        attemptToExitMainComponent = false;

        myPlayerStates = MyPlayer.getInstance().getMyPlayerGameState();
    }

    @Override
    public void print() {
        //todo add class game and logic to switch between game title and last round
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        //todo add display of all available commands

        for (Component component : passiveComponenets) {
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
        nextState();
    }

     boolean nextState() {
        if(model.isGameOver())
            model.setView(new GameOverState(model));
        else if (myPlayerStates == PlayerStates.WAIT)
            model.setView(new WaitState(model));
        else
            print();
    }
}
