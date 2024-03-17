public class CardPlacement {
    private ResourceCard card;
    private Coords coordinates;
    private boolean isFaceUp;

    //if true corner is visible, if false corner is covered by card. Same disposition as Corner Class
    private boolean[] cornerVisibility;

    //Constructor

    public CardPlacement(ResourceCard card, Coords coordinates, boolean isFaceUp, boolean[] cornerVisibility) {
        this.card = card;
        this.coordinates = coordinates;
        this.isFaceUp = isFaceUp;
        this.cornerVisibility = cornerVisibility;
    }


    //setVisibility(boolean, int)
    //sets visibility for each corner
    // Index is the value for the specific corner and value is the boolean.
    public void setCornerVisibility( int index, boolean value){
        if(index >= 4 || index < 0 ){
            throw new IndexOutOfBoundsException("Index of the corner is out of range!");
        }

        this.cornerVisibility[index] = value;
    }

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
