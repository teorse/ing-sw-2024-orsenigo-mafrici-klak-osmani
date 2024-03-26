package Model.Cards;

public class CornerOrientationBuilder {
    //ATTRIBUTES
    private CornerDirection cornerDirection;
    private boolean faceUp;





    //CONSTRUCTOR
    public CornerOrientationBuilder(){}






    //SETTERS
    public CornerOrientationBuilder withCornerDirection(CornerDirection cornerDirection){
        this.cornerDirection = cornerDirection;
        return this;
    }

    public CornerOrientationBuilder onCardsFace(){
        this.faceUp = true;
        return this;
    }

    public CornerOrientationBuilder onCardsRear(){
        this.faceUp = false;
        return this;
    }





    //BUILDER
    public CornerOrientation build(){
        return new CornerOrientation()
                .setCornerDirection(this.cornerDirection)
                .setFaceUp(this.faceUp);
    }
}
