package Model.Other;

import Cards.*;
import Utility.Artifacts;

public class Main {
    public static void main(String[] args) {

        Card cardResourceExample = new CardResourceBuilder()
                .withCardColor(Artifacts.FUNGI)
                .withCorners(
                        new CornerMapBuilder()
                                .withCorner(
                                        new CornerOrientationBuilder()
                                                .withCornerDirection(CornerDirection.NW)
                                                .onCardsFace()
                                                .build(),
                                        new CornerBuilder()
                                                .withArtifact(Artifacts.FUNGI)
                                                .build()
                                )
                                .withCorner(
                                        new CornerOrientationBuilder()
                                                .withCornerDirection(CornerDirection.NE)
                                                .onCardsFace()
                                                .build(),
                                        new CornerBuilder()
                                                .withArtifact(Artifacts.INSECT)
                                                .build()
                                )
                                .withCorner(
                                        new CornerOrientationBuilder()
                                                .withCornerDirection(CornerDirection.SE)
                                                .onCardsFace()
                                                .build(),
                                        new CornerBuilder()
                                                .withCornerType(CornerType.EMPTY)
                                                .build()
                                )
                                .withCorner(
                                        new CornerOrientationBuilder()
                                                .withCornerDirection(CornerDirection.SW)
                                                .onCardsFace()
                                                .build(),
                                        new CornerBuilder()
                                                .withArtifact(Artifacts.MANUSCRIPT)
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}
