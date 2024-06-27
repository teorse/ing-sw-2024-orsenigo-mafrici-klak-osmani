package it.polimi.ingsw.Server.Model.Game;

/**
 * 
 */
//TODO fix the label
public enum Artifacts {
    //ANIMAL(ArtifactCategories.RESOURCE, "\uD83D\uDC3E"),//Blue Paws emoji
    ANIMAL(ArtifactCategories.RESOURCE, "\uD83D\uDC3A"),//Wolf Face emoji
    FUNGI(ArtifactCategories.RESOURCE, "\uD83C\uDF44"),
    INSECT(ArtifactCategories.RESOURCE, "\uD83D\uDC7E"),
    PLANT(ArtifactCategories.RESOURCE, "\uD83C\uDF3F"),
    //INKWELL(ArtifactCategories.ITEM, "\uD83E\uDED9"),//Jar emoji
    INKWELL(ArtifactCategories.ITEM, "\uD83C\uDFFA"),//Vase emoji
    MANUSCRIPT(ArtifactCategories.ITEM, "\uD83D\uDCDC"),
    //QUILL(ArtifactCategories.ITEM, "\uD83E\uDEB6"),//Feather emoji
    QUILL(ArtifactCategories.ITEM, "\uD83D\uDD8B "),//InkPen emoji
    NULL(ArtifactCategories.UTILITY,"  ");

    private final ArtifactCategories categories;
    private final String unicodeIcon;
    private Artifacts(ArtifactCategories categories, String unicodeIcon){
        this.categories = categories;
        this.unicodeIcon = unicodeIcon;
    }

    public ArtifactCategories getCategories() {
        return categories;
    }
    public String getUnicodeIcon(){
        return unicodeIcon;
    }
}