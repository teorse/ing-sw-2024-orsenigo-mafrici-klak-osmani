import java.util.Map;

public class StarterCard extends Card {
    private Map<Resource, Integer> centralResources;

    public StarterCard(Resource resource, int givedPoints, Corner[] corners, Map<Resource, Integer> centralResources) {
        super(resource, givedPoints, corners);
        this.centralResources = centralResources;
    }
    //get method


    public Map<Resource, Integer> getCentralResources() {
        return centralResources;
    }
}
