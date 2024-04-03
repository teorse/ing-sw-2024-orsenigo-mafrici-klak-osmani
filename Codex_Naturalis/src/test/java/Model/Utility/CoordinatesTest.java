package Model.Utility;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

public class CoordinatesTest {

    //DATA
    private ArrayList<Coordinates> coordinates1;
    private ArrayList<Coordinates> coordinates2;
    private ArrayList<Coordinates> coordinatesExpected;





    //TESTING PARAMETERS
    private static final int TEST_SIZE = 50; // Define test size as a constant





    @Before
    public void setUp() throws Exception {

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
    public void testAdd() {
        //Test known cases
        Assert.assertEquals("First calibration test failed", new Coordinates(1,2).add(new Coordinates(4,5)), new Coordinates(5,7));
        Assert.assertEquals("Second calibration test failed", new Coordinates(9,3).add(new Coordinates(-5,-9)), new Coordinates(4,-6));
        Assert.assertTrue("Third calibration test failed", !(new Coordinates(1,2).add(new Coordinates(4,5)).equals(new Coordinates(2,1))));
        Assert.assertTrue("Fourth calibration test failed", !(new Coordinates(9,3).add(new Coordinates(-5,-9)).equals(new Coordinates(4,35))));




        //Test normal cases (randomized sample)
        for(int i = 0; i < TEST_SIZE; i++){
            Coordinates result = coordinates1.get(i).add(coordinates2.get(i));
            Coordinates expected = coordinatesExpected.get(i);
            Assert.assertEquals("Addition of coordinates failed at index " + i+"\n"+
                            "expected: ("+coordinates1.get(i).toString()+") + ("+coordinates2.get(i).toString()+") = ("+expected.toString()+")\n"+
                            "actually: ("+coordinates1.get(i).toString()+") + ("+coordinates2.get(i).toString()+") = ("+result.toString()+")", expected, result);
        }
    }
}