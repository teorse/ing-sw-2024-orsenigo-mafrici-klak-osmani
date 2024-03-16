public class ResourceCard {
    private Corner[] corners;
    //Corner[0] is corner NW on card.
    //All other corners follow clockwise.
    private Resource resource;
    int givedPoint;

    //Constructor
    public ResourceCard(Resource resource, int givedPoints, Corner[] corners){
        this.resource = resource;
        this.givedPoint = givedPoints;
        this.corners = corners;
    }
    //getter methods next
}
