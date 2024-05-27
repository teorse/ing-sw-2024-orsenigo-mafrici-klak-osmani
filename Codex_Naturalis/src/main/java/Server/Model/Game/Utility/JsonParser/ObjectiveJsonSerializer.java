package Server.Model.Game.Utility.JsonParser;

import Server.Model.Game.Objectives.Objective;
import Server.Model.Game.Objectives.ObjectiveGeometric;
import Server.Model.Game.Objectives.ObjectiveNumeric;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A utility class for serializing and deserializing {@link Objective} objects and lists of objectives to and from JSON format.
 */
public class ObjectiveJsonSerializer {
    //OBJECT SERIALIZER
    /**
     * Serializes a single {@link Objective} object to JSON format.
     *
     * @param objective The objective to be serialized.
     * @return The JSON representation of the objective.
     */
    public static String serializeObjective(Objective objective){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()//Needed to correctly serialize Map<CornerOrientation, Corner>.
                .setPrettyPrinting()
                .create();

        return gson.toJson(objective);
    }

    //LIST SERIALIZER
    /**
     * Serializes a list of {@link Objective} objects to JSON format.
     *
     * @param objectives The list of objectives to be serialized.
     * @return The JSON representation of the list of objectives.
     */
    public static String serializeObjectiveList(List<Objective> objectives){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        return gson.toJson(objectives);
    }





    //OBJECT DESERIALIZERS
    /**
     * Deserializes a JSON string into an {@link ObjectiveNumeric} object.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized {@link ObjectiveNumeric} object.
     */
    public static ObjectiveNumeric deserializeObjectiveNumeric(String string){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        gson.getAdapter(ObjectiveNumeric.class);
        return gson.fromJson(string, ObjectiveNumeric.class);
    }

    /**
     * Deserializes a JSON string into an {@link ObjectiveGeometric} object.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized {@link ObjectiveGeometric} object.
     */
    public static ObjectiveGeometric deserializeObjectiveGeometric(String string){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        gson.getAdapter(ObjectiveGeometric.class);
        return gson.fromJson(string, ObjectiveGeometric.class);
    }





    //LIST DESERIALIZER
    /**
     * Deserializes a JSON string into a list of {@link ObjectiveNumeric} objects.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized list of {@link ObjectiveNumeric} objects.
     */
    public static List<ObjectiveNumeric> deserializeObjectiveNumericList(String string){
        Type ObjectiveNumericList = new TypeToken<List<ObjectiveNumeric>>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(string, ObjectiveNumericList);
    }

    /**
     * Deserializes a JSON string into a list of {@link ObjectiveGeometric} objects.
     *
     * @param string The JSON string to be deserialized.
     * @return The deserialized list of {@link ObjectiveGeometric} objects.
     */
    public static List<ObjectiveGeometric> deserializeObjectiveGeometricList(String string){
        Type ObjectiveGeometricList = new TypeToken<List<ObjectiveGeometric>>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(string, ObjectiveGeometricList);
    }
}
