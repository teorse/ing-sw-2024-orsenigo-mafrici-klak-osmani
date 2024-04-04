package Model.Utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoordinatesTest {

    //DATA
    private ArrayList<Coordinates> coordinates1;
    private ArrayList<Coordinates> coordinates2;
    private ArrayList<Coordinates> coordinatesExpected;





    //TESTING PARAMETERS
    private static final int TEST_SIZE = 50; // Define test size as a constant






    @BeforeEach
    void setUp() {
        coordinates1 = new ArrayList<>();
        coordinates2 = new ArrayList<>();
        coordinatesExpected = new ArrayList<>();

        Random rand = new Random();
        int upperBound = 10000;

        for(int i = 0; i < TEST_SIZE; i++){
            int x1 = rand.nextInt(upperBound) -upperBound/2;
            int y1 = rand.nextInt(upperBound) -upperBound/2;
            coordinates1.add(new Coordinates(x1, y1));

            int x2 = rand.nextInt(upperBound) -upperBound/2;
            int y2 = rand.nextInt(upperBound) -upperBound/2;
            coordinates2.add(new Coordinates(x2, y2));

            coordinatesExpected.add(new Coordinates(x1 + x2, y1 + y2));
        }
    }

    @Test
    void add() {
        //Test known cases
        assertEquals(new Coordinates(1,2).add(new Coordinates(4,5)), new Coordinates(5,7),
                "First calibration test failed");
        assertEquals(new Coordinates(9,3).add(new Coordinates(-5,-9)), new Coordinates(4,-6),
                "Second calibration test failed");
        assertTrue(!(new Coordinates(1,2).add(new Coordinates(4,5)).equals(new Coordinates(2,1))),
                "Third calibration test failed");
        assertTrue(!(new Coordinates(9,3).add(new Coordinates(-5,-9)).equals(new Coordinates(4,35))),
                "Fourth calibration test failed");




        //Test normal cases (randomized sample)
        for(int i = 0; i < TEST_SIZE; i++){
            Coordinates result = coordinates1.get(i).add(coordinates2.get(i));
            Coordinates expected = coordinatesExpected.get(i);
            assertEquals(expected, result,
                    "Addition of coordinates failed at index " + i+"\n"+
                    "expected: ("+coordinates1.get(i).toString()+") + ("+coordinates2.get(i).toString()+") = ("+expected.toString()+")\n"+
                    "actually: ("+coordinates1.get(i).toString()+") + ("+coordinates2.get(i).toString()+") = ("+result.toString()+")");
        }
    }
}