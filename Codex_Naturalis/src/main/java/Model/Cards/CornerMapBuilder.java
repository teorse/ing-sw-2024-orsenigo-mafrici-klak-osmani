package Model.Cards;

import java.util.HashMap;
import java.util.Map;

public class CornerMapBuilder {
    //ATTRIBUTES
    private Map<CornerOrientation, Corner> corners = new HashMap<>();





    //CONSTRUCTOR
    public CornerMapBuilder(){}





    //SETTER
    public CornerMapBuilder withCorner(CornerOrientation cornerOrientation, Corner corner){
        corners.put(cornerOrientation, corner);
        return this;
    }





    //BUILDER
    public Map<CornerOrientation, Corner> build(){
        return this.corners;
    }
}
