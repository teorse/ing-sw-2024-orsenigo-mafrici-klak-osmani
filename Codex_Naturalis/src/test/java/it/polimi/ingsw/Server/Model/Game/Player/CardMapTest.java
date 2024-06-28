package it.polimi.ingsw.Server.Model.Game.Player;

import it.polimi.ingsw.Exceptions.Game.Model.Player.CoordinateIndexOutOfBounds;
import it.polimi.ingsw.Server.Model.Game.Artifacts;

import it.polimi.ingsw.Server.Model.Game.Cards.*;
import it.polimi.ingsw.Utils.JsonParser.CardJsonSerializer;
import it.polimi.ingsw.Utils.JsonParser.CardJsonSerializerDebug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CardMapTest {

    @Nested
    public class PlaceTest {
        //DATA
        private List<CardResource> resourceCards = new ArrayList<>();
        private List<CardGolden> goldenCards = new ArrayList<>();
        private List<CardStarter> starterCards = new ArrayList<>();

        //Resets the lists of cards back to their initial state
        void setUp() {

            String resourceCardsJson = "";
            String goldenCardsJson = "";
            String starterCardsJson = "";

            try{
                resourceCardsJson = Files.readString(Path.of("src/test/java/resources/json/CardsResource.json"));
                goldenCardsJson = Files.readString(Path.of("src/test/java/resources/json/CardsGolden.json"));
                starterCardsJson = Files.readString(Path.of("src/test/java/resources/json/CardsStarter.json"));
            }
            catch (IOException m){
                m.printStackTrace();
            }

            resourceCards = CardJsonSerializer.deserializeCardResourceList(resourceCardsJson);
            goldenCards = CardJsonSerializer.deserializeCardGoldenList(goldenCardsJson);
            starterCards = CardJsonSerializer.deserializeCardStarterList(starterCardsJson);
        }

        //Tests if the correct exception is triggered when passing invalid parameters to the method
        @Test
        void placeInvalidCoordinateIndex(){
            setUp();

            CardMap cardmap = new CardMap(null, "testUser");
            List<Coordinates> oldCoordinatesPlaced;
            List<Coordinates> oldAvailableCoordinates;
            Map<Artifacts, Integer> oldArtifactsCounter;

            //Coordinate index is negative so exception should be thrown
            oldCoordinatesPlaced = cardmap.getCoordinatesPlaced();
            oldAvailableCoordinates = cardmap.getAvailablePlacements();
            oldArtifactsCounter = cardmap.getArtifactsCounter();
            assertThrows(CoordinateIndexOutOfBounds.class, () -> cardmap.place(goldenCards.getFirst(), -1, true));
            assertEquals(oldCoordinatesPlaced, cardmap.getCoordinatesPlaced());
            assertEquals(oldAvailableCoordinates, cardmap.getAvailablePlacements());
            assertEquals(oldArtifactsCounter, cardmap.getArtifactsCounter());


            //Coordinate index is greater than size of available coordinates (available 1, [0,0], passing 2 as parameter
            oldCoordinatesPlaced = cardmap.getCoordinatesPlaced();
            oldAvailableCoordinates = cardmap.getAvailablePlacements();
            oldArtifactsCounter = cardmap.getArtifactsCounter();
            assertThrows(CoordinateIndexOutOfBounds.class, () -> cardmap.place(goldenCards.getFirst(), 2, true));
            assertEquals(oldCoordinatesPlaced, cardmap.getCoordinatesPlaced());
            assertEquals(oldAvailableCoordinates, cardmap.getAvailablePlacements());
            assertEquals(oldArtifactsCounter, cardmap.getArtifactsCounter());

            //Placing one card to increase the amount of available coordinates
            assertDoesNotThrow(() -> cardmap.place(resourceCards.getFirst(), 0, false));

            oldCoordinatesPlaced = cardmap.getCoordinatesPlaced();
            oldAvailableCoordinates = cardmap.getAvailablePlacements();
            oldArtifactsCounter = cardmap.getArtifactsCounter();
            assertThrows(CoordinateIndexOutOfBounds.class, () -> cardmap.place(goldenCards.getFirst(), 6, true));
            assertEquals(oldCoordinatesPlaced, cardmap.getCoordinatesPlaced());
            assertEquals(oldAvailableCoordinates, cardmap.getAvailablePlacements());
            assertEquals(oldArtifactsCounter, cardmap.getArtifactsCounter());

            assertDoesNotThrow(() -> cardmap.place(resourceCards.getFirst(), 3, false));

        }

        //Tests if the model updates correctly the placed coordinates list
        @Test
        void PlacedCoordinatesAfterPlace(){
            setUp();
            CardMap cardMap = new CardMap(null, "testUser");
            List<Coordinates> expectedCoordinatesPlaced;

            int coordinateIndex;
            Card cardPlaced;
            boolean faceUp;





            //PLACING THE STARTER CARD
            coordinateIndex = 0;
            cardPlaced = starterCards.getFirst();
            faceUp = false;

            // Set up expected available placements
            expectedCoordinatesPlaced = cardMap.getCoordinatesPlaced();
            expectedCoordinatesPlaced.add(cardMap.getAvailablePlacements().get(coordinateIndex));


            //Placing the card
            Card finalCardPlaced = cardPlaced;
            int finalCoordinateIndex = coordinateIndex;
            boolean finalFaceUp = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced, finalCoordinateIndex, finalFaceUp));

            // Verify post-conditions
            assertTrue(expectedCoordinatesPlaced.containsAll(cardMap.getCoordinatesPlaced()) &&
                    cardMap.getCoordinatesPlaced().containsAll(expectedCoordinatesPlaced));





            //PLACING FIRST RESOURCE CARD
            coordinateIndex = 0;
            cardPlaced = resourceCards.getFirst();
            faceUp = false;

            // Set up expected available placements
            expectedCoordinatesPlaced = cardMap.getCoordinatesPlaced();
            expectedCoordinatesPlaced.add(cardMap.getAvailablePlacements().get(coordinateIndex));


            //Placing the card
            Card finalCardPlaced1 = cardPlaced;
            int finalCoordinateIndex1 = coordinateIndex;
            boolean finalFaceUp1 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced1, finalCoordinateIndex1, finalFaceUp1));

            //Checking post-conditions
            assertTrue(expectedCoordinatesPlaced.containsAll(cardMap.getCoordinatesPlaced()) && cardMap.getCoordinatesPlaced().containsAll(expectedCoordinatesPlaced));





            //Placing first golden card
            coordinateIndex = 2;
            cardPlaced = goldenCards.getFirst();
            faceUp = true;

            expectedCoordinatesPlaced = cardMap.getCoordinatesPlaced();
            expectedCoordinatesPlaced.add(cardMap.getAvailablePlacements().get(coordinateIndex));

            int finalCoordinateIndex2 = coordinateIndex;
            Card finalCardPlaced2 = cardPlaced;
            boolean finalFaceUp2 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced2, finalCoordinateIndex2, finalFaceUp2));

            //Checking post-conditions
            assertTrue(expectedCoordinatesPlaced.containsAll(cardMap.getCoordinatesPlaced()) && cardMap.getCoordinatesPlaced().containsAll(expectedCoordinatesPlaced));





            //Placing second resource card
            coordinateIndex = 1;
            cardPlaced = goldenCards.get(2);
            faceUp = true;

            expectedCoordinatesPlaced = cardMap.getCoordinatesPlaced();
            expectedCoordinatesPlaced.add(cardMap.getAvailablePlacements().get(coordinateIndex));

            int finalCoordinateIndex3 = coordinateIndex;
            Card finalCardPlaced3 = cardPlaced;
            boolean finalFaceUp3 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced3, finalCoordinateIndex3, finalFaceUp3));

            //Checking post-conditions
            assertTrue(expectedCoordinatesPlaced.containsAll(cardMap.getCoordinatesPlaced()) && cardMap.getCoordinatesPlaced().containsAll(expectedCoordinatesPlaced));
        }

        @Test
        void ArtifactCounterAfterPlace(){
            setUp();

            CardMap cardMap = new CardMap(null, "testUser");
            Map<Artifacts, Integer> expectedArtifactsCounter;

            int coordinateIndex;
            Card cardPlaced;
            boolean faceUp;

            List<Coordinates> offsets = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
                add(new Coordinates(-1,1));
            }};





            //PLACING THE STARTER CARD
            coordinateIndex = 0;
            cardPlaced = starterCards.getFirst();
            faceUp = false;

            //The expected artifacts are all the artifacts that were visible on the card just placed
            expectedArtifactsCounter = new HashMap<>(cardPlaced.getAllArtifacts(faceUp));

            //Placing the card
            Card finalCardPlaced = cardPlaced;
            int finalCoordinateIndex = coordinateIndex;
            boolean finalFaceUp = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced, finalCoordinateIndex, finalFaceUp));

            // Verify post-conditions
            assertEquals(expectedArtifactsCounter, cardMap.getArtifactsCounter());





            //PLACING FIRST RESOURCE CARD
            coordinateIndex = 0;
            cardPlaced = resourceCards.getFirst();
            faceUp = false;



            //The expected artifacts are all the old artifacts, plus the new card, minus the covered corners
            expectedArtifactsCounter = cardMap.getArtifactsCounter();

            //Adding all the artifacts provided by the newly placed card
            for(Map.Entry<Artifacts, Integer> entry : cardPlaced.getAllArtifacts(faceUp).entrySet()){
                if(expectedArtifactsCounter.containsKey(entry.getKey()))
                    expectedArtifactsCounter.put(entry.getKey(), expectedArtifactsCounter.get(entry.getKey())+ entry.getValue());
                else
                    expectedArtifactsCounter.put(entry.getKey(), entry.getValue());
            }

            //Removing the artifacts in the covered corners
            Coordinates coordinatePlaced = cardMap.getAvailablePlacements().get(coordinateIndex);
            for(Coordinates offset : offsets){
                //If one of the offsets of the placed card was already in the cardMap then it has been covered and we have to
                //remove its artifact


                if(cardMap.getCardVisibilityByCoordinate(coordinatePlaced.add(offset)).isPresent()){
                    CardVisibility coveredCard = cardMap.getCardVisibilityByCoordinate(coordinatePlaced.add(offset)).get();
                    CornerDirection coveredCorner = coordinatePlaced.add(offset).cornerOfAdjacency(coordinatePlaced).get();

                    Artifacts artifactToDecrement = coveredCard.getCornerArtifact(coveredCorner);
                    if(artifactToDecrement != Artifacts.NULL)
                        expectedArtifactsCounter.put(artifactToDecrement, expectedArtifactsCounter.get(artifactToDecrement)-1);
                }
            }

            //Placing the card
            Card finalCardPlaced1 = cardPlaced;
            int finalCoordinateIndex1 = coordinateIndex;
            boolean finalFaceUp1 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced1, finalCoordinateIndex1, finalFaceUp1));

            //Checking post-conditions
            assertEquals(expectedArtifactsCounter, cardMap.getArtifactsCounter());





            //Placing first golden card
            coordinateIndex = 2;
            cardPlaced = goldenCards.getFirst();
            faceUp = true;

            //The expected artifacts are all the old artifacts, plus the new card, minus the covered corners
            expectedArtifactsCounter = cardMap.getArtifactsCounter();

            //Adding all the artifacts provided by the newly placed card
            for(Map.Entry<Artifacts, Integer> entry : cardPlaced.getAllArtifacts(faceUp).entrySet()){
                if(expectedArtifactsCounter.containsKey(entry.getKey()))
                    expectedArtifactsCounter.put(entry.getKey(), expectedArtifactsCounter.get(entry.getKey())+ entry.getValue());
                else
                    expectedArtifactsCounter.put(entry.getKey(), entry.getValue());
            }

            //Removing the artifacts in the covered corners
            coordinatePlaced = cardMap.getAvailablePlacements().get(coordinateIndex);
            for(Coordinates offset : offsets){
                //If one of the offsets of the placed card was already in the cardMap then it has been covered and we have to
                //remove its artifact


                if(cardMap.getCardVisibilityByCoordinate(coordinatePlaced.add(offset)).isPresent()){
                    CardVisibility coveredCard = cardMap.getCardVisibilityByCoordinate(coordinatePlaced.add(offset)).get();
                    CornerDirection coveredCorner = coordinatePlaced.add(offset).cornerOfAdjacency(coordinatePlaced).get();

                    Artifacts artifactToDecrement = coveredCard.getCornerArtifact(coveredCorner);
                    if(artifactToDecrement != Artifacts.NULL)
                        expectedArtifactsCounter.put(artifactToDecrement, expectedArtifactsCounter.get(artifactToDecrement)-1);
                }
            }


            int finalCoordinateIndex2 = coordinateIndex;
            Card finalCardPlaced2 = cardPlaced;
            boolean finalFaceUp2 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced2, finalCoordinateIndex2, finalFaceUp2));

            //Checking post-conditions
            assertEquals(expectedArtifactsCounter, cardMap.getArtifactsCounter());





            //Placing second resource card
            coordinateIndex = 1;
            cardPlaced = goldenCards.get(2);
            faceUp = true;

            //The expected artifacts are all the old artifacts, plus the new card, minus the covered corners
            expectedArtifactsCounter = cardMap.getArtifactsCounter();

            //Adding all the artifacts provided by the newly placed card
            for(Map.Entry<Artifacts, Integer> entry : cardPlaced.getAllArtifacts(faceUp).entrySet()){
                if(expectedArtifactsCounter.containsKey(entry.getKey()))
                    expectedArtifactsCounter.put(entry.getKey(), expectedArtifactsCounter.get(entry.getKey())+ entry.getValue());
                else
                    expectedArtifactsCounter.put(entry.getKey(), entry.getValue());
            }

            //Removing the artifacts in the covered corners
            coordinatePlaced = cardMap.getAvailablePlacements().get(coordinateIndex);
            for(Coordinates offset : offsets){
                //If one of the offsets of the placed card was already in the cardMap then it has been covered and we have to
                //remove its artifact


                if(cardMap.getCardVisibilityByCoordinate(coordinatePlaced.add(offset)).isPresent()){
                    CardVisibility coveredCard = cardMap.getCardVisibilityByCoordinate(coordinatePlaced.add(offset)).get();
                    CornerDirection coveredCorner = coordinatePlaced.add(offset).cornerOfAdjacency(coordinatePlaced).get();

                    Artifacts artifactToDecrement = coveredCard.getCornerArtifact(coveredCorner);
                    if(artifactToDecrement != Artifacts.NULL)
                        expectedArtifactsCounter.put(artifactToDecrement, expectedArtifactsCounter.get(artifactToDecrement)-1);
                }
            }

            int finalCoordinateIndex3 = coordinateIndex;
            Card finalCardPlaced3 = cardPlaced;
            boolean finalFaceUp3 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced3, finalCoordinateIndex3, finalFaceUp3));

            //Checking post-conditions
            assertEquals(expectedArtifactsCounter, cardMap.getArtifactsCounter());
        }

        @Test
        void AvailablePlacementsAfterPlace(){
            setUp();

            CardMap cardMap = new CardMap(null, "testUser");

            int coordinateIndex;
            Card cardPlaced;
            boolean faceUp;

            List<Coordinates> offsets = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,-1));
                add(new Coordinates(-1,1));
            }};

            List<Coordinates> expectedAvailablePlacements;





            //PLACING THE STARTER CARD
            coordinateIndex = 0;
            cardPlaced = starterCards.getFirst();
            faceUp = false;

            //The expected artifacts are all the artifacts that were visible on the card just placed
            expectedAvailablePlacements = new ArrayList<>(offsets);

            //Placing the card
            Card finalCardPlaced = cardPlaced;
            int finalCoordinateIndex = coordinateIndex;
            boolean finalFaceUp = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced, finalCoordinateIndex, finalFaceUp));

            // Verify post-conditions
            assertTrue(cardMap.getAvailablePlacements().containsAll(expectedAvailablePlacements) && expectedAvailablePlacements.containsAll(cardMap.getAvailablePlacements()));





            //Placing second card
            coordinateIndex = cardMap.getAvailablePlacements().indexOf(new Coordinates(-1,-1));
            cardPlaced = resourceCards.getFirst();
            faceUp = false;

            //The expected artifacts are all the artifacts that were visible on the card just placed
            expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,1));
                add(new Coordinates(-2,-2));
                add(new Coordinates(0,-2));
                add(new Coordinates(-2,0));
            }};

            //Placing the card
            Card finalCardPlaced1 = cardPlaced;
            int finalCoordinateIndex1 = coordinateIndex;
            boolean finalFaceUp1 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced1, finalCoordinateIndex1, finalFaceUp1));

            // Verify post-conditions
            assertTrue(cardMap.getAvailablePlacements().containsAll(expectedAvailablePlacements) && expectedAvailablePlacements.containsAll(cardMap.getAvailablePlacements()));





            //Placing third card
            coordinateIndex = cardMap.getAvailablePlacements().indexOf(new Coordinates(0,-2));
            cardPlaced = goldenCards.getFirst();
            faceUp = false;

            //The expected artifacts are all the artifacts that were visible on the card just placed
            expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,1));
                add(new Coordinates(-2,-2));
                add(new Coordinates(-2,0));
                add(new Coordinates(-1,-3));
                add(new Coordinates(1,-3));
            }};

            //Placing the card
            Card finalCardPlaced2 = cardPlaced;
            int finalCoordinateIndex2 = coordinateIndex;
            boolean finalFaceUp2 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced2, finalCoordinateIndex2, finalFaceUp2));

            // Verify post-conditions
            assertTrue(cardMap.getAvailablePlacements().containsAll(expectedAvailablePlacements) && expectedAvailablePlacements.containsAll(cardMap.getAvailablePlacements()));





            //Placing fourth card
            coordinateIndex = cardMap.getAvailablePlacements().indexOf(new Coordinates(1,-3));
            cardPlaced = goldenCards.getFirst();
            faceUp = true;

            //The expected artifacts are all the artifacts that were visible on the card just placed
            expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,1));
                add(new Coordinates(-2,-2));
                add(new Coordinates(-2,0));
                add(new Coordinates(-1,-3));
                add(new Coordinates(0, -4));
                add(new Coordinates(2, -4));
                add(new Coordinates(2, -2));
            }};

            //Placing the card
            Card finalCardPlaced3 = cardPlaced;
            int finalCoordinateIndex3 = coordinateIndex;
            boolean finalFaceUp3 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced3, finalCoordinateIndex3, finalFaceUp3));

            // Verify post-conditions
            assertTrue(cardMap.getAvailablePlacements().containsAll(expectedAvailablePlacements) && expectedAvailablePlacements.containsAll(cardMap.getAvailablePlacements()));





            //Placing fifth card
            coordinateIndex = cardMap.getAvailablePlacements().indexOf(new Coordinates(-2,0));
            cardPlaced = goldenCards.getFirst();
            faceUp = true;

            //The expected artifacts are all the artifacts that were visible on the card just placed
            expectedAvailablePlacements = new ArrayList<>(){{
                add(new Coordinates(1,1));
                add(new Coordinates(1,-1));
                add(new Coordinates(-1,1));
                add(new Coordinates(-2,-2));
                add(new Coordinates(-1,-3));
                add(new Coordinates(0, -4));
                add(new Coordinates(2, -4));
                add(new Coordinates(2, -2));
                add(new Coordinates(-3,-1));
            }};

            //Placing the card
            Card finalCardPlaced4 = cardPlaced;
            int finalCoordinateIndex4 = coordinateIndex;
            boolean finalFaceUp4 = faceUp;
            assertDoesNotThrow(() -> cardMap.place(finalCardPlaced4, finalCoordinateIndex4, finalFaceUp4));

            // Verify post-conditions
            System.out.println("actualValues: \n"+cardMap.getAvailablePlacements());
            assertTrue(cardMap.getAvailablePlacements().containsAll(expectedAvailablePlacements) && expectedAvailablePlacements.containsAll(cardMap.getAvailablePlacements()));





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





            //Scenario 1:
            //               +--------------+
            //               |              |
            //               |              |
            //               |              |
            //               |          XXXX|
            //+--------------+--------------+
            //|              |
            //|              |                   Not
            //|              |                   Placeable
            //|              |
            //+--------------+--------------+              +--------------+
            //               |              |              |              |
            //               |              |              |      P       |
            //               |              |              |              |
            //               |              |              |              |
            //               +--------------+--------------+--------------+
            //                              |              |
            //                              |    0,0       |
            //                              |              |
            //                              |              |
            //                              +--------------+
        }

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



            void setUp(){

                String defaultResourceCardsJson = "";

                try{
                    defaultResourceCardsJson = Files.readString(Path.of("src/test/java/it/polimi/ingsw/Server/Model/Game/Player/Resources/DefaultResourceCards.txt"));
                }
                catch (IOException m){
                    System.out.println(m);
                }

                defaultResourceCards = CardJsonSerializerDebug.deserializeCardResourceList(defaultResourceCardsJson);

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
                CardMap cardMap0 = new CardMap(null, "");

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


            void updateAvailablePlacements() {

                for(int i = 0; i < scenarios.size(); i++){
                    CardMap currentCardMap = scenarios.get(i);
                    List<Coordinates> currentExpectedAvailablePlacements = expectedAvailablePlacements.get(i);
                    CardVisibility cardVisibilityToPlace = cardsToPlace.get(i);
                    int indexCoordinateToPlace = indexesCoordinateToPlace.get(i);

                    //currentCardMap.updateAvailablePlacements(cardVisibilityToPlace, indexCoordinateToPlace);

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

                    //assertEquals(currentExpectedAvailablePlacements.size(), resultAvailablePlacements.size(),"Lists of different size");
                    //assertTrue(resultAvailablePlacements.containsAll(currentExpectedAvailablePlacements));
                }
            }
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

            cardMap = new CardMap(null, "");

            cardsPlacedValue.put(new Coordinates(3, 2), new CardVisibility(cardResource, true));
            cardsPlacedValue.put(new Coordinates(1, 1), new CardVisibility(cardResource, true));
            cardsPlacedValue.put(new Coordinates(1, -1), new CardVisibility(cardResource, true));
            cardsPlacedValue.put(new Coordinates(-1, 1), new CardVisibility(cardResource, true));
            cardsPlacedValue.put(new Coordinates(-1, -1), new CardVisibility(cardResource, true));

            cardMap = CardMapReflectionBuilder(cardsPlacedValue, artifactsCounterValue);
        }

        public CardMap CardMapReflectionBuilder(Map<Coordinates, CardVisibility> cardsPlacedValue, Map<Artifacts, Integer> artifactsCounterValue){
            //Creating using the usual constructor a normal CardMap object.
            CardMap cardMap = new CardMap(null, "");

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
    public class getAmountOfPatternTest{

        private final CardVisibility red = new CardVisibility(new CardResource(Artifacts.FUNGI, null), true);
        private final CardVisibility green = new CardVisibility(new CardResource(Artifacts.PLANT, null), true);
        private final CardVisibility blue = new CardVisibility(new CardResource(Artifacts.ANIMAL, null), true);
        private final CardVisibility purple = new CardVisibility(new CardResource(Artifacts.INSECT, null), true);

        private List<CardMap> cardMaps;
        private List<Map<Coordinates, Artifacts>> patterns;

        //Double list of integers
        //Outer list represents all the expected patterns from the different patterns for the SAME cardMap
        //Inner list represent the expected points for the cardMap for a specific pattern
        //Ex get(1).get(5) returns the number expected for the second cardMap and the sixth pattern
        private List<List<Integer>> expectedAmountOfPatterns;

        @BeforeEach
        void setup(){
            cardMaps = new ArrayList<>();

            //Setting up all patterns to be tested
            patterns = new ArrayList<>();
            patterns.add(new HashMap<>(){{
                put(new Coordinates(0,0), Artifacts.FUNGI);
                put(new Coordinates(1,1), Artifacts.FUNGI);
                put(new Coordinates(2,2), Artifacts.FUNGI);
            }});
            patterns.add(new HashMap<>(){{
                put(new Coordinates(0,0), Artifacts.INSECT);
                put(new Coordinates(1,-1), Artifacts.INSECT);
                put(new Coordinates(2,-2), Artifacts.INSECT);
            }});
            patterns.add(new HashMap<>(){{
                put(new Coordinates(0,0), Artifacts.PLANT);
                put(new Coordinates(0,-2), Artifacts.PLANT);
                put(new Coordinates(-1,-3), Artifacts.INSECT);
            }});
            patterns.add(new HashMap<>(){{
                put(new Coordinates(0,0), Artifacts.ANIMAL);
                put(new Coordinates(1,-1), Artifacts.INSECT);
                put(new Coordinates(1,-3), Artifacts.INSECT);
            }});




            //setting up the maps to reflect inside cardMap class

            //Map0: this is a map that represents the "normal scenario", below are the expected amounts for each pattern
            //Pattern0: 1
            //Pattern1: 2
            //Pattern2: 2
            //Pattern3: 1

            Map<Coordinates, CardVisibility> map0 = new HashMap<>(){{
                put(new Coordinates(-3,3), purple);
                put(new Coordinates(-2,2), purple);
                put(new Coordinates(-1,1), purple);
                put(new Coordinates(0,0), purple);
                put(new Coordinates(1,-1), purple);
                put(new Coordinates(2,-2), purple);
                put(new Coordinates(3,-3), purple);
                put(new Coordinates(3,-1), purple);

                put(new Coordinates(-2,-2), red);
                put(new Coordinates(-1,-1), red);
                put(new Coordinates(1,1), red);
                put(new Coordinates(2,2), red);
                put(new Coordinates(3,3), red);
                put(new Coordinates(4,4), red);

                put(new Coordinates(-1,5), green);
                put(new Coordinates(0,4), green);
                put(new Coordinates(-1,3), green);
                put(new Coordinates(0,2), green);

                put(new Coordinates(2,0), blue);
            }};

            //Map1: this is an empty map (edge case) that should return all patterns as 0
            //Pattern0: 0
            //Pattern1: 0
            //Pattern2: 0
            //Pattern3: 0

            Map<Coordinates, CardVisibility> map1 = new HashMap<>();




            //Map2: this is a variation of map0 (normal case) that should return all patterns as 0
            //Pattern0: 0
            //Pattern1: 0
            //Pattern2: 0
            //Pattern3: 0

            Map<Coordinates, CardVisibility> map2 = new HashMap<>(){{
                put(new Coordinates(-3,3), purple);
                //put(new Coordinates(-2,2), purple);
                put(new Coordinates(-1,1), purple);
                put(new Coordinates(0,0), purple);
                //put(new Coordinates(1,-1), purple);
                put(new Coordinates(2,-2), purple);
                //put(new Coordinates(3,-3), purple);
                put(new Coordinates(3,-1), purple);

                put(new Coordinates(-2,-2), red);
                put(new Coordinates(-1,-1), red);
                put(new Coordinates(1,1), red);
                //put(new Coordinates(2,2), red);
                put(new Coordinates(3,3), red);
                put(new Coordinates(4,4), red);

                put(new Coordinates(-1,5), green);
                put(new Coordinates(0,4), green);
                put(new Coordinates(-1,3), green);
                //put(new Coordinates(0,2), green);

                put(new Coordinates(2,0), blue);
            }};

            List<Map<Coordinates, CardVisibility>> maps = new ArrayList<>(){{
                add(map0);
                add(map1);
                add(map2);
            }};





            //Creating CardMap objects
            try{
                Field cardPlacedField = CardMap.class.getDeclaredField("cardsPlaced");
                //Field coordinatesPlacedField = CardMap.class.getDeclaredField("coordinatesPlaced");

                cardPlacedField.setAccessible(true);
                //coordinatesPlacedField.setAccessible(true);

                for(int i = 0; i < maps.size(); i++){
                    Map<Coordinates, CardVisibility> map = maps.get(i);

                    CardMap cardMap = new CardMap(null, "");
                    cardPlacedField.set(cardMap, map);

                    cardMaps.add(cardMap);
                }

            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }





            //Writing expected results
            expectedAmountOfPatterns = new ArrayList<>(){{
                add(new ArrayList<>(){{
                    add(1);
                    add(2);
                    add(2);
                    add(1);
                }});
                add(new ArrayList<>(){{
                    add(0);
                    add(0);
                    add(0);
                    add(0);
                }});
                add(new ArrayList<>(){{
                    add(0);
                    add(0);
                    add(0);
                    add(0);
                }});
            }};

            //END OF SETUP
        }

        @Test
        void getAmountOfPattern(){
            for(int i = 0; i < cardMaps.size(); i++){
                CardMap cardMap = cardMaps.get(i);

                for(int j = 0; j < patterns.size(); j++){
                    Map<Coordinates, Artifacts> pattern = patterns.get(j);
                    int expectedResult = expectedAmountOfPatterns.get(i).get(j);
                    int actualResult = cardMap.getAmountOfPattern(pattern);

                    assertEquals(expectedResult, actualResult, "Pattern "+j+" in cardMap "+i+" failed." +
                            "\nExpected: "+expectedResult+" instances" +
                            "\nFound: "+actualResult+" instances.");
                }
            }
        }
    }
}