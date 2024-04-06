package Model.Cards;

import java.util.Objects;

public class CornerOrientation {

    //ATTRIBUTES
    private CornerDirection cornerDirection;
    private boolean faceUp;





    //CONSTRUCTOR
    public CornerOrientation(CornerDirection cornerDirection, boolean faceUp) {
        this.cornerDirection = cornerDirection;
        this.faceUp = faceUp;
    }





    //GETTER
    public CornerDirection getCornerDirection() {
        return cornerDirection;
    }
    public boolean isFaceUp() {
        return faceUp;
    }





    //EQUALS AND HASH
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof CornerOrientation that)
            return faceUp == that.faceUp && cornerDirection == that.cornerDirection;
        else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cornerDirection, faceUp);
    }

    @Override
    public String toString() {
        return "CornerOrientation{" +
                "cornerDirection=" + cornerDirection +
                ", faceUp=" + faceUp +
                '}';
    }
}
