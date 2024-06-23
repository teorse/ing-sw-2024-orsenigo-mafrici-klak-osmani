package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

public class ObjectiveCandidates extends Observable {
    //SINGLETON PATTERN
    private static ObjectiveCandidates INSTANCE;
    private ObjectiveCandidates(){}
    public synchronized static ObjectiveCandidates getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ObjectiveCandidates();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private List<ObjectiveRecord> objectiveCandidates;





    //GETTERS
    public List<ObjectiveRecord> getObjectiveCandidates() {
        return objectiveCandidates;
    }
    public ObjectiveRecord getObjectiveCandidatesByIndex(int index){
        return objectiveCandidates.get(index);
    }






    //SETTERS
    public void setCandidates(List<ObjectiveRecord> objectiveCandidates) {
        this.objectiveCandidates = objectiveCandidates;
    }
}
