import java.util.Map;

public class GoldenCard extends ResourceCard{
    private boolean requiresCorner;
    private Item requiredItem;
    private Map<Resource, Integer> constraint;
    public GoldenCard(Resource resource, Boolean givesPoints, Boolean corner, Corner[] corners, Item item, Map<Resource, Integer> map) {
        super(resource, givesPoints, corners);
        this.requiresCorner = corner;
        this.requiredItem = item;
        this.constraint = map;
    }
    //get methods next
}
