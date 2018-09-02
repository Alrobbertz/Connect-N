package Players;

import Referee.Referee;
import Utilities.*;
import java.util.ArrayList;

public class AIPlayer extends Player {

    boolean usedPop;
    int maxPly;

    public AIPlayer(String name, int turn, int time_limit) {
        super(name, turn, time_limit); //TODO figure out the units for time_limit
        this.usedPop = false;
    }

    public Move getMove(StateTree state) {
        Board current_board = new Board(state);

        long start_time = System.nanoTime();
        long permittedTtime = 10 * (long) (Math.pow(10, 9)); //TODO change this to real time_limit
        System.out.println("Start Time: " + start_time / (long)(Math.pow(10, 9)) + " seconds");
        System.out.println("Permitted Time: " + permittedTtime / (long)(Math.pow(10, 9)) + " seconds");


        // FOR MINIMAX
        //Action your_move = minimax(current_board, 0);

        // FOR ALPHA-BETA-SEARCH
        //maxPly = 4; // Hardcode how many levels deep to go
        //Action your_move = abSearch(current_board, 0);

        // FOR ITERATIVE-DEEPENING
        Action your_move = iterativeDeep(current_board, start_time, permittedTtime);

        if (your_move.getPop()) {
            usedPop = true;
        }
        return new Move(your_move.getPop(), your_move.getColumn());
    }

    // ==============  For Iterative Deepening Search =================

    public Action iterativeDeep(Board board, long startTime, long permittedTime) {
        Action bestAction = new Action(false, -1);
        long searchTime = 0;
        for (int depth = 0;  (2*searchTime) < permittedTime; depth++) {
            System.out.println("ID-SEARCH @depth: " + depth);
            this.maxPly = depth;
            bestAction = abSearch(board, 0);
            searchTime = (System.nanoTime() - startTime);
        }
        return bestAction;
    }

    // ==============  For Alpha-Beta Pruning =================

    @SuppressWarnings("Duplicates")
    public Action abSearch(Board board, int currentPly){
        ArrayList<Action> possible_actions = board.getActions();
        Action bestAction = new Action(false, -1);

        double val = findMax(board, currentPly, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, possible_actions);

        double bestValue = Double.NEGATIVE_INFINITY;
        //System.out.println("Actions: " + possible_actions);
        //System.out.println("Searching For: " + val);
        for(Action action : possible_actions){
            if(action.getValue() > bestValue){
                //System.out.println("FOUND BEST VALUE");
                bestValue = action.getValue();
                bestAction = action;
            }
        }
        return bestAction;
    }

    @SuppressWarnings("Duplicates")
    public double findMax(Board board, int currentPly, double alpha, double beta, ArrayList<Action> givenActions){
        //System.out.println("== MAX-VALUE Depth: " + currentPly + " A: " + alpha + " B: " + beta + " ==");
        if(terminalTest(board) || currentPly > maxPly) {
            //System.out.println("Leaf State Reached in MAX-VALUE");
            return Heuristic.utility(board, this.turn);
        }
        double val = Double.NEGATIVE_INFINITY;
        for(Action action : givenActions) {
            val = Math.max(val, minValueAB(result(board.getCopy(), action), currentPly + 1, alpha, beta));
            action.setValue(val);
            if(val >= beta) {
                return val;
            }
            alpha = Math.max(alpha, val);
        }

        return val;
    }

    @SuppressWarnings("Duplicates")
    public double maxValueAB(Board board, int currentPly, double alpha, double beta) {
        //System.out.println("== MAX-VALUE Depth: " + currentPly + " A: " + alpha + " B: " + beta + " ==");
        if(terminalTest(board) || currentPly > maxPly) {
            //System.out.println("Leaf State Reached in MAX-VALUE");
            return Heuristic.utility(board, this.turn);
        }
        double val = Double.NEGATIVE_INFINITY;
        ArrayList<Action> possible_actions = board.getActions();
        for(Action action : possible_actions) {
            val = Math.max(val, minValueAB(result(board.getCopy(), action), currentPly + 1, alpha, beta));
            if(val >= beta) {
                return val;
            }
            alpha = Math.max(alpha, val);
        }

        return val;
    }

    @SuppressWarnings("Duplicates")
    public double minValueAB(Board board, int currentPly, double alpha, double beta) {
        //System.out.println("== MIN-VALUE Depth: " + currentPly + " A: " + alpha + " B: " + beta + " ==");
        if(terminalTest(board) || currentPly > maxPly) {
            //System.out.println("Leaf State Reached in MIN-VALUE");
            return Heuristic.utility(board, this.turn);
        }
        double val = Double.POSITIVE_INFINITY;
        ArrayList<Action> possible_actino = board.getActions();
        for( Action action : possible_actino) {
            val = Math.min(val, maxValueAB(result(board.getCopy(), action), currentPly + 1, alpha, beta));
            if(val <= alpha) {
                return val;
            }
            beta = Math.min(beta, val);
        }
        return val;
    }

    // ==============  For MINIMAX =================

    @SuppressWarnings("Duplicates")
    public Action minimax(Board board, int currentPly) {
        double bestValue = Double.NEGATIVE_INFINITY;
        Action bestAction = new Action(false, -1);
        ArrayList<Action> possible_actions = board.getActions();
        for (Action action : possible_actions) {
            Board copyBoard = board.getCopy();
            action.setValue(minValue(result(copyBoard, action), currentPly + 1));

            if (action.getValue() > bestValue) {
                bestValue = action.getValue();
                bestAction = action;
            }
        }

        return bestAction;
    }

    @SuppressWarnings("Duplicates")
    public double maxValue(Board board, int currentPly) {
        System.out.println("== MAX-VALUE Depth: " + currentPly + " ==");
        if (terminalTest(board) || currentPly > maxPly) {
            System.out.println("Leaf State Reached in MAX-VALUE");
            return Heuristic.utility(board, this.turn);
        }

        double val = Double.NEGATIVE_INFINITY;
        ArrayList<Action> possible_actions = board.getActions();
        System.out.println(possible_actions);
        for (Action action : possible_actions) {
            //System.out.println("Checking Action: " + action);
            val = Math.max(val, minValue(result(board.getCopy(), action), currentPly + 1));
        }

        return val;
    }

    @SuppressWarnings("Duplicates")
    public double minValue(Board board, int currentPly) {
        System.out.println("== MIN-VALUE Depth: " + currentPly + " ==");
        if (terminalTest(board) || currentPly > maxPly) {
            System.out.println("Leaf State Reached in MIN-VALUE");
            return Heuristic.utility(board, this.turn);
        }

        double val = Double.POSITIVE_INFINITY;
        ArrayList<Action> possible_actions = board.getActions();
        System.out.println(possible_actions);
        for (Action action : possible_actions) {
            //System.out.println("Checking Action: " + action);
            val = Math.min(val, maxValue(result(board.getCopy(), action), currentPly + 1));
        }

        return val;
    }


    public boolean terminalTest(Board board) {
        return Referee.checkForWinner(board) != 0;
    }

    public Board result(Board board, Move move) {
        board.makeMove(move);
        return board;
    }


}
