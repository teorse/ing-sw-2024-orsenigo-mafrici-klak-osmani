package Client.Model.Records;

import java.security.Timestamp;
import java.util.Date;

public class ChatMessageRecord {
    //ATTRIBUTES
    private final Date timestamp;
    private String sender;
    private final String message;
    private final boolean messagePrivate;
    private final String recipient;

    //CONSTRUCTOR
    public ChatMessageRecord(String message, String recipient) {
        this.timestamp = new Date();
        this.message = message;
        this.recipient = recipient;
        messagePrivate = true;
    }
    public ChatMessageRecord(String message){
        this.timestamp = new Date();
        this.message = message;
        this.recipient = null;
        messagePrivate = false;
    }


    public void setSender(String sender) {
        this.sender = sender;
    }


    public boolean isMessagePrivate() {
        return messagePrivate;
    }
    public String getRecipient() {
        return recipient;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public String getSender() {
        return sender;
    }
    public String getMessage() {
        return message;
    }
    @Override
    public String toString() {
        return timestamp + " - " + sender + ": " + message;
    }
}
