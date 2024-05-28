package Server.Model.Game.Cards;

import it.polimi.ingsw.Server.Model.Game.Artifacts;

import it.polimi.ingsw.Server.Model.Game.Cards.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

public class CardStarterTest {

    //ATTRIBUTES
    private Map<CornerOrientation, Corner> corners;
    private Map<Artifacts, Integer> centralArtifacts;
    private Map<Artifacts, Integer> upperArtifacts;
    private Map<Artifacts, Integer> lowerArtifacts;
    

    @BeforeEach
    public void setUp() {
        corners = new HashMap<>();
        centralArtifacts = new HashMap<>();
        upperArtifacts = new HashMap<>();
        lowerArtifacts = new HashMap<>();
        corners.put(new CornerOrientation(CornerDirection.NW, true),new Corner(CornerType.ARTIFACT,Artifacts.PLANT));
        corners.put(new CornerOrientation(CornerDirection.NE, true),new Corner(CornerType.ARTIFACT,Artifacts.FUNGI));
        corners.put(new CornerOrientation(CornerDirection.SE, true),new Corner(CornerType.ARTIFACT,Artifacts.ANIMAL));
        corners.put(new CornerOrientation(CornerDirection.SW, true),new Corner(CornerType.ARTIFACT,Artifacts.INSECT));
        corners.put(new CornerOrientation(CornerDirection.NW, false),new Corner(CornerType.EMPTY,Artifacts.NULL));
        corners.put(new CornerOrientation(CornerDirection.NE, false),new Corner(CornerType.ARTIFACT,Artifacts.PLANT));
        corners.put(new CornerOrientation(CornerDirection.SE, false),new Corner(CornerType.EMPTY,Artifacts.NULL));
        corners.put(new CornerOrientation(CornerDirection.SW, false),new Corner(CornerType.ARTIFACT,Artifacts.INSECT));
        centralArtifacts.put(Artifacts.INSECT,2);
        centralArtifacts.put(Artifacts.FUNGI,1);

        upperArtifacts.put(Artifacts.FUNGI,1);
        upperArtifacts.put(Artifacts.PLANT,1);
        upperArtifacts.put(Artifacts.ANIMAL,1);
        upperArtifacts.put(Artifacts.INSECT,1);
        lowerArtifacts.put(Artifacts.INSECT,3);
        lowerArtifacts.put(Artifacts.PLANT,1);
        lowerArtifacts.put(Artifacts.FUNGI,1);
    }

    @Test
    public void testGetAllArtifacts() {
        Card C = new CardStarter(corners,centralArtifacts);
        assertEquals(upperArtifacts, C.getAllArtifacts(true),"First test failed");
        assertEquals(lowerArtifacts, C.getAllArtifacts(false),"Second test failed");
    }

    @Test
    public void testGetCornerArtifact() {
        Card C = new CardStarter(corners,centralArtifacts);
        assertEquals(Artifacts.PLANT, C.getCornerArtifact(CornerDirection.NW,true),"1st test failed");
        assertEquals(Artifacts.FUNGI, C.getCornerArtifact(CornerDirection.NE,true),"2nd test failed");
        assertEquals(Artifacts.ANIMAL, C.getCornerArtifact(CornerDirection.SE,true),"3rd test failed");
        assertEquals(Artifacts.INSECT, C.getCornerArtifact(CornerDirection.SW,true),"4th test failed");
        assertEquals(Artifacts.NULL, C.getCornerArtifact(CornerDirection.NW,false),"5th test failed");
        assertEquals(Artifacts.PLANT, C.getCornerArtifact(CornerDirection.NE,false),"6th test failed");
        assertEquals(Artifacts.NULL, C.getCornerArtifact(CornerDirection.SE,false),"7th test failed");
        assertEquals(Artifacts.INSECT, C.getCornerArtifact(CornerDirection.SW,false),"8th test failed");
    }
}
