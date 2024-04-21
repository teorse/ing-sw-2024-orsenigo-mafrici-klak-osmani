package Model.Game.States;

import Model.Game.CardPoolTypes;
import Model.Game.Game;
import Model.Game.Table;
import Model.Player.Player;
import Model.Player.PlayerColors;
import Model.Player.PlayerConnectionStatus;
import Model.Player.PlayerStates;

import java.util.*;

/**
 * Represents the setup state where players select their secret objectives.
 * This state is the second setup state in the game and precedes the main game loop.
 * This state implements the GameState interface and handles secret objective selection.
 */
public class ObjectivesSetup implements GameState{
    //ATTRIBUTES
    private final Game game;
    private final Table table;
    private final List<Player> players;

    /**
     * Map that keeps track of which players have completed the setups of this game phase and which haven't.<br>
     * If true, it means the player HAS completed all setups and is ready for the next phase.
     */
    private Map<Player, Boolean> playerReady;





    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public ObjectivesSetup(Game game){
        this.game = game;
        table = game.getTable();
        players = game.getPlayers();

        playerReady = new HashMap<>();

        table.revealSharedObjectives();

        for(Player player : players){
            //Distribute objectives to players
            player.addSecretObjectiveCandidate(table.drawObjective());
            player.addSecretObjectiveCandidate(table.drawObjective());

            //Add players to readiness map.
            playerReady.put(player, false);
        }

        //Only AFTER everything else has been set up, set the player status as PICK_OBJECTIVE.
        //Setting it before this point can cause problems as players could try to access un-initialized variables.
        for(Player player : players)
            player.setPlayerState(PlayerStates.PICK_OBJECTIVE);
    }





    //STATE PATTERN ATTRIBUTES
    /**
     * The method notifies the player that they are not allowed to perform this action during this state of the game.
     */
    @Override
    public void placeCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) {
        throw new RuntimeException("You can't place cards yet.");
    }

    /**
     * The method notifies the player that they are not allowed to perform this action during this state of the game.
     */
    @Override
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) {
        throw new RuntimeException("You can't draw cards yet.");
    }

    /**
     * The method notifies the player that they are not allowed to perform this action during this state of the game.
     */
    @Override
    public void pickPlayerColor(Player player, PlayerColors color) {
        throw new RuntimeException("You can't pick your color at this game state.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pickPlayerObjective(Player player, int objectiveIndex) {
        try{
            //Throws exception if the player has already performed all his moves for this turn.
            if (player.getPlayerState().equals(PlayerStates.WAIT))
                throw new RuntimeException("You have already performed all moves for this turn.");

            //Throws exception if the player can't perform this move.
            else if (!player.getPlayerState().equals(PlayerStates.PICK_OBJECTIVE))
                throw new RuntimeException("You can't perform this move at the moment.");

            //The rest of the method is executed if the player is actually allowed to perform the move.
            player.selectSecretObjective(objectiveIndex);
            player.setPlayerState(PlayerStates.WAIT);
            playerReady.put(player, true);

            nextState();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
    }





    //STATE SWITCHER
    /**
     * Handles the logic to determine whether the conditions for switching to the next state are met and performs
     * the transition itself.
     */
    private void nextState(){
        //If at least one online player that is not yet ready is found then return
        for(Map.Entry<Player, Boolean> entry : playerReady.entrySet()) {
            if (entry.getKey().getConnectionStatus().equals(PlayerConnectionStatus.ONLINE) && !entry.getValue())
                return;
        }
        //If all online players are ready then go to next state.
        game.setState(new MainLoop(game));
    }
}

//Currently does nothing to disconnected players, does not remove them from the game.
//Todo add methods to handle player removal if disconnected during this phase of the game.