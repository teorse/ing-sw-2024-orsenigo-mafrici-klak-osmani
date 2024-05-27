package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.ChatMessageRecord;
import Client.Model.Records.LobbyUserRecord;
import Client.Model.Records.PlayerRecord;
import Client.View.TextUI;
import Network.ClientServer.Packets.CSPSendChatMessage;

import java.util.logging.Logger;

public class ChatState extends ClientState{
    private final ClientState previousState;
    private int choice;
    private String chosenUser;
    private final Logger logger;

    /**
     * Constructs a new ClientState with the specified client model.
     * <p>
     * Initializes the ClientState with the provided client model, initializes the TextUI for interaction,
     * retrieves the player record of the current user, retrieves the lobby user record of the current user,
     * sets the input counter to 0, and initializes the operation success flag to false in the client model.
     *
     * @param model the client model
     */
    public ChatState(ClientModel model, ClientState previousState) {
        super(model);
        this.previousState = previousState;
        model.setChatState(true);
        model.setNewMessage(false);

        logger = Logger.getLogger(ConnectionState.class.getName());
        logger.info("Initializing ChatState");

        print();

        logger.fine("ChatState initialized");
    }

    //TODO fix private chat state

    @Override
    public void print() {
        if (inputCounter == 0) {
            TextUI.clearCMD();
            TextUI.displayChatState();

            System.out.println("If you want to go back at the previous choice, type BACK. If you want to exit the Chat State, type EXIT.");
            System.out.println("\n" + """ 
                    Enter your choice:
                     1 - Public Chat
                     2 - Private Chat""");
        } else if (inputCounter == 1) {
            TextUI.clearCMD();
            TextUI.displayChatState();

            System.out.println("If you want to go back at the previous choice, type BACK. If you want to exit the Chat State, type EXIT.");
            if (choice == 1) {
                System.out.println("\nPUBLIC CHAT");
                for (int i = 0; i < model.getPublicChatMessages().size(); i++) {
                    System.out.println(model.getPublicChatMessages().get(i).toString());
                }
                System.out.println("\nPlease type your public message, to send it press ENTER");
            } else if (choice == 2) {
                System.out.println("\nSelect a player username to send a private message to by inserting an integer:");
                int i = 1;
                for (LobbyUserRecord lobbyUserRecord : model.getLobbyUserRecords()) {
                    if (!lobbyUserRecord.username().equals(model.getMyUsername()))
                        System.out.println(i++ + " - Player username: " + lobbyUserRecord.username());
                }
            }
        } else if (inputCounter == 2 && choice == 2) {
            TextUI.clearCMD();
            TextUI.displayChatState();

            System.out.println("If you want to go back at the previous choice, type BACK. If you want to exit the Chat State, type EXIT.");
            System.out.println("\nPRIVATE CHAT");
            for (int i = 0; i < model.getPrivateChatMessages().get(chosenUser).size(); i++) {
                System.out.println(displayMessage(model.getPrivateChatMessages().get(chosenUser).get(i)));
            }
            System.out.println("\nPlease type your private message, to send it press ENTER");
        }
    }

    @Override
    public void handleInput(String input) {
        if (input.equalsIgnoreCase("BACK")) {
            if(inputCounter == 1) {
                choice = 0;
                inputCounter--;
            } else if(inputCounter == 2){
                inputCounter--;
            }
            print();
        } else if (input.equalsIgnoreCase("EXIT")) {
            nextState();
        }
        else if (inputCounter == 0) {
            if (TextUI.getBinaryChoice(input)) {
                inputCounter++;
                choice = Integer.parseInt(input);
            }
            print();
        }
        else if (inputCounter == 1) {
            if (choice == 1) {
                model.getClientConnector().sendPacket(new CSPSendChatMessage(new ChatMessageRecord(input)));
                print();
            } else if (choice == 2) {
                if (TextUI.checkInputBound(input, 1, model.getLobbyUserRecords().size())) {
                    chosenUser = model.getLobbyUserRecords().get(Integer.parseInt(input)).username();
                    inputCounter++;
                }
                print();
            }
        }
        else if (inputCounter == 2 && choice == 2) {
                model.getClientConnector().sendPacket(new CSPSendChatMessage(new ChatMessageRecord(input,chosenUser)));
                print();
        }
    }

    @Override
    public void nextState() {
        model.setChatState(false);
        previousState.nextState();
    }
}
