package it.polimi.ingsw.Server.Model.Game.Cards;

import it.polimi.ingsw.Server.Model.Game.Cards.*;
import it.polimi.ingsw.Server.Model.Game.Player.CardMap;
import it.polimi.ingsw.Server.Model.Game.Player.CardVisibility;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CardGoldenTest {

    //ATTRIBUTES
    private CardMap CM;
    private Map<Artifacts, Integer> constraint;
    private Map<CornerOrientation, Corner> corners;


    @BeforeEach
    public void setUp() {
        CM = new CardMap(null, "userTest");
        constraint = new HashMap<>();
        corners = new HashMap<>();
    }

    @Test
    public void testIsPlaceable() {
        constraint.put(Artifacts.ANIMAL,3);
        constraint.put(Artifacts.FUNGI,2);
        constraint.put(Artifacts.INSECT,0);
        constraint.put(Artifacts.PLANT,1);


        Card cheatCard = new CardStarter(
                new HashMap<>(){{
                    put(new CornerOrientation(CornerDirection.NW,true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.NE,true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SE,true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SW,true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.NW,false), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.NE,false), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SE,false), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SW,false), new Corner(CornerType.EMPTY));
                }},
                new HashMap<>(){{
                    put(Artifacts.ANIMAL, 3);
                    put(Artifacts.FUNGI, 5);
                    put(Artifacts.INSECT, 2);
                    put(Artifacts.PLANT, 1);
                }}
        );

        assertDoesNotThrow(() -> CM.place(cheatCard, 0, false));

        Card c = new CardGolden(Artifacts.ANIMAL, 2, corners, Artifacts.QUILL, constraint);
        assertTrue(c.isPlaceable(CM));
    }

    @Test
    public void testCountPointsArtifact() {
        Card cheatCard = new CardStarter(
                new HashMap<>(){{
                    put(new CornerOrientation(CornerDirection.NW,true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.NE,true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SE,true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SW,true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.NW,false), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.NE,false), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SE,false), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SW,false), new Corner(CornerType.EMPTY));
                }},
                new HashMap<>(){{
                    put(Artifacts.INKWELL, 2);
                    put(Artifacts.MANUSCRIPT, 3);
                    put(Artifacts.QUILL, 5);
                }}
        );

        assertDoesNotThrow(() -> CM.place(cheatCard, 0, false));

        Coordinates coordinates = new Coordinates(0,0);
        Card c1 = new CardGolden(Artifacts.ANIMAL, 2, corners, Artifacts.INKWELL, constraint);
        Card c2 = new CardGolden(Artifacts.ANIMAL, 3, corners, Artifacts.MANUSCRIPT, constraint);
        Card c3 = new CardGolden(Artifacts.ANIMAL, 1, corners, Artifacts.QUILL, constraint);
        assertEquals(4, c1.countPoints(CM,coordinates,true),"First test failed");
        assertEquals(9, c2.countPoints(CM,coordinates,true),"Second test failed");
        assertEquals(5, c3.countPoints(CM,coordinates,true),"Third test failed");
    }

    @Test
    public void testCountPointsCorners() {
        Card testCard = new CardResource(Artifacts.ANIMAL, corners);
        Map<Coordinates, CardVisibility> cardsPlacedValue = new HashMap<>();
        Map<Coordinates, CardVisibility> cardsPlacedValue1 = new HashMap<>();
        Map<Coordinates, CardVisibility> cardsPlacedValue2 = new HashMap<>();
        Map<Coordinates, CardVisibility> cardsPlacedValue3 = new HashMap<>();

        CardVisibility CV = new CardVisibility(testCard, true);
        Card c1 = new CardGolden(Artifacts.ANIMAL, 2, corners, true, constraint);
        Card c2 = new CardGolden(Artifacts.ANIMAL, 3, corners, true, constraint);
        Coordinates coord0 = new Coordinates(0,0);

        cardsPlacedValue1.put(new Coordinates(1,1), CV);
        cardsPlacedValue2.put(new Coordinates(1,1), CV);
        cardsPlacedValue2.put(new Coordinates(1,-1), CV);
        cardsPlacedValue3.put(new Coordinates(1,1), CV);
        cardsPlacedValue3.put(new Coordinates(1,-1), CV);
        cardsPlacedValue3.put(new Coordinates(-1,-1), CV);
        cardsPlacedValue.put(new Coordinates(1,1), CV);
        cardsPlacedValue.put(new Coordinates(1,-1), CV);
        cardsPlacedValue.put(new Coordinates(-1,-1), CV);
        cardsPlacedValue.put(new Coordinates(-1,1), CV);

        CardMap CM1 = CardMapReflectionBuilder(cardsPlacedValue1);
        CardMap CM2 = CardMapReflectionBuilder(cardsPlacedValue2);
        CardMap CM3 = CardMapReflectionBuilder(cardsPlacedValue3);
        CM = CardMapReflectionBuilder(cardsPlacedValue);

        assertEquals(2, c1.countPoints(CM1,coord0,true),"1st test failed");
        assertEquals(3, c2.countPoints(CM1,coord0,true),"2nd test failed");
        assertEquals(4, c1.countPoints(CM2,coord0,true),"3rd test failed");
        assertEquals(6, c2.countPoints(CM2,coord0,true),"4th test failed");
        assertEquals(6, c1.countPoints(CM3,coord0,true),"5th test failed");
        assertEquals(9, c2.countPoints(CM3,coord0,true),"6th test failed");
        assertEquals(8, c1.countPoints(CM,coord0,true),"7th test failed");
        assertEquals(12, c2.countPoints(CM,coord0,true),"8th test failed");
        assertEquals(0, c1.countPoints(CM,coord0,false),"9th test failed");
        assertEquals(0, c2.countPoints(CM,coord0,false),"10th test failed");
    }

    public CardMap CardMapReflectionBuilder(Map<Coordinates, CardVisibility> cardsPlacedValue){
        CardMap cardMap = new CardMap(null, "");
        Field field0;
        try{
            field0 = CardMap.class.getDeclaredField("cardsPlaced");
            field0.setAccessible(true);
            field0.set(cardMap, cardsPlacedValue);
        }
        catch (NoSuchFieldException | IllegalAccessException m){
            System.out.println(m);
        }
        return cardMap;
    }
}
