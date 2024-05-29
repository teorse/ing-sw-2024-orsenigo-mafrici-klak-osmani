package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardVisibilityRecord;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;

import java.util.Map;

public class CardMaps extends Observable{
    //SINGLETON PATTERN
    private static CardMaps INSTANCE;
    private CardMaps(){};
    public static CardMaps getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CardMaps();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private Map<String, CardMapRecord> cardMaps;





    //GETTERS
    public Map<String, CardMapRecord> getCardMaps() {
        return cardMaps;
    }
    public CardVisibilityRecord getCardByCoordinate(Coordinates coordinate, String owner){
        return cardMaps.get(owner).getCardVisibilityRecord(coordinate);
    }





    //SETTERS
    public void setCardMaps(Map<String, CardMapRecord> cardMaps) {
        this.cardMaps = cardMaps;
        super.updateObservers();
    }
    public void setSpecificCardMap(String username, CardMapRecord cardMapRecord) {
        this.cardMaps.put(username, cardMapRecord);
        super.updateObservers();
    }




    //METHODS
    //todo add attribute maxCoordinate that caches value of max coordinate if not updates are made to the map
    public int maxCoordinate() {
        int mbs = 0;
        for (String username : cardMaps.keySet())
            for (Coordinates coordinates : cardMaps.get(username).cardsPlaced().keySet()) {
                int max = Math.max(Math.abs(coordinates.getCoordY()), Math.abs(coordinates.getCoordY()));
                if (max > mbs) {
                    mbs = max;
                }
            }
        return Math.max(mbs, 5);
    }

    public int maxBoardSide(){
        return maxCoordinate() *2 +3;
    }

    public Coordinates charsToCoordinates(char row, char column){
        int maxBoardSide = this.maxCoordinate() + 1;

        //Insert before the row than the column but in the map the x-axis is the one of the column
        int x = (int) (column) - 'A' - maxBoardSide;
        int y = (int) (row) - 'A' - maxBoardSide;

        return new Coordinates(x,-y);
    }

    public int coordinateIndexByCharIndexes(char row, char column, String owner){
        Coordinates coordinate = charsToCoordinates(row, column);
        CardMapRecord cardMapRecord = cardMaps.get(owner);

        if (cardMapRecord.availablePlacements().contains(coordinate))
            return  cardMapRecord.availablePlacements().indexOf(coordinate);
        else
            return -1;
    }
}

