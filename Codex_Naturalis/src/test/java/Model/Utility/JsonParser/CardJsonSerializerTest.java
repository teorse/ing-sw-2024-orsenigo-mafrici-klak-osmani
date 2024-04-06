package Model.Utility.JsonParser;

import Model.Cards.*;
import Model.Utility.Artifacts;
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

public class CardJsonSerializerTest {

    //Lists of cards for testing serialization->deserialization
    private List<Card> resourceCards = new ArrayList<>();
    private List<Card> goldenCards = new ArrayList<>();
    private List<Card> starterCards = new ArrayList<>();


    //Lists of strings to test deserialization
    private String CardResourceJsonCorrect;
    private List<String> CardResourceJsonWrongValues = new ArrayList<>();
    private List<String> CardResourceJsonWrongParenthesis = new ArrayList<>();

    private String CardGoldenJsonCorrect;
    private List<String> CardGoldenJsonWrongValues = new ArrayList<>();
    private List<String> CardGoldenJsonWrongParenthesis = new ArrayList<>();

    private String CardStarterJsonCorrect;
    private List<String> CardStarterJsonWrongValues = new ArrayList<>();
    private List<String> CardStarterJsonWrongParenthesis = new ArrayList<>();

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
                        new HashMap<>(){{
                            put(Artifacts.INSECT, 1);
                        }},
                        new HashMap<CornerOrientation, Corner>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.PLANT));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.INSECT));
                            put(new CornerOrientation(CornerDirection.NW, false), new Corner(Artifacts.FUNGI));
                            put(new CornerOrientation(CornerDirection.NE, false), new Corner(Artifacts.PLANT));
                            put(new CornerOrientation(CornerDirection.SE, false), new Corner(Artifacts.ANIMAL));
                            put(new CornerOrientation(CornerDirection.SW, false), new Corner(Artifacts.INSECT));
                        }}
                )
        );

        starterCards.add(
                new CardStarter(
                        new HashMap<>(){{
                            put(Artifacts.ANIMAL, 1);
                            put(Artifacts.INSECT, 1);
                        }},
                        new HashMap<CornerOrientation, Corner>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NW, false), new Corner(Artifacts.PLANT));
                            put(new CornerOrientation(CornerDirection.NE, false), new Corner(Artifacts.INSECT));
                            put(new CornerOrientation(CornerDirection.SE, false), new Corner(Artifacts.FUNGI));
                            put(new CornerOrientation(CornerDirection.SW, false), new Corner(Artifacts.ANIMAL));
                        }}
                )
        );

        starterCards.add(
                new CardStarter(
                        new HashMap<>(){{
                            put(Artifacts.PLANT, 1);
                            put(Artifacts.ANIMAL, 1);
                            put(Artifacts.FUNGI, 1);
                        }},
                        new HashMap<CornerOrientation, Corner>(){{
                            put(new CornerOrientation(CornerDirection.NW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.SW, true), new Corner(CornerType.EMPTY));
                            put(new CornerOrientation(CornerDirection.NW, false), new Corner(Artifacts.FUNGI));
                            put(new CornerOrientation(CornerDirection.NE, false), new Corner(Artifacts.ANIMAL));
                            put(new CornerOrientation(CornerDirection.SE, false), new Corner(Artifacts.INSECT));
                            put(new CornerOrientation(CornerDirection.SW, false), new Corner(Artifacts.PLANT));
                        }}
                )
        );

        return;
    }
    public void setUpResourceStrings(){
        String testingFilesDirectory = "src/test/java/Model/Utility/JsonParser/Resources/";

        try{
            CardResourceJsonCorrect = Files.readString(Path.of(testingFilesDirectory+"ResourceCardCorrect.txt"));
            CardGoldenJsonCorrect = Files.readString(Path.of(testingFilesDirectory+"GoldenCardCorrect.txt"));
            CardStarterJsonCorrect = Files.readString(Path.of(testingFilesDirectory+"StarterCardCorrect.txt"));

            for(int i = 1; i<6; i++){
                CardResourceJsonWrongValues.add(Files.readString(Path.of(testingFilesDirectory+"ResourceCardWrongValues"+i+".txt")));
                CardResourceJsonWrongParenthesis.add(Files.readString(Path.of(testingFilesDirectory+"ResourceCardWrongParenthesis"+i+".txt")));

                CardGoldenJsonWrongValues.add(Files.readString(Path.of(testingFilesDirectory+"GoldenCardWrongValues"+i+".txt")));
                CardGoldenJsonWrongParenthesis.add(Files.readString(Path.of(testingFilesDirectory+"GoldenCardWrongParenthesis"+i+".txt")));

                CardStarterJsonWrongValues.add(Files.readString(Path.of(testingFilesDirectory+"StarterCardWrongValues"+i+".txt")));
                CardStarterJsonWrongParenthesis.add(Files.readString(Path.of(testingFilesDirectory+"StarterCardWrongParenthesis"+i+".txt")));
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
        //therefore this test is simply a ""calibration" test to ensure that the gson method was implemented correctly
        //and that the process of serialization->deserialization works as expected.

        for (int i = 0; i< testSize; i++){
            Card cardResourceExpected = resourceCards.get(i);
            Card cardResourceResult = CardJsonSerializer.deserializeCardResource(CardJsonSerializer.serializeCard(resourceCards.get(i)));
            assertEquals(cardResourceExpected, cardResourceResult, "Serialization->Deserialization of resource card failed at index: "+i);

            Card cardGoldenExpected = goldenCards.get(i);
            Card cardGoldenResult = CardJsonSerializer.deserializeCardGolden(CardJsonSerializer.serializeCard(goldenCards.get(i)));
            assertEquals(cardGoldenExpected, cardGoldenResult, "Serialization->Deserialization of golden card failed at index: "+i);

            Card cardStarterExpected = starterCards.get(i);
            Card cardStarterResult = CardJsonSerializer.deserializeCardStarter(CardJsonSerializer.serializeCard(starterCards.get(i)));
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
        assertAll(()-> CardJsonSerializer.deserializeCardResource(CardResourceJsonCorrect));
        assertThrows(NullPointerException.class, ()-> CardJsonSerializer.deserializeCardResource(empty),
                "NullPointerException was not thrown at empty String");

        //Testing that appropriate exceptions are thrown in edge cases.
        for(int i = 0; i< CardResourceJsonWrongParenthesis.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(JsonSyntaxException.class, ()-> CardJsonSerializer.deserializeCardResource(CardResourceJsonWrongParenthesis.get(finalI)),
                    "JsonSyntaxException was not thrown at ResourceCardJsonWrongParenthesis"+fileNum);
        }

        for(int i = 0; i< CardResourceJsonWrongValues.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(IllegalArgumentException.class, ()-> CardJsonSerializer.deserializeCardResource(CardResourceJsonWrongValues.get(finalI)),
                    "IllegalArgumentException was not thrown at ResourceCardJsonWrongValues"+fileNum);
        }
    }

    @Test
    void deserializeCardGolden() {
        //Calibration test.
        //Making sure that method doesn't simply throw exceptions at everything but can actually successfully deserialize
        // at least one card in this test environment.
        assertAll(()-> CardJsonSerializer.deserializeCardGolden(CardGoldenJsonCorrect));
        assertThrows(NullPointerException.class, ()-> CardJsonSerializer.deserializeCardGolden(empty),
                "NullPointerException was not thrown at empty String");

        //Testing that appropriate exceptions are thrown in edge cases.
        for(int i = 0; i< CardGoldenJsonWrongParenthesis.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(JsonSyntaxException.class, ()-> CardJsonSerializer.deserializeCardGolden(CardGoldenJsonWrongParenthesis.get(finalI)),
                    "JsonSyntaxException was not thrown at GoldenCardJsonWrongParenthesis"+fileNum);
        }

        for(int i = 0; i< CardGoldenJsonWrongValues.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(IllegalArgumentException.class, ()-> CardJsonSerializer.deserializeCardGolden(CardGoldenJsonWrongValues.get(finalI)),
                    "IllegalArgumentException was not thrown at GoldenCardJsonWrongValues"+fileNum);
        }
    }

    @Test
    void deserializeCardStarter() {
        //Calibration test.
        //Making sure that method doesn't simply throw exceptions at everything but can actually successfully deserialize
        // at least one card in this test environment.
        assertAll(()-> CardJsonSerializer.deserializeCardStarter(CardStarterJsonCorrect));
        assertThrows(NullPointerException.class, ()-> CardJsonSerializer.deserializeCardStarter(empty),
                "NullPointerException was not thrown at empty String");

        //Testing that appropriate exceptions are thrown in edge cases.
        for(int i = 0; i< CardStarterJsonWrongParenthesis.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(JsonSyntaxException.class, ()-> CardJsonSerializer.deserializeCardStarter(CardStarterJsonWrongParenthesis.get(finalI)),
                    "JsonSyntaxException was not thrown at StarterCardJsonWrongParenthesis"+fileNum);
        }

        for(int i = 0; i< CardStarterJsonWrongValues.size(); i++){
            int finalI = i;
            int fileNum = i+1;
            assertThrows(IllegalArgumentException.class, ()-> CardJsonSerializer.deserializeCardStarter(CardStarterJsonWrongValues.get(finalI)),
                    "IllegalArgumentException was not thrown at StarterCardJsonWrongValues"+fileNum);
        }
    }


    //Test conclusion:


    //All deserializers will throw JsonSyntaxException if:
    //-there are missing parenthesis.
    //-field is of int type and value is not an int.
    //-Duplicate keys in map

    //All deserializers will throw IllegalArgumentException if:
    //-JsonSyntaxException is not thrown but values provided break class specifications.

    //All deserializers will throw NullPointerException if:
    //-provided string is null, empty or contains just a space " ".
}