import java.util.Map;

public class NumericObjectiveCard implements ObjectiveCard{
    private String description;
    private Item item;

    //Potrebbe essere utile aggiungere un boolean per scegliere subito la mappa corretta
    private Map<Item, Integer> requiredItems;
    private Map<Resource, Integer> requiredResources;
    @Override
    public String getDescription() {
        return description;
    }
    //constructor and get methods next
}
