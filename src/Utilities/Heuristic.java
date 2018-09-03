package Utilities;

public class Heuristic {
    //assigns a value on the viabilty of each move
    public static int utility(Board board, int player_turn) {
        //creates a copy of the board
        int[][] data = board.getCopy().getBoardMatrix();
        //number of consecutive pieces that is required for a win
        int N = board.winNumber;

        int hVal = 0;

        // Checks the board for the horizontal pieces
        for (int row = 0; row < board.rows; row++) {
            for (int column = 0; column < board.columns - N; column++) { // TODO Will have to change this for different N
                int in_a_row = 0;
                int max = 0;
                int startc = -1;
                int startr = -1;
                //checks to see pieces in a horizontal row
                for (int offset = 0; offset <= N; offset++) { // TODO Will have to change this for different N
                    //System.out.println("Check Horizontal:" + " row:" + row + " col: " + (column + offset) );
                    int value = data[row][column + offset];
                    //checks if value matches the players turn
                    if (value == player_turn)
                    {
                        in_a_row += 1;
                        //sets the new max for consecutive pieces
                        if (in_a_row > max) {
                            max = in_a_row;

                            startc = column + offset - (max - 1);
                            startr = row;
                        }
                    }
                    else
                    {
                        in_a_row = 0;
                    }
                }

                boolean isopen = false;

                // Calcuate, based on startr and startc, whether the sequence is open or not
                // An open sequence is a sequence, that has the possibility to be a sequence of four in a row

                for (int offset = 0; offset <= N - max; offset++) { // TODO Will have to change this for different N
                    if (startc - offset + (N-1) >= board.columns) { continue; }
                    if (startc - offset < 0) { break; }

                    boolean seq = true;
                    for (int i = 0; i < N; i++) { // TODO Will have to change this for different N
                        int v = data[row][startc - offset + i];

                        seq = seq && (v == 0 || v == player_turn);

                        if (!seq) { break; }
                    }

                    if (seq) { isopen = true; break; }
                }

                // Only add values for open sequences or sequences of four
                if (isopen || max == N) { // TODO Will have to change this for different N
                    hVal += deltaheurestic(max);
                }
            }
        }

        // Check vertical
        for (int column = 0; column < board.columns; column++) {
            for (int row = 0; row < board.rows - N; row++) { // TODO Will have to change this for different N
                int in_a_row = 0;
                int max = 0;
                int startc = -1;
                int startr = -1;

                for (int offset = 0; offset <= N; offset++) { // TODO Will have to change this for different N
                    //System.out.println("Check Vertical:" + " row:" + (row + offset) + " col: " + column );
                    int value = data[row + offset][column];

                    if (value == player_turn)
                    {
                        in_a_row += 1;
                        if (in_a_row > max) {
                            max = in_a_row;

                            startc = column;
                            startr = row + offset - (max - 1);
                        }
                    }
                    else
                    {
                        in_a_row = 0;
                    }
                }


                boolean isopen = false;

                for (int offset = 0; offset <= N - max; offset++) { // TODO Will have to change this for different N
                    if (startr - offset + (N-1) >= board.rows) { continue; }
                    if (startr - offset < 0) { break; }

                    boolean seq = true;
                    for (int i = 0; i < N; i++) { // TODO Will have to change this for different N
                        int v = data[startr - offset + i][column];

                        seq = seq && (v == 0 || v == player_turn);

                        if (!seq) { break; }
                    }

                    if (seq) { isopen = true; break; }
                }

                // Only add values for open sequences or sequences of four
                if (isopen || max == N) { // TODO Will have to change this for different N
                    hVal += deltaheurestic(max);
                }
            }
        }

        // Check diagonal
        for (int column =  0; column < board.columns; column++) { // TODO Will have to change this for different N
            //System.out.println("Check Diagonal Inside Column Loop Forwards");
            for (int row = 0; row < board.rows; row++) {
                //System.out.println("Check Diagonal Row Loop Forward");
                int in_a_row = 0;
                int maxConnection = 0;

                for (int offset = 0; offset <= N; offset++) { // TODO Will have to change this for different N
                    //System.out.println("Check Diagonal Forward:" + " row:" + (row + offset) + " col: " + (column + offset) );
                    int value = getValue(board, row + offset, column + offset);

                    if (value == player_turn)
                    {
                        in_a_row += 1;
                        if (in_a_row > maxConnection) {
                            maxConnection = in_a_row;
                        }
                    }
                    else
                    {
                        in_a_row = 0;
                    }
                }

                hVal += deltaheurestic(maxConnection);
            }

            for (int row = 0; row < board.rows; row++) { // TODO Will have to change this for different N
                int in_a_row = 0;
                int max = 0;

                for (int offset = 0; offset <= N; offset++) {  // TODO Will have to change this for different N
                    //System.out.println("Check Diagonal Back:" + " row:" + (row + offset) + " col: " + (column - offset) );
                    int value = getValue(board, row + offset, column - offset);

                    if (value == player_turn)
                    {
                        in_a_row += 1;
                        if (in_a_row > max) {
                            max = in_a_row;
                        }
                    }
                    else
                    {
                        in_a_row = 0;
                    }
                }

                hVal += deltaheurestic(max);
            }
        }

        return hVal;
    }

    private static int getValue(Board board, int row, int col){
        if((row >= 0 && row < board.rows) && (col >= 0 && col < board.columns)){
            return board.getBoardMatrix()[row][col];
        }

        return -1;
    }

    public static int deltaheurestic(int max) {
        int h = (int)Math.pow(max, max);

        if (max == 4) { h = (int)Math.pow(h, max); } // TODO Will have to change this for different N

        return h;
    }

}
