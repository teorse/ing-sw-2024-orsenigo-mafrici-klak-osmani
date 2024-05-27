package Server.Model.Game.Utility.JsonParser.SupportClasses;

import Server.Model.Game.Cards.CornerType;
import Server.Model.Game.Utility.Artifacts;

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
