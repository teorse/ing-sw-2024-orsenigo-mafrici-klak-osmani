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

//todo fix javadoc
public class DrawInitialCards extends SynchronousGameState {

    //ATTRIBUTES
    private final Table table;

    private final Map<Player, Integer> goldenCardsDrawn;
    private final Map<Player, Integer> resourceCardsDrawn;

    private int setupStateCounter;




    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public DrawInitialCards(Game game){
        super(game, new PlayerSwitcherNonResilient(game.getPlayers()));
        table = game.getTable();

        goldenCardsDrawn = new HashMap<>();
        resourceCardsDrawn = new HashMap<>();

        setupStateCounter = 0;

        logger = Logger.getLogger(DrawInitialCards.class.getName());


        for(Player player : players){

            //Set up the card-draw counters.
            resourceCardsDrawn.put(player, 0);
            goldenCardsDrawn.put(player,0);
        }

        Map<CardPoolTypes, Boolean> drawability = new HashMap<>();
        drawability.put(CardPoolTypes.GOLDEN, true);
        drawability.put(CardPoolTypes.RESOURCE, true);

        gameObserverRelay.update(new SCPUpdateCardPoolDrawability(drawability));

        findFirstPlayer();
    }





    //STATE PATTERN METHODS
    /**
     * {@inheritDoc}
     *
     * @param player       The player who is drawing the card.
     * @param cardPoolType The type of card pool from which the card will be drawn.
     * @param index        The index of the card in the card pool.
     * @throws NotYourTurnException                 Thrown if the player attempts to draw a card out of turn.
     * @throws InvalidActionForPlayerStateException Thrown if the player attempts an invalid action in their current state.
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

    private void updateCardDrawability(Player player){
        Map<CardPoolTypes, Boolean> drawability = new HashMap<>();
        if(goldenCardsDrawn.get(player) == GameConstants.goldenCardsToDrawSetup)
            drawability.put(CardPoolTypes.GOLDEN, false);
        else
            drawability.put(CardPoolTypes.GOLDEN, true);

        if(resourceCardsDrawn.get(player) == GameConstants.resourceCardsToDrawSetup)
            drawability.put(CardPoolTypes.RESOURCE, false);
        else
            drawability.put(CardPoolTypes.RESOURCE, true);

        gameObserverRelay.update(player.getUsername(), new SCPUpdateCardPoolDrawability(drawability));
    }





    //RECONNECTION
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

    @Override
    protected PlayerStates determinePlayerState(Player player) {
        if(player.equals(players.get(currentPlayerIndex)))
            return PlayerStates.DRAW;
        else
            return PlayerStates.WAIT;
    }
}
