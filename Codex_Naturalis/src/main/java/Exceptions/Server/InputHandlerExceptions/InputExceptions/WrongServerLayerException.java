package Exceptions.Server.InputHandlerExceptions.InputExceptions;

/**
 * Thrown when a client input is forwarded to the wrong server layer for handling
 */
public class WrongServerLayerException extends InputHandlingException {
    private final String currentLayer;
    private final String expectedLayer;

    public WrongServerLayerException(String currentLayer, String expectedLayer, String message) {
        super(message);
        this.currentLayer = currentLayer;
        this.expectedLayer = expectedLayer;
    }
}
