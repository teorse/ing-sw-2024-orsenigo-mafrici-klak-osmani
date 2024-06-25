package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.View.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The abstract class Observable represents an observable object that can be observed by observers.
 * It manages a list of registered observers and provides methods to subscribe, unsubscribe,
 * and notify them of changes.
 */
public abstract class Observable {

    /** The list of observers subscribed to this observable object. */
    protected final List<Observer> observers;

    /** Logger for logging messages related to observable actions. */
    private final Logger logger;

    /**
     * Constructs an Observable object and initializes the list of observers.
     * Initializes the logger for logging observable actions.
     */
    protected Observable() {
        logger = Logger.getLogger(Observable.class.getName());
        logger.info("Initializing abstract class Observable");
        this.observers = new ArrayList<>();
    }

    /**
     * Subscribes an observer to this observable object.
     *
     * @param observer The observer to subscribe.
     */
    public synchronized void subscribe(Observer observer){
        observers.add(observer);
    }

    /**
     * Unsubscribes an observer from this observable object.
     *
     * @param observer The observer to unsubscribe.
     */
    public synchronized void unsubscribe(Observer observer){
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers about a change in the observable object.
     * Starts a new thread for each observer to execute its update method.
     */
    protected synchronized void updateObservers(){
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
            new Thread(observer::update).start();
        }
    }
}
