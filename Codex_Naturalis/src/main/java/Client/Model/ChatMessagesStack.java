package Client.Model;

import Client.Model.Records.ChatMessageRecord;

public class ChatMessagesStack {

    private final int size;
    private ChatMessageRecord[] buffer;
    private int first; // Points to the first (oldest) element
    private int last;  // Points to the last (newest) element
    private int count; // Number of elements in the buffer


    public ChatMessagesStack(int size) {
        this.size = size;
        buffer = new ChatMessageRecord[size];
        first = 0;
        last = -1;
        count = 0;
    }

    // Method to add a message to the array
    public void add(ChatMessageRecord message) {
        if (count == size) {
            // Buffer is full, overwrite the oldest message
            first = (first + 1) % size;
        } else {
            // Buffer is not full, increment the count
            count++;
        }
        // Add the new message to the buffer
        last = (last + 1) % size;
        buffer[last] = message;
    }

    // Get the first element
    public ChatMessageRecord getFirst() {
        if (count == 0) {
            return null; // Buffer is empty
        }
        return buffer[first];
    }

    // Get the last element
    public ChatMessageRecord getLast() {
        if (count == 0) {
            return null; // Buffer is empty
        }
        return buffer[last];
    }

    // Get the i-th element
    public ChatMessageRecord get(int i) {
        if (i < 0 || i >= count) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return buffer[(first + i) % size];
    }

    public int size(){
        return this.count;
    }
}
