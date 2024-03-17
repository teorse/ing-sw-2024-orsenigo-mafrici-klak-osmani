public class Corner {
    private CornerType cornerType;
    private Resource resource;
    private Item item;

    //Constructor for Corner

    public Corner(CornerType cornerType, Resource resource, Item item) {
        this.cornerType = cornerType;
        this.resource = resource;
        this.item = item;
    }


    //Getters
    public CornerType getCornerType() {
        return cornerType;
    }

    public Resource getResource() {
        return resource;
    }

    public Item getItem() {
        return item;
    }
}
