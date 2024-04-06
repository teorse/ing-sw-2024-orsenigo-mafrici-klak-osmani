package Model.Utility.JsonParser;

import Model.Cards.Card;
import Model.Cards.CardGolden;
import Model.Cards.CardResource;
import Model.Cards.CardStarter;
import Model.Utility.JsonParser.SupportClasses.CardResourceSupportClass;
import Model.Utility.JsonParser.SupportClasses.CardGoldenSupportClass;
import Model.Utility.JsonParser.SupportClasses.CardStarterSupportClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class contains static methods to serialize Card->Json and to deserialize Json->Card Objects using the gson library.<br>
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
public class CardJsonSerializer {
    //SERIALIZER
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





    //DESERIALIZERS

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
}
