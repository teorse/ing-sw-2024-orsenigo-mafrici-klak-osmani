package it.polimi.ingsw.Server.Model.Game.Cards;

import it.polimi.ingsw.Server.Model.Game.Artifacts;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class stores information about individual corners of a card.<br>
 * If the corner type is EMPTY or NULL then its artifact will always be NULL, conversely if the artifact is
 * NULL then Corner will always be expected to be EMPTY or NULL.<br>
 * If the artifact is not NULL then corner type will always be ARTIFACT, conversely if the corner type
 * is ARTIFACT then the artifact is always expected to not be NULL.<br>
 * CornerType and artifact are always explicitly stated.
 */
public class Corner implements Serializable {

    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -2674400925262245787L;
    private CornerType cornerType;
    private Artifacts artifact;





    //CONSTRUCTOR
    public Corner(CornerType cornerType, Artifacts artifact) {
        if((cornerType.equals(CornerType.NULL) || cornerType.equals(CornerType.EMPTY)) && !artifact.equals(Artifacts.NULL))
            throw new IllegalArgumentException("Incompatible CornerType and Artifact.\nProvided not null artifact but " +
                    "corner is empty or null.");

        this.cornerType = cornerType;
        this.artifact = artifact;
    }

    /**
     * Constructor that can be used if corner is EMPTY or NULL, automatically assigns to artifact value Artifacts.NULL.
     * @param cornerType
     */
    public Corner(CornerType cornerType) {
        this.cornerType = cornerType;

        if (!cornerType.equals(CornerType.ARTIFACT))
            artifact = Artifacts.NULL;
        else throw new IllegalArgumentException("Missing Artifact name.\nProvided (CornerType.ARTIFACT), expected (CornerType.ARTIFACT, Artifacts) " +
                "or (CornerType.NULL) or (CornerType.EMPTY).");

    }

    /**
     * Constructor that can be used if corner has an artifact that is not Artifacts.NULL, automatically assigns
     * to cornerType value CornerType.ARTIFACT.
     * @param artifact
     */
    public Corner(Artifacts artifact) {
        if(artifact.equals(Artifacts.NULL))
            throw  new IllegalArgumentException("Missing Corner type.\nProvided (Artifacts.NULL), expected explicit CornerType " +
                    "or not null Artifact.");

        this.cornerType = CornerType.ARTIFACT;
        this.artifact = artifact;
    }





    //GETTERS
    public CornerType getCornerType() {
        return cornerType;
    }
    public Artifacts getArtifact() {
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