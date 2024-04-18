package Model.Game;

import Model.Cards.Card;
import Model.Objectives.Objective;
import Model.Objectives.ObjectiveItem;
import Model.Player.Player;
import Model.Utility.JsonParser.CardJsonSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Nested
    public class checkGameEndingTest{
        public List<Game> games = new ArrayList<>();
        public List<GamePhases> expectedGamePhases = new ArrayList<>();//List of games


        @BeforeEach
        void setUp() {
            setUpScenario0();
            setUpScenario1();
            setUpScenario2();
            setUpScenario3();
            setUpScenario4();
            setUpScenario5();
        }

        void scenarioBuilder(Map<CardPoolTypes, CardPool> cardPools, List<Player> players, GamePhases expectedGamePhase){

            List<List<Card>> sampleCards = new ArrayList<>();
            String cardJsonPath = "src/test/java/Model/Game/Resources/DefaultResourceCards.txt";
            String cardJson;

            try{
                cardJson = Files.readString(Path.of(cardJsonPath));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            for(int i = 0; i<6; i++){
                sampleCards.add(CardJsonSerializer.deserializeCardResourceList(cardJson));
            }

            List<Objective> sampleObjectives1 = new ArrayList<>(){{
                add(new ObjectiveItem("Sample", 1, null));
                add(new ObjectiveItem("Sample", 1, null));
            }};

            List<Objective> sampleObjectives2 = new ArrayList<>(){{
                add(new ObjectiveItem("Sample", 1, null));
                add(new ObjectiveItem("Sample", 1, null));
            }};

            Game game = new Game(sampleCards.get(0), sampleCards.get(1),sampleCards.get(2),sampleObjectives1, players);
            Table table = new Table(sampleCards.get(3), sampleCards.get(4), sampleCards.get(5),sampleObjectives2);

            Field cardPoolsField;
            Field tableField;
            Field gamePhaseField;

            try{
                cardPoolsField = Table.class.getDeclaredField("cardPools");
                tableField = Game.class.getDeclaredField("table");
                gamePhaseField = Game.class.getDeclaredField("gamePhase");

                cardPoolsField.setAccessible(true);
                tableField.setAccessible(true);
                gamePhaseField.setAccessible(true);

                cardPoolsField.set(table, cardPools);
                tableField.set(game, table);
                gamePhaseField.set(game, GamePhases.MAIN_LOOP);
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            games.add(game);
            expectedGamePhases.add(expectedGamePhase);
        }

        //No players above 19 points and all decks have cards
        void setUpScenario0(){
            //Sample cards to use for the two decks
            List<Card> sampleCards1;
            List<Card> sampleCards2;

            //Setting up sample cards
            String cardJsonPath = "src/test/java/Model/Game/Resources/DefaultResourceCards.txt";
            String cardJson;

            try{
                cardJson = Files.readString(Path.of(cardJsonPath));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            sampleCards1 = CardJsonSerializer.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializer.deserializeCardResourceList(cardJson);



            //Setting up the list of players.
            Player player1 = new Player("Leeroy1");
            player1.addPoints(15);
            Player player2 = new Player("Leeroy2");
            player2.addPoints(15);
            Player player3 = new Player("Leeroy3");
            player3.addPoints(15);
            Player player4 = new Player("Leeroy4");
            player4.addPoints(15);

            List<Player> players = new ArrayList<>() {{
                add(player1);
                add(player2);
                add(player3);
                add(player4);
            }};



            //Setting up the pools of cards.
            CardPool cardPool1 = new CardPool(sampleCards1);

            CardPool cardPool2 = new CardPool(sampleCards2);

            Map<CardPoolTypes, CardPool> cardPools = new HashMap<>(){{
                put(CardPoolTypes.RESOURCE, cardPool1);
                put(CardPoolTypes.GOLDEN, cardPool2);
            }};



            //Defining the expected outcome.
            GamePhases expectedGamePhase = GamePhases.MAIN_LOOP;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedGamePhase);
        }

        //One player above 20 points and all decks have cards
        void setUpScenario1(){
            //Sample cards to use for the two decks
            List<Card> sampleCards1;
            List<Card> sampleCards2;

            //Setting up sample cards
            String cardJsonPath = "src/test/java/Model/Game/Resources/DefaultResourceCards.txt";
            String cardJson;

            try{
                cardJson = Files.readString(Path.of(cardJsonPath));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            sampleCards1 = CardJsonSerializer.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializer.deserializeCardResourceList(cardJson);



            //Setting up the list of players.
            Player player1 = new Player("Leeroy1");
            player1.addPoints(24);
            Player player2 = new Player("Leeroy2");
            player2.addPoints(15);
            Player player3 = new Player("Leeroy3");
            player3.addPoints(15);
            Player player4 = new Player("Leeroy4");
            player4.addPoints(15);

            List<Player> players = new ArrayList<>() {{
                add(player1);
                add(player2);
                add(player3);
                add(player4);
            }};



            //Setting up the pools of cards.
            CardPool cardPool1 = new CardPool(sampleCards1);

            CardPool cardPool2 = new CardPool(sampleCards2);

            Map<CardPoolTypes, CardPool> cardPools = new HashMap<>(){{
                put(CardPoolTypes.RESOURCE, cardPool1);
                put(CardPoolTypes.GOLDEN, cardPool2);
            }};



            //Defining the expected outcome.
            GamePhases expectedGamePhase = GamePhases.ENDING;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedGamePhase);
        }

        //One player with exactly 20 points and all decks have cards
        void setUpScenario2(){
            //Sample cards to use for the two decks
            List<Card> sampleCards1;
            List<Card> sampleCards2;

            //Setting up sample cards
            String cardJsonPath = "src/test/java/Model/Game/Resources/DefaultResourceCards.txt";
            String cardJson;

            try{
                cardJson = Files.readString(Path.of(cardJsonPath));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            sampleCards1 = CardJsonSerializer.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializer.deserializeCardResourceList(cardJson);



            //Setting up the list of players.
            Player player1 = new Player("Leeroy1");
            player1.addPoints(15);
            Player player2 = new Player("Leeroy2");
            player2.addPoints(15);
            Player player3 = new Player("Leeroy3");
            player3.addPoints(20);
            Player player4 = new Player("Leeroy4");
            player4.addPoints(15);

            List<Player> players = new ArrayList<>() {{
                add(player1);
                add(player2);
                add(player3);
                add(player4);
            }};



            //Setting up the pools of cards.
            CardPool cardPool1 = new CardPool(sampleCards1);

            CardPool cardPool2 = new CardPool(sampleCards2);

            Map<CardPoolTypes, CardPool> cardPools = new HashMap<>(){{
                put(CardPoolTypes.RESOURCE, cardPool1);
                put(CardPoolTypes.GOLDEN, cardPool2);
            }};



            //Defining the expected outcome.
            GamePhases expectedGamePhase = GamePhases.ENDING;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedGamePhase);
        }

        //No players above 19 points and one deck with no cards
        void setUpScenario3(){
            //Sample cards to use for the two decks
            List<Card> sampleCards1;
            List<Card> sampleCards2;

            //Setting up sample cards
            String cardJsonPath = "src/test/java/Model/Game/Resources/DefaultResourceCards.txt";
            String cardJson;

            try{
                cardJson = Files.readString(Path.of(cardJsonPath));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            sampleCards1 = CardJsonSerializer.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializer.deserializeCardResourceList(cardJson);



            //Setting up the list of players.
            Player player1 = new Player("Leeroy1");
            player1.addPoints(15);
            Player player2 = new Player("Leeroy2");
            player2.addPoints(15);
            Player player3 = new Player("Leeroy3");
            player3.addPoints(15);
            Player player4 = new Player("Leeroy4");
            player4.addPoints(15);

            List<Player> players = new ArrayList<>() {{
                add(player1);
                add(player2);
                add(player3);
                add(player4);
            }};



            //Setting up the pools of cards.
            CardPool cardPool1 = new CardPool(sampleCards1);
            cardPool1.getCard(-1);
            cardPool1.getCard(-1);
            cardPool1.getCard(-1);

            CardPool cardPool2 = new CardPool(sampleCards2);

            Map<CardPoolTypes, CardPool> cardPools = new HashMap<>(){{
                put(CardPoolTypes.RESOURCE, cardPool1);
                put(CardPoolTypes.GOLDEN, cardPool2);
            }};



            //Defining the expected outcome.
            GamePhases expectedGamePhase = GamePhases.MAIN_LOOP;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedGamePhase);
        }

        //No players above 19 points and all decks with no cards
        void setUpScenario4(){
            //Sample cards to use for the two decks
            List<Card> sampleCards1;
            List<Card> sampleCards2;

            //Setting up sample cards
            String cardJsonPath = "src/test/java/Model/Game/Resources/DefaultResourceCards.txt";
            String cardJson;

            try{
                cardJson = Files.readString(Path.of(cardJsonPath));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            sampleCards1 = CardJsonSerializer.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializer.deserializeCardResourceList(cardJson);



            //Setting up the list of players.
            Player player1 = new Player("Leeroy1");
            player1.addPoints(15);
            Player player2 = new Player("Leeroy2");
            player2.addPoints(15);
            Player player3 = new Player("Leeroy3");
            player3.addPoints(15);
            Player player4 = new Player("Leeroy4");
            player4.addPoints(15);

            List<Player> players = new ArrayList<>() {{
                add(player1);
                add(player2);
                add(player3);
                add(player4);
            }};



            //Setting up the pools of cards.
            CardPool cardPool1 = new CardPool(sampleCards1);
            cardPool1.getCard(-1);
            cardPool1.getCard(-1);
            cardPool1.getCard(-1);

            CardPool cardPool2 = new CardPool(sampleCards2);
            cardPool2.getCard(-1);
            cardPool2.getCard(-1);
            cardPool2.getCard(-1);


            Map<CardPoolTypes, CardPool> cardPools = new HashMap<>(){{
                put(CardPoolTypes.RESOURCE, cardPool1);
                put(CardPoolTypes.GOLDEN, cardPool2);
            }};



            //Defining the expected outcome.
            GamePhases expectedGamePhase = GamePhases.ENDING;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedGamePhase);
        }

        //One player above 20 points and all decks with no cards
        void setUpScenario5(){
            //Sample cards to use for the two decks
            List<Card> sampleCards1;
            List<Card> sampleCards2;

            //Setting up sample cards
            String cardJsonPath = "src/test/java/Model/Game/Resources/DefaultResourceCards.txt";
            String cardJson;

            try{
                cardJson = Files.readString(Path.of(cardJsonPath));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            sampleCards1 = CardJsonSerializer.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializer.deserializeCardResourceList(cardJson);



            //Setting up the list of players.
            Player player1 = new Player("Leeroy1");
            player1.addPoints(15);
            Player player2 = new Player("Leeroy2");
            player2.addPoints(15);
            Player player3 = new Player("Leeroy3");
            player3.addPoints(27);
            Player player4 = new Player("Leeroy4");
            player4.addPoints(15);

            List<Player> players = new ArrayList<>() {{
                add(player1);
                add(player2);
                add(player3);
                add(player4);
            }};



            //Setting up the pools of cards.
            CardPool cardPool1 = new CardPool(sampleCards1);
            cardPool1.getCard(-1);
            cardPool1.getCard(-1);
            cardPool1.getCard(-1);

            CardPool cardPool2 = new CardPool(sampleCards2);
            cardPool2.getCard(-1);
            cardPool2.getCard(-1);
            cardPool2.getCard(-1);


            Map<CardPoolTypes, CardPool> cardPools = new HashMap<>(){{
                put(CardPoolTypes.RESOURCE, cardPool1);
                put(CardPoolTypes.GOLDEN, cardPool2);
            }};



            //Defining the expected outcome.
            GamePhases expectedGamePhase = GamePhases.ENDING;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedGamePhase);
        }


        @Test
        void checkGameEnding() {
            for (int i = 0; i < games.size(); i++){
                Game game = games.get(i);
                GamePhases expectedGamePhase = expectedGamePhases.get(i);

                game.checkGameEnding();

                assertEquals(expectedGamePhase, game.getGamePhase(),"Game phase is not as expected");
            }
        }
    }

}