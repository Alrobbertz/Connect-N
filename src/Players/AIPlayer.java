/*
    Written By Andrew Robbertz alrobbertz@wpi.edu and Trevor Dowd tddowd@wpi.edu
    Last Update: 09/03/18
 */

package Players;

import Referee.Referee;
import Utilities.*;
import java.util.ArrayList;

public class AIPlayer extends Player {
    //boolean to see if the pop move was used
    boolean usedPop;
    //integer to decied the max play
    int maxPly;
    long permittedTime;
    
    //
    public AIPlayer(String name, int turn, int time_limit) {
        super(name, turn, time_limit); 
        permittedTime = time_limit*(long) (Math.pow(10, 9));
        this.usedPop = false;
    }
    //function to find the best move from the state tree
    public Move getMove(StateTree state) {
        //create the new current board
        Board current_board = new Board(state);
        //long that holds a starting time to use in calculation of when to stop the move
        long start_time = System.nanoTime();

        // FOR MINIMAX
        //Action your_move = minimax(current_board, 0);

        // FOR ALPHA-BETA-SEARCH
        //maxPly = 4; // Hardcode how many levels deep to go
        //Action your_move = abSearch(current_board, 0);

        // FOR ITERATIVE-DEEPENING
        //calls the iterative deeping function to find the uptimal move
        Action your_move = iterativeDeep(current_board, start_time, permittedTime);
        //checks if move was a pop
        if (your_move.getPop()) {
            usedPop = true;
        }
        //returns the best move
        return new Move(your_move.getPop(), your_move.getColumn());
    }

    // ==============  For Iterative Deepening Search =================
    // 
    public Action iterativeDeep(Board board, long startTime, long permittedTime) {
        //
        Action bestAction = new Action(false, -1);
        long searchTime = 0;
        //for loop that checks at deeper and deeper levels as time permits
        for (int depth = 1;  (5*searchTime) < permittedTime; depth++) {
            //uses ab search to find the best move at the given depth
            this.maxPly = depth;
            bestAction = abSearch(board, 0);
            searchTime = (System.nanoTime() - startTime);
        }
        //returns the best action at the given depth that the loop was able to reach
        return bestAction;
    }

    // ==============  For Alpha-Beta Pruning =================

    @SuppressWarnings("Duplicates")
    //alpha beta pruning that returns the best action at the current play level
    public Action abSearch(Board board, int currentPly){
        //an array of actions that are valid moves
        ArrayList<Action> possible_actions = board.getActions();
        Action bestAction = new Action(false, -1);
        //finds the max utility of the actions
        double val = findMax(board, currentPly, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, possible_actions);
        //sets a value to hold the best move
        double bestValue = Double.NEGATIVE_INFINITY;
        //System.out.println("Actions: " + possible_actions);
        //System.out.println("Searching For: " + val);
        // for loop that goes through each possible action and determines which is best
        for(Action action : possible_actions){
            //compares each value to the best value for each action
            if(action.getValue() > bestValue){
                //System.out.println("FOUND BEST VALUE");
                //if it is greater value then the action is set as new best value
                bestValue = action.getValue();
                bestAction = action;
            }
        }
        //returns the best action found
        return bestAction;
    }

    @SuppressWarnings("Duplicates")
    //function that finds the max play on the board using a-b prunning 
    public double findMax(Board board, int currentPly, double alpha, double beta, ArrayList<Action> givenActions){
        //System.out.println("== MAX-VALUE Depth: " + currentPly + " A: " + alpha + " B: " + beta + " ==");
        //finds if the current play has reached the max play value
        if(terminalTest(board) || currentPly > maxPly) {
            //System.out.println("Leaf State Reached in MAX-VALUE");
            //returns the utility of this turn
            return Heuristic.utility(board, this.turn);
        }
        double val = Double.NEGATIVE_INFINITY;
        //goes through the actions and prunes according to ab pruning
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
    //finds the maxium value from alpha beta search
    public double maxValueAB(Board board, int currentPly, double alpha, double beta) {
        //System.out.println("== MAX-VALUE Depth: " + currentPly + " A: " + alpha + " B: " + beta + " ==");
        //checks to find if the play if has the max value
        if(terminalTest(board) || currentPly > maxPly) {
            //System.out.println("Leaf State Reached in MAX-VALUE");
            return Heuristic.utility(board, this.turn);
        }
        double val = Double.NEGATIVE_INFINITY;
        //sets an array of possible actions that can be taken
        ArrayList<Action> possible_actions = board.getActions();
        //loops through those actions and preforms alpha beta pruning
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
       
        //checks to see if the min value play has been reached
        if(terminalTest(board) || currentPly > maxPly) {
            //System.out.println("Leaf State Reached in MIN-VALUE");
            return Heuristic.utility(board, this.turn);
        }
        double val = Double.POSITIVE_INFINITY;
        //goes through a loop of all possible actions that can be done
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
    //function for use of minimax
    public Action minimax(Board board, int currentPly) {
        //sets intial values for bestValue and bestAction which will hold the best value move and the action to make the best move 
        double bestValue = Double.NEGATIVE_INFINITY;
        Action bestAction = new Action(false, -1);
        // gets array of all possible actions and loops throught them
        ArrayList<Action> possible_actions = board.getActions();
        for (Action action : possible_actions) {
            //creates copy of the board
            Board copyBoard = board.getCopy();
            //
            action.setValue(minValue(result(copyBoard, action), currentPly + 1));
            //checks if the current action is better then the previous best action
            if (action.getValue() > bestValue) {
                //sets it as the new best action
                bestValue = action.getValue();
                bestAction = action;
            }
        }
        //returns the action with the highest calculated value
        return bestAction;
    }

    @SuppressWarnings("Duplicates")
    //finds the max value for minimax
    public double maxValue(Board board, int currentPly) {
        System.out.println("== MAX-VALUE Depth: " + currentPly + " ==");
        //checks to see if the current play has the max value
        if (terminalTest(board) || currentPly > maxPly) {
            System.out.println("Leaf State Reached in MAX-VALUE");
            return Heuristic.utility(board, this.turn);
        }

        double val = Double.NEGATIVE_INFINITY;
        //array of possible actions
        ArrayList<Action> possible_actions = board.getActions();
        System.out.println(possible_actions);
        //loops through actions
        for (Action action : possible_actions) {
            //System.out.println("Checking Action: " + action);
            val = Math.max(val, minValue(result(board.getCopy(), action), currentPly + 1));
        }
        //returns the max value
        return val;
    }

    @SuppressWarnings("Duplicates")
    //find the minimum value for mini max
    public double minValue(Board board, int currentPly) {
        System.out.println("== MIN-VALUE Depth: " + currentPly + " ==");
        //checks to see if the min value is the current play
        if (terminalTest(board) || currentPly > maxPly) {
            System.out.println("Leaf State Reached in MIN-VALUE");
            return Heuristic.utility(board, this.turn);
        }

        double val = Double.POSITIVE_INFINITY;
        //list of possible actions
        ArrayList<Action> possible_actions = board.getActions();
        System.out.println(possible_actions);
        //loops through actions
        for (Action action : possible_actions) {
            //System.out.println("Checking Action: " + action);
            val = Math.min(val, maxValue(result(board.getCopy(), action), currentPly + 1));
        }
        //returns min value
        return val;
    }

   //function that checks if this is a board that has already won
    public boolean terminalTest(Board board) {
        return Referee.checkForWinner(board) != 0;
    }
    //makes the move on the board and returns the board
    public Board result(Board board, Move move) {
        board.makeMove(move);
        return board;
    }


}
