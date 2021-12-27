package tech.gralerfics.gamelogic;

import tech.gralerfics.controller.Main;
import tech.gralerfics.raytracer.scenes.SceneManager;

import java.util.ArrayList;

public class ReplayThread extends Thread {
    public int del;

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    @Override
    public void run() {
        SceneManager.currentGame.gameReplaying = true;
        SceneManager.currentGame.replayGoAhead = true;
        try {
            int ctmp = SceneManager.currentGame.judger.curChess;
            ArrayList<Step> tmpList = new ArrayList<>(SceneManager.currentGame.judger.stepList);
            SceneManager.currentGame.judger.init();
            SceneManager.currentGame.judger.stepList.clear();
            for (int i = 0; i < tmpList.size(); i ++) {
                Step s = tmpList.get(i);
                SceneManager.currentGame.judger.curChess = s.getColor();
                if (SceneManager.currentGame.replayGoAhead) sleep(del);
                SceneManager.currentGame.judger.put(s.getI(), s.getJ(), s.getColor());
                Main.glfwCtrl.getHandles().setFrameCnt(0);
            }
            SceneManager.currentGame.judger.curChess = ctmp;
        } catch (InterruptedException e) {
            // sleep ¹ÊÕÏ
        }
        SceneManager.currentGame.gameReplaying = false;
    }
}
