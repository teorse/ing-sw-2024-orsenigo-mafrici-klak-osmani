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

/**
 * Class responsible for loading and starting a new game.
 */
public class GameLoader {

    /**
     * Starts a new game with the given lobby users, lobby, and observer relay.
     *
     * @param lobbyUsers the list of users in the lobby
     * @param lobby the lobby in which the game is started
     * @param updater the observer relay to update game state
     * @return the newly started game
     */
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

    /**
     * Loads cards from the specified file path.
     *
     * @param cardsResource the list to store the loaded cards
     * @param cardsPath the path to the cards directory
     * @param cardsResourceFileName the name of the file containing the cards
     * @return the list of loaded cards
     */
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
