public class ResourceCard {
    private Corner[] corners;
    //Corner[0] is corner NW on card.
    //All other corners follow clockwise.
    private Resource resource;
    int givesPoint;

    //Constructor
    public ResourceCard(Resource resource, int givesPoints, Corner[] corners){
        this.resource = resource;
        this.givesPoint = givesPoints;
        this.corners = corners;
    }

    public ResourceCard(){};


    //getter methods

    public Corner[] getCorners() {
        return corners;
    }

    public Resource getResource() {
        return resource;
    }

    public int getGivesPoint() {
        return givesPoint;
    }
}
