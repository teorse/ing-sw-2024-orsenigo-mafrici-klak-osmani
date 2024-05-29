package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

public class SecretObjective extends Observable{
    //SINGLETON PATTERN
    private static SecretObjective INSTANCE;
    private SecretObjective(){}
    public static SecretObjective getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SecretObjective();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private ObjectiveRecord secretObjective;





    //GETTERS
    public ObjectiveRecord getSecretObjective() {
        return secretObjective;
    }





    //SETTERS
    public void setSecretObjective(ObjectiveRecord secretObjective) {
        this.secretObjective = secretObjective;
        super.updateObservers();
    }
}