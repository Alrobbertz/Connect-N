package Utilities;

import java.util.ArrayList;

public class Board extends StateTree {
    double alpha, beta;

    public Board(int rows, int cols, int win, int turn, boolean pop1, boolean pop2, StateTree parent, double alpha, double beta) {
        super(rows, cols, win, turn, pop1, pop2, parent);
        this.alpha = alpha;
        this.beta = beta;
    }

    public Board getCopy () {
        Board copy = new Board(this.rows, this.columns, this.winNumber, this.turn, this.pop1, this.pop2, this.parent, this.alpha, this.beta);

        for (int i = 0; i < copy.rows; i++) {
            for(int j = 0; j < copy.columns; j++) {
                copy.boardMatrix[i][j] = this.boardMatrix[i][j];
            }
        }

        return copy;
    }

}
