package Model.Cards;

/**
 * Method is static as it is not tied to any object, it is available to all classes in the package
 * @return
 */
public class CardGoldenFactory implements CardFactory{
        protected static Card createCard() {
            return CardGolden.Build();
        }
}
