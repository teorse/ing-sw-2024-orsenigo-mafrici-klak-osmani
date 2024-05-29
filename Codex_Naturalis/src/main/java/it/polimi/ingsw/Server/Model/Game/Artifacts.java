package it.polimi.ingsw.Server.Model.Game;

/**
 * 
 */
//TODO fix the label
public enum Artifacts {
    ANIMAL(ArtifactCategories.RESOURCE),
    FUNGI(ArtifactCategories.RESOURCE),
    INSECT(ArtifactCategories.RESOURCE),
    PLANT(ArtifactCategories.RESOURCE),
    INKWELL(ArtifactCategories.ITEM),
    MANUSCRIPT(ArtifactCategories.ITEM),
    QUILL(ArtifactCategories.ITEM),
    NULL(ArtifactCategories.UTILITY);

    private final ArtifactCategories categories;
    private Artifacts(ArtifactCategories categories){
        this.categories = categories;
    }

    public ArtifactCategories getCategories() {
        return categories;
    }
}