package Players;

import Referee.Referee;
import Utilities.*;

import java.util.ArrayList;

public class AutoPlayer2 extends Player {

    boolean usedPop;
    int maxPly;

    public AutoPlayer2(String name, int turn, int time_limit) {
        super(name, turn, time_limit); //TODO figure out the units for time_limit
        this.usedPop = false;
        maxPly = 3;
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

        Action your_move = minimax(current_board, 0);

        if (your_move.getPop()) {
            usedPop = true;
        }
        return new Move(your_move.getPop(), your_move.getColumn());
    }

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
            System.out.println("Checking Action: " + action);
            val = Math.max(val, minValue(result(board.getCopy(), action), currentPly + 1));
        }

        return val;
    }

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
            System.out.println("Checking Action: " + action);
            val = Math.min(val, maxValue(result(board.getCopy(), action), currentPly + 1));
        }

        return val;
    }

    public Action iterativeDeep(long startTime, int permitedTime, Board boardState) {
        long searchTime = 0;
        for (int depth = 0; ((searchTime * searchTime) / 1000) < permitedTime; depth++) {
            long startSearch = System.nanoTime();
            //Action bestAction = abSearch(boardState, depth);
            searchTime = +(System.nanoTime() - startSearch);
        }
        return new Action(false, -1);
    }


    public boolean terminalTest(Board board) {
        return Referee.checkForWinner(board) != 0;
    }

    public Board result(Board board, Move move) {
        board.makeMove(move);
        return board;
    }


}
