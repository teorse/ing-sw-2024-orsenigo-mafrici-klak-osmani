package it.polimi.ingsw.CommunicationProtocol.ClientServer;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

public interface ClientServerMessageExecutor {
    //SERVER LAYER COMMANDS
    void logIn(String username, String password);
    void signUp(String username, String password);
    void logOut();
    void viewLobbyPreviews();
    void stopViewingLobbyPreviews();
    void startLobby(String lobbyName, int lobbySize);
    void joinLobby(String lobbyName);

    //LOBBY LAYER COMMANDS
    void startGame();
    void quitLobby();
    void changeColor(LobbyUserColors newColor);
    void sendChatMessage(ChatMessageRecord chatMessage);

    //GAME LAYER COMMANDS
    void playCard(int cardIndex, int coordinateIndex, boolean faceUp);
    void drawCard(CardPoolTypes cardPoolType, int cardIndex);
    void pickObjective(int objectiveIndex);
}