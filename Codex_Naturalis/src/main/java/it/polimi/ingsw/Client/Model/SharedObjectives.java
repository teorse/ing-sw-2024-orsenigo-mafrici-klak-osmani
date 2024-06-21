package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.ArrayList;
import java.util.List;

public class SharedObjectives extends Observable {
    //SINGLETON PATTERN
    private static SharedObjectives INSTANCE;
    private SharedObjectives(){
        sharedObjectives = new ArrayList<>();
    }
    public static SharedObjectives getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SharedObjectives();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private List<ObjectiveRecord> sharedObjectives;





    //GETTERS
    public List<ObjectiveRecord> getSharedObjectives() {
        return sharedObjectives;
    }





    //SETTERS
    public void setSharedObjectives(List<ObjectiveRecord> sharedObjectives) {
        if(sharedObjectives != null)
            this.sharedObjectives.addAll(sharedObjectives);
        else
            this.sharedObjectives = new ArrayList<>();

        super.updateObservers();
    }
}

