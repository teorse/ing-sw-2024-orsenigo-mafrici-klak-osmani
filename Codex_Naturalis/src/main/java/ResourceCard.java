public class ResourceCard {
    private Corner[] corners;
    //Corner[0] is corner NW on card.
    //All other corners follow clockwise.
    private Resource resource;
    Boolean givesPoint;

    //Constructor
    public ResourceCard(Resource resource, Boolean givesPoints, Corner[] corners){
        this.resource = resource;
        this.givesPoint = givesPoints;
        this.corners = corners;
    }
    //getter methods next
}
