package it.polimi.ingsw.Client.Model;

public class MyPlayer {
    //SINGLETON PATTERN
    private static MyPlayer INSTANCE;
    private MyPlayer(){}
    public static MyPlayer getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MyPlayer();
        }

        return INSTANCE;
    }


    private String username;

    public String getUsername() {
        return username;
    }
}
