package Model.Game.States;

import Model.Cards.Card;
import Model.Game.Game;
import Model.Game.CardPoolTypes;
import Model.Game.Table;
import Model.Player.Player;
import Model.Player.PlayerColors;
import Model.Player.PlayerConnectionStatus;
import Model.Player.PlayerStates;

import java.util.List;

/**
 * Represents the main loop state in the game, where players take turns to place cards and draw new cards.<br>
 * This state implements the GameState interface and defines methods to handle actions during the main gameplay loop.
 */
public class MainLoop implements GameState{
    //ATTRIBUTES
    private final Game game;
    private final Table table;
    private final List<Player> players;
    private int currentPlayerIndex;





    //CONSTRUCTOR
    /**
     * Default Constructor.
     *
     * @param game The game instance to which this MainLoop state belongs.
     */
    public MainLoop(Game game){
        this.game = game;
        table = this.game.getTable();
        players = this.game.getPlayers();

        findFirstPlayer();
    }





    //STATE PATTERN METHODS
    /**
     * {@inheritDoc}
     */
    @Override
    public void placeCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) {
        int points;

        try {
            //Throws exception if the player has already performed all his moves for this turn.
            if (player.getPlayerState().equals(PlayerStates.WAIT))
                throw new RuntimeException("You have already performed all moves for this turn.");

            //Throws exception if it's not the player's turn.
            else if (!players.get(currentPlayerIndex).equals(player)) {
                throw new RuntimeException("Wait for your turn.");
            }

            //Throws exception if the player can't perform this move.
            else if (!player.getPlayerState().equals(PlayerStates.PLACE))
                throw new RuntimeException("You can't perform this move at the moment.");

            //The rest of the method is executed if the player is actually allowed to perform the move.
            points = player.playCard(cardIndex, coordinateIndex, faceUp);
            game.addPointsToPlayer(player, points);
            player.setPlayerState(PlayerStates.DRAW);
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
        game.checkGameEndingConditions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) {
        try{
            //Throws exception if the player has already performed all his moves for this turn.
            if (player.getPlayerState().equals(PlayerStates.WAIT))
                throw new RuntimeException("You have already performed all moves for this turn.");

            //Throws exception if it's not the player's turn.
            else if (!players.get(currentPlayerIndex).equals(player)) {
                throw new RuntimeException("Wait for your turn.");
            }

            //Throws exception if the player can't perform this move.
            else if (!player.getPlayerState().equals(PlayerStates.DRAW))
                throw new RuntimeException("You can't perform this move at the moment.");

            //The rest of the method is executed if the player is actually allowed to perform the move.

            Card cardDrawn = table.drawCard(cardPoolType, index);
            player.addCardHeld(cardDrawn);
        }
        catch(RuntimeException e){
            System.out.println(e);
        }
        player.setPlayerState(PlayerStates.WAIT);

        game.checkGameEndingConditions();
        nextPlayer();
    }

    /**
     * The method notifies the player that they are not allowed to perform this move during this state of the game.
     */
    @Override
    public void pickPlayerColor(Player player, PlayerColors color) {
        throw new RuntimeException("You can't pick your character color in this state");
    }

    /**
     * The method notifies the player that they are not allowed to perform this move during this state of the game.
     */
    @Override
    public void pickPlayerObjective(Player player, int objectiveIndex) {
        throw new RuntimeException("You can't pick your secret objective in this state");
    }





    //TURN SWITCHER
    /**
     * Advances the round to the next player's turn.
     */
    private void nextPlayer(){
        Player currentPlayer = players.get(currentPlayerIndex);
        int playersSize = players.size();

        //If current player is the last players call next state.
        if(currentPlayerIndex +1 == playersSize)
            nextState();

        else {
            //For all the remaining players
            for (int i = currentPlayerIndex + 1; i < playersSize; i++) {
                Player nextPlayer = players.get(i);
                //Set as current player the first online player found among the remaining ones.
                if(nextPlayer.getConnectionStatus().equals(PlayerConnectionStatus.ONLINE)){
                    currentPlayerIndex = i;
                    nextPlayer.setPlayerState(PlayerStates.PLACE);
                    return;
                }
            }

            //If no eligible players were found after the current player then advance to next round.
            nextState();
        }
    }





    //FIRST PLAYER FINDER
    /**
     * Determines the initial player for each round.<br>
     * It is necessary to prevent the game from stalling if the player at position 0 in the list has temporarily disconnected.
     *
     * @throws RuntimeException If no players are found online.
     */
    private void findFirstPlayer(){
        currentPlayerIndex = -1;
        Player firstPlayer;

        //Look for the first(in the list's order) online player in the list and set them as the current player
        for(int i = 0; i < game.getPlayers().size(); i++){
            if(game.getPlayers().get(i).getConnectionStatus().equals(PlayerConnectionStatus.ONLINE)){
                currentPlayerIndex = i;
                //The player found is set as current player.
                firstPlayer = game.getPlayers().get(currentPlayerIndex);
                //The player's state is updated to reflect their next expected move
                firstPlayer.setPlayerState(PlayerStates.PLACE);
                break;
            }
        }

        if(currentPlayerIndex == -1)
            throw new RuntimeException("No players found online");
    }





    //STATE SWITCHER
    /**
     * Switches the game state to the next state.<br>
     * Check for the boolean LastRoundFlag. If true then the next state will be the FinalRound, otherwise
     * the next state will be another MainLoop.
     */
    private void nextState(){
        game.incrementRoundsCompleted();
        if(game.isLastRoundFlag())
            game.setState(new FinalRound(game));
        else
            game.setState(new MainLoop(game));
    }
}


//Players ARE allowed to disconnect and reconnect during this phase of the game.

//If players disconnect before they have performed their last move and reconnect only after they have been
//skipped by the "next player" method, they will not perform any moves during this round.