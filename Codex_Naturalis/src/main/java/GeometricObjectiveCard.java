import java.util.Map;

public class GeometricObjectiveCard implements ObjectiveCard{
    private String description;
    private Map<Integer[], Resource> cardPattern;
    @Override
    public String getDescription() {
        return description;
    }
    //constructor and get methods next
}
