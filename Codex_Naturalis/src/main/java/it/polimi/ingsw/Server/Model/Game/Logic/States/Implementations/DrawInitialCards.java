package it.polimi.ingsw.Server.Model.Game.Logic.States.Implementations;

import it.polimi.ingsw.Server.Model.Game.Logic.Game;
import it.polimi.ingsw.Server.Model.Game.Logic.GameConstants;
import it.polimi.ingsw.Server.Model.Game.Logic.States.PlayerSwitchers.PlayerSwitcherNonResilient;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Game.Logic.States.SynchronousGameState;
import it.polimi.ingsw.Server.Model.Game.Table.Table;
import it.polimi.ingsw.Server.Model.Game.Player.Player;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateCardPoolDrawability;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateClientGameState;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.Exceptions.Game.InvalidActionForPlayerStateException;
import it.polimi.ingsw.Exceptions.Game.MaxGoldenCardsDrawnException;
import it.polimi.ingsw.Exceptions.Game.MaxResourceCardsDrawnException;
import it.polimi.ingsw.Exceptions.Game.NotYourTurnException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents the game state where players draw initial cards during the setup phase.
 * This state extends {@link SynchronousGameState} and manages the process of drawing
 * golden and resource cards for each player in a synchronous turn-based manner.
 */
public class DrawInitialCards extends SynchronousGameState {

    //ATTRIBUTES
    private final Table table;

    private final Map<Player, Integer> goldenCardsDrawn;
    private final Map<Player, Integer> resourceCardsDrawn;

    private int setupStateCounter;




    //CONSTRUCTOR
    /**
     * Constructs a new DrawInitialCards state for the game.
     *
     * @param game The game instance associated with this state.
     */
    public DrawInitialCards(Game game) {
        super(game, new PlayerSwitcherNonResilient(game.getPlayers()));
        table = game.getTable();
        goldenCardsDrawn = new HashMap<>();
        resourceCardsDrawn = new HashMap<>();
        setupStateCounter = 0;
        logger = Logger.getLogger(DrawInitialCards.class.getName());

        // Initialize card draw counters for each player
        for (Player player : players) {
            resourceCardsDrawn.put(player, 0);
            goldenCardsDrawn.put(player, 0);
        }

        // Notify clients about initial card drawability
        Map<CardPoolTypes, Boolean> drawability = new HashMap<>();
        drawability.put(CardPoolTypes.GOLDEN, true);
        drawability.put(CardPoolTypes.RESOURCE, true);
        gameObserverRelay.update(new SCPUpdateCardPoolDrawability(drawability));

        // Start the setup phase by determining the first player
        findFirstPlayer();
    }





    //STATE PATTERN METHODS
    /**
     * Handles a player's attempt to draw a card during the setup phase.
     *
     * @param player       The player attempting to draw the card.
     * @param cardPoolType The type of card pool from which the card will be drawn (golden or resource).
     * @param index        The index of the card in the card pool.
     * @throws NotYourTurnException                 If the player attempts to draw out of turn.
     * @throws InvalidActionForPlayerStateException If the player attempts an invalid action in their current state.
     * @throws MaxGoldenCardsDrawnException         If the player tries to draw more golden cards than allowed.
     * @throws MaxResourceCardsDrawnException       If the player tries to draw more resource cards than allowed.
     */
    @Override
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws NotYourTurnException, InvalidActionForPlayerStateException, MaxGoldenCardsDrawnException, MaxResourceCardsDrawnException {
        //Throws exception if it's not the player's turn.
        if (!players.get(currentPlayerIndex).equals(player))
            throw new NotYourTurnException("Wait for your turn!");

        //Throws exception if the player can't perform this move.
        PlayerStates playerState = player.getPlayerState();
        if(playerState != PlayerStates.DRAW)
            //todo implement exception
            throw new RuntimeException("You can't perform this move in your current state.");

        if(goldenCardsDrawn.get(player) == GameConstants.goldenCardsToDrawSetup && cardPoolType == CardPoolTypes.GOLDEN){
            //todo implement exception
            throw new RuntimeException("Player State DRAW_GOLDEN but player wants to draw something else");
        }
        if(resourceCardsDrawn.get(player) == GameConstants.resourceCardsToDrawSetup  && cardPoolType == CardPoolTypes.RESOURCE)
            //todo implement exception
            throw new RuntimeException("Player State DRAW_RESOURCE but player wants to draw something else");



        //The rest of the method is executed if the player is actually allowed to perform the move.

        logger.fine("Deciding which card to draw");
        switch (cardPoolType) {
            case GOLDEN -> {
                logger.fine("Drawing golden card");
                if (goldenCardsDrawn.containsKey(player)) {
                    int counter = goldenCardsDrawn.get(player);
                    if (counter >= GameConstants.goldenCardsToDrawSetup)
                        throw new MaxGoldenCardsDrawnException("You have already drawn your golden card");
                    else {
                        player.addCardHeld(table.drawCard(cardPoolType, index));
                        goldenCardsDrawn.put(player, counter + 1);
                    }
                }
            }

            case RESOURCE -> {
                logger.fine("Drawing golden card");
                if (resourceCardsDrawn.containsKey(player)) {
                    int counter = resourceCardsDrawn.get(player);
                    if (counter >= GameConstants.resourceCardsToDrawSetup)
                        throw new MaxResourceCardsDrawnException("You have already drawn your two resource cards");
                    else {
                        player.addCardHeld(table.drawCard(cardPoolType, index));
                        resourceCardsDrawn.put(player, counter + 1);
                    }
                }
            }
        }

        player.setPlayerState(PlayerStates.WAIT);
        gameObserverRelay.update(player.getUsername(), new SCPUpdateClientGameState(PlayerStates.WAIT));
        findNextPlayer();

        updateCardDrawability(player);
    }

