package it.polimi.ingsw.Server.Model.Game;

/**
 * Enum representing different types of artifacts with their respective categories.
 * Each artifact is associated with a specific category defined in {@link ArtifactCategories}.
 */
public enum Artifacts {
    //ANIMAL(ArtifactCategories.RESOURCE, "\uD83D\uDC3E"),//Blue Paws emoji
    ANIMAL(ArtifactCategories.RESOURCE, "\uD83D\uDC3A", "Animal    "),//Wolf Face emoji
    FUNGI(ArtifactCategories.RESOURCE, "\uD83C\uDF44", "Fungi     "),
    INSECT(ArtifactCategories.RESOURCE, "\uD83D\uDC7E", "Insect    "),
    PLANT(ArtifactCategories.RESOURCE, "\uD83C\uDF3F", "Plant     "),
    //INKWELL(ArtifactCategories.ITEM, "\uD83E\uDED9"),//Jar emoji
    INKWELL(ArtifactCategories.ITEM, "\uD83C\uDFFA", "Inkwell   "),//Vase emoji
    MANUSCRIPT(ArtifactCategories.ITEM, "\uD83D\uDCDC", "Manuscript"),
    //QUILL(ArtifactCategories.ITEM, "\uD83E\uDEB6"),//Feather emoji
    QUILL(ArtifactCategories.ITEM, "\uD83D\uDD8B ", "Quill     "),//InkPen emoji
    NULL(ArtifactCategories.UTILITY,"  ", "");

    private final ArtifactCategories categories;
    private final String unicodeIcon;
    private final String name;

    private Artifacts(ArtifactCategories categories, String unicodeIcon, String name){
        this.categories = categories;
        this.unicodeIcon = unicodeIcon;
        this.name = name;
    }

    public ArtifactCategories getCategories() {
        return categories;
    }
    public String getUnicodeIcon() {
        return unicodeIcon;
    }

    public String getName() {
        return name;
    }
}