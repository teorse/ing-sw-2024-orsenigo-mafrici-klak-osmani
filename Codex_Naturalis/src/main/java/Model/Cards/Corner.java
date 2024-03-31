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
    private Artifacts artifact;





    //CONSTRUCTOR
    /**
     * Default constructor
     */
    public Corner(CornerType cornerType, Artifacts artifact) {
        this.cornerType = cornerType;
        this.artifact = artifact;
    }

    public Corner(CornerType cornerType) {
        this.cornerType = cornerType;
    }

    public Corner(Artifacts artifact) {
        this.cornerType = CornerType.ARTIFACT;
        this.artifact = artifact;
    }





    //GETTERS
    /**
     * @return
     */
    protected CornerType getCornerType() {
        return cornerType;
    }

    /**
     * @return
     */
    protected Artifacts getArtifact() {
        return artifact;
    }





    //EQUALS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Corner corner)) return false;
        return cornerType == corner.cornerType && artifact == corner.artifact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cornerType, artifact);
    }
}