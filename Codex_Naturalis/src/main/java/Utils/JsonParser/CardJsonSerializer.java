package Utils.JsonParser;

import Server.Model.Game.Cards.Card;
import Server.Model.Game.Cards.CardGolden;
import Server.Model.Game.Cards.CardResource;
import Server.Model.Game.Cards.CardStarter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * A utility class for serializing and deserializing {@link Card} objects and lists of Cards
 * to and from JSON format using the Gson library.<br>
 * Serialization is achieved with one method for all Card classes, de-serialization has to be done using the method for the
 * specific dynamic type.
 */
public class CardJsonSerializer {
    //OBJECT SERIALIZER
    /**
     * Serializes a single {@link Card} object to JSON format.
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
     * Serializes a list of {@link Card} objects to JSON format.
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
     * Deserializes a JSON string into a {@link CardResource} object.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized {@link CardResource} object.
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
     * Deserializes a JSON string into a {@link CardGolden} object.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized {@link CardGolden} object.
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
     * Deserializes a JSON string into a {@link CardStarter} object.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized {@link CardStarter} object.
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
     * Deserializes a JSON string into a list of {@link CardResource} objects.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized list of {@link CardResource} objects.
     */
    public static List<CardResource> deserializeCardResourceList(String string){
        Type CardResourceList = new TypeToken<List<CardResource>>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(string, CardResourceList);
    }

    /**
     * Deserializes a JSON string into a list of {@link CardGolden} objects.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized list of {@link CardGolden} objects.
     */
    public static List<CardGolden> deserializeCardGoldenList(String string){
        Type CardGoldenList = new TypeToken<List<CardGolden>>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(string, CardGoldenList);
    }

    /**
     * Deserializes a JSON string into a list of {@link CardStarter} objects.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized list of {@link CardStarter} objects.
     */
    public static List<CardStarter> deserializeCardStarterList(String string){
        Type CardStarterList = new TypeToken<List<CardStarter>>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(string, CardStarterList);
    }
}
