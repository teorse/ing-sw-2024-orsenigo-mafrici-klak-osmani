package Model.Other;

import Model.Other.*;
import Model.Utility.*;
import Model.Cards.*;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        Card cardResourceExample1 = new CardResource(
                Artifacts.FUNGI,
                new HashMap<>(){{
                    put(new CornerOrientation(CornerDirection.NW, true), new Corner(Artifacts.FUNGI));
                    put(new CornerOrientation(CornerDirection.NE, true), new Corner(Artifacts.INSECT));
                    put(new CornerOrientation(CornerDirection.SE, true), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SW, true), new Corner(Artifacts.MANUSCRIPT));
                    put(new CornerOrientation(CornerDirection.SE, false), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SE, false), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SE, false), new Corner(CornerType.EMPTY));
                    put(new CornerOrientation(CornerDirection.SE, false), new Corner(CornerType.EMPTY));
        }});
    }
}
