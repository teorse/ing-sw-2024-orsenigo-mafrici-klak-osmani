/**
 * The Corner class defines corners and all their intrinsic attributes, that is, the attributes that do not depend
 * on how the player placed the card but rather describe what the corner contains and how it can be interacted with.<br>
 * All corners are always linked to only one card.
 */

public class Corner {
    /**
     * Describes the type of corner.<br>
     * Allowed values are:<br>
     * -EMPTY, the corner has no item or resource, but it CAN be covered by another card.
     * -NULL, the corner has no item nor resource and CAN'T be covered by another card.
     * -ITEM, the corner contains an item.
     * -RESOURCE, the corner contains a resource.
     */
    private CornerType cornerType;
    /**
     * Describes which resource is contained in the corner.
     */
    private Resource resource;
    /**
     * Describes which item is contained in the corner.
     */
    private Item item;

    /**
     * Constructor for empty or null corners.
     * @param cornerType    defines type of corner. Allowed values: NULL or EMPTY
     */
    public Corner(CornerType cornerType){
        this.cornerType = cornerType;
    }

    /**
     * Constructor for resource-holding corners. Only requires which resource is held by the corner.
     * @param resource  Resource held by the corner. Allowed values are: ANIMAL,FUNGI,INSECT,PLANT.
     */
    public Corner(Resource resource) {
        this.cornerType = CornerType.RESOURCE;
        this.resource = resource;
    }

    /**
     * Constructor for item-holding corners. Only requires which item is held by the corner.
     * @param item  Item held by the corner. Allowed values are: INKWELL, MANUSCRIPT, QUILL
     */
    public Corner(Item item) {
        this.cornerType = CornerType.ITEM;
        this.item = item;
    }


    //Getters

    /**
     * Retruns Enum value of CornerType.
     * @return  Enum value of CornerType.
     */
    public CornerType getCornerType() {
        return cornerType;
    }

    /**
     * Returns the Enum value of the resource held by the corner.
     * @return  Enum value of Resource.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Returns the Enum value of the Item held by the corner.
     * @return  Enum value of Item.
     */
    public Item getItem() {
        return item;
    }
}
