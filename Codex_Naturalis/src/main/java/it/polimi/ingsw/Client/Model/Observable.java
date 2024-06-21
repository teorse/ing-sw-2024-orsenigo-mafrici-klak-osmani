package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.View.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class Observable {
    //ATTRIBUTES
    protected final List<Observer> observers;
    private final Logger logger;




    //CONSTURCTOR
    protected Observable() {
        logger = Logger.getLogger(Observable.class.getName());
        logger.info("Initializing abstract class Observable");
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
        logger.info("Updating observers.");

        if(observers.isEmpty())
            logger.fine("Current observer list is empty");
        else {
            logger.fine("Contents of observer list are:");
            for(Observer observer : observers) {
                logger.fine("observer: "+ observer.getClass().getName());
            }
        }
        for(Observer observer : observers) {
            logger.info("Updating observer: "+ observer.getClass().getName());
            observer.update();
        }
    }
}
