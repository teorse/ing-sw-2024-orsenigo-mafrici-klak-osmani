package Model.Game;

import Model.Cards.Card;
import Model.Objectives.Objective;
import Model.Objectives.ObjectiveNumeric;
import Model.Player.Player;
import Model.Utility.JsonParser.CardJsonSerializerDebug;
import Server.Model.Lobby.LobbyRoles;
import Server.Model.Lobby.LobbyUser;
import Server.Model.ServerUser;
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
    public class checkGameEndingConditionsTest{
        public List<Game> games = new ArrayList<>();
        public List<Boolean> expectedLastRoundTriggers = new ArrayList<>();//List of games


        @BeforeEach
        void setUp() {
            setUpScenario0();
            setUpScenario1();
            setUpScenario2();
            setUpScenario3();
            setUpScenario4();
            setUpScenario5();
        }

        void scenarioBuilder(Map<CardPoolTypes, CardPool> cardPools, List<Player> players, boolean expectedLastRoundTrigger){

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
                sampleCards.add(CardJsonSerializerDebug.deserializeCardResourceList(cardJson));
            }

            List<Objective> sampleObjectives1 = new ArrayList<>(){{
                add(new ObjectiveNumeric("Sample", 1, null));
                add(new ObjectiveNumeric("Sample", 1, null));
            }};

            List<Objective> sampleObjectives2 = new ArrayList<>(){{
                add(new ObjectiveNumeric("Sample", 1, null));
                add(new ObjectiveNumeric("Sample", 1, null));
            }};

            Game game = new Game(null, sampleCards.get(0), sampleCards.get(1),sampleCards.get(2),sampleObjectives1, new ArrayList<>());
            Table table = new Table(sampleCards.get(3), sampleCards.get(4), sampleCards.get(5),sampleObjectives2);

            Field cardPoolsField;
            Field tableField;
            Field playersField;

            try{
                cardPoolsField = Table.class.getDeclaredField("cardPools");
                tableField = Game.class.getDeclaredField("table");
                playersField = Game.class.getDeclaredField("players");

                cardPoolsField.setAccessible(true);
                tableField.setAccessible(true);
                playersField.setAccessible(true);

                cardPoolsField.set(table, cardPools);
                tableField.set(game, table);
                playersField.set(game, players);
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            games.add(game);
            expectedLastRoundTriggers.add(expectedLastRoundTrigger);
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
            sampleCards1 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);


            List<Player> players;
            try {
                Field points = Player.class.getDeclaredField("points");
                points.setAccessible(true);

                //Setting up the list of players.
                Player player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 15);
                Player player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                Player player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 15);
                Player player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 15);

                players = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }


            //Setting up the pools of cards.
            CardPool cardPool1 = new CardPool(sampleCards1);

            CardPool cardPool2 = new CardPool(sampleCards2);

            Map<CardPoolTypes, CardPool> cardPools = new HashMap<>(){{
                put(CardPoolTypes.RESOURCE, cardPool1);
                put(CardPoolTypes.GOLDEN, cardPool2);
            }};



            //Defining the expected outcome.
            boolean expectedLastRoundTrigger = false;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedLastRoundTrigger);
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
            sampleCards1 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);



            List<Player> players;
            try {
                Field points = Player.class.getDeclaredField("points");
                points.setAccessible(true);

                //Setting up the list of players.
                Player player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 24);
                Player player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                Player player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 15);
                Player player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 15);

                players = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }


            //Setting up the pools of cards.
            CardPool cardPool1 = new CardPool(sampleCards1);

            CardPool cardPool2 = new CardPool(sampleCards2);

            Map<CardPoolTypes, CardPool> cardPools = new HashMap<>(){{
                put(CardPoolTypes.RESOURCE, cardPool1);
                put(CardPoolTypes.GOLDEN, cardPool2);
            }};



            //Defining the expected outcome.
            boolean expectedLastRoundTrigger = true;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedLastRoundTrigger);
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
            sampleCards1 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);



            List<Player> players;
            try {
                Field points = Player.class.getDeclaredField("points");
                points.setAccessible(true);

                //Setting up the list of players.
                Player player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 15);
                Player player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                Player player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 20);
                Player player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 15);

                players = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }


            //Setting up the pools of cards.
            CardPool cardPool1 = new CardPool(sampleCards1);

            CardPool cardPool2 = new CardPool(sampleCards2);

            Map<CardPoolTypes, CardPool> cardPools = new HashMap<>(){{
                put(CardPoolTypes.RESOURCE, cardPool1);
                put(CardPoolTypes.GOLDEN, cardPool2);
            }};



            //Defining the expected outcome.
            boolean expectedLastRoundTrigger = true;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedLastRoundTrigger);
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
            sampleCards1 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);



            List<Player> players;
            try {
                Field points = Player.class.getDeclaredField("points");
                points.setAccessible(true);

                //Setting up the list of players.
                Player player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 15);
                Player player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                Player player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 15);
                Player player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 15);

                players = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }


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
            boolean expectedLastRoundTrigger = false;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedLastRoundTrigger);
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
            sampleCards1 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);



            List<Player> players;
            try {
                Field points = Player.class.getDeclaredField("points");
                points.setAccessible(true);

                //Setting up the list of players.
                Player player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 15);
                Player player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                Player player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 15);
                Player player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 15);

                players = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }


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
            boolean expectedLastRoundTrigger = true;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedLastRoundTrigger);
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
            sampleCards1 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);
            sampleCards2 = CardJsonSerializerDebug.deserializeCardResourceList(cardJson);



            List<Player> players;
            try {
                Field points = Player.class.getDeclaredField("points");
                points.setAccessible(true);

                //Setting up the list of players.
                Player player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 15);
                Player player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                Player player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 27);
                Player player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 15);

                players = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }



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
            boolean expectedLastRoundTrigger = true;

            //Adding the scenario to test pipeline.
            scenarioBuilder(cardPools, players, expectedLastRoundTrigger);
        }


        @Test
        void checkGameEndingConditions() {
            for (int i = 0; i < games.size(); i++){
                Game game = games.get(i);
                boolean expectedLastRoundTrigger = expectedLastRoundTriggers.get(i);

                game.checkGameEndingConditions();

                assertEquals(expectedLastRoundTrigger, game.isLastRoundFlag(),"Trigger is not as expected in scenario "+i);
            }
        }
    }

    @Nested
    public class selectWinnersTest{
        //List of winners calculated by select winners method
        public List<List<Player>> sortedPlayers = new ArrayList<>();
        //List of players that are supposed to be winners
        public List<List<Player>> expectedPlayers = new ArrayList<>();

        @BeforeEach
        void setUp(){
            setUpScenario0();
            setUpScenario1();
            setUpScenario2();
            setUpScenario3();
            setUpScenario4();
        }

        void scenarioBuilder(List<Player> playersInitial, List<Player> playersExpected) {

            List<List<Card>> sampleCards = new ArrayList<>();
            String cardJsonPath = "src/test/java/Model/Game/Resources/DefaultResourceCards.txt";
            String cardJson;

            try {
                cardJson = Files.readString(Path.of(cardJsonPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < 6; i++) {
                sampleCards.add(CardJsonSerializerDebug.deserializeCardResourceList(cardJson));
            }

            List<Objective> sampleObjectives1 = new ArrayList<>() {{
                add(new ObjectiveNumeric("Sample", 1, null));
                add(new ObjectiveNumeric("Sample", 1, null));
            }};

            Game game = new Game(null, sampleCards.get(0), sampleCards.get(1), sampleCards.get(2), sampleObjectives1, new ArrayList<>());

            try{
                Field players = Game.class.getDeclaredField("players");
                players.setAccessible(true);
                players.set(game, playersInitial);
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            game.selectWinners();

            try {
                Field players = Game.class.getDeclaredField("players");
                players.setAccessible(true);
                sortedPlayers.add((List<Player>) players.get(game));

                expectedPlayers.add(playersExpected);
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        //All players tied on same score and objectives, sample test
        void setUpScenario0(){
            List<Player> playersInitial;
            List<Player> playersExpected;
            Player player1;
            Player player2;
            Player player3;
            Player player4;

            try {
                Field points = Player.class.getDeclaredField("points");
                Field objectivesCompleted = Player.class.getDeclaredField("objectivesCompleted");
                Field players = Game.class.getDeclaredField("players");
                points.setAccessible(true);
                objectivesCompleted.setAccessible(true);
                players.setAccessible(true);

                //Setting up the list of players.
                player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 15);
                objectivesCompleted.set(player1, 1);
                player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                objectivesCompleted.set(player2, 1);
                player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 15);
                objectivesCompleted.set(player3, 1);
                player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 15);
                objectivesCompleted.set(player4, 1);


                playersInitial = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};

                playersExpected = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};

            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            scenarioBuilder(playersInitial, playersExpected);
        }

        //No Ties - Single Winner:
        void setUpScenario1(){
            List<Player> playersInitial;
            List<Player> playersExpected;
            Player player1;
            Player player2;
            Player player3;
            Player player4;

            try {
                Field points = Player.class.getDeclaredField("points");
                Field objectivesCompleted = Player.class.getDeclaredField("objectivesCompleted");
                Field players = Game.class.getDeclaredField("players");
                points.setAccessible(true);
                objectivesCompleted.setAccessible(true);
                players.setAccessible(true);

                //Setting up the list of players.
                player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 20);
                objectivesCompleted.set(player1, 2);
                player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                objectivesCompleted.set(player2, 1);
                player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 18);
                objectivesCompleted.set(player3, 3);
                player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 10);
                objectivesCompleted.set(player4, 0);


                playersInitial = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};

                playersExpected = new ArrayList<>() {{
                    add(player1);
                }};

            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            scenarioBuilder(playersInitial, playersExpected);
        }

        //Tie in Points - Single Winner:
        void setUpScenario2(){
            List<Player> playersInitial;
            List<Player> playersExpected;
            Player player1;
            Player player2;
            Player player3;
            Player player4;

            try {
                Field points = Player.class.getDeclaredField("points");
                Field objectivesCompleted = Player.class.getDeclaredField("objectivesCompleted");
                Field players = Game.class.getDeclaredField("players");
                points.setAccessible(true);
                objectivesCompleted.setAccessible(true);
                players.setAccessible(true);

                //Setting up the list of players.
                player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 20);
                objectivesCompleted.set(player1, 2);
                player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 20);
                objectivesCompleted.set(player2, 1);
                player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 15);
                objectivesCompleted.set(player3, 3);
                player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 10);
                objectivesCompleted.set(player4, 0);


                playersInitial = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};

                playersExpected = new ArrayList<>() {{
                    add(player1);
                }};

            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            scenarioBuilder(playersInitial, playersExpected);
        }

        //Tie in Objectives - Single Winner:
        void setUpScenario3(){
            List<Player> playersInitial;
            List<Player> playersExpected;
            Player player1;
            Player player2;
            Player player3;
            Player player4;

            try {
                Field points = Player.class.getDeclaredField("points");
                Field objectivesCompleted = Player.class.getDeclaredField("objectivesCompleted");
                Field players = Game.class.getDeclaredField("players");
                points.setAccessible(true);
                objectivesCompleted.setAccessible(true);
                players.setAccessible(true);

                //Setting up the list of players.
                player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 15);
                objectivesCompleted.set(player1, 2);
                player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                objectivesCompleted.set(player2, 3);
                player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 18);
                objectivesCompleted.set(player3, 2);
                player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 20);
                objectivesCompleted.set(player4, 2);


                playersInitial = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};

                playersExpected = new ArrayList<>() {{
                    add(player4);
                }};

            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            scenarioBuilder(playersInitial, playersExpected);
        }

        //Tie in Points and Objectives - Multiple Winners:
        void setUpScenario4(){
            List<Player> playersInitial;
            List<Player> playersExpected;
            Player player1;
            Player player2;
            Player player3;
            Player player4;

            try {
                Field points = Player.class.getDeclaredField("points");
                Field objectivesCompleted = Player.class.getDeclaredField("objectivesCompleted");
                Field players = Game.class.getDeclaredField("players");
                points.setAccessible(true);
                objectivesCompleted.setAccessible(true);
                players.setAccessible(true);

                //Setting up the list of players.
                player1 = new Player(new LobbyUser(new ServerUser("Leeroy1"), LobbyRoles.ADMIN));
                points.set(player1, 20);
                objectivesCompleted.set(player1, 2);
                player2 = new Player(new LobbyUser(new ServerUser("Leeroy2"), LobbyRoles.ADMIN));
                points.set(player2, 15);
                objectivesCompleted.set(player2, 1);
                player3 = new Player(new LobbyUser(new ServerUser("Leeroy3"), LobbyRoles.ADMIN));
                points.set(player3, 20);
                objectivesCompleted.set(player3, 2);
                player4 = new Player(new LobbyUser(new ServerUser("Leeroy4"), LobbyRoles.ADMIN));
                points.set(player4, 15);
                objectivesCompleted.set(player4, 1);


                playersInitial = new ArrayList<>() {{
                    add(player1);
                    add(player2);
                    add(player3);
                    add(player4);
                }};

                playersExpected = new ArrayList<>() {{
                    add(player1);
                    add(player3);
                }};

            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            scenarioBuilder(playersInitial, playersExpected);
        }

        @Test
        void selectWinners(){
            for(int i = 0; i < expectedPlayers.size(); i++){

                assertTrue(sortedPlayers.get(i).containsAll(expectedPlayers.get(i)), "Sorting failed in scenario: "+i+
                        "\nexpected: "+expectedPlayers.get(i).toString()+"\nactual:");
            }
        }
    }
}