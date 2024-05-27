package Server.Model.Game.Utility.JsonParser;

import Server.Model.Game.Cards.Card;
import Server.Model.Game.Utility.JsonParser.SupportClasses.CardResourceSupportClass;
import Server.Model.Game.Utility.JsonParser.SupportClasses.CardGoldenSupportClass;
import Server.Model.Game.Utility.JsonParser.SupportClasses.CardStarterSupportClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains static methods to serialize Card->Json and to deserialize Json->Card Objects using the gson library with built-in
 * logic to check for errors in the json.<br>
 * <ul>
 *     <li><b>SERIALIZATION</b>: It is achieved with standard gson library methods.</li>
 *     <li><b>DESERIALIZATION</b>: By default, the gson parser does not check that the deserialized object matches class specifications because
 *     it bypasses the constructors for the class.<br>
 *     This implementation makes use of support classes to enforce the usage of constructors in the following way:<br>
 *     <ol>
 *          <li>Gson is used to parse the input string and is deserialized into an instance of the support class.</li>
 *          <li>The support class contains the logic to check that the values read with gson do not break class specifications.</li>
 *          <li>If the check is successful, the support class creates and returns the object of the actual class using the appropriate constructors.</li>
 *      </ol></li>
 * </ul>
 */
public class CardJsonSerializerDebug {
    //OBJECT SERIALIZER
    /**
     * Method serializes all polymorphisms of Card objects into Json strings using gson library.
     * @param card Card object to be serialized.
     * @return Json String of serialized card.
     */
    public static String serializeCard(Card card){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()//Needed to correctly serialize Map<CornerOrientation, Corner>.
                .setPrettyPrinting()
                .create();

        return gson.toJson(card);
    }

    //LIST SERIALIZER

    /**
     * Method serializes List of Card objects into Json strings of Cards separated by CARD_OVER keyword.<br>
     * Method uses serializeCard to serialize individual List entries and joins them using CARD_OVER as separator keyword.
     * @param cardList  List of cards to be serialized.
     * @return  Json String of serialized List.
     */
    public static String serializeCardList(List<Card> cardList){
        StringBuilder jsonResult = new StringBuilder();

        for(int i =0; i< cardList.size(); i++){
            if(i > 0)
                jsonResult.append("CARD_OVER\n");
            jsonResult.append(serializeCard(cardList.get(i)));
        }

        return jsonResult.toString();
    }





    //OBJECT DESERIALIZERS

    /**
     * Method deserializes Json strings into CardResource objects Typecast to Card type.<br>
     * @param string String containing Json to deserialize.
     * @return  CardResource object Typecast to Card deserialized from input string.
     */
    public static Card deserializeCardResource(String string){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        gson.getAdapter(CardResourceSupportClass.class);
        CardResourceSupportClass cardSupport = gson.fromJson(string, CardResourceSupportClass.class);

        return cardSupport.createCardResource();
    }

    /**
     * Method deserializes Json strings into CardGolden objects Typecast to Card type.<br>
     * @param string String containing Json to deserialize.
     * @return  CardGolden object Typecast to Card deserialized from input string.
     */
    public static Card deserializeCardGolden(String string){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        gson.getAdapter(CardGoldenSupportClass.class);
        CardGoldenSupportClass cardSupport = gson.fromJson(string, CardGoldenSupportClass.class);

        return cardSupport.createGoldenCard();
    }

    /**
     * Method deserializes Json strings into CardStarter objects Typecast to Card type.<br>
     * @param string String containing Json to deserialize.
     * @return  CardStarter object Typecast to Card deserialized from input string.
     */
    public static Card deserializeCardStarter(String string){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        gson.getAdapter(CardStarterSupportClass.class);
        CardStarterSupportClass cardSupport = gson.fromJson(string, CardStarterSupportClass.class);

        return cardSupport.createCardStarter();
    }





    //LIST DESERIALIZERS

