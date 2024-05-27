package Server.Model.Game.Utility.JsonParser;

import Server.Model.Game.Cards.*;
import Server.Model.Game.Utility.Artifacts;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardJsonSerializerDebugTest {

    //Lists of cards for testing serialization->deserialization
    private List<Card> resourceCards = new ArrayList<>();
    private List<Card> goldenCards = new ArrayList<>();
    private List<Card> starterCards = new ArrayList<>();


    //Lists of strings to test deserialization
    private String CardResourceJsonCorrect;
    private String CardResourceListJsonCorrect;
    private List<String> CardResourceJsonWrongValues = new ArrayList<>();
    private List<String> CardResourceJsonWrongParenthesis = new ArrayList<>();
    private List<String> CardResourceListJsonWrongValues = new ArrayList<>();
    private List<String> CardResourceListJsonWrongParenthesis = new ArrayList<>();

    private String CardGoldenJsonCorrect;
    private String CardGoldenListJsonCorrect;
    private List<String> CardGoldenJsonWrongValues = new ArrayList<>();
    private List<String> CardGoldenJsonWrongParenthesis = new ArrayList<>();
    private List<String> CardGoldenListJsonWrongValues = new ArrayList<>();
    private List<String> CardGoldenListJsonWrongParenthesis = new ArrayList<>();

    private String CardStarterJsonCorrect;
    private String CardStarterListJsonCorrect;
    private List<String> CardStarterJsonWrongValues = new ArrayList<>();
    private List<String> CardStarterJsonWrongParenthesis = new ArrayList<>();
    private List<String> CardStarterListJsonWrongValues = new ArrayList<>();
    private List<String> CardStarterListJsonWrongParenthesis = new ArrayList<>();

    private final String empty = "";

    private final int testSize = 3;





    @BeforeEach
    void setUp() {
        setUpResourceCards();
        setUpGoldenCards();
        setUpStarterCards();
        setUpResourceStrings();
    }

    public void setUpResourceCards(){
        resourceCards.add(
                new CardResource(
                        Artifacts.FUNGI,
                        new HashMap<>(){
                            {
                                put(new CornerOrientation(CornerDirection.NW, true), new Corner(Artifacts.FUNGI));
                                put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.INSECT));
                                put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
                                put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.MANUSCRIPT));
                            }}
                )
        );

        resourceCards.add(
                new CardResource(
                        Artifacts.FUNGI,
                        1,
                        new HashMap<>(){{
                    put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.FUNGI));
                    put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.NULL));
                    put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.EMPTY));
                }}
                )
        );

        resourceCards.add(
                new CardResource(
                        Artifacts.INSECT,
                        new HashMap<>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.INSECT));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(Artifacts.INSECT));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.NULL));
                        }}
                )
        );

    }
    public void setUpGoldenCards(){
        goldenCards.add(
                new CardGolden(
                        Artifacts.FUNGI,
                        1,
                        new HashMap<CornerOrientation, Corner>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.NULL));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(Artifacts.QUILL));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.NULL));
                        }},
                        Artifacts.QUILL,
                        new HashMap<>(){{
                            put(Artifacts.FUNGI, 2);
                            put(Artifacts.ANIMAL, 1);
                        }}
                        )
        );

        goldenCards.add(
                new CardGolden(
                        Artifacts.ANIMAL,
                        2,
                        new HashMap<CornerOrientation, Corner>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.NULL));
                        }},
                        true,
                        new HashMap<>(){{
                            put(Artifacts.ANIMAL, 3);
                            put(Artifacts.INSECT, 1);
                        }}
                )
        );

        goldenCards.add(
                new CardGolden(
                        Artifacts.INSECT,
                        5,
                        new HashMap<CornerOrientation, Corner>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.NULL));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.NULL));
                        }},
                        new HashMap<>(){{
                            put(Artifacts.INSECT, 5);
                        }}
                )
        );

        return;
    }
    public void setUpStarterCards(){
        starterCards.add(
                new CardStarter(
                        new HashMap<CornerOrientation, Corner>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.PLANT));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.INSECT));
                            put(new CornerOrientation(CornerDirection.NW, false), new Corner(Artifacts.FUNGI));
                            put(new CornerOrientation(CornerDirection.NE, false), new Corner(Artifacts.PLANT));
                            put(new CornerOrientation(CornerDirection.SE, false), new Corner(Artifacts.ANIMAL));
                            put(new CornerOrientation(CornerDirection.SW, false), new Corner(Artifacts.INSECT));
                        }},
                        new HashMap<>(){{
                            put(Artifacts.INSECT, 1);
                        }}
                )
        );

        starterCards.add(
                new CardStarter(
                        new HashMap<CornerOrientation, Corner>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NW, false), new Corner(Artifacts.PLANT));
                            put(new CornerOrientation(CornerDirection.NE, false), new Corner(Artifacts.INSECT));
                            put(new CornerOrientation(CornerDirection.SE, false), new Corner(Artifacts.FUNGI));
                            put(new CornerOrientation(CornerDirection.SW, false), new Corner(Artifacts.ANIMAL));
                        }},
                        new HashMap<>(){{
                            put(Artifacts.ANIMAL, 1);
                            put(Artifacts.INSECT, 1);
                        }}
                )
        );

        starterCards.add(
                new CardStarter(
                        new HashMap<CornerOrientation, Corner>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NW, false), new Corner(Artifacts.FUNGI));
                            put(new CornerOrientation(CornerDirection.NE, false), new Corner(Artifacts.ANIMAL));
                            put(new CornerOrientation(CornerDirection.SE, false), new Corner(Artifacts.INSECT));
                            put(new CornerOrientation(CornerDirection.SW, false), new Corner(Artifacts.PLANT));
                        }},
                        new HashMap<>(){{
                            put(Artifacts.PLANT, 1);
                            put(Artifacts.ANIMAL, 1);
                            put(Artifacts.FUNGI, 1);
                        }}
                )
        );

        return;
    }
    public void setUpResourceStrings(){
        String testingFilesDirectory = "src/test/java/Server/Model/Game/Utility/JsonParser/Resources/";

        try{
            CardResourceJsonCorrect = Files.readString(Path.of(testingFilesDirectory+"ResourceCardCorrect.txt"));
            CardResourceListJsonCorrect = Files.readString(Path.of(testingFilesDirectory+"ResourceCardListCorrect.txt"));
            CardGoldenJsonCorrect = Files.readString(Path.of(testingFilesDirectory+"GoldenCardCorrect.txt"));
            CardGoldenListJsonCorrect = Files.readString(Path.of(testingFilesDirectory+"GoldenCardListCorrect.txt"));
            CardStarterJsonCorrect = Files.readString(Path.of(testingFilesDirectory+"StarterCardCorrect.txt"));
            CardStarterListJsonCorrect = Files.readString(Path.of(testingFilesDirectory+"StarterCardListCorrect.txt"));

            for(int i = 1; i<6; i++){
                CardResourceJsonWrongValues.add(Files.readString(Path.of(testingFilesDirectory+"ResourceCardWrongValues"+i+".txt")));
                CardResourceJsonWrongParenthesis.add(Files.readString(Path.of(testingFilesDirectory+"ResourceCardWrongParenthesis"+i+".txt")));
                CardResourceListJsonWrongValues.add(Files.readString(Path.of(testingFilesDirectory+"ResourceCardListWrongValues"+i+".txt")));
                CardResourceListJsonWrongParenthesis.add(Files.readString(Path.of(testingFilesDirectory+"ResourceCardListWrongParenthesis"+i+".txt")));

                CardGoldenJsonWrongValues.add(Files.readString(Path.of(testingFilesDirectory+"GoldenCardWrongValues"+i+".txt")));
                CardGoldenJsonWrongParenthesis.add(Files.readString(Path.of(testingFilesDirectory+"GoldenCardWrongParenthesis"+i+".txt")));
                CardGoldenListJsonWrongValues.add(Files.readString(Path.of(testingFilesDirectory+"GoldenCardListWrongValues"+i+".txt")));
                CardGoldenListJsonWrongParenthesis.add(Files.readString(Path.of(testingFilesDirectory+"GoldenCardListWrongParenthesis"+i+".txt")));

                CardStarterJsonWrongValues.add(Files.readString(Path.of(testingFilesDirectory+"StarterCardWrongValues"+i+".txt")));
                CardStarterJsonWrongParenthesis.add(Files.readString(Path.of(testingFilesDirectory+"StarterCardWrongParenthesis"+i+".txt")));
                CardStarterListJsonWrongValues.add(Files.readString(Path.of(testingFilesDirectory+"StarterCardListWrongValues"+i+".txt")));
                CardStarterListJsonWrongParenthesis.add(Files.readString(Path.of(testingFilesDirectory+"StarterCardListWrongParenthesis"+i+".txt")));
            }
        }
        catch (IOException i){
            System.out.println(i);
        }

        return;
    }





    //Serialization itself is implemented using directly gson library methods which have already been tested by google.
    //This test verifies that the process of serialization->deserialization produces a card equivalent to the seed card.
    @Test
    void serializeCard() {
        //This method implements the serialization method of the gson library where it has already been tested
        //therefore this test is simply a "calibration" test to ensure that the gson method was implemented correctly
        //and that the process of serialization->deserialization works as expected.

        for (int i = 0; i< testSize; i++){
            Card cardResourceExpected = resourceCards.get(i);
            Card cardResourceResult = CardJsonSerializerDebug.deserializeCardResource(CardJsonSerializerDebug.serializeCard(resourceCards.get(i)));
            assertEquals(cardResourceExpected, cardResourceResult, "Serialization->Deserialization of resource card failed at index: "+i);

            Card cardGoldenExpected = goldenCards.get(i);
            Card cardGoldenResult = CardJsonSerializerDebug.deserializeCardGolden(CardJsonSerializerDebug.serializeCard(goldenCards.get(i)));
            assertEquals(cardGoldenExpected, cardGoldenResult, "Serialization->Deserialization of golden card failed at index: "+i);

            Card cardStarterExpected = starterCards.get(i);
            Card cardStarterResult = CardJsonSerializerDebug.deserializeCardStarter(CardJsonSerializerDebug.serializeCard(starterCards.get(i)));
            assertEquals(cardStarterExpected, cardStarterResult, "Serialization->Deserialization of starter card failed at index: "+i);
        }

    }


    //Testing edge cases on how the deserialization method handles strings with wrong formatting
    //Desired behaviour: JsonSyntaxException is thrown when json syntax is wrong, IllegalArgumentException is thrown when
    //Json data does not respect target class specifications.
    @Test
    void deserializeCardResource() {
        //Calibration test.
        //Making sure that method doesn't simply throw exceptions at everything but can actually successfully deserialize
        // at least one card in this test environment.
        assertAll(()-> CardJsonSerializerDebug.deserializeCardResource(CardResourceJsonCorrect));
        assertThrows(NullPointerException.class, ()-> CardJsonSerializerDebug.deserializeCardResource(empty),
                "NullPointerException was not thrown at empty String");


        //Testing that appropriate exceptions are thrown in edge cases.
        for(int i = 0; i< CardResourceJsonWrongParenthesis.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(JsonSyntaxException.class, ()-> CardJsonSerializerDebug.deserializeCardResource(CardResourceJsonWrongParenthesis.get(finalI)),
                    "JsonSyntaxException was not thrown at ResourceCardJsonWrongParenthesis"+fileNum);
        }

        for(int i = 0; i< CardResourceJsonWrongValues.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(IllegalArgumentException.class, ()-> CardJsonSerializerDebug.deserializeCardResource(CardResourceJsonWrongValues.get(finalI)),
                    "IllegalArgumentException was not thrown at ResourceCardJsonWrongValues"+fileNum);
        }
    }

    @Test
    void deserializeCardGolden() {
        //Calibration test.
        //Making sure that method doesn't simply throw exceptions at everything but can actually successfully deserialize
        // at least one card in this test environment.
        assertAll(()-> CardJsonSerializerDebug.deserializeCardGolden(CardGoldenJsonCorrect));
        assertThrows(NullPointerException.class, ()-> CardJsonSerializerDebug.deserializeCardGolden(empty),
                "NullPointerException was not thrown at empty String");

        //Testing that appropriate exceptions are thrown in edge cases.
        for(int i = 0; i< CardGoldenJsonWrongParenthesis.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(JsonSyntaxException.class, ()-> CardJsonSerializerDebug.deserializeCardGolden(CardGoldenJsonWrongParenthesis.get(finalI)),
                    "JsonSyntaxException was not thrown at GoldenCardJsonWrongParenthesis"+fileNum);
        }

        for(int i = 0; i< CardGoldenJsonWrongValues.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(IllegalArgumentException.class, ()-> CardJsonSerializerDebug.deserializeCardGolden(CardGoldenJsonWrongValues.get(finalI)),
                    "IllegalArgumentException was not thrown at GoldenCardJsonWrongValues"+fileNum);
        }
    }

    @Test
    void deserializeCardStarter() {
        //Calibration test.
        //Making sure that method doesn't simply throw exceptions at everything but can actually successfully deserialize
        // at least one card in this test environment.
        assertAll(()-> CardJsonSerializerDebug.deserializeCardStarter(CardStarterJsonCorrect));
        assertThrows(NullPointerException.class, ()-> CardJsonSerializerDebug.deserializeCardStarter(empty),
                "NullPointerException was not thrown at empty String");

        //Testing that appropriate exceptions are thrown in edge cases.
        for(int i = 0; i< CardStarterJsonWrongParenthesis.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(JsonSyntaxException.class, ()-> CardJsonSerializerDebug.deserializeCardStarter(CardStarterJsonWrongParenthesis.get(finalI)),
                    "JsonSyntaxException was not thrown at StarterCardJsonWrongParenthesis"+fileNum);
        }

        for(int i = 0; i< CardStarterJsonWrongValues.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(IllegalArgumentException.class, ()-> CardJsonSerializerDebug.deserializeCardStarter(CardStarterJsonWrongValues.get(finalI)),
                    "IllegalArgumentException was not thrown at StarterCardJsonWrongValues"+fileNum);
        }
    }



    //SerializeList and DeSerializeList internally use serializeCard and deserializeCard methods
    //For this reason extensive testing is redundant as all exceptions thrown by serializeCard and deserializeCard
    //will also necessarily be thrown by the respective methods for list serialization/deserialization.

    //Test normal cases: ensuring that serializeCardList and deserializeCardList behave as expected with known inputs.
    @Test
    void serializeCardList(){
        List<Card> cardResourceListResult = CardJsonSerializerDebug.deserializeCardResourceList(CardJsonSerializerDebug.serializeCardList(resourceCards));
        assertEquals(resourceCards, cardResourceListResult, "Serialization->Deserialization of resource card list failed");

        List<Card> cardGoldenListResult = CardJsonSerializerDebug.deserializeCardGoldenList(CardJsonSerializerDebug.serializeCardList(goldenCards));
        assertEquals(goldenCards, cardGoldenListResult, "Serialization->Deserialization of golden card list failed");

        List<Card> cardStarterListResult = CardJsonSerializerDebug.deserializeCardStarterList(CardJsonSerializerDebug.serializeCardList(starterCards));
        assertEquals(starterCards, cardStarterListResult, "Serialization->Deserialization of starter card list failed");
    }

    @Test
    void deserializeCardResourceList() {

        //Calibration test to ensure that method does not simply always throw Exceptions.
        assertAll(() -> CardJsonSerializerDebug.deserializeCardResourceList(CardResourceListJsonCorrect));

        //Testing edge cases with anomalous inputs
        assertThrows(NullPointerException.class, () -> CardJsonSerializerDebug.deserializeCardResourceList(empty));
        assertThrows(NullPointerException.class, () -> CardJsonSerializerDebug.deserializeCardResourceList("    "));
        assertThrows(NullPointerException.class, () -> CardJsonSerializerDebug.deserializeCardResourceList("   \n"));
        assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardResourceList("a"));
        assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardResourceList("aa"));
        assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardResourceList("aaa"));

        //Testing that same exceptions are thrown in edge cases as with single card deserializer.
        for (int i = 0; i < CardResourceListJsonWrongParenthesis.size(); i++) {
            int finalI = i;
            int fileNum = i + 1;
            assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardResourceList(CardResourceListJsonWrongParenthesis.get(finalI)),
                    "JsonSyntaxException was not thrown at ResourceCardListJsonWrongParenthesis" + fileNum);
        }

        for (int i = 0; i < CardResourceListJsonWrongValues.size(); i++) {
            int finalI = i;
            int fileNum = i + 1;
            assertThrows(IllegalArgumentException.class, () -> CardJsonSerializerDebug.deserializeCardResourceList(CardResourceListJsonWrongValues.get(finalI)),
                    "IllegalArgumentException was not thrown at ResourceCardListJsonWrongValues" + fileNum);

        }
    }

    @Test
    void deserializeCardGoldenList() {

        //Calibration test to ensure that method does not simply always throw Exceptions.
        assertAll(() -> CardJsonSerializerDebug.deserializeCardGoldenList(CardGoldenListJsonCorrect));

        //Testing edge cases with anomalous inputs
        assertThrows(NullPointerException.class, () -> CardJsonSerializerDebug.deserializeCardGoldenList(empty));
        assertThrows(NullPointerException.class, () -> CardJsonSerializerDebug.deserializeCardGoldenList("    "));
        assertThrows(NullPointerException.class, () -> CardJsonSerializerDebug.deserializeCardGoldenList("   \n"));
        assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardGoldenList("a"));
        assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardGoldenList("aa"));
        assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardGoldenList("aaa"));

        //Testing that same exceptions are thrown in edge cases as with single card deserializer.
        for (int i = 0; i < CardGoldenListJsonWrongParenthesis.size(); i++) {
            int finalI = i;
            int fileNum = i + 1;
            assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardGoldenList(CardGoldenListJsonWrongParenthesis.get(finalI)),
                    "JsonSyntaxException was not thrown at GoldenCardListJsonWrongParenthesis" + fileNum);
        }

        for (int i = 0; i < CardGoldenListJsonWrongValues.size(); i++) {
            int finalI = i;
            int fileNum = i + 1;
            assertThrows(IllegalArgumentException.class, () -> CardJsonSerializerDebug.deserializeCardGoldenList(CardGoldenListJsonWrongValues.get(finalI)),
                    "IllegalArgumentException was not thrown at GoldenCardListJsonWrongValues" + fileNum);

        }
    }

    @Test
    void deserializeCardStarterList(){

        //Calibration test to ensure that method does not simply always throw Exceptions.
        assertAll(() -> CardJsonSerializerDebug.deserializeCardStarterList(CardStarterListJsonCorrect));

        //Testing edge cases with anomalous inputs
        assertThrows(NullPointerException.class, () -> CardJsonSerializerDebug.deserializeCardStarterList(empty));
        assertThrows(NullPointerException.class, () -> CardJsonSerializerDebug.deserializeCardStarterList("    "));
        assertThrows(NullPointerException.class, () -> CardJsonSerializerDebug.deserializeCardStarterList("   \n"));
        assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardStarterList("a"));
        assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardStarterList("aa"));
        assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardStarterList("aaa"));

        //Testing that same exceptions are thrown in edge cases as with single card deserializer.
        for (int i = 0; i < CardStarterListJsonWrongParenthesis.size(); i++) {
            int finalI = i;
            int fileNum = i + 1;
            assertThrows(JsonSyntaxException.class, () -> CardJsonSerializerDebug.deserializeCardStarterList(CardStarterListJsonWrongParenthesis.get(finalI)),
                    "JsonSyntaxException was not thrown at GoldenCardListJsonWrongParenthesis" + fileNum);
        }

        for (int i = 0; i < CardStarterListJsonWrongValues.size(); i++) {
            int finalI = i;
            int fileNum = i + 1;
            assertThrows(IllegalArgumentException.class, () -> CardJsonSerializerDebug.deserializeCardStarterList(CardStarterListJsonWrongValues.get(finalI)),
                    "IllegalArgumentException was not thrown at GoldenCardListJsonWrongValues" + fileNum);

        }
    }





    //Tests conclusion:


    //All Card deserializers will throw JsonSyntaxException if:
    //-there are missing parenthesis.
    //-field is of int type and value is not an int.
    //-Duplicate keys in map.

    //All Card deserializers will throw IllegalArgumentException if:
    //-JsonSyntaxException is not thrown but values provided break class specifications.

    //All deserializers will throw NullPointerException if:
    //-provided string is null, empty or contains just a space " ".



    //All Card list deserializers will throw IllegalArgumentException if:
    //-provided empty string.

    //All Card list deserializers will throw NullPointerException if:
    //-provided string has no character but only spaces " " or "\n"

    //All Card list deserializers will throw JsonSyntaxException if:
    //-provided string is not empty but is also not valid.
    //-provided string has separator between list entries misplaced or missing
    //-provided string has a separator that is not preceded AND succeeded by a card.

    //All other cases are the same as with Card deserializers.
}