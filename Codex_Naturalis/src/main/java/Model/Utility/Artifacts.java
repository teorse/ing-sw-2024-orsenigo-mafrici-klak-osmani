package Model.Utility;

/**
 * 
 */
public enum Artifacts {
    ANIMAL(ArtifactCategories.RESOURCE),
    FUNGI(ArtifactCategories.RESOURCE),
    INSECT(ArtifactCategories.RESOURCE),
    PLANT(ArtifactCategories.RESOURCE),
    INKWELL(ArtifactCategories.ITEM),
    MANUSCRIPT(ArtifactCategories.ITEM),
    QUILL(ArtifactCategories.ITEM),
    NULL(ArtifactCategories.UTILITY);

    public final ArtifactCategories categories;
    private Artifacts(ArtifactCategories categories){
        this.categories = categories;
    }
}