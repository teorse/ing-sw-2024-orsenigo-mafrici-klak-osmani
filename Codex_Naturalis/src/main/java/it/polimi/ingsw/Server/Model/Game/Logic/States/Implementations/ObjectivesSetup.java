package it.polimi.ingsw.Server.Model.Game.Logic.States.Implementations;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;
import it.polimi.ingsw.Exceptions.Game.InvalidActionForPlayerStateException;
import it.polimi.ingsw.Exceptions.Game.MoveAttemptOnWaitStateException;
import it.polimi.ingsw.Server.Model.Game.Logic.Game;
import it.polimi.ingsw.Server.Model.Game.Logic.States.ASynchronousGameState;
import it.polimi.ingsw.Server.Model.Game.Table.Table;
import it.polimi.ingsw.Server.Model.Game.Objectives.Objective;
import it.polimi.ingsw.Server.Model.Game.Player.Player;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateSecretObjectiveCandidates;

import java.util.*;
import java.util.logging.Logger;

/**
 * Represents the setup state where players select their secret objectives.
 * This state is the second setup state in the game and precedes the main game loop.
 * This state implements the GameState interface and handles secret objective selection.
 */
public class ObjectivesSetup extends ASynchronousGameState {
    Map<String, List<Objective>> secretObjectiveCandidates;

    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public ObjectivesSetup(Game game){
        super(game);
        logger = Logger.getLogger(ObjectivesSetup.class.getName());

        Table table = game.getTable();
        table.revealSharedObjectives();

        secretObjectiveCandidates = new HashMap<>();

        for(Player player : players){
            //Distribute objectives to players
            List<Objective> candidates = new ArrayList<>();
            List<ObjectiveRecord> candidateRecords = new ArrayList<>();

            for(int i = 0; i < 2; i++) {
                Objective objective = table.drawObjective();
                ObjectiveRecord objectiveRecord = objective.toRecord();
                candidates.add(objective);
                candidateRecords.add(objectiveRecord);
            }

            secretObjectiveCandidates.put(player.getUsername(), candidates);
            gameObserverRelay.update(player.getUsername(), new SCPUpdateSecretObjectiveCandidates(candidateRecords));

            player.setPlayerState(PlayerStates.PICK_OBJECTIVE);
        }
    }





    //STATE PATTERN ATTRIBUTES

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

        Objective selectedObjective = secretObjectiveCandidates.get(player.getUsername()).get(objectiveIndex);

        player.addSecretObjective(selectedObjective);
        player.setPlayerState(PlayerStates.WAIT);
        playerReadiness.put(player, true);

        nextState();
    }





    //RECONNECTION
    /**
     * Handles the reconnection procedure for a player during a game session.
     *
     * @param player The player who is reconnecting.
     */
    @Override
    public void userReconnectionProcedure(Player player) {
        // Perform any general reconnection procedure defined in the superclass
        super.userReconnectionProcedure(player);

        // Retrieve secret objective candidates for the reconnected player
        List<ObjectiveRecord> candidateRecords = new ArrayList<>();
        List<Objective> objectivesCandidates = secretObjectiveCandidates.get(player.getUsername());

        // Convert objectives to their record representations
        for (Objective objective : objectivesCandidates) {
            candidateRecords.add(objective.toRecord());
        }

        // Update the client with the secret objective candidates available to the player
        gameObserverRelay.update(player.getUsername(), new SCPUpdateSecretObjectiveCandidates(candidateRecords));
    }






    //STATE SWITCHER
    /**
     * Handles the logic to determine whether the conditions for switching to the next state are met and performs
     * the transition itself.
     */
    protected void nextState(){
        //If at least one player is not yet ready then return
        for(Map.Entry<Player, Boolean> entry : playerReadiness.entrySet()) {
            if (!entry.getValue())
                return;
        }
        //If all online players are ready then go to next state.
        game.setState(new MainLoop(game));
    }
}