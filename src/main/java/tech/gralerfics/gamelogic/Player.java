package tech.gralerfics.gamelogic;

import java.awt.*;

public class Player {
    public int nextStep = -1;
    public int tag = 0; // 0 - local; 1x - ai ; -1 - global
    public int wins = 0, fines = 0, fails = 0;
    public String name;
    public Color color = new Color(204, 204, 204);

    public Player() {}
    public Player(Player p) {
        this.name = p.name;
        this.tag = p.tag;
        this.wins = p.wins;
        this.fines = p.fines;
        this.fails = p.fails;
        this.color = p.color;
    }

    public double getRate() {
        float r = wins, t = wins + fines + fails;
        if (wins == 0 && fines == 0 && fails == 0) {
            return -1;
        } else {
            return r / t;
        }
    }
}
