package Players;

import Utilities.Action;
import Utilities.Move;
import Utilities.StateTree;

import java.util.ArrayList;


/**
 * This is an example of how to make a player.
 * This player is extremely simple and does no tree building
 * but its good to test against at first.
 *
 * @author Ethan Prihar
 */
public class SimplePlayer1 extends Player {

    boolean usedPop;

    public SimplePlayer1(String n, int t, int l) {
        super(n, t, l);
        usedPop = false;
    }

    public Move getMove(StateTree state) {

        ArrayList<Action> validMoves = this.getActions(state);

        for (int j = 0; j < state.columns; j++) {
            for (int i = 0; i < state.rows; i++) {
                if (state.getBoardMatrix()[i][j] == 0) {
                    return new Move(false, j);
                }
            }
        }
        return new Move(false, 100);
    }

    // Returns the list of all the valid moves the current player can make at a given state in the game.
    @SuppressWarnings("Duplicates")
    public ArrayList<Action> getActions(StateTree state) {
        ArrayList<Action> validMoves = new ArrayList<Action>();
        int[][] board = state.getBoardMatrix();
        if (state.turn == this.turn) {
            for (int i = 0; i < state.columns; i++) {
                if (!this.usedPop) {
                    if (board[0][i] == this.turn) validMoves.add(new Action(true, i));
                }
                //check for valid columns
                if (board[state.rows - 1][i] == 0) validMoves.add(new Action(false, i));
            }
        }
        System.out.println(validMoves);
        return validMoves;
    }

}