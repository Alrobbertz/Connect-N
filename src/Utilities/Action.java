package Utilities;

public class Action extends Move{

    double value;

    public Action(boolean is_pop, int column_used) {
        super(is_pop, column_used);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        String str = "{";
        if(this.pop){
            str += "P" + column;
        } else {
            str += "D" + column;
        }
        str += " Value: " + value + "}";
        return str;
    }

}
