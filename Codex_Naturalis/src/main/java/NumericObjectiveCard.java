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
    //constructor

    public NumericObjectiveCard(String description, Item item, Map<Item, Integer> requiredItems, Map<Resource, Integer> requiredResources) {
        this.description = description;
        this.item = item;
        this.requiredItems = requiredItems;
        this.requiredResources = requiredResources;
    }

    //Get Methods
    public Item getItem() {
        return item;
    }

    public Map<Item, Integer> getRequiredItems() {
        return requiredItems;
    }

    public Map<Resource, Integer> getRequiredResources() {
        return requiredResources;
    }
}
