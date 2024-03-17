import java.util.Map;

public class GoldenCard extends ResourceCard{
    private boolean requiresCorner;
    private Item requiredItem;
    private Map<Resource, Integer> constraint;
    public GoldenCard(Resource resource, int givedPoints, Corner[] corners, boolean requiresCorner, Item requiredItem, Map<Resource, Integer> constraint) {
        super(resource, givedPoints, corners);
        this.requiresCorner = requiresCorner;
        this.requiredItem = requiredItem;
        this.constraint = constraint;
    }
    //get methods next


    public boolean isRequiresCorner() {
        return requiresCorner;
    }

    public Item getRequiredItem() {
        return requiredItem;
    }

    public Map<Resource, Integer> getConstraint() {
        return constraint;
    }
}
