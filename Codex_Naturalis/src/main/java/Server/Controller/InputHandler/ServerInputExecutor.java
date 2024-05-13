package Server.Controller.InputHandler;

import Model.Game.CardPoolTypes;
import Server.Model.Lobby.LobbyUserColors;

public interface ServerInputExecutor {
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

    //GAME LAYER COMMANDS
    void playCard(int cardIndex, int coordinateIndex, boolean faceUp);
    void drawCard(CardPoolTypes cardPoolType, int cardIndex);
    void pickObjective(int objectiveIndex);
}