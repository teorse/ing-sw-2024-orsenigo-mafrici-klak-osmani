package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.View.Observer;

import java.util.Map;

/**
 * Abstract class representing the state of the client in the game.
 * <p>
 * This class serves as a blueprint for different states that the client can be in during the game.
 * It contains methods to print information, handle user input, and transition to the next state.
 * Each concrete subclass of ViewState represents a specific state in the game, such as login, lobby, or gameplay.
 * The class also provides a method to determine the next game state based on the current game conditions.
 */
public abstract class ViewState implements Observer {
    Map<Observable, Integer> observableMap;
    ClientModel model;

    public ViewState(ClientModel model){
        this.model = model;
        model.subscribe(this);

        print();
    }

    public abstract void print();

    public abstract boolean handleInput(String input);

    public void addObserved(Observable observed){
        if(!observableMap.containsKey(observed)) {
            observableMap.put(observed, 1);
            observed.subscribe(this);
        }
        else{
            int oldCounter = observableMap.get(observed);
            oldCounter++;
            observableMap.put(observed, oldCounter);
        }
    }

    public void removeObserved(Observable observed){
        if(observableMap.containsKey(observed)){
            if(observableMap.get(observed) == 1) {
                observableMap.remove(observed);
                observed.unsubscribe(this);
            }
            else{
                int oldCounter = observableMap.get(observed);
                oldCounter--;
                observableMap.put(observed, oldCounter);
            }
        }
    }

    public void sleepOnObservables(){
        for(Observable observed : observableMap.keySet())
            observed.unsubscribe(this);
    }

    public void wakeUpOnObservables(){
        for(Observable observed : observableMap.keySet())
            observed.subscribe(this);
    }
}

