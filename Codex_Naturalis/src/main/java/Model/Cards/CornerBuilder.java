package Model.Cards;

import Model.Other.*;
import Model.Utility.*;

public class CornerBuilder {
    //ATTRIBUTES
    private CornerDirection cornerDirection;
    private CornerType cornerType;
    private Artifacts artifacts;
    private boolean faceUp;





    //CONSTRUCTOR
    public CornerBuilder(){};





    //SETTERS
    public CornerBuilder withCornerType(CornerType cornerType) {
        this.cornerType = cornerType;
        return this;
    }

    public CornerBuilder withArtifact(Artifacts artifacts) {
        this.artifacts = artifacts;
        this.cornerType = CornerType.ARTIFACT;
        return this;
    }





    //BUILDER
    public Corner build() {
        //return new Corner(cornerDirection, cornerType, artifacts, faceUp);
        return new Corner()
                .setCornerType(cornerType)
                .setArtifact(artifacts);
    }
}