package tech.gralerfics.gamelogic;

import tech.gralerfics.ai.AIThread;
import tech.gralerfics.controller.Main;
import tech.gralerfics.persistence.profiles.PlayerManager;

import java.awt.*;
import java.util.Objects;

import static tech.gralerfics.raytracer.scenes.SceneManager.currentGame;

public class Game {
    public String name = "";
    public Player white, black;
    public Judger judger;
    public boolean whiteCheatModeOn = false, blackCheatModeOn = false;
    public boolean cheatModeOned = false;
    public boolean gameEnded = false, gameReplaying = false, redoing = false;
    public boolean replayGoAhead = false;

    AIThread aiThr = new AIThread();

    public Game() {
        judger = new Judger(this);
        judger.init();
        aiThr.start();
    }

    public void loop() {
        // 设置状态板组件内容
        boolean flag = true;
        if (currentGame != null) {
            if (!currentGame.gameReplaying && !currentGame.gameEnded) {
                if (blackCheatModeOn) {
                    cheatModeOned = true;
                    Main.statusFrame.getCheatBtn1().setBackground(new Color(255, 102, 102));
                } else {
                    Main.statusFrame.getCheatBtn1().setBackground(new Color(0, 204, 102));
                }
            }
            if (!currentGame.gameReplaying && !currentGame.gameEnded) {
                if (whiteCheatModeOn) {
                    cheatModeOned = true;
                    Main.statusFrame.getCheatBtn2().setBackground(new Color(255, 102, 102));
                } else {
                    Main.statusFrame.getCheatBtn2().setBackground(new Color(0, 204, 102));
                }
            }
        }
        if (currentGame != null && currentGame.black != null) {
            Main.statusFrame.getInfLab1().setText(black.name);
        } else {
            flag = false;
            if (!currentGame.gameReplaying && !currentGame.gameEnded) {
                Main.statusFrame.getPly1().setBackground(new Color(205, 205, 205));
//                Main.statusFrame.getCheatBtn1().setBackground(new Color(0, 204, 102));
            }
            Main.statusFrame.getInfLab1().setText("Waiting for the black player...");
        }
        if (currentGame != null && currentGame.white != null) {
            Main.statusFrame.getInfLab2().setText(white.name);
        } else {
            flag = false;
            if (!currentGame.gameReplaying && !currentGame.gameEnded) {
                Main.statusFrame.getPly2().setBackground(new Color(205, 205, 205));
//                Main.statusFrame.getCheatBtn2().setBackground(new Color(0, 204, 102));
            }
            Main.statusFrame.getInfLab2().setText("Waiting for the white player...");
        }

        // 如果游戏正在进行
        if (flag) {
            Player curPlayer = (judger.curChess == 1) ? black : white;

            // 局势检查
            if (!gameReplaying) {
                InspectResult ir = judger.inspect();
                if (ir.equals(InspectResult.SWITCH)) {
                    judger.curChess = -judger.curChess;
                    Main.statusFrame.getStatusLab().setText("It's still your turn.");
                } else if (ir.equals(InspectResult.ENDGAME)) {
                    int wnr = judger.countChess();
                    Main.statusFrame.getStatusLab().setText(((wnr == 0) ? "Nobody" : ((wnr == 1) ? black.name : white.name)) + " wins!");
                    if (!Objects.equals(black.name, white.name) && !gameReplaying && !gameEnded && !cheatModeOned) {
                        if (wnr == 0) {
                            black.fines += 1;
                            white.fines += 1;
                        } else if (wnr == 1) {
                            black.wins += 1;
                            white.fails += 1;
                        } else {
                            black.fails += 1;
                            white.wins += 1;
                        }
                        PlayerManager.savePlayer(black);
                        PlayerManager.savePlayer(white);
                    }
                    gameEnded = true;
                    return;
                } else if (!Objects.equals(Main.statusFrame.getStatusLab().getText(), "It's still your turn.")) {
                    Main.statusFrame.getStatusLab().setText("Continuing...");
                }
            } else {
                Main.statusFrame.getStatusLab().setText("Replaying...");
            }

            // AI
            if (!redoing && !gameReplaying && curPlayer.tag / 10 == 1) {
                if (Main.statusFrame.getStatusLab().getText().contains("It's still")) {
                    Main.statusFrame.getStatusLab().setText("It is still " + curPlayer.name + "'s turn...");
                } else {
                    Main.statusFrame.getStatusLab().setText(curPlayer.name + " is thinking...");
                }
                if (aiThr.result == -2) {
                    boolean cheating = (judger.curChess == 1) ? blackCheatModeOn : whiteCheatModeOn;
                    int level = curPlayer.tag % 10;
                    if (judger.curChess == 1 && blackCheatModeOn || judger.curChess == -1 && whiteCheatModeOn) {
                        level = Math.min(level, 4);
                    }
                    aiThr.go(judger.contents, judger.curChess, level, cheating);
                } else if (aiThr.result >= 0) {
                    curPlayer.nextStep = aiThr.result;
                    aiThr.result = -2;
                }
            }

            /* NetPlayer */

            // 检测待落子
            if (!gameReplaying && curPlayer.nextStep != -1) {
                int i = curPlayer.nextStep / 10, j = curPlayer.nextStep % 10;

                if (judger.check(i, j, judger.curChess)) {
                    judger.put(i, j, judger.curChess);
                }

                Main.glfwCtrl.getHandles().setFrameCnt(0);
                curPlayer.nextStep = -1;
            }

            // 执子状态
            if (judger.curChess == 1) {
                Main.statusFrame.getPly1().setBackground(new Color(159, 159, 159));
                Main.statusFrame.getPly2().setBackground(new Color(205, 205, 205));
            } else if (judger.curChess == -1) {
                Main.statusFrame.getPly1().setBackground(new Color(205, 205, 205));
                Main.statusFrame.getPly2().setBackground(new Color(159, 159, 159));
            } else {
                Main.statusFrame.getPly1().setBackground(new Color(205, 205, 205));
                Main.statusFrame.getPly2().setBackground(new Color(205, 205, 205));
            }
        }
    }
}
