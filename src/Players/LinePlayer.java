package Players;

import Utilities.Move;
import Utilities.StateTree;

import java.util.Scanner;

public class LinePlayer extends Player {

    boolean usedPop;

    public LinePlayer(String name, int turn, int time_lmit) {
        super(name, turn, time_lmit);
        usedPop = false;
    }


    public Move getMove(StateTree state) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Its Your Turn!");
        System.out.println("Enter p<n> to pop in Column <n>, Enter d<n> to drop in Column <n>");

        String move = scanner.nextLine();

        if(move.charAt(0) == 'p'){
            int col = Character.getNumericValue(move.charAt(1));
            if(!usedPop) {
                this.usedPop = true;
                return new Move(true, col);
            }
        } else if(move.charAt(0) == 'd') {
            int col = Character.getNumericValue(move.charAt(1));
            return new Move(false, col);
        } else {
            System.out.println("You Did Not Enter a Valid Move, You Lost Your Turn.");
            for(int j=0; j<state.columns; j++)
            {
                for(int i=0; i<state.rows; i++)
                {
                    if(state.getBoardMatrix()[i][j] == 0)
                    {
                        return new Move(false, j);
                    }
                }
            }

        }
        return null;
    }

}
