package Players;

import Referee.Referee;
import Utilities.Action;
import Utilities.Board;
import Utilities.Move;
import Utilities.StateTree;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;

public class AutoPlayer extends Player {

    boolean usedPop;
    Action bestAction;

    public AutoPlayer(String name, int turn, int time_limit) {
        super(name, turn, time_limit); //TODO figure out the units for time_limit
        this.usedPop = false;
        this.bestAction = new Action(false, -1);
    }

    public Move getMove(StateTree state) {
        Board current_board = new Board(state);

        /* READ ME
        Right now we're not using the alpha and beta held in each Action
        We could switch back to using only Move if we wanted to (i think)

        We also don't ever set the alpha or beta for each Board
        so we might be able use StateTrees still.

        At least we have the more powerful option implemented now.

        also, it only calls MIN and MAX once for each move
        so idk if this is a problem in those functions or if its just the terminal-test
         */

        Action a = abSearch(current_board);
        System.out.println("Best Action Found: " + a);


        if(a.getPop()){
            usedPop = true;
        }
        return new Move(a.getPop(), a.getColumn());
    }

    public Action abSearch(Board board) {
        double val = maxValue(board, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

        return bestAction;
    }

    public double maxValue(Board board, double alpha, double beta) {
        System.out.println("MAX-VALUE CALLED");
        double utility = 0;
        if((utility = Referee.checkForWinner(board)) != 0) {
            return utility;
        }

        Board boardCopy = board.getCopy();
        double val = Double.NEGATIVE_INFINITY;
        ArrayList<Action> possible_actions = getActions(boardCopy);
        for(Action action: possible_actions){
            val = Math.max(val, minValue(result(boardCopy,action), alpha, beta));
            if(val >= beta) {
                bestAction = action;
                return val;
            }
            alpha = Math.max(alpha, val);
        }

        return val;
    }

    public double minValue(Board board, double alpha, double beta) {
        System.out.println("MIN-VALUE CALLED");
        double utility = 0;
        if((utility = Referee.checkForWinner(board)) != 0) {
            return utility;
        }

        Board boardCopy = board.getCopy();
        double val = Double.POSITIVE_INFINITY;
        ArrayList<Action> possibe_actions = getActions(boardCopy);
        for(Action action: possibe_actions){
            val = Math.min(val, maxValue(result(boardCopy,action), alpha, beta));
            if(val <= alpha) {
                this.bestAction = action;
                return val;
            }
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
        //System.out.println(validMoves);
        return validMoves;
    }

}

