package Server.Controller.InputHandler.Exceptions;

import Server.Exceptions.ServerException;

public class WrongServerLayerException extends ServerException {
    private final String currentLayer;
    private final String expectedLayer;

    public WrongServerLayerException(String currentLayer, String expectedLayer, String message) {
        super(message);
        this.currentLayer = currentLayer;
        this.expectedLayer = expectedLayer;
    }
}
