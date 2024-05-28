package it.polimi.ingsw.Server.Model.Game.Cards;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Player.CardMap;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CardStarter extends Card {

    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -4633125594886874203L;
    /**
     * Useful when the card is placed back (faceUp == false). Can contain multiple Artifacts, unlike other cards, which
     * always have a resource on the back of the same type as the card itself.
     */
    private Map<Artifacts, Integer> centralArtifacts;





    //CONSTRUCTORS
    public CardStarter(Map<CornerOrientation, Corner> corners, Map<Artifacts, Integer> centralArtifacts) {
        super(corners);
        this.centralArtifacts = centralArtifacts;
    }





    //GETTER
    public Artifacts getCardColor() {
        return Artifacts.NULL;
    }
    public int getPoints() {
        return 0;
    }
    public Map<Artifacts, Integer> getCentralArtifacts() {
        return centralArtifacts;
    }





    //ABSTRACT CLASS METHODS
    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp) {
        return 0;
    }

    public Map<Artifacts, Integer> getAllArtifacts(boolean faceUp) {
        Map<CornerOrientation, Corner> corners = this.getCorners();
        Map<Artifacts, Integer> mapArtifacts = new HashMap<>();
        Artifacts art;
        int value;
        if (faceUp) {
            for (CornerOrientation co : corners.keySet()) {
                if (co.isFaceUp()) {
                    art = corners.get(co).getArtifact();
                    if (art != Artifacts.NULL) {
                        if (mapArtifacts.containsKey(art)) {
                            value = mapArtifacts.get(art) + 1;
                            mapArtifacts.put(art, value);
                        } else
                            mapArtifacts.put(art, 1);
                    }
                }
            }
        } else {
            //Start to add the central artifacts then check if other artifacts in corners are present (no duplicate key)
            for (Artifacts a : this.centralArtifacts.keySet()){
                mapArtifacts.put(a,centralArtifacts.get(a));
            }
            for (CornerOrientation co : corners.keySet()) {
                if (!co.isFaceUp()) {
                    art = corners.get(co).getArtifact();
                    if (art != Artifacts.NULL) {
                        if (mapArtifacts.containsKey(art)) {
                            value = mapArtifacts.get(art) + 1;
                            mapArtifacts.put(art, value);
                        } else
                            mapArtifacts.put(art, 1);
                    }
                }
            }
        }
        return mapArtifacts;
    }





    //EQUALS AND HASH
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardStarter that)) return false;
        return Objects.equals(centralArtifacts, that.centralArtifacts) && Objects.equals(this.getCorners(), that.getCorners());
    }

    @Override
    public int hashCode() {
        return Objects.hash(centralArtifacts, this.getCorners());
    }

    @Override
    public CardRecord toRecord() {
        return new CardRecord(null, 0, super.getCorners(), false, null, null, centralArtifacts);
    }

    @Override
    public CardRecord toRecord(boolean faceUp) {
        return new CardRecord(null, 0, super.getCorners(faceUp), false, null, null, centralArtifacts);
    }
}