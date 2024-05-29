package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

public class SharedObjectives extends Observable {
    //SINGLETON PATTERN
    private static SharedObjectives INSTANCE;
    private SharedObjectives(){}
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
        this.sharedObjectives.addAll(sharedObjectives);
        super.updateObservers();
    }
}

