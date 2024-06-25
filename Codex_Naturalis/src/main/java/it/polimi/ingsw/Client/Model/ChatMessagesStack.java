package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;

/**
 * The ChatMessagesStack class represents a circular buffer for storing chat messages.
 * It extends Observable to notify observers of changes in the stack.
 */
public class ChatMessagesStack extends Observable {
    // ATTRIBUTES
    private final int size;
    private final ChatMessageRecord[] buffer;
    private int first; // Points to the first (oldest) element
    private int last;  // Points to the last (newest) element
    private int count; // Number of elements in the buffer

    // CONSTRUCTOR
    /**
     * Constructs a ChatMessagesStack with a specified size.
     *
     * @param size The size of the circular buffer.
     */
    public ChatMessagesStack(int size) {
        this.size = size;
        buffer = new ChatMessageRecord[size];
        first = 0;
        last = -1;
        count = 0;
    }

    // METHODS
    /**
     * Adds a new chat message to the stack.
     * If the stack is full, the oldest message is overwritten.
     *
     * @param message The ChatMessageRecord to add to the stack.
     */
    public void add(ChatMessageRecord message) {
        if (count == size) {
            first = (first + 1) % size; // Move the first pointer to overwrite the oldest message
        } else {
            count++;
        }
        last = (last + 1) % size; // Move the last pointer to insert the new message
        buffer[last] = message;

        super.updateObservers(); // Notify observers of the change
    }

    /**
     * Retrieves the oldest message (first in the stack).
     *
     * @return The oldest ChatMessageRecord in the stack, or null if the stack is empty.
     */
    public ChatMessageRecord getFirst() {
        if (count == 0) {
            return null;
        }
        return buffer[first];
    }

    /**
     * Retrieves the newest message (last in the stack).
     *
     * @return The newest ChatMessageRecord in the stack, or null if the stack is empty.
     */
    public ChatMessageRecord getLast() {
        if (count == 0) {
            return null;
        }
        return buffer[last];
    }

    /**
     * Retrieves the message at a specific index in the stack.
     *
     * @param i The index of the message to retrieve.
     * @return The ChatMessageRecord at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public ChatMessageRecord get(int i) {
        if (i < 0 || i >= count) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return buffer[(first + i) % size];
    }

    /**
     * Returns the number of messages currently in the stack.
     *
     * @return The number of messages in the stack.
     */
    public int size() {
        return this.count;
    }
}
