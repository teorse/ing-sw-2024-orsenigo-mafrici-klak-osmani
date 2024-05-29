package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;

public class ChatMessagesStack extends Observable{
    //ATTRIBUTE
    private final int size;
    private final ChatMessageRecord[] buffer;
    private int first; // Points to the first (oldest) element
    private int last;  // Points to the last (newest) element
    private int count; // Number of elements in the buffer

    //CONSTRUCTOR
    public ChatMessagesStack(int size) {
        this.size = size;
        buffer = new ChatMessageRecord[size];
        first = 0;
        last = -1;
        count = 0;
    }





    //METHODS
    public void add(ChatMessageRecord message) {
        if (count == size) {
            first = (first + 1) % size;
        } else {
            count++;
        }
        last = (last + 1) % size;
        buffer[last] = message;

        super.updateObservers();
    }

    public ChatMessageRecord getFirst() {
        if (count == 0) {
            return null;
        }
        return buffer[first];
    }

    public ChatMessageRecord getLast() {
        if (count == 0) {
            return null;
        }
        return buffer[last];
    }

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
