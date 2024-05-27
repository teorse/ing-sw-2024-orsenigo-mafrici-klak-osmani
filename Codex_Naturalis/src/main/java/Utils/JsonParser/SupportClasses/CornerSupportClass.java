package Utils.JsonParser.SupportClasses;

import Server.Model.Game.Cards.CornerType;
import Server.Model.Game.Artifacts;

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
