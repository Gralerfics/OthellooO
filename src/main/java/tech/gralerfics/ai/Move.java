package tech.gralerfics.ai;

public class Move {
    private int x,y;
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public Move() {
        x=-1; y=-1;
    }

    public Move(int w) {
        x = w/10;
        y = w%10;
    }

    public Move(int x,int y) {
        this.x = x;
        this.y = y;
    }

    public int getNumber() {
        return x*10+y;
    }
}
