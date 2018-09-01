package Players;

import Referee.Referee;
import Utilities.*;

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
         */
        long start_time = System.nanoTime();
        System.out.println("Start Time: " + start_time);

        Action a = abSearch(current_board, 0);
        System.out.println("Best Action Found: " + a);


        if(a.getPop()){
            usedPop = true;
        }
        return new Move(a.getPop(), a.getColumn());
    }

    public Action iterativeDeep(long startTime, int permitedTime, Board boardState){
        long searchTime =0;
        for (int depth = 0; ((searchTime*searchTime)/1000) < permitedTime; depth++) {
            long startSearch = System.nanoTime();
            Action bestAction = abSearch(boardState, depth);
            searchTime =+ (System.nanoTime() - startSearch);
        }
        return bestAction;
    }

    public Action abSearch(Board board, int depth) {
        double val = maxValue(board, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        System.out.println("Original MAX-VALUE returned with: " + val);
        return bestAction;
    }

    public double maxValue(Board board, double alpha, double beta) {
        System.out.println("MAX-VALUE CALLED A: " + alpha + " B: " + beta);
        double utility = 0;
        if(terminalTest(board) != 0) {
            utility = Heuristic.utility(board, this.turn);
            System.out.println("Heuristic.utility() Returned a non-zero utility: " + utility);
            return utility;
        }

        Board boardCopy = board.getCopy();
        double val = Double.NEGATIVE_INFINITY;
        for(Action action: getActions(boardCopy)){          //For each of the possible actions
            val = Math.max(val, minValue(result(boardCopy,action), alpha, beta));
            if(val >= beta) {
                System.out.println("val >= beta: " + val);
                bestAction = action;                        // Not Part of the pseudocde
                //return val;                               // This is in the book's pseudocode for AB-Search but it terminates our algorithm after 2ply
                beta = val;
            }
            alpha = Math.max(alpha, val);
        }

        System.out.println("MAX-VALUE hit bottom. Returns: " + val);
        return val;
    }

    public double minValue(Board board, double alpha, double beta) {
        System.out.println("MIN-VALUE CALLED A: " + alpha + " B: " + beta);
        double utility = 0;
        if((terminalTest(board)) != 0) {
            utility = Heuristic.utility(board, this.turn);
            System.out.println("Heuristic.utility() Returned a non-zero utility: " + utility);
            return utility;
        }

        Board boardCopy = board.getCopy();
        double val = Double.POSITIVE_INFINITY;
        for(Action action: getActions(boardCopy)){          // For each of the possible actions
            val = Math.min(val, maxValue(result(boardCopy,action), alpha, beta));
            if(val <= alpha) {
                System.out.println("val <= alpha: " + val);
                //return val; // This is in the book's pseudocode for AB-Search but it terminates our algorithm after 2ply
                alpha = val;
            }
            beta = Math.min(beta, val);
        }

        System.out.println("MIN-VALUE hit bottom. Returns: " + val);
        return val;
    }

    public double terminalTest(Board board) {
        int win = Referee.checkForWinner(board);
        if(win == this.turn) return 1;
        else if(win == Math.abs(this.turn - 3)) return -1;
        else return 0; //todo find a better solution for this
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