    /**
     * Updates the drawability of cards for a specific player based on the number of cards drawn.
     * Notifies the game observer relay about the updated card pool drawability.
     *
     * @param player The player for whom to update card drawability.
     */
    private void updateCardDrawability(Player player) {
        Map<CardPoolTypes, Boolean> drawability = new HashMap<>();

        // Check if the player has drawn all allowed golden cards
        if (goldenCardsDrawn.get(player) == GameConstants.goldenCardsToDrawSetup) {
            drawability.put(CardPoolTypes.GOLDEN, false); // No more golden cards can be drawn
        } else {
            drawability.put(CardPoolTypes.GOLDEN, true); // Golden cards can still be drawn
        }

        // Check if the player has drawn all allowed resource cards
        if (resourceCardsDrawn.get(player) == GameConstants.resourceCardsToDrawSetup) {
            drawability.put(CardPoolTypes.RESOURCE, false); // No more resource cards can be drawn
        } else {
            drawability.put(CardPoolTypes.RESOURCE, true); // Resource cards can still be drawn
        }

        // Notify the game observer relay about the updated card pool drawability for the player
        gameObserverRelay.update(player.getUsername(), new SCPUpdateCardPoolDrawability(drawability));
    }






    //RECONNECTION
    /**
     * Handles reconnection procedure for a player in the DrawInitialCards state.
     *
     * @param player The player who is reconnecting.
     */
    @Override
    public void userReconnectionProcedure(Player player){
       super.userReconnectionProcedure(player);

        updateCardDrawability(player);
    }





    //STATE SWITCHER
    /**
     * Switches the game state to the next state.<br>
     * Check for the boolean LastRoundFlag. If true then the next state will be the FinalRound, otherwise
     * the next state will be another MainLoop.
     */
    protected void nextState(){
        logger.info("Checking for next state");
        //Next state checks if the setup state has been completed, if so it starts the pick objective state
        //otherwise it continues this state by finding the next first player.
        setupStateCounter++;
        if(setupStateCounter == GameConstants.goldenCardsToDrawSetup+GameConstants.resourceCardsToDrawSetup) {
            logger.fine("Setup Draw state has finished, proceeding to next state.");
            game.setState(new ObjectivesSetup(game));

            Map<CardPoolTypes, Boolean> drawability = new HashMap<>();
            drawability.put(CardPoolTypes.GOLDEN, true);
            drawability.put(CardPoolTypes.RESOURCE, true);

            gameObserverRelay.update(new SCPUpdateCardPoolDrawability(drawability));

        }
        else {
            logger.fine("Setup Draw state has not yet finished, proceeding to another round of drawing.");
            findFirstPlayer();
        }
    }

    /**
     * Determines the initial player state for each player in the setup phase.
     *
     * @param player The player for whom to determine the initial state.
     * @return The initial state of the player.
     */
    @Override
    protected PlayerStates determinePlayerState(Player player) {
        if(player.equals(players.get(currentPlayerIndex)))
            return PlayerStates.DRAW;
        else
            return PlayerStates.WAIT;
    }
}
