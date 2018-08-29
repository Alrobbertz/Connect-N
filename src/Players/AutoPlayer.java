package Players;

import Referee.Referee;
import Utilities.Board;
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


    public Move abSearch(Board board) {
        double val = maxValue(board, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        //return the action in ACTIONS(s) with value val

        return null;
    }

    public double maxValue(Board board, double alpha, double beta) {
        double utility = 0;
        if((utility = Referee.checkForWinner(board)) != 0) {
            return utility;
        }

        Board boardCopy = board.getCopy();
        double val = Double.NEGATIVE_INFINITY;
        ArrayList<Move> actions = getActions(boardCopy);
        for(Move move: actions){
            val = Math.max(val, minValue(result(boardCopy,move), alpha, beta));
            if(val >= beta) return val;
            alpha = Math.max(alpha, val);
        }

        return val;
    }

    public double minValue(Board board, double alpha, double beta) {
        double utility = 0;
        if((utility = Referee.checkForWinner(board)) != 0) {
            return utility;
        }

        Board boardCopy = board.getCopy();
        double val = Double.POSITIVE_INFINITY;
        ArrayList<Move> actions = getActions(boardCopy);
        for(Move move: actions){
            val = Math.min(val, maxValue(result(boardCopy,move), alpha, beta));
            if(val <= alpha) return val;
            beta = Math.min(beta, val);
        }

        return val;
    }

    public double terminalTest(Board board) {
        int win = Referee.checkForWinner(board);
        if(win == 0) return 0;
        else if(win == this.turn) return 1;
        else if(win == 3) return 0; //todo find a better solution for this
        else return -1;
    }

    public Board result(Board board, Move move) {
        board.makeMove(move);
        return board;

    }

    // Returns the list of all the valid moves the current player can make at a given state in the game.
    @SuppressWarnings("Duplicates")
    public ArrayList<Move> getActions(StateTree state) {
        ArrayList<Move> validMoves = new ArrayList<Move>();
        int[][] board = state.getBoardMatrix();
        if (state.turn == this.turn) {
            for (int i = 0; i < state.columns; i++) {
                if (!this.usedPop) {
                    if (board[0][i] == this.turn) validMoves.add(new Move(true, i));
                }
                //check for valid columns
                if (board[state.rows - 1][i] == 0) validMoves.add(new Move(false, i));
            }
        }
        System.out.println(validMoves);
        return validMoves;
    }

}

