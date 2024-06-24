package it.polimi.ingsw.Server.Model.Game.Player;

import it.polimi.ingsw.Server.Model.Game.Cards.CornerDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
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

    /**
     * Test the cornerOfAdjacency method for various coordinate offsets from the origin (0, 0).
     */
    @Test
    public void testCornerOfAdjacencyFromOrigin() {
        Coordinates origin = new Coordinates(0, 0);

        // Coordinate offset (1, 1) should return NE
        Coordinates ne = new Coordinates(1, 1);
        assertEquals(Optional.of(CornerDirection.NE), origin.cornerOfAdjacency(ne), "Offset (1, 1) should return NE");

        // Coordinate offset (-1, 1) should return NW
        Coordinates nw = new Coordinates(-1, 1);
        assertEquals(Optional.of(CornerDirection.NW), origin.cornerOfAdjacency(nw), "Offset (-1, 1) should return NW");

        // Coordinate offset (-1, -1) should return SW
        Coordinates sw = new Coordinates(-1, -1);
        assertEquals(Optional.of(CornerDirection.SW), origin.cornerOfAdjacency(sw), "Offset (-1, -1) should return SW");

        // Coordinate offset (1, -1) should return SE
        Coordinates se = new Coordinates(1, -1);
        assertEquals(Optional.of(CornerDirection.SE), origin.cornerOfAdjacency(se), "Offset (1, -1) should return SE");

        // Coordinate offset (0, 1) should return empty (invalid offset)
        Coordinates invalid1 = new Coordinates(0, 1);
        assertEquals(Optional.empty(), origin.cornerOfAdjacency(invalid1), "Offset (0, 1) should return empty");

        // Coordinate offset (1, 0) should return empty (invalid offset)
        Coordinates invalid2 = new Coordinates(1, 0);
        assertEquals(Optional.empty(), origin.cornerOfAdjacency(invalid2), "Offset (1, 0) should return empty");

        // Coordinate offset (0, 0) should return empty (no offset)
        Coordinates same = new Coordinates(0, 0);
        assertEquals(Optional.empty(), origin.cornerOfAdjacency(same), "Offset (0, 0) should return empty");

        // Coordinate offset (2, 2) should return empty (outside the radius of 1)
        Coordinates outsideRadius = new Coordinates(2, 2);
        assertEquals(Optional.empty(), origin.cornerOfAdjacency(outsideRadius), "Offset (2, 2) should return empty");
    }

    /**
     * Test the cornerOfAdjacency method for various coordinate offsets from a non-origin starting point.
     */
    @Test
    public void testCornerOfAdjacencyFromNonOrigin() {
        Coordinates start = new Coordinates(3, 3);

        // Coordinate offset (1, 1) should return NE
        Coordinates ne = new Coordinates(4, 4);
        assertEquals(Optional.of(CornerDirection.NE), start.cornerOfAdjacency(ne), "Offset (1, 1) from (3, 3) should return NE");

        // Coordinate offset (-1, 1) should return NW
        Coordinates nw = new Coordinates(2, 4);
        assertEquals(Optional.of(CornerDirection.NW), start.cornerOfAdjacency(nw), "Offset (-1, 1) from (3, 3) should return NW");

        // Coordinate offset (-1, -1) should return SW
        Coordinates sw = new Coordinates(2, 2);
        assertEquals(Optional.of(CornerDirection.SW), start.cornerOfAdjacency(sw), "Offset (-1, -1) from (3, 3) should return SW");

        // Coordinate offset (1, -1) should return SE
        Coordinates se = new Coordinates(4, 2);
        assertEquals(Optional.of(CornerDirection.SE), start.cornerOfAdjacency(se), "Offset (1, -1) from (3, 3) should return SE");

        // Coordinate offset (0, 1) should return empty (invalid offset)
        Coordinates invalid1 = new Coordinates(3, 4);
        assertEquals(Optional.empty(), start.cornerOfAdjacency(invalid1), "Offset (0, 1) from (3, 3) should return empty");

        // Coordinate offset (1, 0) should return empty (invalid offset)
        Coordinates invalid2 = new Coordinates(4, 3);
        assertEquals(Optional.empty(), start.cornerOfAdjacency(invalid2), "Offset (1, 0) from (3, 3) should return empty");

        // Coordinate offset (0, 0) should return empty (no offset)
        Coordinates same = new Coordinates(3, 3);
        assertEquals(Optional.empty(), start.cornerOfAdjacency(same), "Offset (0, 0) from (3, 3) should return empty");

        // Coordinate offset (2, 2) should return empty (outside the radius of 1)
        Coordinates outsideRadius = new Coordinates(5, 5);
        assertEquals(Optional.empty(), start.cornerOfAdjacency(outsideRadius), "Offset (2, 2) from (3, 3) should return empty");
    }
}