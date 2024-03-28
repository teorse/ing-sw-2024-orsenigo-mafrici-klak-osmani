package Model.Cards;

import Model.Other.*;
import Model.Utility.*;

import java.util.Objects;

/**
 * 
 */
public class Corner {
    //ATTRIBUTES
    /**
     * 
     */
    private CornerType cornerType;
    /**
     * 
     */
    private Artifacts artifacts;





    //CONSTRUCTOR
    /**
     * Default constructor
     */
    public Corner(CornerType cornerType, Artifacts artifacts) {
        this.cornerType = cornerType;
        this.artifacts = artifacts;
    }

    public Corner(CornerType cornerType) {
        this.cornerType = cornerType;
    }

    public Corner(Artifacts artifacts) {
        this.cornerType = CornerType.ARTIFACT;
        this.artifacts = artifacts;
    }





    //GETTERS
    /**
     * @return
     */
    protected CornerType getCornerType() {
        return null;
    }

    /**
     * @return
     */
    protected Artifacts getArtifact() {
        return null;
    }





    //EQUALS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Corner corner)) return false;
        return cornerType == corner.cornerType && artifacts == corner.artifacts;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cornerType, artifacts);
    }
}