package Model.Player;

import Model.Utility.Artifacts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardMapTest {

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
                "First calibration test failed");
    }

    @Test
    void getAmountOfArtifactsDecrementArtifactTest() {
        cardMap.changeArtifactAmount(Artifacts.ANIMAL, -2);
        //Test known cases
        assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.ANIMAL),
                "First calibration test failed");
    }

    @Test
    void getAmountOfArtifactsMissingArtifactTest() {
        //Test known cases
        assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.PLANT),
                "Second calibration test failed");
    }

    @Test
    void getAmountOfArtifactsZeroArtifactTest() {
        //Test known cases
        assertEquals(0, cardMap.getAmountOfArtifacts(Artifacts.INSECT),
                "Second calibration test failed");
    }
}