package Server.Model.Game.Objectives;

import Server.Model.Game.Player.CardMap;
import Server.Model.Game.Player.CardVisibility;
import Server.Model.Game.Utility.Artifacts;
import Server.Model.Game.Utility.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveNumericTest {

    public CardMap CardMapReflectionBuilder(Map<Coordinates, CardVisibility> cardsPlacedValue, Map<Artifacts, Integer> artifactsCounterValue, List<Coordinates> coordinatesPlacedValue){
        //Creating using the usual constructor a normal CardMap object.
        CardMap cardMap = new CardMap(null, "");

        //Defining two field type object
        Field field0;
        Field field1;
        Field field2;

        try{
            //field0 now will be able to access the attribute "cardsPlaced" inside objects of the CardMap class.
            field0 = CardMap.class.getDeclaredField("cardsPlaced");
            //setting the accessibility of the field to true to allow us to use getters and setters.
            field0.setAccessible(true);

            //Using the set method in the following way:
            //field0 is the field we want to interact with, in this case it is cardsPlaced as defined above.
            //field0.set(object inside which we want to apply the set, value we want to set the attribute to)
            field0.set(cardMap, cardsPlacedValue);



            //field1 now will be able to access the attribute "artifactsCounter" inside objects of the CardMap class.
            field1 = CardMap.class.getDeclaredField("artifactsCounter");
            //setting the accessibility of the field to true to allow us to use getters and setters.
            field1.setAccessible(true);

            //Using the set method in the following way:
            //field1 is the field we want to interact with, in this case it is artifactsCounter as defined above.
            //field1.set(object inside which we want to apply the set, value we want to set the attribute to)
            field1.set(cardMap, artifactsCounterValue);



            //field2 now will be able to access the attribute "coordinatesPlaced" inside objects of the CardMap class.
            field2 = CardMap.class.getDeclaredField("coordinatesPlaced");
            //setting the accessibility of the field to true to allow us to use getters and setters.
            field2.setAccessible(true);

            //Using the set method in the following way:
            //field2 is the field we want to interact with, in this case it is coordinatesPlaced as defined above.
            //field2.set(object inside which we want to apply the set, value we want to set the attribute to)
            field2.set(cardMap, coordinatesPlacedValue);
        }
        catch (NoSuchFieldException | IllegalAccessException m){
            System.out.println(m);
        }

        //returning the object inside which we set the new values.
        return cardMap;
    }

    public class Scenario{
        int expectedValue;
        int actualValue;
    }

    Scenario scenario1 = new Scenario();
    Scenario scenario2 = new Scenario();
    Scenario scenario3 = new Scenario();
    Scenario scenario4 = new Scenario();
    Scenario scenario5 = new Scenario();



    @BeforeEach
    void setUp() {

        scenario1();
        scenario2();
        scenario3();
        scenario4();
        scenario5();
    }

    void scenario1(){


        Map<Coordinates, CardVisibility> cardsToBePlaced = new HashMap<>();
        Map<Artifacts, Integer> artifactsIntegerReflection = new HashMap<>();
        List<Coordinates> coordinatesToBePlaced = new ArrayList<>();
        CardMap cardMap = CardMapReflectionBuilder(cardsToBePlaced,artifactsIntegerReflection,coordinatesToBePlaced);

        cardMap.changeArtifactAmount(Artifacts.ANIMAL, 5);
        cardMap.changeArtifactAmount(Artifacts.FUNGI,3);
        cardMap.changeArtifactAmount(Artifacts.INSECT, 6);
        cardMap.changeArtifactAmount(Artifacts.PLANT,8);

        Map<Artifacts,Integer> requiredItems = new HashMap<>();
        requiredItems.put(Artifacts.INSECT, 3);

        ObjectiveNumeric objective = new ObjectiveNumeric("Generic Item Objective", 2, requiredItems);
        scenario1.expectedValue = 4;
        scenario1.actualValue = objective.countPoints(cardMap);

    }

    void scenario2(){


        Map<Coordinates, CardVisibility> cardsToBePlaced = new HashMap<>();
        Map<Artifacts, Integer> artifactsIntegerReflection = new HashMap<>();
        List<Coordinates> coordinatesToBePlaced = new ArrayList<>();
        CardMap cardMap = CardMapReflectionBuilder(cardsToBePlaced,artifactsIntegerReflection,coordinatesToBePlaced);

        cardMap.changeArtifactAmount(Artifacts.ANIMAL, 5);
        cardMap.changeArtifactAmount(Artifacts.FUNGI,3);
        cardMap.changeArtifactAmount(Artifacts.INSECT, 6);
        cardMap.changeArtifactAmount(Artifacts.PLANT,8);

        Map<Artifacts,Integer> requiredItems = new HashMap<>();
        requiredItems.put(Artifacts.INSECT, 1);
        requiredItems.put(Artifacts.ANIMAL, 1);
        requiredItems.put(Artifacts.FUNGI, 1);

        ObjectiveNumeric objective = new ObjectiveNumeric("Generic Item Objective", 2, requiredItems);
        scenario2.expectedValue = 6;
        scenario2.actualValue = objective.countPoints(cardMap);

    }

    void scenario3(){


        Map<Coordinates, CardVisibility> cardsToBePlaced = new HashMap<>();
        Map<Artifacts, Integer> artifactsIntegerReflection = new HashMap<>();
        List<Coordinates> coordinatesToBePlaced = new ArrayList<>();
        CardMap cardMap = CardMapReflectionBuilder(cardsToBePlaced,artifactsIntegerReflection,coordinatesToBePlaced);



        Map<Artifacts,Integer> requiredItems = new HashMap<>();
        requiredItems.put(Artifacts.FUNGI, 3);


        ObjectiveNumeric objective = new ObjectiveNumeric("Generic Item Objective", 2, requiredItems);
        scenario3.expectedValue = 0;
        scenario3.actualValue = objective.countPoints(cardMap);

    }

    void scenario4(){

        Map<Coordinates, CardVisibility> cardsToBePlaced = new HashMap<>();
        Map<Artifacts, Integer> artifactsIntegerReflection = new HashMap<>();
        List<Coordinates> coordinatesToBePlaced = new ArrayList<>();
        CardMap cardMap = CardMapReflectionBuilder(cardsToBePlaced,artifactsIntegerReflection,coordinatesToBePlaced);

        cardMap.changeArtifactAmount(Artifacts.ANIMAL, 5);
        cardMap.changeArtifactAmount(Artifacts.FUNGI,3);
        cardMap.changeArtifactAmount(Artifacts.INSECT, 6);
        cardMap.changeArtifactAmount(Artifacts.PLANT,8);

        Map<Artifacts,Integer> requiredItems = new HashMap<>();
        requiredItems.put(Artifacts.INSECT, 2);


        ObjectiveNumeric objective = new ObjectiveNumeric("Generic Item Objective", 2, requiredItems);
        scenario4.expectedValue = 6;
        scenario4.actualValue = objective.countPoints(cardMap);
    }

    void scenario5(){

        Map<Coordinates, CardVisibility> cardsToBePlaced = new HashMap<>();
        Map<Artifacts, Integer> artifactsIntegerReflection = new HashMap<>();
        List<Coordinates> coordinatesToBePlaced = new ArrayList<>();
        CardMap cardMap = CardMapReflectionBuilder(cardsToBePlaced,artifactsIntegerReflection,coordinatesToBePlaced);

        cardMap.changeArtifactAmount(Artifacts.ANIMAL, 5);
        cardMap.changeArtifactAmount(Artifacts.FUNGI,3);
        cardMap.changeArtifactAmount(Artifacts.INSECT, 6);
        cardMap.changeArtifactAmount(Artifacts.PLANT,8);
        cardMap.changeArtifactAmount(Artifacts.INKWELL,3);
        cardMap.changeArtifactAmount(Artifacts.MANUSCRIPT,2);
        cardMap.changeArtifactAmount(Artifacts.QUILL,4);

        Map<Artifacts,Integer> requiredItems = new HashMap<>();
        requiredItems.put(Artifacts.INKWELL, 1);
        requiredItems.put(Artifacts.MANUSCRIPT, 1);
        requiredItems.put(Artifacts.QUILL, 1);


        ObjectiveNumeric objective = new ObjectiveNumeric("Generic Item Objective", 3, requiredItems);
        scenario5.expectedValue = 6;
        scenario5.actualValue = objective.countPoints(cardMap);
    }


    @Test
    void countPoints() {

        assertEquals(scenario1.expectedValue, scenario1.actualValue, "Test 1 Failed");
        assertEquals(scenario2.expectedValue, scenario2.actualValue, "Test 2 Failed");
        assertEquals(scenario3.expectedValue, scenario3.actualValue, "Test 3 Failed");
        assertEquals(scenario4.expectedValue, scenario4.actualValue, "Test 4 Failed");
        assertEquals(scenario5.expectedValue, scenario5.actualValue, "Test 5 Failed");

    }
}