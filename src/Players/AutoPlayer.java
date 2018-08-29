package Players;

import Utilities.Move;
import Utilities.StateTree;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;

public class AutoPlayer extends Player {

    boolean usedPop;

    public AutoPlayer(String name, int turn, int time_limit) {
        super(name, turn, time_limit); //TODO figure out the units for time_limit
        this.usedPop = false;
    }

    public Move getMove(StateTree state) {

        //GENERAL NOTES
        /*

            ALL WITHIN MINIMAX
                Make a function that returns all the possible moves
                iterate through them
                recursively explore those board states.




            Implement a node class by extending StateTree
            Implement a real tree?

         */

        /*
        MINIMAX (s) = {
            Utility(s)      if TERMINAL-STATE(s)
            max for each possible action (MINIMAX(actions, s)) if its max's turn
            min for each possible action (MINIMAX(actions, s)) if its min's turn

        }
         */
        ArrayList<Move> validMoves = this.getActions(state);

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
    public ArrayList<Move> getActions(StateTree state) {
        ArrayList<Move> validMoves = new ArrayList<Move>();
        int[][] board = state.getBoardMatrix();
        if (state.turn == this.turn) {
            for (int i = 0; i < state.columns; i++) {
                if (!this.usedPop) {
                    if (board[0][i] == this.turn) validMoves.add(new Move(true, i));
                }
                //check for valid columns
                if (board[state.rows-1][i] == 0) validMoves.add(new Move(false, i));
            }
        }
        System.out.println(validMoves);
        return validMoves;
    }

    //this is a new change

}

