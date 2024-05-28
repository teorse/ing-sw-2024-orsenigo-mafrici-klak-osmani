package it.polimi.ingsw.Server.Model.Game.Objectives;

import it.polimi.ingsw.Server.Model.Game.Cards.Card;
import it.polimi.ingsw.Server.Model.Game.Objectives.ObjectiveGeometric;
import it.polimi.ingsw.Server.Model.Game.Player.CardMap;
import it.polimi.ingsw.Server.Model.Game.Player.CardVisibility;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;
import it.polimi.ingsw.Utils.JsonParser.CardJsonSerializerDebug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveGeometricTest {

        public class Scenario{
            CardMap cardMap;
            List<List<Coordinates>> pattern;
            List<List<Coordinates>> expectedResult;

            public List<List<Coordinates>> getExpectedResult() {
                return expectedResult;
            }

            public void setExpectedResult(List<List<Coordinates>> expectedResult) {
                this.expectedResult = expectedResult;
            }

            ObjectiveGeometric objectiveGeometric;

            public ObjectiveGeometric getObjectiveGeometric() {
                return objectiveGeometric;
            }

            public void setObjectiveGeometric(ObjectiveGeometric objectiveGeometric) {
                this.objectiveGeometric = objectiveGeometric;
            }

            public CardMap getCardMap() {
                return cardMap;
            }

            public void setCardMap(CardMap cardMap) {
                this.cardMap = cardMap;
            }

            public List<List<Coordinates>> getPattern() {
                return pattern;
            }

            public void setPattern(List<List<Coordinates>> pattern) {
                this.pattern = pattern;
            }
        }

        Scenario scenario1 = new Scenario();
        Scenario scenario2 = new Scenario();
        Scenario scenario3 = new Scenario();
        Scenario scenario4 = new Scenario();

        @BeforeEach
        void setUp() {
            scenario1();
            scenario2();
            scenario3();
            scenario4();

        }

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




        void scenario1(){
            Map<Coordinates,CardVisibility> cardsToBePlaced = new HashMap<>();
            List<Coordinates> listOfCoordinatesToBeUsed = new ArrayList<>();
            Map<Artifacts, Integer> artifactsCounter = new HashMap<>();
            String cardJsonPath = "src/main/resources/CardDecks/CardResourceDeck.txt";
            String cardJson;
            try {
                cardJson = Files.readString(Path.of(cardJsonPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<Card> cards = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);
            List<Artifacts> testArtifacts = new ArrayList<>();
            List<Card> testCards = new ArrayList<>();
            for (Card card : cards){
                if (!testArtifacts.contains(card.getCardColor())){
                    testCards.add(card);
                    testArtifacts.add(card.getCardColor());
                }
            }

            listOfCoordinatesToBeUsed.add(new Coordinates(0,0));
            listOfCoordinatesToBeUsed.add(new Coordinates(1,1));
            listOfCoordinatesToBeUsed.add(new Coordinates(2,2));
            listOfCoordinatesToBeUsed.add(new Coordinates(3,3));


            for(int i = 0; i<4; i++){
                CardVisibility cardVisibility = new CardVisibility(testCards.get(i),true);
                cardsToBePlaced.put(listOfCoordinatesToBeUsed.get(i),cardVisibility);
                //System.out.println(testCards.get(i).getCardColor() + " " + listOfCoordinatesToBeUsed.get(i));
            }

            artifactsCounter.put(Artifacts.ANIMAL,1);
            artifactsCounter.put(Artifacts.FUNGI,1);
            artifactsCounter.put(Artifacts.PLANT,1);
            artifactsCounter.put(Artifacts.INSECT,1);



            Map<Coordinates,Artifacts> pattern = new HashMap<>();
            pattern.put(new Coordinates(0,0), Artifacts.FUNGI);
            pattern.put(new Coordinates(1,1) , Artifacts.PLANT);
            pattern.put(new Coordinates(2,2) , Artifacts.ANIMAL);

            ObjectiveGeometric geometricObjective = new ObjectiveGeometric("Generic Geometric Card" , 3 , pattern);

            List<List<Coordinates>> listOfPatternCoordinates = new ArrayList<>();
            List<Coordinates> patternCoordinates = new ArrayList<>(pattern.keySet());

            listOfPatternCoordinates.add(patternCoordinates);

            scenario1.setCardMap(CardMapReflectionBuilder(cardsToBePlaced,artifactsCounter, listOfCoordinatesToBeUsed));
            scenario1.setPattern(listOfPatternCoordinates);
            scenario1.setObjectiveGeometric(geometricObjective);
            scenario1.setExpectedResult(listOfPatternCoordinates);


        }
        void scenario2(){
            Map<Coordinates,CardVisibility> cardsToBePlaced = new HashMap<>();
            List<Coordinates> listOfCoordinatesToBeUsed = new ArrayList<>();
            Map<Artifacts, Integer> artifactsCounter = new HashMap<>();
            String cardJsonPath = "src/test/java/it/polimi/ingsw/Server/Model/Game/Logic/Resources/RandomCardList.txt";
            String cardJson;
            try {
                cardJson = Files.readString(Path.of(cardJsonPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<Card> cards = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);


            listOfCoordinatesToBeUsed.add(new Coordinates(0,0));
            listOfCoordinatesToBeUsed.add(new Coordinates(1,1));
            listOfCoordinatesToBeUsed.add(new Coordinates(2,2));
            listOfCoordinatesToBeUsed.add(new Coordinates(3,3));
            listOfCoordinatesToBeUsed.add(new Coordinates(-1,-1));
            listOfCoordinatesToBeUsed.add(new Coordinates(-1,-2));



            for(int i = 0; i<6; i++){
                CardVisibility cardVisibility = new CardVisibility(cards.get(i),true);
                cardsToBePlaced.put(listOfCoordinatesToBeUsed.get(i),cardVisibility);
                //System.out.println(cards.get(i).getCardColor() + " " + listOfCoordinatesToBeUsed.get(i));
            }

            artifactsCounter.put(Artifacts.ANIMAL,1);
            artifactsCounter.put(Artifacts.FUNGI,1);
            artifactsCounter.put(Artifacts.PLANT,1);
            artifactsCounter.put(Artifacts.INSECT,1);



            Map<Coordinates,Artifacts> pattern = new HashMap<>();
            pattern.put(new Coordinates(0,0), Artifacts.FUNGI);
            pattern.put(new Coordinates(-1,-1) , Artifacts.ANIMAL);
            pattern.put(new Coordinates(-1,-2) , Artifacts.ANIMAL);

            ObjectiveGeometric geometricObjective = new ObjectiveGeometric("Generic Geometric Card" , 3 , pattern);

            List<List<Coordinates>> listOfPatternCoordinates = new ArrayList<>();
            List<Coordinates> patternCoordinates = new ArrayList<>(pattern.keySet());

            listOfPatternCoordinates.add(patternCoordinates);

            scenario2.setCardMap(CardMapReflectionBuilder(cardsToBePlaced,artifactsCounter, listOfCoordinatesToBeUsed));
            scenario2.setPattern(listOfPatternCoordinates);
            scenario2.setObjectiveGeometric(geometricObjective);
            scenario2.setExpectedResult(listOfPatternCoordinates);
        }
        void scenario3(){
        Map<Coordinates,CardVisibility> cardsToBePlaced = new HashMap<>();
        List<Coordinates> listOfCoordinatesToBeUsed = new ArrayList<>();
        Map<Artifacts, Integer> artifactsCounter = new HashMap<>();
        String cardJsonPath = "src/test/java/it/polimi/ingsw/Server/Model/Game/Logic/Resources/Scenario3.txt";
        String cardJson;
        try {
            cardJson = Files.readString(Path.of(cardJsonPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Card> cards = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);


        listOfCoordinatesToBeUsed.add(new Coordinates(0,0));
        listOfCoordinatesToBeUsed.add(new Coordinates(1,1));
        listOfCoordinatesToBeUsed.add(new Coordinates(2,2));
        listOfCoordinatesToBeUsed.add(new Coordinates(3,3));
        listOfCoordinatesToBeUsed.add(new Coordinates(4,4));
        listOfCoordinatesToBeUsed.add(new Coordinates(5,5));



        for(int i = 0; i<6; i++){
            CardVisibility cardVisibility = new CardVisibility(cards.get(i),true);
            cardsToBePlaced.put(listOfCoordinatesToBeUsed.get(i),cardVisibility);
            //System.out.println(cards.get(i).getCardColor() + " " + listOfCoordinatesToBeUsed.get(i));
        }


        artifactsCounter.put(Artifacts.ANIMAL,1);
        artifactsCounter.put(Artifacts.FUNGI,1);
        artifactsCounter.put(Artifacts.PLANT,1);
        artifactsCounter.put(Artifacts.INSECT,1);



        Map<Coordinates,Artifacts> pattern1 = new HashMap<>();
        pattern1.put(new Coordinates(0,0), Artifacts.FUNGI);
        pattern1.put(new Coordinates(1,1) , Artifacts.FUNGI);
        pattern1.put(new Coordinates(2,2) , Artifacts.PLANT);



        ObjectiveGeometric geometricObjective = new ObjectiveGeometric("Generic Geometric Card" , 3 , pattern1);

        List<List<Coordinates>> listOfPatternCoordinates = new ArrayList<>();

        List<Coordinates> pattern2 = new ArrayList<>();
        pattern2.add(new Coordinates(3,3));
        pattern2.add(new Coordinates(4,4));
        pattern2.add(new Coordinates(5,5));

        List<Coordinates> pattern1Coords = new ArrayList<>(pattern1.keySet());



        listOfPatternCoordinates.add(pattern1Coords);
        listOfPatternCoordinates.add(pattern2);









        scenario3.setCardMap(CardMapReflectionBuilder(cardsToBePlaced,artifactsCounter, listOfCoordinatesToBeUsed));
        scenario3.setPattern(listOfPatternCoordinates);
        scenario3.setObjectiveGeometric(geometricObjective);
        scenario3.setExpectedResult(listOfPatternCoordinates);
    }
        void scenario4(){
            Map<Coordinates,CardVisibility> cardsToBePlaced = new HashMap<>();
            List<Coordinates> listOfCoordinatesToBeUsed = new ArrayList<>();
            Map<Artifacts, Integer> artifactsCounter = new HashMap<>();
            String cardJsonPath = "src/main/resources/CardDecks/CardResourceDeck.txt";
            String cardJson;
            try {
                cardJson = Files.readString(Path.of(cardJsonPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<Card> cards = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);
            List<Artifacts> testArtifacts = new ArrayList<>();
            List<Card> testCards = new ArrayList<>();
            for (Card card : cards){
                if (!testArtifacts.contains(card.getCardColor())){
                    testCards.add(card);
                    testArtifacts.add(card.getCardColor());
                }
            }

            listOfCoordinatesToBeUsed.add(new Coordinates(0,0));
            listOfCoordinatesToBeUsed.add(new Coordinates(1,1));
            listOfCoordinatesToBeUsed.add(new Coordinates(2,2));
            listOfCoordinatesToBeUsed.add(new Coordinates(3,3));


            for(int i = 0; i<4; i++){
                CardVisibility cardVisibility = new CardVisibility(testCards.get(i),true);
                cardsToBePlaced.put(listOfCoordinatesToBeUsed.get(i),cardVisibility);
                //System.out.println(testCards.get(i).getCardColor() + " " + listOfCoordinatesToBeUsed.get(i));
            }

            artifactsCounter.put(Artifacts.ANIMAL,1);
            artifactsCounter.put(Artifacts.FUNGI,1);
            artifactsCounter.put(Artifacts.PLANT,1);
            artifactsCounter.put(Artifacts.INSECT,1);



            Map<Coordinates,Artifacts> pattern = new HashMap<>();
            pattern.put(new Coordinates(0,0), Artifacts.FUNGI);
            pattern.put(new Coordinates(1,2) , Artifacts.ANIMAL);
            pattern.put(new Coordinates(2,3) , Artifacts.PLANT);

            ObjectiveGeometric geometricObjective = new ObjectiveGeometric("Generic Geometric Card" , 3 , pattern);

            List<List<Coordinates>> listOfPatternCoordinates = new ArrayList<>();
            List<Coordinates> patternCoordinates = new ArrayList<>(pattern.keySet());
            List<List<Coordinates>> expectedResult = new ArrayList<>();
            listOfPatternCoordinates.add(patternCoordinates);

            scenario4.setCardMap(CardMapReflectionBuilder(cardsToBePlaced,artifactsCounter, listOfCoordinatesToBeUsed));
            scenario4.setPattern(listOfPatternCoordinates);
            scenario4.setObjectiveGeometric(geometricObjective);
            scenario4.setExpectedResult(expectedResult);
        }


        @Test
        void countPoints() {

         assertEquals(3,scenario1.getObjectiveGeometric().countPoints(scenario1.getCardMap()), "Test 1 Failed");
         assertEquals(3,scenario2.getObjectiveGeometric().countPoints(scenario2.getCardMap()), "Test 2 Failed");
         assertEquals(6,scenario3.getObjectiveGeometric().countPoints(scenario3.getCardMap()), "Test 3 Failed");
        assertEquals(0,scenario4.getObjectiveGeometric().countPoints(scenario4.getCardMap()), "Test 3 Failed");

    }
}





