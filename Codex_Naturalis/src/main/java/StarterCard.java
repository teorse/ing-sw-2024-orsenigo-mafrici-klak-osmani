import java.util.Map;

public class StarterCard extends ResourceCard{
    private Map<Resource, Integer> centralResources;

    public StarterCard(Resource resource, Boolean givesPoints, Corner[] corners, Map<Resource, Integer> centralResources) {
        super(resource, givesPoints, corners);
        this.centralResources = centralResources;
    }
    //get method
}
