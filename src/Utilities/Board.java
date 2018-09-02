package Utilities;

import java.util.ArrayList;

public class Board extends StateTree {

    public Board(int rows, int cols, int win, int turn, boolean pop1, boolean pop2, StateTree parent) {
        super(rows, cols, win, turn, pop1, pop2, parent);
    }

    public Board(StateTree s) {
        super(s.rows, s.columns, s.winNumber, s.turn, s.pop1, s.pop2, s.parent);
        this.boardMatrix = s.getBoardMatrix();
    }

    // Returns the list of all the valid moves the current player can make at a given state in the game.
    @SuppressWarnings("Duplicates")
    public ArrayList<Action> getActions() {
        ArrayList<Action> validMoves = new ArrayList<Action>();
        int[][] board = this.getBoardMatrix();
        for (int i = 0; i < columns; i++) {
            if (turn == 1 && !pop1) {
                if (board[0][i] == turn) validMoves.add(new Action(true, i));
            }
            if (turn == 2 && !pop2) {
                if (board[0][i] == turn) validMoves.add(new Action(true, i));
            }
            //check for valid columns
            if (board[rows - 1][i] == 0) validMoves.add(new Action(false, i));
        }

        //System.out.println(validMoves);
        return validMoves;
    }

    public Board getCopy() {
        Board copy = new Board(this.rows, this.columns, this.winNumber, this.turn, this.pop1, this.pop2, this.parent);
        for (int i = 0; i < copy.rows; i++) {
            for (int j = 0; j < copy.columns; j++) {
                copy.boardMatrix[i][j] = this.boardMatrix[i][j];
            }
        }
        return copy;
    }

    public ArrayList<StateTree> getChildren() {
        return this.children;
    }

}
