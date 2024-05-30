package it.polimi.ingsw.Client.Model;

public class MyPlayer extends Observable{
    //SINGLETON PATTERN
    private static MyPlayer INSTANCE;
    private MyPlayer(){}
    public static MyPlayer getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MyPlayer();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private String username;
    private boolean isAdmin;





    //GETTERS
    public String getUsername() {
        return username;
    }
    public boolean isAdmin() { return isAdmin; }





    //SETTERS
    public void setUsername(String username) {
        this.username = username;
        super.updateObservers();
    }
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        super.updateObservers();
    }
}
