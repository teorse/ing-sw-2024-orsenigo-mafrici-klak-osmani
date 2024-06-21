package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;

import java.util.*;
import java.util.logging.Logger;

public class RefreshManager implements Observer {
    //SINGLETON PATTERN
    private static RefreshManager INSTANCE;
    public static RefreshManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RefreshManager();
        }
        return INSTANCE;
    }

    private final Map<Observable, Integer> observableMap;
    private final Map<LiveComponent, Set<Observable>> observerMap;
    private final Logger logger;


    private RefreshManager(){
        ClientModel.getInstance().subscribe(this);
        logger = Logger.getLogger(RefreshManager.class.getName());
        logger.info("Initializing refresh manager...");

        observableMap = new HashMap<>();
        observerMap = new HashMap<>();
    }

    public synchronized void addObserved(LiveComponent observer, Observable observed){
        //Checks if there are duplicate requests and ignores them
        if(observerMap.containsKey(observer) && observerMap.get(observer).contains(observed))
            return;
        else{
            if(observerMap.containsKey(observer)){
                observerMap.get(observer).add(observed);
            }
            else {
                observerMap.put(observer, new HashSet<>());
                observerMap.get(observer).add(observed);
            }
        }


        addObserved(observed);
    }

    public synchronized void addObserved(Observable observed){
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

    public synchronized void removeObserved(LiveComponent observer, Observable observed){

        if(!observerMap.containsKey(observer) || !observerMap.get(observer).contains(observed))
            return;
        else{
            Set<Observable> set = observerMap.get(observer);
            set.remove(observed);

            if(set.isEmpty())
                observerMap.remove(observer);
        }


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

    public synchronized void resetObservables(){
        List<Observable> observables = new ArrayList<>(observableMap.keySet());
        for(Observable observable : observables){
            observableMap.remove(observable);
            observable.unsubscribe(this);
        }

        observableMap.clear();
        observerMap.clear();
    }

    @Override
    public void update() {
        logger.info("Received an update, updating the view.");
        ClientModel.getInstance().getView().update();
    }
}
