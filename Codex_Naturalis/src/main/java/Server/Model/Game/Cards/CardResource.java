package Server.Model.Game.Cards;

import Client.Model.Records.CardRecord;
import Server.Model.Game.Player.CardMap;
import Server.Model.Game.Utility.Artifacts;
import Server.Model.Game.Utility.Coordinates;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CardResource extends Card {

    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -8859474130320820205L;
    private Artifacts cardColor;
    private int points;





    //CONSTRUCTOR
    /* Two case: one providing the points, that are set automatically at the same value; while the other one set by
     * default the points to zero when no int attribute is provided. */
    public CardResource(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners) {
        super(corners);
        this.cardColor = cardColor;
        this.points = points;
    }

    public CardResource(Artifacts cardColor, Map<CornerOrientation, Corner> corners) {
        super(corners);
        this.cardColor = cardColor;
        //If missing points attribute is set to zero by default
    }





    //GETTER
    public Artifacts getCardColor(){
        return cardColor;
    };
    public int getPoints(){
        return points;
    };

    //ABSTRACT CLASS METHODS
    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp) {
        if (faceUp)
            return this.getPoints();
        else
            return 0;
    };

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
                        if (mapArtifacts.putIfAbsent(art, 1) != null) {
                            value = mapArtifacts.get(art) + 1;
                            mapArtifacts.remove(art);
                            mapArtifacts.put(art, value);
                        }
                    }
                }
            }
        }
        else
            //Every CardResource/Golden don't have any artifacts on back corners, put only the central one
            mapArtifacts.put(this.getCardColor(),1);
        return mapArtifacts;
    }

    @Override
    public CornerType getCornerType(CornerDirection direction, boolean faceUp){
        if(!faceUp)
            return CornerType.EMPTY;
        else
            return super.getCornerType(direction, true);
    }

    @Override
    public Artifacts getCornerArtifact(CornerDirection direction, boolean faceUp){
        if(!faceUp)
            return Artifacts.NULL;
        else
            return super.getCornerArtifact(direction, true);
    }




    //EQUALS AND HASH
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardResource that)) return false;
        return points == that.points && cardColor == that.cardColor && Objects.equals(this.getCorners(), that.getCorners());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardColor, points, this.getCorners());
    }

    @Override
    public CardRecord toRecord() {
        return new CardRecord(getCardColor(), getPoints(), super.getCorners(), false, null, null, null);
    }

    @Override
    public CardRecord toRecord(boolean faceUp) {
        return new CardRecord(getCardColor(), getPoints(), super.getCorners(faceUp), false, null, null, null);
    }
}