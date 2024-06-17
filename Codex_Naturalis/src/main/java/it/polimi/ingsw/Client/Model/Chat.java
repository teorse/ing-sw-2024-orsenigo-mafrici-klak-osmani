package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;

import java.util.Map;

public class Chat extends Observable{
    //SINGLETON PATTERN
    private static Chat INSTANCE;
    private Chat(){}
    public static Chat getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Chat();
        }

        return INSTANCE;
    }

    private boolean newMessages;





    //ATTRIBUTES
    ChatMessagesStack publicChatMessages;
    Map<String, ChatMessagesStack> privateChatMessages;






    //GETTERS
    public ChatMessagesStack getPublicChat(){
        return publicChatMessages;
    }
    public ChatMessagesStack getPrivateChat(String recipient){
        return privateChatMessages.get(recipient);
    }
    public boolean isNewMessages() {
        return newMessages;
    }





    //SETTERS
    public void setPublicChatMessages(ChatMessagesStack publicChatMessages) {
        this.publicChatMessages = publicChatMessages;
        super.updateObservers();
    }
    public void setPrivateChatMessages(Map<String, ChatMessagesStack> privateChatMessages) {
        this.privateChatMessages = privateChatMessages;
        super.updateObservers();
    }
    public void setSpecificPrivateChatMessages(String username, ChatMessagesStack privateChatMessages) {
        this.privateChatMessages.put(username, privateChatMessages);
        super.updateObservers();
    }
    public void resetNewMessages(){
        newMessages = false;
    }





    //METHODS
    public void receiveChatMessage(ChatMessageRecord chatMessage){
        newMessages = true;
        if(chatMessage.isMessagePrivate()){
            String sender = chatMessage.getSender();
            if (sender.equals(MyPlayer.getInstance().getUsername()))
                privateChatMessages.get(chatMessage.getRecipient()).add(chatMessage);
            else
                privateChatMessages.get(sender).add(chatMessage);
        }
        else
            publicChatMessages.add(chatMessage);
    }
}
