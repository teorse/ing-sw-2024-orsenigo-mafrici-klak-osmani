package Server.Model.Game.Utility.AssetSerializer;

import Server.Model.Game.Cards.Card;
import Server.Model.Game.Cards.CardGolden;
import Server.Model.Game.Cards.CardResource;
import Server.Model.Game.Cards.CardStarter;
import Server.Model.Game.Objectives.Objective;
import Server.Model.Game.Objectives.ObjectiveGeometric;
import Server.Model.Game.Objectives.ObjectiveNumeric;
import Server.Model.Game.Utility.JsonParser.ObjectiveJsonSerializer;
import Server.Model.Game.Utility.JsonParser.CardJsonSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A tool for serializing JSON files into .bin files in the assets folder, ready for use by the game.
 * This tool compiles all the assets required for the game, including objectives and cards.
 */
public class AssetSerializer {
    /**
     * Compiles and serializes all the assets required for the game into .bin files.
     * This method reads JSON files containing objectives and cards, deserializes them, and serializes them into .bin files.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        // Source path for JSON files
        String sourcePath = "json/";
        // Target paths for serialized .bin files
        String objectivesTargetPath = "Codex_Naturalis/src/main/resources/assets/objectives/";
        String cardsTargetPath = "Codex_Naturalis/src/main/resources/assets/cards/";
        String json = "";

        // Lists to store deserialized objectives and cards
        List<Objective> objectives = new ArrayList<>();
        List<Card> cardsResource = new ArrayList<>();
        List<Card> cardsGolden = new ArrayList<>();
        List<Card> cardsStarter = new ArrayList<>();


        //Serialize Objectives

        //Read geometric objectives
        try (InputStream inputStream = AssetSerializer.class.getClassLoader().getResourceAsStream(sourcePath + "ObjectivesGeometric.json"); Scanner scanner = new Scanner(inputStream)) {
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            json = json + (content);
        } catch (IOException e) {
            System.out.println(e);
        }

        List<ObjectiveGeometric> objectivesGeometric = ObjectiveJsonSerializer.deserializeObjectiveGeometricList(json);
        json = "";
        objectives.addAll(objectivesGeometric);


        //Read numeric objectives
        try (InputStream inputStream = AssetSerializer.class.getClassLoader().getResourceAsStream(sourcePath + "ObjectivesNumeric.json"); Scanner scanner = new Scanner(inputStream)) {
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            json = json + (content);
        } catch (IOException e) {
            System.out.println(e);
        }

        List<ObjectiveNumeric> objectivesNumeric = ObjectiveJsonSerializer.deserializeObjectiveNumericList(json);
        json = "";
        objectives.addAll(objectivesNumeric);



        //Serialize
        try(FileOutputStream fileOutputStream = new FileOutputStream("Codex_Naturalis/src/main/resources/assets/objectives/Objectives.bin");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){

            objectOutputStream.writeObject(objectives);
            objectOutputStream.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }





        //Serialize Cards

        //Read Resource cards
        try (InputStream inputStream = AssetSerializer.class.getClassLoader().getResourceAsStream(sourcePath + "CardsResource.json"); Scanner scanner = new Scanner(inputStream)) {
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            json = json + (content);
        } catch (IOException e) {
            System.out.println(e);
        }

        List<CardResource> cardsResourceDeserialized = CardJsonSerializer.deserializeCardResourceList(json);
        json = "";
        cardsResource.addAll(cardsResourceDeserialized);


        //Read Golden cards
        try (InputStream inputStream = AssetSerializer.class.getClassLoader().getResourceAsStream(sourcePath + "CardsGolden.json"); Scanner scanner = new Scanner(inputStream)) {
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            json = json + (content);
        } catch (IOException e) {
            System.out.println(e);
        }

        List<CardGolden> cardsGoldenDeserialized = CardJsonSerializer.deserializeCardGoldenList(json);
        json = "";
        cardsGolden.addAll(cardsGoldenDeserialized);


        //Read Starter cards
        try (InputStream inputStream = AssetSerializer.class.getClassLoader().getResourceAsStream(sourcePath + "CardsStarter.json"); Scanner scanner = new Scanner(inputStream)) {
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            json = json + (content);
        } catch (IOException e) {
            System.out.println(e);
        }

        List<CardStarter> cardsStarterDeserialized = CardJsonSerializer.deserializeCardStarterList(json);
        json = "";
        cardsStarter.addAll(cardsStarterDeserialized);




        //Serialize
        try(FileOutputStream fileOutputStream = new FileOutputStream("Codex_Naturalis/src/main/resources/assets/cards/CardsResource.bin");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){

            objectOutputStream.writeObject(cardsResource);
            objectOutputStream.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(FileOutputStream fileOutputStream = new FileOutputStream("Codex_Naturalis/src/main/resources/assets/cards/CardsGolden.bin");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){

            objectOutputStream.writeObject(cardsGolden);
            objectOutputStream.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(FileOutputStream fileOutputStream = new FileOutputStream("Codex_Naturalis/src/main/resources/assets/cards/CardsStarter.bin");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){

            objectOutputStream.writeObject(cardsStarter);
            objectOutputStream.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
