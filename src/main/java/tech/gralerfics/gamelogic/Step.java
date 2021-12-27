package tech.gralerfics.gamelogic;

public class Step {
    private int i, j, color;

    public Step(int i, int j, int color) {
        this.i = i;
        this.j = j;
        this.color = color;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
