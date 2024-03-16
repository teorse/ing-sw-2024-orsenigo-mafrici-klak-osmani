public class CardPlacement {
    private ResourceCard card;
    private Coords coordinates;
    private boolean isFaceUp;

    //if true corner is visible, if false corner is covered by card. Same disposition as Corner Class
    private boolean[] cornerVisibility;

    //Constructor
    //setVisibility(boolean, int)
    //sets visibility for each corner

    public int getCoordinate(String coord){
        if(coord.equals("X"))
            return this.coordinates.getCoordX();
        if(coord.equals("Y"))
            return this.coordinates.getCoordY();
        return 0;
    }
    //Returns individual coordinate

    public Coords getCoordinates(){
        return this.coordinates;
    }
    //Returns coordinates object
}
