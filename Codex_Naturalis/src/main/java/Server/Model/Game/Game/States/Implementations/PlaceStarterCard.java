package Server.Model.Game.Game.States.Implementations;

import Exceptions.Game.*;
import Server.Model.Game.Game.Game;
import Server.Model.Game.Game.States.ASynchronousGameState;
import Server.Model.Game.Player.Player;
import Server.Model.Game.Game.Table;
import Server.Model.Game.Player.PlayerStates;
import CommunicationProtocol.ServerClient.Packets.SCPUpdateClientGameState;

import java.util.Map;
import java.util.logging.Logger;

//todo fix javadoc

/**
 * Represents the initial setup state in the game, where players receive their starting cards and choose their colors.<br>
 * This state implements the GameState interface and handles starter card distribution and placement, initial card draws and player color selection.
 */
public class PlaceStarterCard extends ASynchronousGameState {
    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public PlaceStarterCard(Game game){
        super(game);

        logger = Logger.getLogger(PlaceStarterCard.class.getName());
        logger.info("Initializing Game state: 'PlaceStarterCard'.");

        Table table = game.getTable();

        for(Player player : players){
            //Give each player a starter card
            player.addCardHeld(table.drawStarterCard());
            player.setPlayerState(PlayerStates.PLACE);
        }

        logger.fine("Game state PlaceStarterCard initialized.\n" +
                "Broadcasting Client State Update PLACE.");
        gameObserverRelay.update(new SCPUpdateClientGameState(PlayerStates.PLACE));
    }





    //STATE PATTERN METHODS

    /**
     * {@inheritDoc}
     *
     * @param player          The player who is placing the card.
     * @param cardIndex       The index of the card in the player's hand.
     * @param coordinateIndex The index of the available coordinate in the cardMap where the card will be placed.
     * @param faceUp          True if the card should be placed face up, false if face down.
     * @throws MoveAttemptOnWaitStateException      Thrown if the player attempts a move while in a "wait" state.
     * @throws InvalidActionForPlayerStateException Thrown if the player attempts an action that is not valid in their current state.
     */
    @Override
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException {
        logger.info("Player "+player.getUsername()+" is now placing a card");
        logger.fine("Checking if player has the correct internal state to place cards");
        //Throws exception if the player has already performed all his moves for this turn.
        if (player.getPlayerState().equals(PlayerStates.WAIT))
            throw new MoveAttemptOnWaitStateException("You have already performed all moves for this turn.");

        //Throws exception if the player can't perform this move.
        else if (!player.getPlayerState().equals(PlayerStates.PLACE))
            throw new InvalidActionForPlayerStateException("You can't perform this move at the moment.");

        logger.fine("Player does have the required state to place cards, proceeding to place card.");
        //The rest of the method is executed if the player is actually allowed to perform the move.
        player.playCard(cardIndex, coordinateIndex, faceUp);
        logger.fine("Updating Client State to WAIT");
        player.setPlayerState(PlayerStates.WAIT);
        logger.fine("Sending to player "+player.getUsername()+" Update Client State package to "+PlayerStates.WAIT);
        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT));

        logger.fine("Setting player "+player.getUsername()+" as READY for this state.");
        playerReadiness.put(player, true);
        nextState();
    }





    //STATE SWITCHER
    /**
     * Handles the logic to determine whether the conditions for switching to the next state are met and performs
     * the transition itself.
     */
    protected void nextState(){
        //If at least one player is not yet ready return
        for(Map.Entry<Player, Boolean> entry : playerReadiness.entrySet()) {
            if (!entry.getValue())
                return;
        }
        //If all players are ready then go to next state.
        game.setState(new DrawInitialCards(game));
    }
}