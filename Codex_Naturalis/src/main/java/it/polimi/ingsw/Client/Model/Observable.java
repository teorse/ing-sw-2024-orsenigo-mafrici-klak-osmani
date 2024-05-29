package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.View.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    //ATTRIBUTES
    protected final List<Observer> observers;




    //CONSTURCTOR
    protected Observable() {
        this.observers = new ArrayList<>();
    }





    //METHODS
    public void subscribe(Observer observer){
        observers.add(observer);
    }
    public void unsubscribe(Observer observer){
        observers.remove(observer);
    }
    protected void updateObservers(){
        for(Observer observer : observers)
            observer.update();
    }
}
