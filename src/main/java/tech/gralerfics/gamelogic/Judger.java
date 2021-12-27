package tech.gralerfics.gamelogic;

import tech.gralerfics.raytracer.scenes.SceneManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Judger {
    private final int[][] dirset = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    public int[][] contents = new int[8][8];
    public ArrayList<Step> stepList = new ArrayList<>();
    public int curChess;

    public Game game;

    public Judger(Game game) {
        this.game = game;
    }

    /**
     * 初始化初始四子、黑方先手
     */
    public void init() {
        for (int i = 0; i <= 7; i ++) Arrays.fill(contents[i], 0);
        contents[3][3] = contents[4][4] = 1;
        contents[3][4] = contents[4][3] = -1;
        curChess = 1;
    }

    /**
     * 判断某点是否在边界内
     */
    private boolean checkIn(int i, int j) {
        return !(i < 0 || j < 0 || i >= 8 || j >= 8);
    }

    /**
     * 对某点起某个方向的判定
     */
    private boolean checkDir(int i, int j, int cur, int[][] v, int D) {
        if (v[i][j] != 0) return false;
        int offsetI = dirset[D][0], offsetJ = dirset[D][1], oI = i + offsetI, oJ = j + offsetJ;
        if (!checkIn(oI, oJ) || v[oI][oJ] != -cur) return false;
        for (int I = oI, J = oJ; checkIn(I, J) && v[I][J] != 0; I += offsetI, J += offsetJ) if (v[I][J] == cur) return true;
        return false;
    }

    /**
     * 翻转某点起的某个方向
     */
    private void reverse(int i, int j, int cur, int[][] v, int D) {
        int offsetI = dirset[D][0], offsetJ = dirset[D][1], oI = i + offsetI, oJ = j + offsetJ;
        for (int I = oI, J = oJ; checkIn(I, J) && v[I][J] != 0; I += offsetI, J += offsetJ) {
            if (v[I][J] == cur) return;
            v[I][J] = cur;
        }
    }

    /**
     * 某点能否落子
     */
    public boolean check(int i, int j, int cur) {
        if (contents[i][j] != 0) return false;
        if (cur == 1 && game.blackCheatModeOn) return true;
        if (cur == -1 && game.whiteCheatModeOn) return true;
        for (int D = 0; D < 8; D ++) {
            if (checkDir(i, j, cur, contents, D)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某人能否继续落子
     */
    public boolean checkPlayer(int cur) {
        boolean flag = false;
        int t = 0;
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                if (check(i, j, cur)) {
                    flag = true;
                }
                if (contents[i][j] == 0) {
                    t ++;
                }
            }
        }
        if (t == 0) flag = false;
        return flag;
    }

    /**
     * 义无反顾地落子
     */
    public void put(int i, int j, int cur) {
        for (int D = 0; D < 8; D ++) {
            if (checkDir(i, j, cur, contents, D)) {
                reverse(i, j, cur, contents, D);
            }
        }
        contents[i][j] = cur;
        stepList.add(new Step(i, j, cur));
        curChess = -curChess;
    }

    /**
     * 检视棋盘状况
     */
    InspectResult inspect() {
        int rst = count();
        if (rst / 100 == 0 || rst % 100 == 0) {
            return InspectResult.ENDGAME;
        }
        if (checkPlayer(curChess)) {
            return InspectResult.NOTHING;
        } else {
            if (checkPlayer(-curChess)) {
                return InspectResult.SWITCH;
            } else {
                return InspectResult.ENDGAME;
            }
        }
    }

    /**
     * 数子
     */
    public int count() {
        int b = 0, w = 0;
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                if (contents[i][j] == 1) {
                    b ++;
                } else if (contents[i][j] == -1) {
                    w ++;
                }
            }
        }
        return b * 100 + w;
    }

    public int countChess() {
        int cnt = 0;
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                cnt += contents[i][j];
            }
        }
        return Integer.compare(cnt, 0);
    }

    /**
     * 回退历史
     */
    public boolean redo(int i) {
        if (i < 0 || i >= stepList.size()) return false;

        ArrayList<Step> tmpList = new ArrayList<>(stepList);
        stepList.clear();
        init();

        for (int k = 0; k < i; k ++) {
            Step s = tmpList.get(k);
            put(s.getI(), s.getJ(), s.getColor());
        }

        curChess = tmpList.get(i).getColor();

        return true;
    }

    public boolean redoOnce() {
        return redo(stepList.size() - 1);
    }
}
