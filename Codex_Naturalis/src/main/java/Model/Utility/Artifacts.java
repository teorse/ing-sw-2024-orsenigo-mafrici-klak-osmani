package Model.Utility;

/**
 * 
 */
public enum Artifacts {
    ANIMAL(ArtifactCategories.RESOURCE),
    FUNGI(ArtifactCategories.RESOURCE),
    INSECT(ArtifactCategories.RESOURCE),
    PLANT(ArtifactCategories.RESOURCE),
    INKWELL(ArtifactCategories.RESOURCE),
    MANUSCRIPT(ArtifactCategories.RESOURCE),
    QUILL(ArtifactCategories.RESOURCE),
    NULL(ArtifactCategories.RESOURCE);

    public final ArtifactCategories categories;
    private Artifacts(ArtifactCategories categories){
        this.categories = categories;
    }
}