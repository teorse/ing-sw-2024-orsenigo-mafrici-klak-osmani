package Model.Cards;

import Model.Cards.*;
import Model.Other.*;
import Model.Utility.*;

import java.util.*;

/**
 * 
 */
public class CardStarterFactory implements CardFactory {

    /**
     * Default constructor
     */
    public CardStarterFactory() {
    }

    /**
     * @return
     */
    public static Card createCard() {
        return CardStarter.Build();
    }

}