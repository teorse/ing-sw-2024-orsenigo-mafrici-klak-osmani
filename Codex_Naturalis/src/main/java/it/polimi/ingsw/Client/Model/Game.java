package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.GameRecord;

public class Game extends Observable{
    //SINGLETON PATTERN
    private static Game INSTANCE;
    public static Game getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Game();
        }
        return INSTANCE;
    }

    //ATTRIBUTES
    private int roundsCompleted;
    private boolean lastRoundFlag;
    private boolean setupFinished;
    private boolean waitingForReconnections;





    //SETTER
    public void updateGame(GameRecord game){
        roundsCompleted = game.roundsCompleted();
        lastRoundFlag = game.lastRoundFlag();
        setupFinished = game.setupFinished();
        waitingForReconnections = game.waitingForReconnections();

        super.updateObservers();
    }





    //GETTERS
    public int getRoundsCompleted() {
        return roundsCompleted;
    }

    public boolean isLastRoundFlag() {
        return lastRoundFlag;
    }

    public boolean isSetupFinished() {
        return setupFinished;
    }

    public boolean isWaitingForReconnections() {
        return waitingForReconnections;
    }
}
