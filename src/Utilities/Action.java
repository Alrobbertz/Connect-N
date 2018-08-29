package Utilities;

public class Action extends Move{

    int alpha, beta; // The max and min values achievable through making the given move.

    public Action(boolean is_pop, int column_used) {
        super(is_pop, column_used);
        this.alpha = 0;
        this.beta = 0;
    }

    @Override
    public String toString() {
        String str = "{";
        if(this.pop){
            str += "POP in Column: " + column;
        } else {
            str += "DROP in Column: " + column;
        }
        str += " Alpha: " + alpha + " Beta: " + beta + "}";
        return str;
    }

}
