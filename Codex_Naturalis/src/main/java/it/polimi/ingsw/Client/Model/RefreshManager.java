package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;

import java.util.*;
import java.util.logging.Logger;

/**
 * Singleton class responsible for managing the refresh of observed components in the client-side model.
 */
public class RefreshManager implements Observer {
    // Singleton instance
    private static RefreshManager INSTANCE;

    // Maps to track observed components and their observables
    private final Map<Observable, Integer> observableMap;
    private final Map<LiveComponent, Set<Observable>> observerMap;
    private final Logger logger;

    /**
     * Private constructor to enforce singleton pattern and initialize necessary maps and logger.
     */
    private RefreshManager() {
        ClientModel.getInstance().subscribe(this);
        logger = Logger.getLogger(RefreshManager.class.getName());
        logger.info("Initializing refresh manager...");

        observableMap = new HashMap<>();
        observerMap = new HashMap<>();
    }

    /**
     * Retrieves the singleton instance of RefreshManager.
     *
     * @return The singleton instance of RefreshManager.
     */
    public synchronized static RefreshManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RefreshManager();
        }
        return INSTANCE;
    }

    /**
     * Adds a LiveComponent to observe a specific Observable.
     *
     * @param observer The LiveComponent that will observe the Observable.
     * @param observed The Observable to be observed.
     */
    public synchronized void addObserved(LiveComponent observer, Observable observed) {
        // Check if there are duplicate requests and ignore them
        if (observerMap.containsKey(observer) && observerMap.get(observer).contains(observed))
            return;
        else {
            if (observerMap.containsKey(observer)) {
                observerMap.get(observer).add(observed);
            } else {
                observerMap.put(observer, new HashSet<>());
                observerMap.get(observer).add(observed);
            }
        }

        addObserved(observed);
    }

    /**
     * Adds an Observable to be observed.
     *
     * @param observed The Observable to be observed.
     */
    public synchronized void addObserved(Observable observed) {
        if (!observableMap.containsKey(observed)) {
            observableMap.put(observed, 1);
            observed.subscribe(this);
        } else {
            int oldCounter = observableMap.get(observed);
            oldCounter++;
            observableMap.put(observed, oldCounter);
        }
    }

    /**
     * Removes an Observable from being observed by a specific LiveComponent.
     *
     * @param observer The LiveComponent that was observing the Observable.
     * @param observed The Observable to stop observing.
     */
    public synchronized void removeObserved(LiveComponent observer, Observable observed) {
        if (!observerMap.containsKey(observer) || !observerMap.get(observer).contains(observed))
            return;
        else {
            Set<Observable> set = observerMap.get(observer);
            set.remove(observed);

            if (set.isEmpty())
                observerMap.remove(observer);
        }

        if (observableMap.containsKey(observed)) {
            if (observableMap.get(observed) == 1) {
                observableMap.remove(observed);
                observed.unsubscribe(this);
            } else {
                int oldCounter = observableMap.get(observed);
                oldCounter--;
                observableMap.put(observed, oldCounter);
            }
        }
    }

    /**
     * Resets all observables being managed by this RefreshManager.
     */
    public synchronized void resetObservables() {
        List<Observable> observables = new ArrayList<>(observableMap.keySet());
        for (Observable observable : observables) {
            observableMap.remove(observable);
            observable.unsubscribe(this);
        }

        observableMap.clear();
        observerMap.clear();
    }

    /**
     * Method called when this observer is notified of a change in any Observable it observes.
     * It updates the client view to reflect changes in the model.
     */
    @Override
    public void update() {
        logger.info("Refresh Manager Received an update, updating the view.");
        ClientModel.getInstance().getView().update();
        logger.fine("View Updated from RefreshManager");
    }
}
