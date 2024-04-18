package Model.Player;

import Model.Cards.*;
import Model.Utility.Artifacts;

import Model.Utility.Coordinates;
import Model.Utility.JsonParser.CardJsonSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardMapTest {

    @Nested
    public class PlaceTest {
        //DATA
        private CardMap cardMap;
        private List<Card> resourceCards = new ArrayList<>();
        private List<Card> goldenCards = new ArrayList<>();
        private List<Card> starterCards = new ArrayList<>();
        private List<Coordinates> expectedCoordinatesPlaced = new ArrayList<>();
        private List<Coordinates> expectedAvailablePlacements = new ArrayList<>();

        @BeforeEach
        void setUp() {

            cardMap = new CardMap();

            String resourceCardsJson = "";
            String goldenCardsJson = "";
            String starterCardsJson = "";

            try{
                resourceCardsJson = Files.readString(Path.of("src/main/resources/CardDecks/CardResourceDeck.txt"));
                goldenCardsJson = Files.readString(Path.of("src/main/resources/CardDecks/CardGoldenDeck.txt"));
                starterCardsJson = Files.readString(Path.of("src/main/resources/CardDecks/CardStarterDeck.txt"));
            }
            catch (IOException m){
                System.out.println(m);
            }

            resourceCards = CardJsonSerializer.deserializeCardResourceList(resourceCardsJson);
            goldenCards = CardJsonSerializer.deserializeCardGoldenList(goldenCardsJson);
            starterCards = CardJsonSerializer.deserializeCardStarterList(starterCardsJson);
        }

        @Test
        void placeStarterCard() {

            cardMap.place(starterCards.getFirst(), 0, true);

            //coordinatesPlaced is [(0;0)]
            expectedCoordinatesPlaced.add(new Coordinates(0, 0));

            //availablePlacements is [(-1;1), (1;1), (1;-1), (-1;-1)]
            expectedAvailablePlacements.add(new Coordinates(-1, 1)); //NW
            expectedAvailablePlacements.add(new Coordinates(1, 1)); //NE
            expectedAvailablePlacements.add(new Coordinates(1, -1)); //SE
            expectedAvailablePlacements.add(new Coordinates(-1, -1)); //SW

            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.PLANT),
                    "First calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                    "Second calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.INSECT),
                    "Third calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.FUNGI),
                    "Fourth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.MANUSCRIPT),
                    "Fifth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.QUILL),
                    "Sixth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.INKWELL),
                    "Seventh calibration test failed");
            assertEquals(expectedCoordinatesPlaced, cardMap.getCoordinatesPlaced(),
                    "Eighth calibration test failed");
            assertEquals(expectedAvailablePlacements, cardMap.getAvailablePlacements(),
                    "Ninth calibration test failed");
        }

        @Test
        void placeTestResourceCard() {

            cardMap.place(starterCards.getFirst(), 0, true);
            cardMap.place(resourceCards.getFirst(), 1, true);
            //coordinatesPlaced is [(0;0), (1;1)]
            expectedCoordinatesPlaced.add(new Coordinates(0, 0));
            expectedCoordinatesPlaced.add(new Coordinates(1, 1));
            //availablePlacements is [(-1;1), (1;-1), (-1;-1), (0;2), (2;2)]
            expectedAvailablePlacements.add(new Coordinates(-1, 1));
            expectedAvailablePlacements.add(new Coordinates(1, -1));
            expectedAvailablePlacements.add(new Coordinates(-1, -1));
            expectedAvailablePlacements.add(new Coordinates(0, 2));
            expectedAvailablePlacements.add(new Coordinates(2, 2));

            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.PLANT),
                    "First calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                    "Second calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.INSECT),
                    "Third calibration test failed");
            assertEquals(3, cardMap.getAmountOfArtifacts(Artifacts.FUNGI),
                    "Fourth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.MANUSCRIPT),
                    "Fifth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.QUILL),
                    "Sixth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.INKWELL),
                    "Seventh calibration test failed");
            assertEquals(expectedCoordinatesPlaced, cardMap.getCoordinatesPlaced(),
                    "Eighth calibration test failed");
            assertEquals(expectedAvailablePlacements, cardMap.getAvailablePlacements(),
                    "Ninth calibration test failed");
        }

        @Test
        void placeTestGoldenCard() {

            cardMap.place(starterCards.getFirst(), 0, true);
            cardMap.place(resourceCards.getFirst(), 1, true);
            cardMap.place(goldenCards.getFirst(), 4, true);
            //coordinatesPlaced is [(0;0), (1;1), (2;2)]
            expectedCoordinatesPlaced.add(new Coordinates(0, 0));
            expectedCoordinatesPlaced.add(new Coordinates(1, 1));
            expectedCoordinatesPlaced.add(new Coordinates(2, 2));
            //availablePlacements is [(-1;1), (1;-1), (-1;-1), (0;2), (3;3), (3;1)]
            expectedAvailablePlacements.add(new Coordinates(-1, 1));
            expectedAvailablePlacements.add(new Coordinates(1, -1));
            expectedAvailablePlacements.add(new Coordinates(-1, -1));
            expectedAvailablePlacements.add(new Coordinates(0, 2));
            expectedAvailablePlacements.add(new Coordinates(3, 3));
            expectedAvailablePlacements.add(new Coordinates(3, 1));

            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.PLANT),
                    "First calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                    "Second calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.INSECT),
                    "Third calibration test failed");
            assertEquals(3, cardMap.getAmountOfArtifacts(Artifacts.FUNGI),
                    "Fourth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.MANUSCRIPT),
                    "Fifth calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.QUILL),
                    "Sixth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.INKWELL),
                    "Seventh calibration test failed");
            assertEquals(expectedCoordinatesPlaced, cardMap.getCoordinatesPlaced(),
                    "Eighth calibration test failed");
            assertEquals(expectedAvailablePlacements, cardMap.getAvailablePlacements(),
                    "Ninth calibration test failed");
        }

        @Test
        void placeTestSecondResourceCard() {

            cardMap.place(starterCards.getFirst(), 0, true);
            cardMap.place(resourceCards.getFirst(), 1, true);
            cardMap.place(goldenCards.getFirst(), 4, true);
            cardMap.place(resourceCards.get(1), 3, true);
            //coordinatesPlaced is [(0;0), (1;1), (2;2), (0;2)]
            expectedCoordinatesPlaced.add(new Coordinates(0, 0));
            expectedCoordinatesPlaced.add(new Coordinates(1, 1));
            expectedCoordinatesPlaced.add(new Coordinates(2, 2));
            expectedCoordinatesPlaced.add(new Coordinates(0, 2));
            //availablePlacements is [(1;-1), (-1;-1), (3;3), (3;1), (-1;3), (1;3)]
            expectedAvailablePlacements.add(new Coordinates(1, -1));
            expectedAvailablePlacements.add(new Coordinates(-1, -1));
            expectedAvailablePlacements.add(new Coordinates(3, 3));
            expectedAvailablePlacements.add(new Coordinates(3, 1));
            expectedAvailablePlacements.add(new Coordinates(-1, 3));

            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.PLANT),
                    "First calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                    "Second calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.INSECT),
                    "Third calibration test failed");
            assertEquals(4, cardMap.getAmountOfArtifacts(Artifacts.FUNGI),
                    "Fourth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.MANUSCRIPT),
                    "Fifth calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.QUILL),
                    "Sixth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.INKWELL),
                    "Seventh calibration test failed");
            assertEquals(expectedCoordinatesPlaced, cardMap.getCoordinatesPlaced(),
                    "Eighth calibration test failed");
            assertEquals(expectedAvailablePlacements, cardMap.getAvailablePlacements(),
                    "Ninth calibration test failed");
        }

        @Test
        void placeTestSecondGoldenCardFaceDown() {

            cardMap.place(starterCards.getFirst(), 0, true);
            cardMap.place(resourceCards.getFirst(), 1, true);
            cardMap.place(goldenCards.getFirst(), 4, true);
            cardMap.place(resourceCards.get(1), 3, true);
            cardMap.place(goldenCards.get(1), 1, false);
            //coordinatesPlaced is [(0;0), (1;1), (2;2), (0;2), (-1;-1)]
            expectedCoordinatesPlaced.add(new Coordinates(0, 0));
            expectedCoordinatesPlaced.add(new Coordinates(1, 1));
            expectedCoordinatesPlaced.add(new Coordinates(2, 2));
            expectedCoordinatesPlaced.add(new Coordinates(0, 2));
            expectedCoordinatesPlaced.add(new Coordinates(-1, -1));
            //availablePlacements is [(1;-1), (3;3), (3;1), (-1;3), (1;3), (-2;0), (0;-2), (-2;-2)]
            expectedAvailablePlacements.add(new Coordinates(1, -1));
            expectedAvailablePlacements.add(new Coordinates(3, 3));
            expectedAvailablePlacements.add(new Coordinates(3, 1));
            expectedAvailablePlacements.add(new Coordinates(-1, 3));
            expectedAvailablePlacements.add(new Coordinates(-2, 0));
            expectedAvailablePlacements.add(new Coordinates(0, -2));
            expectedAvailablePlacements.add(new Coordinates(-2, -2));

            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.PLANT),
                    "First calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                    "Second calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.INSECT),
                    "Third calibration test failed");
            assertEquals(5, cardMap.getAmountOfArtifacts(Artifacts.FUNGI),
                    "Fourth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.MANUSCRIPT),
                    "Fifth calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.QUILL),
                    "Sixth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.INKWELL),
                    "Seventh calibration test failed");
            assertEquals(expectedCoordinatesPlaced, cardMap.getCoordinatesPlaced(),
                    "Eighth calibration test failed");
            assertEquals(expectedAvailablePlacements, cardMap.getAvailablePlacements(),
                    "Ninth calibration test failed");
        }
    }

    @Nested
    public class GetAmountOfArtifactsTest {
        //DATA
        private CardMap cardMap;


        @BeforeEach
        void setUp() {

            cardMap = new CardMap();
            cardMap.changeArtifactAmount(Artifacts.ANIMAL, 2);
            cardMap.changeArtifactAmount(Artifacts.INSECT, 0);

        }

        @Test
        void getAmountOfArtifactsTest() {
            //Test known cases
            assertEquals(2, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                    "First calibration test failed");
        }

        @Test
        void getAmountOfArtifactsIncrementArtifactTest() {
            cardMap.changeArtifactAmount(Artifacts.ANIMAL, 2);
            //Test known cases
            assertEquals(4, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                    "Second calibration test failed");
        }

        @Test
        void getAmountOfArtifactsDecrementArtifactTest() {
            cardMap.changeArtifactAmount(Artifacts.ANIMAL, -2);
            //Test known cases
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                    "Third calibration test failed");
        }

        @Test
        void getAmountOfArtifactsMissingArtifactTest() {
            //Test known cases
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.PLANT),
                    "Fourth calibration test failed");
        }

        @Test
        void getAmountOfArtifactsZeroArtifactTest() {
            //Test known cases
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.INSECT),
                    "Fifth calibration test failed");
        }
    }

    @Nested
    public class UpdateCoveredCornersAndArtifactsTest {
        //DATA
        private CardMap cardMap;

        @BeforeEach
        void setUp() {
            cardMap = new CardMap();

            Map<Coordinates, CardVisibility> cardsPlacedValue = new HashMap<>();
            Map<Artifacts, Integer> artifactsCounterValue = new HashMap<>();

            Map<CornerOrientation, Corner> cornersCardResource = new HashMap<>();
            cornersCardResource.put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.MANUSCRIPT));
            cornersCardResource.put(new CornerOrientation(CornerDirection.SE, true), new Corner(Artifacts.QUILL));
            cornersCardResource.put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.ANIMAL));
            cornersCardResource.put(new CornerOrientation(CornerDirection.NW, true), new Corner(Artifacts.PLANT));
            CardResource cardResource = new CardResource(Artifacts.ANIMAL, cornersCardResource);

            Map<CornerOrientation, Corner> dummyCardCorners = new HashMap<>();
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.MANUSCRIPT));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SE, true), new Corner(Artifacts.INSECT));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NW, true), new Corner(Artifacts.INKWELL));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
            CardResource dummyCard = new CardResource(Artifacts.ANIMAL, dummyCardCorners);

            cardsPlacedValue.put(new Coordinates(1, 1), new CardVisibility(cardResource, true));
            Map<Artifacts, Integer> newArtifacts1 = new CardVisibility(cardResource, true).getAllArtifacts();
            for(Map.Entry<Artifacts, Integer> entry : newArtifacts1.entrySet()){
                if(artifactsCounterValue.containsKey(entry.getKey())){
                    artifactsCounterValue.put(entry.getKey(), entry.getValue() + artifactsCounterValue.get(entry.getKey()));
                } else {
                    artifactsCounterValue.put(entry.getKey(), entry.getValue());
                }
            }

            cardsPlacedValue.put(new Coordinates(1, -1), new CardVisibility(cardResource, true));
            Map<Artifacts, Integer> newArtifacts2 = new CardVisibility(cardResource, true).getAllArtifacts();
            for(Map.Entry<Artifacts, Integer> entry : newArtifacts2.entrySet()){
                if(artifactsCounterValue.containsKey(entry.getKey())){
                    artifactsCounterValue.put(entry.getKey(), entry.getValue() + artifactsCounterValue.get(entry.getKey()));
                } else {
                    artifactsCounterValue.put(entry.getKey(), entry.getValue());
                }
            }

            cardsPlacedValue.put(new Coordinates(-1, 1), new CardVisibility(cardResource, true));
            Map<Artifacts, Integer> newArtifacts3 = new CardVisibility(cardResource, true).getAllArtifacts();
            for(Map.Entry<Artifacts, Integer> entry : newArtifacts3.entrySet()){
                if(artifactsCounterValue.containsKey(entry.getKey())){
                    artifactsCounterValue.put(entry.getKey(), entry.getValue() + artifactsCounterValue.get(entry.getKey()));
                } else {
                    artifactsCounterValue.put(entry.getKey(), entry.getValue());
                }
            }

            cardsPlacedValue.put(new Coordinates(-1, -1), new CardVisibility(cardResource, true));
            Map<Artifacts, Integer> newArtifacts4 = new CardVisibility(cardResource, true).getAllArtifacts();
            for(Map.Entry<Artifacts, Integer> entry : newArtifacts4.entrySet()){
                if(artifactsCounterValue.containsKey(entry.getKey())){
                    artifactsCounterValue.put(entry.getKey(), entry.getValue() + artifactsCounterValue.get(entry.getKey()));
                } else {
                    artifactsCounterValue.put(entry.getKey(), entry.getValue());
                }
            }

            cardsPlacedValue.put(new Coordinates(0, 0), new CardVisibility(dummyCard, true));
            Map<Artifacts, Integer> newArtifacts5 = new CardVisibility(dummyCard, true).getAllArtifacts();
            for(Map.Entry<Artifacts, Integer> entry : newArtifacts5.entrySet()){
                if(artifactsCounterValue.containsKey(entry.getKey())){
                    artifactsCounterValue.put(entry.getKey(), entry.getValue() + artifactsCounterValue.get(entry.getKey()));
                } else {
                    artifactsCounterValue.put(entry.getKey(), entry.getValue());
                }
            }

            cardMap = CardMapReflectionBuilder(cardsPlacedValue, artifactsCounterValue);

            cardMap.updateCoveredCornersAndArtifacts(new Coordinates(0, 0));
        }

        public CardMap CardMapReflectionBuilder(Map<Coordinates, CardVisibility> cardsPlacedValue, Map<Artifacts, Integer> artifactsCounterValue){
            //Creating using the usual constructor a normal CardMap object.
            CardMap cardMap = new CardMap();

            //Defining two field type object
            Field field0;
            Field field1;

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
            }
            catch (NoSuchFieldException | IllegalAccessException m){
                System.out.println(m);
            }

            //returning the object inside which we set the new values.
            return cardMap;
        }

        @Test
        void updateCoveredCornersAndArtifactsTest() {

            assertEquals(4, cardMap.getAmountOfArtifacts(Artifacts.MANUSCRIPT),
                    "First calibration test failed");
            assertEquals(3, cardMap.getAmountOfArtifacts(Artifacts.QUILL),
                    "Second calibration test failed");
            assertEquals(3, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                    "Third calibration test failed");
            assertEquals(3, cardMap.getAmountOfArtifacts(Artifacts.PLANT),
                    "Fourth calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.INSECT),
                    "Fifth calibration test failed");
            assertEquals(1, cardMap.getAmountOfArtifacts(Artifacts.INKWELL),
                    "Sixth calibration test failed");
            assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.FUNGI),
                    "Seventh calibration test failed");
        }
    }

    @Nested
    public class GetAmountOfNearbyCornersTest {
        //DATA
        private CardMap cardMap;




        @BeforeEach
        void setUp() {

            Map<CornerOrientation, Corner> cornersCardResource = new HashMap<>();
            cornersCardResource.put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.MANUSCRIPT));
            cornersCardResource.put(new CornerOrientation(CornerDirection.SE, true), new Corner(Artifacts.QUILL));
            cornersCardResource.put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.ANIMAL));
            cornersCardResource.put(new CornerOrientation(CornerDirection.NW, true), new Corner(Artifacts.PLANT));
            CardResource cardResource = new CardResource(Artifacts.ANIMAL, cornersCardResource);

            Map<Coordinates, CardVisibility> cardsPlacedValue = new HashMap<>();
            Map<Artifacts, Integer> artifactsCounterValue = new HashMap<>();

            cardMap = new CardMap();

            cardsPlacedValue.put(new Coordinates(3, 2), new CardVisibility(cardResource, true));
            cardsPlacedValue.put(new Coordinates(1, 1), new CardVisibility(cardResource, true));
            cardsPlacedValue.put(new Coordinates(1, -1), new CardVisibility(cardResource, true));
            cardsPlacedValue.put(new Coordinates(-1, 1), new CardVisibility(cardResource, true));
            cardsPlacedValue.put(new Coordinates(-1, -1), new CardVisibility(cardResource, true));

            cardMap = CardMapReflectionBuilder(cardsPlacedValue, artifactsCounterValue);
        }

        public CardMap CardMapReflectionBuilder(Map<Coordinates, CardVisibility> cardsPlacedValue, Map<Artifacts, Integer> artifactsCounterValue){
            //Creating using the usual constructor a normal CardMap object.
            CardMap cardMap = new CardMap();

            //Defining two field type object
            Field field0;
            Field field1;

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
            }
            catch (NoSuchFieldException | IllegalAccessException m){
                System.out.println(m);
            }

            //returning the object inside which we set the new values.
            return cardMap;
        }

        //Test for one corner
        @Test
        void getAmountOfNearbyCornersTest() {

            Map<CornerOrientation, Corner> dummyCardCorners = new HashMap<>();
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.MANUSCRIPT));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SE, true), new Corner(Artifacts.INSECT));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NW, true), new Corner(Artifacts.INKWELL));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
            CardResource dummyCard = new CardResource(Artifacts.ANIMAL, dummyCardCorners);

            assertEquals(1, cardMap.getAmountOfNearbyCorners(new Coordinates(2, 3)),
                    "First calibration test failed");
        }

        //Test for all corners
        @Test
        void getAmountOfNearbyCornersAllCornersCoveredTest() {

            Map<CornerOrientation, Corner> dummyCardCorners = new HashMap<>();
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.MANUSCRIPT));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SE, true), new Corner(Artifacts.INSECT));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NW, true), new Corner(Artifacts.INKWELL));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.QUILL));
            CardResource dummyCard = new CardResource(Artifacts.PLANT, dummyCardCorners);

            assertEquals(4, cardMap.getAmountOfNearbyCorners(new Coordinates(0, 0)),
                    "Second calibration test failed");
        }

        //Test for zero corners
        @Test
        void getAmountOfNearbyCornersZeroTest() {

            Map<CornerOrientation, Corner> dummyCardCorners = new HashMap<>();
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.MANUSCRIPT));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SE, true), new Corner(Artifacts.INSECT));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NW, true), new Corner(Artifacts.INKWELL));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.QUILL));
            CardResource dummyCard = new CardResource(Artifacts.INSECT, dummyCardCorners);

            assertEquals(0, cardMap.getAmountOfNearbyCorners(new Coordinates(4, 4)),
                    "Third calibration test failed");
        }

        @Test
        void getAmountOfNearbyCornersAllEmptyCornersTest() {

            Map<CornerOrientation, Corner> dummyCardCorners = new HashMap<>();
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.EMPTY));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
            dummyCardCorners.put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
            CardResource dummyCard = new CardResource(Artifacts.INSECT, dummyCardCorners);

            assertEquals(4, cardMap.getAmountOfNearbyCorners(new Coordinates(0, 0)),
                    "Fourth calibration test failed");
        }
    }

    @Nested
    public class UpdateAvailablePlacementsTest{
        private List<CardMap> scenarios = new ArrayList<>();
        private List<List<Coordinates>> expectedAvailablePlacements = new ArrayList<>();
        private List<CardVisibility> cardsToPlace = new ArrayList<>();
        private List<Integer> indexesCoordinateToPlace = new ArrayList<>();
        private List<Card> defaultResourceCards = new ArrayList<>();


        Coordinates[] coordinates = new Coordinates[]{
                new Coordinates(-2,2),
                new Coordinates(0,2),
                new Coordinates(2,2),
                new Coordinates(2,0),
                new Coordinates(2,-2),
                new Coordinates(0,-2),
                new Coordinates(-2,-2),
                new Coordinates(-2,0)
        };


        @BeforeEach
        void setUp(){

            String defaultResourceCardsJson = "";

            try{
                defaultResourceCardsJson = Files.readString(Path.of("src/test/java/Model/Player/Resources/DefaultResourceCards.txt"));
            }
            catch (IOException m){
                System.out.println(m);
            }

            defaultResourceCards = CardJsonSerializer.deserializeCardResourceList(defaultResourceCardsJson);

            setUpScenario0();
            setUpScenario1();
            setUpScenario2();
            setUpScenario3();
            setUpScenario4();

            setUpScenario5();
            setUpScenario6();
            setUpScenario7();
            setUpScenario8();
            setUpScenario9();
            setUpScenario10();
            setUpScenario11();
            setUpScenario12();
        }
        void scenarioBuilder(Map<Coordinates, CardVisibility> cardsPlaced0, List<Coordinates> availablePlacements0, List<Coordinates> expectedAvailablePlacements0, int coordinatesIndex, CardVisibility cardToPlace){
            //setting up cardMap class that will store the scenario
            CardMap cardMap0 = new CardMap();

            //Using reflection to put cardsPlaced and availablePlacements into the cardMap object
            Field cardsPlaced;
            Field availablePlacements;
            try{
                cardsPlaced = CardMap.class.getDeclaredField("cardsPlaced");
                cardsPlaced.setAccessible(true);
                cardsPlaced.set(cardMap0, cardsPlaced0);

                availablePlacements = CardMap.class.getDeclaredField("availablePlacements");
                availablePlacements.setAccessible(true);
                availablePlacements.set(cardMap0, availablePlacements0);
            }
            catch (NoSuchFieldException | IllegalAccessException m){
                System.out.println(m);
            }

            //adding cardMap to the array of cardMaps.
            scenarios.add(cardMap0);

            //adding the expected availableCoordinates after update in the scenario into the array.
            expectedAvailablePlacements.add(expectedAvailablePlacements0);

            //adding the card and the coordinate to place to the array
            cardsToPlace.add(cardToPlace);
            indexesCoordinateToPlace.add(coordinatesIndex);
        }

        //Scenarios representing the progression of a simple game
        void setUpScenario0(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>();
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(0,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,1));
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario1(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(new Coordinates(0,0), new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,1));
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
                add(new Coordinates(-2,2));
                add(new Coordinates(0,2));
                add(new Coordinates(-2,0));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario2(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(new Coordinates(0,0), new CardVisibility(defaultResourceCards.get(0), true));
                put(new Coordinates(-1,-1), new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
                add(new Coordinates(-2,2));
                add(new Coordinates(0,2));
                add(new Coordinates(-2,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 4;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(3), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
                add(new Coordinates(-2,2));
                add(new Coordinates(-2,0));
                add(new Coordinates(-1,3));
                add(new Coordinates(1,3));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario3(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(new Coordinates(0,0), new CardVisibility(defaultResourceCards.get(0), true));
                put(new Coordinates(-1,-1), new CardVisibility(defaultResourceCards.get(0), true));
                put(new Coordinates(0,2), new CardVisibility(defaultResourceCards.get(3), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
                add(new Coordinates(-2,2));
                add(new Coordinates(-2,0));
                add(new Coordinates(-1,3));
                add(new Coordinates(1,3));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(4), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,-1));
                add(new Coordinates(-2,2));
                add(new Coordinates(-2,0));
                add(new Coordinates(-1,3));
                add(new Coordinates(1,3));
                add(new Coordinates(2,0));
                add(new Coordinates(2,-2));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario4(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(new Coordinates(0,0), new CardVisibility(defaultResourceCards.get(0), true));
                put(new Coordinates(-1,-1), new CardVisibility(defaultResourceCards.get(0), true));
                put(new Coordinates(0,2), new CardVisibility(defaultResourceCards.get(3), true));
                put(new Coordinates(1,-1), new CardVisibility(defaultResourceCards.get(4), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,-1));
                add(new Coordinates(-2,2));
                add(new Coordinates(-2,0));
                add(new Coordinates(-1,3));
                add(new Coordinates(1,3));
                add(new Coordinates(2,0));
                add(new Coordinates(2,-2));
            }};
            //Defining input for the test.
            int coordinatesIndex = 5;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(4), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,-1));
                add(new Coordinates(-2,2));
                add(new Coordinates(-2,0));
                add(new Coordinates(-1,3));
                add(new Coordinates(1,3));
                add(new Coordinates(2,-2));
                add(new Coordinates(3,1));
                add(new Coordinates(3,-1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }

        //Scenarios representing 8 important cases the updater might face during any game.
        //specifically each scenario is designed to see if the updater will recognize "indirect" blockage of a coordinate.
        //Indirect blockage refers to when a coordinate is blocked by the null corner of a card that is not directly adjacent to
        //the card we just placed.
        //For example:
        //
        // ┌──────┐              By placing a Card with all empty/resource type corners in 0,0, usually all of its
        // │ -2,2 │              surrounding coordinates if they don't already have a card on them, including -1,1
        // └──────┘              would become next turn's available placements.
        //         -1,1          However if for example there already was a card in -2,2 and it had a NULL type South-East corner
        //              ┌──────┐ then by placing the card in 0,0 we must be careful not to automatically add 1,1 to
        //              │ 0,0  │ the available placements as it is "indirectly" blocked by the -2,2 card.
        //              └──────┘
        //
        //The following are 8 such scenarios representing different permutations of this case.


        void setUpScenario5(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(coordinates[0], new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(0,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario6(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(coordinates[1], new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(0,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario7(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(coordinates[2], new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(0,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario8(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(coordinates[3], new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(0,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,1));
                add(new Coordinates(-1,-1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario9(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(coordinates[4], new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(0,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,1));
                add(new Coordinates(1,1));
                add(new Coordinates(-1,-1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario10(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(coordinates[5], new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(0,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,1));
                add(new Coordinates(1,1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario11(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(coordinates[6], new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(0,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(-1,1));
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }
        void setUpScenario12(){
            //Defining initial conditions of the test.
            Map<Coordinates, CardVisibility> cardsPlaced = new HashMap<>(){{
                put(coordinates[7], new CardVisibility(defaultResourceCards.get(0), true));
            }};
            List<Coordinates> availablePlacements = new ArrayList<>(){{
                add(new Coordinates(0,0));
            }};
            //Defining input for the test.
            int coordinatesIndex = 0;
            CardVisibility cardToPlace = new CardVisibility(defaultResourceCards.get(0), true);

            //Defining the expected result.
            List<Coordinates> expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
            }};

            //Creating the test scenario.
            scenarioBuilder(cardsPlaced, availablePlacements, expectedAvailablePlacements, coordinatesIndex, cardToPlace);
        }

        @Test
        void updateAvailablePlacements() {

            for(int i = 0; i < scenarios.size(); i++){
                CardMap currentCardMap = scenarios.get(i);
                List<Coordinates> currentExpectedAvailablePlacements = expectedAvailablePlacements.get(i);
                CardVisibility cardVisibilityToPlace = cardsToPlace.get(i);
                int indexCoordinateToPlace = indexesCoordinateToPlace.get(i);

                currentCardMap.updateAvailablePlacements(cardVisibilityToPlace, indexCoordinateToPlace);

                List<Coordinates> resultAvailablePlacements = new ArrayList<>();
                Field availablePlacements;
                try{
                    availablePlacements = CardMap.class.getDeclaredField("availablePlacements");
                    availablePlacements.setAccessible(true);
                    resultAvailablePlacements = (List<Coordinates>) availablePlacements.get(currentCardMap);
                }
                catch (NoSuchFieldException | IllegalAccessException m){
                    System.out.println(m);
                }

                /*System.out.println("Printing expected of scenario: "+i);
                for(int j = 0; j < currentExpectedAvailablePlacements.size(); j++) {
                    System.out.println(currentExpectedAvailablePlacements.get(j).toString());
                }
                System.out.println("Printing results of scenario: "+i);
                for(int j = 0; j < resultAvailablePlacements.size(); j++){
                    System.out.println(resultAvailablePlacements.get(j).toString());
                }*/

                assertEquals(currentExpectedAvailablePlacements.size(), resultAvailablePlacements.size(),"Lists of different size");
                assertTrue(resultAvailablePlacements.containsAll(currentExpectedAvailablePlacements));
            }
        }
    }
}