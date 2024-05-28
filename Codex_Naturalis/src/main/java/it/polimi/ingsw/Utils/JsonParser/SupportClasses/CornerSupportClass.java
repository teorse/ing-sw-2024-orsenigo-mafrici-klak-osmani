package it.polimi.ingsw.Utils.JsonParser.SupportClasses;

import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;
import it.polimi.ingsw.Server.Model.Game.Artifacts;

public class CornerSupportClass {
    private CornerType cornerType;
    private Artifacts artifact;

    public CornerType getCornerType() {
        return cornerType;
    }

    public Artifacts getArtifact() {
        return artifact;
    }
}
