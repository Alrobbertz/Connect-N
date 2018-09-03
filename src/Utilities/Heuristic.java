/*
    Written By Andrew Robbertz alrobbertz@wpi.edu and Trevor Dowd tddowd@wpi.edu
    Last Update: 09/03/18
 */

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
            for (int column = 0; column < board.columns - N; column++) {
                int in_a_row = 0;
                int max = 0;
                int startc = -1;
                int startr = -1;
                //checks to see pieces in a horizontal row
                for (int offset = 0; offset <= N; offset++) {
                    //System.out.println("Check Horizontal:" + " row:" + row + " col: " + (column + offset) );
                    int value = data[row][column + offset];
                    //checks if value matches the players turn
                    if (value == player_turn)
                    {
                        in_a_row += 1;
                        //sets the new max for consecutive pieces
                        if (in_a_row > max) {
                            max = in_a_row;
                            //start postions for column and row
                            startc = column + offset - (max - 1);
                            startr = row;
                        }
                    }
                    //sets in a row to 0 if there are currently no pieces in a row
                    else
                    {
                        in_a_row = 0;
                    }
                }

                boolean isopen = false;

                // Calcuate, based on startr and startc, whether the sequence is open or not
                // An open sequence is a sequence, that has the possibility to be a sequence of four in a row

                for (int offset = 0; offset <= N - max; offset++) { 
                    //checks to see if it is within the board
                    if (startc - offset + (N-1) >= board.columns) { continue; }
                    if (startc - offset < 0) { break; }

                    boolean seq = true;
                    //checks the columns
                    for (int i = 0; i < N; i++) {
                        int v = data[row][startc - offset + i];
                        
                        seq = seq && (v == 0 || v == player_turn);

                        if (!seq) { break; }
                    }

                    if (seq) { isopen = true; break; }
                }

                // Only add values for open sequences or sequences of four
                if (isopen || max == N) {
                    hVal += deltaheurestic(max, board.winNumber);
                }
            }
        }

        // Check vertical pieces in a row
        for (int column = 0; column < board.columns; column++) {
            //goes through the rows
            for (int row = 0; row < board.rows - N; row++) {
                //sets starting parameters
                int in_a_row = 0;
                int max = 0;
                int startc = -1;
                int startr = -1;
                //checks through rows with the offset
                for (int offset = 0; offset <= N; offset++) { 
                    //System.out.println("Check Vertical:" + " row:" + (row + offset) + " col: " + column );
                    int value = data[row + offset][column];
                    //checks if the value pulled from data is the players turn
                    if (value == player_turn)
                    {
                        //adds one to in a row 
                        in_a_row += 1;
                        if (in_a_row > max) {
                            //sees if it the new max for items in a row
                            max = in_a_row;

                            startc = column;
                            startr = row + offset - (max - 1);
                        }
                    }
                    //if their is no max sets in a row sets it as 0
                    else
                    {
                        in_a_row = 0;
                    }
                }


                boolean isopen = false;
                //looks through rows with the offset from 0 to win conditions
                for (int offset = 0; offset <= N - max; offset++) { 
                    if (startr - offset + (N-1) >= board.rows) { continue; }
                    if (startr - offset < 0) { break; }
    
                    boolean seq = true;
                    //checks the rows
                    for (int i = 0; i < N; i++) { 
                        int v = data[startr - offset + i][column];
                        
                        seq = seq && (v == 0 || v == player_turn);

                        if (!seq) { break; }
                    }

                    if (seq) { isopen = true; break; }
                }

                // Only add values for open sequences or sequences of four
                if (isopen || max == N) {
                    hVal += deltaheurestic(max, board.winNumber);
                }
            }
        }

        // Check diagonal for pieces in a row works the same as the previous two but now is checking the diagonal
        for (int column =  0; column < board.columns; column++) {
            //System.out.println("Check Diagonal Inside Column Loop Forwards");
            for (int row = 0; row < board.rows; row++) {
                //System.out.println("Check Diagonal Row Loop Forward");
                int in_a_row = 0;
                int maxConnection = 0;

                for (int offset = 0; offset <= N; offset++) {
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

                hVal += deltaheurestic(maxConnection, board.winNumber);
            }

            for (int row = 0; row < board.rows; row++) {
                int in_a_row = 0;
                int max = 0;

                for (int offset = 0; offset <= N; offset++) {
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

                hVal += deltaheurestic(max, board.winNumber);
            }
        }

        return hVal;
    }
       //gets the value of each move on the board
    private static int getValue(Board board, int row, int col){
        //checks to see if it is in the board
        if((row >= 0 && row < board.rows) && (col >= 0 && col < board.columns)){
            //returns value of postion
            return board.getBoardMatrix()[row][col];
        }
        //error if it is outide the board
        return -1;
    }

    public static int deltaheurestic(int max, int N) {
        int h = (int)Math.pow(max, max);

        if (max == N) { h = (int)Math.pow(h, max); } 

        return h;
    }

    public static int distributionScore(Board board, int player_turn) {
        int[][] data = board.getBoardMatrix();
        int score = 0;
        for(int i = 0; i < board.rows; i++){
            for(int j = 0; j < board.columns; j++) {
                if( data[i][j] == player_turn) {
                    score += Math.abs(board.winNumber - i);
                    score += Math.abs(board.winNumber - j);
                } else if( data[i][j] == Math.abs(player_turn - 3)) {
                    score -= Math.abs(board.winNumber - i);
                    score -= Math.abs(board.winNumber - j);
                }
            }
        }
    return score;
    }


}
