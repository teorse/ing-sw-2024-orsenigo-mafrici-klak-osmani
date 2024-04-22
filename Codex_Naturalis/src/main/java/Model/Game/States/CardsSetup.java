package Model.Game.States;

import Model.Game.CardPoolTypes;
import Model.Game.Game;
import Model.Player.Player;
import Model.Player.PlayerColors;
import Model.Game.Table;
import Model.Player.PlayerConnectionStatus;
import Model.Player.PlayerStates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the initial setup state in the game, where players receive their starting cards and choose their colors.<br>
 * This state implements the GameState interface and handles starter card distribution and placement, initial card draws and player color selection.
 */
public class CardsSetup implements GameState{
    //ATTRIBUTES
    private final Game game;
    private final List<Player> players;
    private final Table table;

    private Map<Player, Integer> goldenCardsDrawn;
    private Map<Player, Integer> resourceCardsDrawn;

    private final int goldenCardsToDraw;
    private final int resourceCardsToDraw;

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
    public CardsSetup(Game game){
        this.game = game;
        players = game.getPlayers();
        table = game.getTable();

        //Maps to keep track of how many cards of a given type each player has drawn.
        goldenCardsDrawn = new HashMap<>();
        resourceCardsDrawn = new HashMap<>();

        //Parameters indicating how many cards of each type players have to draw
        goldenCardsToDraw = 1;
        resourceCardsToDraw = 2;

        //Map containing the readiness status of each player.
        //if true, player has completed all required setups for this game state.
        //if false, player is still missing some steps.
        playerReady = new HashMap<>();

        for(Player player : players){
            //Give each player a starter card
            player.addCardHeld(table.drawStarterCard());

            //Set up the card-draw counters.
            resourceCardsDrawn.put(player, 0);
            goldenCardsDrawn.put(player,0);

            //Set each player as unready
            playerReady.put(player, false);
        }

        //Only AFTER everything else has been set up, set the player status as PLACE.
        //Setting it before this point can cause problems as players could try to access un-initialized variables.
        for(Player player : players){
            player.setPlayerState(PlayerStates.PLACE);
        }
    }





    //STATE PATTERN METHODS
    /**
     * {@inheritDoc}
     */
    @Override
    public void placeCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) {
        try {
            //Throws exception if the player has already performed all his moves for this turn.
            if (player.getPlayerState().equals(PlayerStates.WAIT))
                throw new RuntimeException("You have already performed all moves for this turn.");

            //Throws exception if the player can't perform this move.
            else if (!player.getPlayerState().equals(PlayerStates.PLACE))
                throw new RuntimeException("You can't perform this move at the moment.");

            //The rest of the method is executed if the player is actually allowed to perform the move.
            player.playCard(cardIndex, coordinateIndex, faceUp);
            player.setPlayerState(PlayerStates.PICK_COLOR);
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) {
        try {
            //Throws exception if the player has already performed all his moves for this turn.
            if (player.getPlayerState().equals(PlayerStates.WAIT))
                throw new RuntimeException("You have already performed all moves for this turn.");

            //Throws exception if the player can't perform this move.
            else if (!player.getPlayerState().equals(PlayerStates.DRAW))
                throw new RuntimeException("You can't perform this move at the moment.");

            //The rest of the method is executed if the player is actually allowed to perform the move.

            //Switch to update the counters for cards drawn.
            switch (cardPoolType) {
                case GOLDEN: {
                    if (goldenCardsDrawn.containsKey(player)) {
                        int counter = goldenCardsDrawn.get(player);
                        if (counter >= 1)
                            throw new RuntimeException("You have already drawn your golden card");
                        else {
                            player.addCardHeld(table.drawCard(cardPoolType, index));
                            goldenCardsDrawn.put(player, counter + 1);
                        }
                    } else {
                        throw new RuntimeException("You are not part of this game, who are you?");
                    }
                }

                case RESOURCE: {
                    if (resourceCardsDrawn.containsKey(player)) {
                        int counter = resourceCardsDrawn.get(player);
                        if (counter >= 2)
                            throw new RuntimeException("You have already drawn your two golden cards");
                        else {
                            player.addCardHeld(table.drawCard(cardPoolType, index));
                            resourceCardsDrawn.put(player, counter + 1);
                        }
                    } else {
                        throw new RuntimeException("You are not part of this game, who are you?");
                    }
                }
            }

            //If the player has reached the required number of draws for each card type then his state is updated to WAIT,
            //and he is set to ready in the readiness map.
            if (resourceCardsDrawn.get(player) == resourceCardsToDraw && goldenCardsDrawn.get(player) == goldenCardsToDraw) {
                player.setPlayerState(PlayerStates.WAIT);
                playerReady.put(player, true);
            }

            nextState();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pickPlayerColor(Player player, PlayerColors color) {
        try {
            //Throws exception if the player has already performed all his moves for this turn.
            if (player.getPlayerState().equals(PlayerStates.WAIT))
                throw new RuntimeException("You have already performed all moves for this turn.");

            //Throws exception if the player can't perform this move.
            else if (!player.getPlayerState().equals(PlayerStates.PICK_COLOR))
                throw new RuntimeException("You can't perform this move at the moment.");

            //The rest of the method is executed if the player is actually allowed to perform the move.

            //Checks if the color is not already used by another player.
            for(Player rival : players){
                if(rival.getColor().equals(color))
                    throw new RuntimeException("Color is already used by another player");
            }

            player.setColor(color);
            player.setPlayerState(PlayerStates.DRAW);
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
    }

    /**
     * The method notifies the player that they are not allowed to perform this move during this state of the game.
     */
    @Override
    public void pickPlayerObjective(Player player, int objectiveIndex) {
        throw new RuntimeException("You can't pick your objective yet.");
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
        game.setState(new ObjectivesSetup(game));
    }
}


//Currently does nothing to disconnected players, does not remove them from the game.
//Todo add methods to handle player removal if disconnected during this phase of the game.