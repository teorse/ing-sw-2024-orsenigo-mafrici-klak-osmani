package Model.Cards;

/**
 * 
 */
public class CardResourceFactory implements CardFactory {

    /**
     * Method is static as it is not tied to any object, it is available to all classes in the package
     * @return
     */
    protected static Card createCard() {
        return CardResource.Build();
    }
}