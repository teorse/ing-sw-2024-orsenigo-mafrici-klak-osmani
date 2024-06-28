import it.polimi.ingsw.Client.View.TUI.Components.CardViewPretty;
import it.polimi.ingsw.Server.Model.Game.Cards.Card;
import it.polimi.ingsw.Server.Model.Game.GameLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class Temp {



    public static void main(String[] args) {
//        System.out.println("""
//                ┌───────────────────────────────┐
//                │░┌────┐░░┌───────────┐░░┌────┐░│
//                │░│░░░░│░░│░░░░░░░░░░░│░░│░░░░│░│
//                │░└────┘░░└───────────┘░░└────┘░│
//                │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
//                │░┌────┐░░┌───────────┐░░┌────┐░│
//                │░│░░░░│░░│░░░░░░░░░░░│░░│░░░░│░│
//                │░└────┘░░└───────────┘░░└────┘░│
//                └───────────────────────────────┘""");
//
//        System.out.println("┌────┐\n│░\uD83D\uDC3E░│");
//
//        System.out.println("""
//                ┌────┬──┬────────────┬──┬────┐
//                │░░░░│░░│░░░░░░░░░░░░│░░│░░░░│
//                ├────┘░░└────────────┘░░└────┤
//                │░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
//                ├────┐░░┌────────────┐░░┌────┤
//                │░░░░│░░│░░░░░░░░░░░░│░░│░░░░│
//                └────┴──┴────────────┴──┴────┘""");

        List<Card> cardsResource = null;
        List<Card> cardsGolden = null;
        List<Card> cardsStarter = null;

        String cardsPath = "assets/cards/";

        String cardsGoldenFileName = "CardsGolden.bin";
        String cardsResourceFileName = "CardsResource.bin";
        String cardsStarterFileName = "CardsStarter.bin";


        //Load the assets from the resources directory
        cardsResource = getCards(cardsResource, cardsPath, cardsResourceFileName);

        cardsGolden = getCards(cardsGolden, cardsPath, cardsGoldenFileName);

        cardsStarter = getCards(cardsStarter, cardsPath, cardsStarterFileName);


        new CardViewPretty(cardsResource.get(5).toRecord(), true, true).print();
        new CardViewPretty(cardsResource.get(26).toRecord(), true, true).print();
        new CardViewPretty(cardsGolden.get(2).toRecord(), true, true).print();
        new CardViewPretty(cardsGolden.get(36).toRecord(), true, true).print();
        new CardViewPretty(cardsGolden.get(23).toRecord(), true, true).print();
        new CardViewPretty(cardsStarter.get(5).toRecord(), true, true).print();
    }


    private static List<Card> getCards(List<Card> cardsResource, String cardsPath, String cardsResourceFileName) {
        try (InputStream inputStream = GameLoader.class.getClassLoader().getResourceAsStream(cardsPath + cardsResourceFileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            cardsResource = (List<Card>) objectInputStream.readObject();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cardsResource;
    }
}
