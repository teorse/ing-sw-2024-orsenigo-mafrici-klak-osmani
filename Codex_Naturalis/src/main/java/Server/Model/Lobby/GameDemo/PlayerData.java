package Server.Model.Lobby.GameDemo;

import java.beans.PropertyChangeSupport;

public class PlayerData {
    private String personalData;
    private String publicData;
    private PropertyChangeSupport pcs;

    public PlayerData(){
        personalData = "";
        publicData = "";
        pcs = new PropertyChangeSupport(this);
    }

    public void appendPersonalData(String data){
        personalData = personalData+data;
        pcs.firePropertyChange("personalData", null, personalData);
    }

    public void appendPublicData(String data){
        publicData = publicData+data;
        pcs.firePropertyChange("publicData", null, publicData);
    }

    public void appendPersonalPublicData(String personalData, String publicData){
        this.personalData = this.personalData+personalData;
        this.publicData = this.publicData+publicData;

        pcs.firePropertyChange("personalData", null, this.personalData);
        pcs.firePropertyChange("publicData", null, this.publicData);
    }

    public void addListener(PlayerDataListener listener){
        pcs.addPropertyChangeListener(listener);
    }
}
