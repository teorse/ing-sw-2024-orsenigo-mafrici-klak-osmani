public class CardPlacement {
    private ResourceCard card;
    private Coords coordinates;
    private boolean isFaceUp;
    private boolean[] cornerVisibility;//if true corner is visible, if false corner is covered by card

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
