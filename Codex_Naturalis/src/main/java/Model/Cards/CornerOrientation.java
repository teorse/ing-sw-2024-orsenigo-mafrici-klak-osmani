package Model.Cards;

import java.util.Objects;

public class CornerOrientation {
    //ATTRIBUTES
    private CornerDirection cornerDirection;
    private boolean faceUp;





    //CONSTRUCTOR
    protected CornerOrientation(){};





    //SETTERS
    protected CornerOrientation setCornerDirection(CornerDirection cornerDirection){
        this.cornerDirection = cornerDirection;
        return this;
    }
    protected CornerOrientation setFaceUp(boolean faceUp){
        this.faceUp = faceUp;
        return this;
    }





    //EQUALS AND HASH

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CornerOrientation that)) return false;
        return faceUp == that.faceUp && cornerDirection == that.cornerDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cornerDirection, faceUp);
    }
}
