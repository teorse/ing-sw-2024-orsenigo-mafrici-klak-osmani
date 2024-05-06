package Model.Game;

import Exceptions.Game.*;
import Model.Cards.Card;
import Model.Objectives.Objective;
import Model.Player.Player;
import Model.Game.States.*;
import Server.Model.Lobby.LobbyUserColors;
import Server.Interfaces.LayerUser;
import Server.Interfaces.ServerModelLayer;
import Server.Model.Lobby.Lobby;
import Server.Model.Lobby.LobbyUser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This Class stores all the information about the current game, including the list of players, the current game phase,
 * the current round etc.
 */
public class Game implements ServerModelLayer {
    //ATTRIBUTES
    private List<Player> players;
    private final Map<LobbyUser, Player> lobbyUserToPlayerMap;
    private final Lobby lobby;

    private final Table table;
    private boolean lastRoundFlag;
    private boolean gameOver;
    private GameState state;
    private final List<Player> winners;


    //todo add procedures to start time-out if only 1 player is left in session or if no players are left


    /**
     * Stores number of rounds completed by the table.<br>
     * A round is deemed completed if all the currently online players have completed their turn for the current round.
     */
    private int roundsCompleted;





    //CONSTRUCTOR
    /**
     * Default Constructor.<br>
     * Initializes a Model.Game.Game object by taking Players ArrayList, Golden and Resource Model.Game.Deck, and ArrayList of Objectives.<br>
     * Automatically
     * @param goldenCards   List of Golden Cards to use during this game.
     * @param resourceCards List of Resource Cards to use during this game.
     * @param starterCards  List of Starter Cards to use during this game.
     * @param objectives    List of Objectives to use during this game.
     * @param lobbyUsers    List of lobby users taking part to this game.
     */
    public Game(Lobby lobby, List<Card> goldenCards, List<Card> resourceCards, List<Card> starterCards, List<Objective> objectives, List<LobbyUser> lobbyUsers) {
        lobbyUserToPlayerMap = new HashMap<>();
        this.lobby = lobby;
        players = new ArrayList<>();
        for(LobbyUser lobbyUser : lobbyUsers){
            Player player = new Player(lobbyUser);
            //todo add listeners

            lobbyUserToPlayerMap.put(lobbyUser, player);
            players.add(player);
        }

        Collections.shuffle(this.players);
        this.table = new Table(goldenCards, resourceCards, starterCards, objectives);
        this.lastRoundFlag = false;
        this.gameOver = false;
        winners = new ArrayList<>();
        state = new CardsSetup(this);

        System.out.println("Game has started, inside game class now!");
        System.out.println("goldenCards: "+goldenCards.size());
        System.out.println("resourceCards: "+resourceCards.size());
        System.out.println("starterCards: "+starterCards.size());
        System.out.println("objectives: "+objectives.size());
        System.out.println("players: "+players.size());
    }





    //GETTERS
    public List<Player> getPlayers() {
        return players;
    }
    public Player getPlayerByLobbyUser(LobbyUser lobbyUser){
        return lobbyUserToPlayerMap.get(lobbyUser);
    }
    public Table getTable() {
        return table;
    }
    public int getRoundsCompleted() {
        return roundsCompleted;
    }
    public boolean isLastRoundFlag(){
        return this.lastRoundFlag;
    }
    public Lobby getLobby(){
        return this.lobby;
    }
    public Map<LobbyUser, Player> getLobbyUserToPlayerMap(){
        return this.lobbyUserToPlayerMap;
    }





    //STATE PATTERN METHODS
    public void setState(GameState state){
        this.state = state;
    }
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws NotYourTurnException, MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        state.playCard(player, cardIndex, coordinateIndex, faceUp);
    }
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, MoveAttemptOnWaitStateException, MaxResourceCardsDrawnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, MaxGoldenCardsDrawnException {
        state.drawCard(player, cardPoolType, index);
    }
    public void pickPlayerObjective(Player player, int objectiveIndex) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        state.pickPlayerObjective(player, objectiveIndex);
    }
    public boolean shouldRemovePlayerOnDisconnect(){
        return state.shouldRemovePlayerOnDisconnect();
    }
    public void removePlayer(LobbyUser lobbyUser){
        state.removePlayer(lobbyUser);
    }
    @Override
    public void userDisconnectionProcedure(LayerUser user) {
        state.userDisconnectionProcedure(user);
    }
    @Override
    public void quit(LayerUser user) {
        state.quit(user);
    }





    //METHODS
    public void incrementRoundsCompleted(){
        this.roundsCompleted++;
    }

    /**
     * Method verifies whether the conditions for the game-end are met, if so sets to true the lastRound flag.
     */
    public void checkGameEndingConditions(){

        if(table.areDecksEmpty()){
            this.lastRoundFlag = true;
            return;
        }

        for(Player player : players){
            if(player.getPoints() >= 20){
                this.lastRoundFlag = true;
                return;
            }
        }
        return;
    }

    protected void selectWinners(){
        //Comparator to sort players first by points and then by objectives completed
        Comparator<Player> comparator = Comparator.comparing(Player::getPoints);
        comparator = comparator.thenComparing(Comparator.comparing(Player::getObjectivesCompleted));

        //Sort players using above comparator
        Stream<Player> playerStream = players.stream().sorted(comparator);
        players = playerStream.collect(Collectors.toList());

        //Select winners, multiple winners if tie on points and objectives.
        Player referenceWinner = players.getFirst();
        for(Player player : players){
            if(player.getPoints() == referenceWinner.getPoints() && player.getObjectivesCompleted() == referenceWinner.getObjectivesCompleted())
                winners.add(player);
        }
    }

    public void gameOver(){
        List<Objective> sharedObjectives = table.getSharedObjectives();

        //Count all the points for each player.
        for(Player player : players){
            player.countAllPoints(sharedObjectives);
        }

        selectWinners();

        //After all players are given their points the winner is calculated.
        gameOver = true;
    }
}
