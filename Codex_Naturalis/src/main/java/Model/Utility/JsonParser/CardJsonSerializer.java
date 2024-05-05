package Model.Utility.JsonParser;

import Model.Cards.Card;
import Model.Cards.CardGolden;
import Model.Cards.CardResource;
import Model.Cards.CardStarter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * A utility class for serializing and deserializing {@link Model.Cards.Card} objects and lists of Cards
 * to and from JSON format using the Gson library.<br>
 * Serialization is achieved with one method for all Card classes, de-serialization has to be done using the method for the
 * specific dynamic type.
 */
public class CardJsonSerializer {
    //OBJECT SERIALIZER
    /**
     * Serializes a single {@link Model.Cards.Card} object to JSON format.
     *
     * @param card The card to be serialized.
     * @return The JSON representation of the card.
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
     * Serializes a list of {@link Model.Cards.Card} objects to JSON format.
     *
     * @param cardList The list of cards to be serialized.
     * @return The JSON representation of the list of cards.
     */
    public static String serializeCardList(List<Card> cardList){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        return gson.toJson(cardList);
    }





    //OBJECT DESERIALIZER
    /**
     * Deserializes a JSON string into a {@link Model.Cards.CardResource} object.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized {@link Model.Cards.CardResource} object.
     */
    public static CardResource deserializeCardResource(String string){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        return gson.fromJson(string, CardResource.class);
    }

    /**
     * Deserializes a JSON string into a {@link Model.Cards.CardGolden} object.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized {@link Model.Cards.CardGolden} object.
     */
    public static CardGolden deserializeCardGolden(String string){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        return gson.fromJson(string, CardGolden.class);
    }

    /**
     * Deserializes a JSON string into a {@link Model.Cards.CardStarter} object.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized {@link Model.Cards.CardStarter} object.
     */
    public static CardStarter deserializeCardStarter(String string){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        return gson.fromJson(string, CardStarter.class);
    }





    //LIST DESERIALIZER
    /**
     * Deserializes a JSON string into a list of {@link Model.Cards.CardResource} objects.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized list of {@link Model.Cards.CardResource} objects.
     */
    public static List<CardResource> deserializeCardResourceList(String string){
        Type CardResourceList = new TypeToken<List<CardResource>>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(string, CardResourceList);
    }

    /**
     * Deserializes a JSON string into a list of {@link Model.Cards.CardGolden} objects.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized list of {@link Model.Cards.CardGolden} objects.
     */
    public static List<CardGolden> deserializeCardGoldenList(String string){
        Type CardGoldenList = new TypeToken<List<CardGolden>>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(string, CardGoldenList);
    }

    /**
     * Deserializes a JSON string into a list of {@link Model.Cards.CardStarter} objects.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized list of {@link Model.Cards.CardStarter} objects.
     */
    public static List<CardStarter> deserializeCardStarterList(String string){
        Type CardStarterList = new TypeToken<List<CardStarter>>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(string, CardStarterList);
    }
}
