package Model.Game.States;

import Model.Game.CardPoolTypes;
import Model.Game.Table;
import Model.Objectives.Objective;
import Model.Player.*;
import Model.Game.Game;

import java.util.List;
import java.util.Map;

/**
 * Represents the final round state in the game, where players can only place cards but not draw new ones.
 * This state implements the GameState interface and defines methods to handle actions during the final round.
 */
public class FinalRound implements GameState{
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
    public FinalRound(Game game){
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
            player.playCard(cardIndex, coordinateIndex, faceUp);
            player.setPlayerState(PlayerStates.WAIT);
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
        nextPlayer();
    }

    /**
     * The method notifies the player that they are not allowed to perform this move during this state of the game.
     */
    @Override
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) {
        throw new RuntimeException("You can't draw a new card during the final round");
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
     * Advances the game to the next player's turn.
     */
    private void nextPlayer(){
        Player currentPlayer = game.getPlayers().get(currentPlayerIndex);
        currentPlayer.setPlayerState(PlayerStates.WAIT);
        int playerCount = game.getPlayers().size();

        if(currentPlayerIndex +1 == playerCount)
            nextState();
        else {
            for (int i = currentPlayerIndex + 1; i < game.getPlayers().size(); i++) {
                Player nextPlayer = game.getPlayers().get(i);
                if(nextPlayer.getConnectionStatus().equals(PlayerConnectionStatus.ONLINE)){
                    currentPlayerIndex = i;
                    nextPlayer.setPlayerState(PlayerStates.PLACE);
                    return;
                }
            }

            nextState();
        }
    }





    //FIRST PLAYER FINDER
    /**
     * Determines the initial player for each round.<br>
     * It is necessary to prevent the game from stalling if the player at position 0 in the list has disconnected.
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
     * Switches the game state to the next state: GameEnd.
     */
    private void nextState(){
        game.incrementRoundsCompleted();
        game.gameOver();
    }
}

//Players ARE allowed to disconnect and reconnect during this phase of the game.

//If players disconnect before they have performed their last move and reconnect only after they have been
//skipped by the "next player" method, they will not perform any moves during this round.