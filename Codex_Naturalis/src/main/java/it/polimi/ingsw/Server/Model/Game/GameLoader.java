package it.polimi.ingsw.Server.Model.Game;

import it.polimi.ingsw.Server.Model.Game.Cards.Card;
import it.polimi.ingsw.Server.Model.Game.Logic.Game;
import it.polimi.ingsw.Server.Model.Game.Objectives.Objective;
import it.polimi.ingsw.Server.Model.Lobby.Lobby;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUser;
import it.polimi.ingsw.Server.Model.Lobby.ObserverRelay;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class GameLoader {

    public static Game startNewGame(List<LobbyUser> lobbyUsers, Lobby lobby, ObserverRelay updater){
        List<Objective> objectives = null;
        List<Card> cardsResource = null;
        List<Card> cardsGolden = null;
        List<Card> cardsStarter = null;

        String objectivesPath = "assets/objectives/";
        String cardsPath = "assets/cards/";


        String objectivesFileName = "Objectives.bin";

        String cardsGoldenFileName = "CardsGolden.bin";
        String cardsResourceFileName = "CardsResource.bin";
        String cardsStarterFileName = "CardsStarter.bin";


        //Load the assets from the resources directory
        try (InputStream inputStream = GameLoader.class.getClassLoader().getResourceAsStream(objectivesPath + objectivesFileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            objectives = (List<Objective>) objectInputStream.readObject();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        cardsResource = getCards(cardsResource, cardsPath, cardsResourceFileName);

        cardsGolden = getCards(cardsGolden, cardsPath, cardsGoldenFileName);

        cardsStarter = getCards(cardsStarter, cardsPath, cardsStarterFileName);


        //Start the game
        Game game = new Game(lobby, lobbyUsers, updater, cardsGolden, cardsResource, cardsStarter, objectives);

        return game;
    }

    private static List<Card> getCards(List<Card> cardsResource, String cardsPath, String cardsResourceFileName) {
        try (InputStream inputStream = GameLoader.class.getClassLoader().getResourceAsStream(cardsPath + cardsResourceFileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            cardsResource = (List<Card>) objectInputStream.readObject();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cardsResource;
    }
}