    /**
     * Method splits provided string using CARD_OVER separator keyword and feeds the substrings to deserializeCardResource().
     * Then it puts the serialized cards into a Map and returns the map.
     * @param jsonString    String containing Json to deserialize.
     * @return  List containing Resource Cards deserialized from input string.
     */
    public static List<Card> deserializeCardResourceList(String jsonString){
        if(jsonString == null)
            throw new IllegalArgumentException("Provided string is null.");

        List<Card> cardList = new ArrayList<>();

        String[] cardStrings = jsonString.split("CARD_OVER");

        int i = 0;
        for (String cardString : cardStrings) {
            try {
                if (cardStrings.length > 1 && cardString.trim().isEmpty())
                    throw new JsonSyntaxException("Missing card object after CARD_OVER");

                cardList.add(deserializeCardResource(cardString.trim()));
            }
            catch (IllegalArgumentException m){
                throw new IllegalArgumentException("Exception thrown reading card number "+i+":\n"+m);
            }
            catch (JsonSyntaxException m){
                throw new JsonSyntaxException("Exception thrown reading card number "+i+":\n"+m);
            }
            catch (NullPointerException m){
                throw new NullPointerException("Exception thrown reading card number "+i+":\n"+m);
            }

            i++;
        }

    return cardList;
    }

    /**
     * Method splits provided string using CARD_OVER separator keyword and feeds the substrings to deserializeCardGolden().
     * Then it puts the serialized cards into a Map and returns the map.
     * @param jsonString    String containing Json to deserialize.
     * @return  List containing Golden Cards deserialized from input string.
     */
    public static List<Card> deserializeCardGoldenList(String jsonString){
        if(jsonString == null)
            throw new IllegalArgumentException("Provided string is null.");

        List<Card> cardList = new ArrayList<>();

        String[] cardStrings = jsonString.split("CARD_OVER");

        int i = 0;
        for (String cardString : cardStrings) {
            try {


                if (cardStrings.length > 1 && cardString.trim().isEmpty())
                    throw new JsonSyntaxException("Missing card object after CARD_OVER");

                cardList.add(deserializeCardGolden(cardString.trim()));
            }
            catch (IllegalArgumentException m){
                throw new IllegalArgumentException("Exception thrown reading card number "+i+":\n"+m);
            }
            catch (JsonSyntaxException m){
                throw new JsonSyntaxException("Exception thrown reading card number "+i+":\n"+m);
            }
            catch (NullPointerException m){
                throw new NullPointerException("Exception thrown reading card number "+i+":\n"+m);
            }

            i++;
        }

        return cardList;
    }

    /**
     * Method splits provided string using CARD_OVER separator keyword and feeds the substrings to deserializeCardStarter().
     * Then it puts the serialized cards into a Map and returns the map.
     * @param jsonString    String containing Json to deserialize.
     * @return  List containing Starter Cards deserialized from input string.
     */
    public static List<Card> deserializeCardStarterList(String jsonString){
        if(jsonString == null)
            throw new IllegalArgumentException("Provided string is null.");

        List<Card> cardList = new ArrayList<>();

        String[] cardStrings = jsonString.split("CARD_OVER");

        int i = 0;
        for (String cardString : cardStrings) {
            try{
                if(cardStrings.length > 1 && cardString.trim().isEmpty())
                    throw new JsonSyntaxException("Missing card object after CARD_OVER");

                cardList.add(deserializeCardStarter(cardString.trim()));
            }
            catch (IllegalArgumentException m){
                throw new IllegalArgumentException("Exception thrown reading card number "+i+":\n"+m);
            }
            catch (JsonSyntaxException m){
                throw new JsonSyntaxException("Exception thrown reading card number "+i+":\n"+m);
            }
            catch (NullPointerException m){
                throw new NullPointerException("Exception thrown reading card number "+i+":\n"+m);
            }

            i++;
        }

        return cardList;
    }
}
