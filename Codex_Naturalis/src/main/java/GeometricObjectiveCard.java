import java.util.Map;

public class GeometricObjectiveCard implements ObjectiveCard{
    private String description;
    private Map<Integer[], Resource> cardPattern;
    @Override
    public String getDescription() {
        return description;
    }
    //constructor and get methods next


    public GeometricObjectiveCard(String description, Map<Integer[], Resource> cardPattern) {
        this.description = description;
        this.cardPattern = cardPattern;
    }

    public Map<Integer[], Resource> getCardPattern() {
        return cardPattern;
    }
}
