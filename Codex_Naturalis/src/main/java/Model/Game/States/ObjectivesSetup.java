package Model.Game.States;

import Model.Game.CardPoolTypes;
import Exceptions.Game.InvalidActionForGameStateException;
import Exceptions.Game.InvalidActionForPlayerStateException;
import Exceptions.Game.MoveAttemptOnWaitStateException;
import Model.Game.Game;
import Model.Game.Table;
import Model.Objectives.Objective;
import Model.Player.Player;
import Model.Player.PlayerStates;
import Network.ServerClient.Packets.SCPUpdateClientGameState;
import Server.Model.Lobby.ObserverRelay;

import java.util.*;

/**
 * Represents the setup state where players select their secret objectives.
 * This state is the second setup state in the game and precedes the main game loop.
 * This state implements the GameState interface and handles secret objective selection.
 */
public class ObjectivesSetup implements GameState{
    //ATTRIBUTES
    private final Game game;
    private final List<Player> players;
    private final ObserverRelay gameObserverRelay;

    /**
     * Map that keeps track of which players have completed the setups of this game phase and which haven't.<br>
     * If true, it means the player HAS completed all setups and is ready for the next phase.
     */
    private final Map<Player, Boolean> playerReadiness;





    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public ObjectivesSetup(Game game){
        this.game = game;
        players = game.getPlayers();
        Table table = game.getTable();
        gameObserverRelay = game.getGameObserverRelay();

        playerReadiness = new HashMap<>();

        table.revealSharedObjectives();

        for(Player player : players){
            //Distribute objectives to players
            List<Objective> candidates = new ArrayList<>();
            candidates.add(table.drawObjective());
            candidates.add(table.drawObjective());

            player.setSecretObjectiveCandidate(candidates);

            //Add players to readiness map.
            playerReadiness.put(player, false);
        }

        //Only AFTER everything else has been set up, set the player status as PICK_OBJECTIVE.
        //Setting it before this point can cause problems as players could try to access un-initialized variables.
        for(Player player : players)
            player.setPlayerState(PlayerStates.PICK_OBJECTIVE);
        gameObserverRelay.update(new SCPUpdateClientGameState(PlayerStates.PICK_OBJECTIVE));
    }





    //STATE PATTERN ATTRIBUTES

    /**
     * The method throws an exception as players are not allowed to perform this move during this state of the game.
     *
     * @param player          The player who is placing the card.
     * @param cardIndex       The index of the card in the player's hand.
     * @param coordinateIndex The index of the available coordinate in the cardMap where the card will be placed.
     * @param faceUp          True if the card should be placed face up, false if face down.
     * @throws InvalidActionForGameStateException   Always thrown as players are not allowed to place cards in this state.
     */
    @Override
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws InvalidActionForGameStateException {
        throw new InvalidActionForGameStateException("You can't place cards yet.");
    }

    /**
     * The method throws an exception as players are not allowed to perform this move during this state of the game.
     *
     * @param player       The player who is drawing the card.
     * @param cardPoolType The type of card pool from which the card will be drawn.
     * @param index        The index of the card in the card pool.
     * @throws InvalidActionForGameStateException   Always thrown as players are not allowed to draw cards in this state.
     */
    @Override
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws InvalidActionForGameStateException {
        throw new InvalidActionForGameStateException("You can't draw cards yet.");
    }

    /**
     * {@inheritDoc}
     *
     * @param player        The player who is picking the objective.
     * @param objectiveIndex The index of the objective the player is picking.
     * @throws MoveAttemptOnWaitStateException      Thrown if the player attempts a move while in a "wait" state.
     * @throws InvalidActionForPlayerStateException Thrown if the player attempts an action that is not valid in their current state.
     */
    @Override
    public void pickPlayerObjective(Player player, int objectiveIndex) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException {
        //Throws exception if the player has already performed all his moves for this turn.
        if (player.getPlayerState().equals(PlayerStates.WAIT))
            throw new MoveAttemptOnWaitStateException("You have already performed all moves for this turn.");

        //Throws exception if the player can't perform this move.
        else if (!player.getPlayerState().equals(PlayerStates.PICK_OBJECTIVE))
            throw new InvalidActionForPlayerStateException("You can't perform this move at the moment.");

        //The rest of the method is executed if the player is actually allowed to perform the move.
        player.selectSecretObjective(objectiveIndex);
        player.setPlayerState(PlayerStates.WAIT);
        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT));
        playerReadiness.put(player, true);

        nextState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldRemovePlayerOnDisconnect() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePlayer(Player player) {
        playerReadiness.remove(player);
        players.remove(player);

        if(players.size() > 1)
            nextState();
        else
            game.gameOver();
    }

    /**
     * Handles disconnection of a user from the game.<br>
     * This method prepares the game for later player removal, it does not directly remove the player
     * (the removal will be triggered by the outside lobby by calling
     * the remove method).
     *
     * @param player The user who disconnected.
     */
    @Override
    public void userDisconnectionProcedure(Player player) {
        String username = player.getUsername();

        System.out.println("Player " + username + " has disconnected from the game," +
                "They have 90 seconds to reconnect before being kicked from the game");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit(Player player) {
        removePlayer(player);
    }


    //STATE SWITCHER
    /**
     * Handles the logic to determine whether the conditions for switching to the next state are met and performs
     * the transition itself.
     */
    private void nextState(){
        //If at least one player is not yet ready then return
        for(Map.Entry<Player, Boolean> entry : playerReadiness.entrySet()) {
            if (!entry.getValue())
                return;
        }
        //If all online players are ready then go to next state.
        game.setState(new MainLoop(game));
    }
}