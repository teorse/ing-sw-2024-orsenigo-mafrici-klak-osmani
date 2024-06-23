package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends Observable implements Observer {
    //SINGLETON PATTERN
    private static Chat INSTANCE;
    private Chat(){
        publicChatMessages = new ChatMessagesStack(ClientModelConstants.PublicMessageStackSize);
        privateChatMessages = new HashMap<>();

        LobbyUsers.getInstance().subscribe(this);
        update();
    }
    public synchronized static Chat getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Chat();
        }

        return INSTANCE;
    }

    private boolean newMessages;





    //ATTRIBUTES
    private ChatMessagesStack publicChatMessages;
    private Map<String, ChatMessagesStack> privateChatMessages;






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
        if(chatMessage.isMessagePrivate()){
            String sender = chatMessage.getSender();
            if (sender.equals(MyPlayer.getInstance().getUsername()))
                privateChatMessages.get(chatMessage.getRecipient()).add(chatMessage);
            else
                privateChatMessages.get(sender).add(chatMessage);
        }
        else
            publicChatMessages.add(chatMessage);

        newMessages = true;
        super.updateObservers();
    }

    @Override
    public void update() {
        List<LobbyUserRecord> users = LobbyUsers.getInstance().getLobbyUserRecords();

        for(LobbyUserRecord user : users){
            if(!privateChatMessages.containsKey(user.username()))
                privateChatMessages.put(user.username(), new ChatMessagesStack(ClientModelConstants.PrivateMessageStackSize));
        }
        for(String username : new ArrayList<>(privateChatMessages.keySet())){
            List<String> newUsernames = users.stream().map(LobbyUserRecord::username).toList();
            if(!newUsernames.contains(username))
                privateChatMessages.remove(username);
        }
    }
}
