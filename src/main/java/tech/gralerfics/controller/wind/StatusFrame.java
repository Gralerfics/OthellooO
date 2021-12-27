package tech.gralerfics.controller.wind;

import java.awt.event.*;
import tech.gralerfics.controller.Main;
import tech.gralerfics.gamelogic.Game;
import tech.gralerfics.gamelogic.Player;
import tech.gralerfics.gamelogic.ReplayThread;
import tech.gralerfics.gamelogic.Step;
import tech.gralerfics.persistence.options.GUIConstants;
import tech.gralerfics.persistence.options.OptionsManager;
import tech.gralerfics.persistence.profiles.ProfileManager;
import tech.gralerfics.raytracer.scenes.SceneManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class StatusFrame extends JFrame {
    private static final float MAX_DEPTH = 5;
    private boolean moreSettingsShown = false;

    int delay = 10;
    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            loopTask();
        }
    };

    public void loopTask() {
        if (this.isVisible()) {
            // 数子
            if (SceneManager.currentGame != null) {
                int rst = SceneManager.currentGame.judger.count();
                int bc = rst / 100, wc = rst % 100;
                ptr1.setText("" + bc);
                ptr2.setText("" + wc);
            }

            // 摄像机模式按钮
            if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "1")) {
                draggingBtn.setBackground(new Color(51, 153, 255));
                wanderingBtn.setBackground(new Color(153, 204, 255));
                RoundingBtn.setBackground(new Color(153, 204, 255));
                OverlookingBtn.setBackground(new Color(153, 204, 255));
            } else if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "2")) {
                draggingBtn.setBackground(new Color(153, 204, 255));
                wanderingBtn.setBackground(new Color(51, 153, 255));
                RoundingBtn.setBackground(new Color(153, 204, 255));
                OverlookingBtn.setBackground(new Color(153, 204, 255));
            } else if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "3")) {
                draggingBtn.setBackground(new Color(153, 204, 255));
                wanderingBtn.setBackground(new Color(153, 204, 255));
                RoundingBtn.setBackground(new Color(51, 153, 255));
                OverlookingBtn.setBackground(new Color(153, 204, 255));
            } else if (Objects.equals(OptionsManager.options.getProperty("cameraViewingMode"), "4")) {
                draggingBtn.setBackground(new Color(153, 204, 255));
                wanderingBtn.setBackground(new Color(153, 204, 255));
                RoundingBtn.setBackground(new Color(153, 204, 255));
                OverlookingBtn.setBackground(new Color(51, 153, 255));
            }

            // 视频设置按钮
            if (OptionsManager.options.getProperty("simplifyChess").equals("off")) {
                simpleChessBtn.setBackground(new Color(153, 204, 255));
            } else {
                simpleChessBtn.setBackground(new Color(51, 153, 255));
            }
            if (OptionsManager.options.getProperty("simplifyBoard").equals("off")) {
                simpleBoardBtn.setBackground(new Color(153, 204, 255));
            } else {
                simpleBoardBtn.setBackground(new Color(51, 153, 255));
            }
            if (OptionsManager.options.getProperty("chessBoardSpecular").equals("off")) {
                specularBoardBtn.setBackground(new Color(153, 204, 255));
            } else {
                specularBoardBtn.setBackground(new Color(51, 153, 255));
            }
            if (OptionsManager.options.getProperty("hdrFilename").equals("")) {
                hdrBtn.setBackground(new Color(153, 204, 255));
            } else {
                hdrBtn.setBackground(new Color(51, 153, 255));
            }
            if (OptionsManager.options.getProperty("lightOfBoard").equals("off")) {
                boardLightBtn.setBackground(new Color(153, 204, 255));
            } else {
                boardLightBtn.setBackground(new Color(51, 153, 255));
            }
            if (OptionsManager.options.getProperty("lightOfChess").equals("off")) {
                chessLightBtn.setBackground(new Color(153, 204, 255));
            } else {
                chessLightBtn.setBackground(new Color(51, 153, 255));
            }
            float Il = Float.parseFloat(OptionsManager.options.getProperty("hdrLightFactor")) / 1.2f;
            surLightBtn.setBackground(new Color(50 + (int) (102 * Il), 152 + (int) (51 * Il), 255));
            float Ra = Float.parseFloat(OptionsManager.options.getProperty("rayTracingDepth")) / MAX_DEPTH;
            depthBtn.setBackground(new Color(50 + (int) (102 * Ra), 152 + (int) (51 * Ra), 255));

            // 游戏设置按钮
            if (OptionsManager.options.getProperty("showLegalPlace").equals("off")) {
                showPlaceBtn.setBackground(new Color(153, 204, 255));
            } else {
                showPlaceBtn.setBackground(new Color(51, 153, 255));
            }
            if (OptionsManager.options.getProperty("showLastPlace").equals("off")) {
                showLastPlaceBtn.setBackground(new Color(153, 204, 255));
            } else {
                showLastPlaceBtn.setBackground(new Color(51, 153, 255));
            }

            // 回放进行时
            if (SceneManager.currentGame.gameReplaying) {
                restartBtn.setBackground(new Color(153, 153, 153));
                redoBtn.setBackground(new Color(153, 153, 153));
                replayBtn.setBackground(new Color(51, 153, 255));
                loadBtn.setBackground(new Color(153, 153, 153));
                saveBtn.setBackground(new Color(153, 153, 153));
                modBtn1.setBackground(new Color(153, 153, 153));
                modBtn2.setBackground(new Color(153, 153, 153));
                cheatBtn1.setBackground(new Color(153, 153, 153));
                cheatBtn2.setBackground(new Color(153, 153, 153));
            } else {
                restartBtn.setBackground(new Color(255, 204, 102));
                redoBtn.setBackground(new Color(255, 102, 102));
                replayBtn.setBackground(new Color(51, 204, 255));
                loadBtn.setBackground(new Color(204, 153, 255));
                saveBtn.setBackground(new Color(204, 153, 255));
                modBtn1.setBackground(new Color(255, 204, 102));
                modBtn2.setBackground(new Color(255, 204, 102));
            }

            // 未开始或胜负已分
            if (SceneManager.currentGame != null && SceneManager.currentGame.gameEnded) {
                redoBtn.setBackground(new Color(153, 153, 153));
                modBtn1.setBackground(new Color(153, 153, 153));
                modBtn2.setBackground(new Color(153, 153, 153));
                cheatBtn1.setBackground(new Color(153, 153, 153));
                cheatBtn2.setBackground(new Color(153, 153, 153));
            } else if (SceneManager.currentGame == null || SceneManager.currentGame.black == null || SceneManager.currentGame.white == null) {
                saveBtn.setBackground(new Color(153, 153, 153));
                redoBtn.setBackground(new Color(153, 153, 153));
                replayBtn.setBackground(new Color(153, 153, 153));
            }
        }
    }

    public void correctSize() {
        Insets insets = this.getInsets();
        int vr = (moreSettingsShown) ? GUIConstants.STATUS_FULL_VRES : GUIConstants.STATUS_DEFAULT_VRES;
        this.setSize(new Dimension(GUIConstants.STATUS_DEFAULT_HRES + insets.left + insets.right, vr + insets.top + insets.bottom));
    }

    public StatusFrame() {
        initComponents();
        correctSize();
        this.setVisible(false);
        new Timer(delay, taskPerformer).start();
    }

    private void modBtn1MouseReleased(MouseEvent e) {
        if (SceneManager.currentGame != null && !SceneManager.currentGame.gameEnded && !SceneManager.currentGame.gameReplaying) {
            SceneManager.currentGame.black = (new PlayerDialog(this)).show("Choose Black Player (Then Close the Dialog)");
        }
    }

    private void modBtn2MouseReleased(MouseEvent e) {
        if (SceneManager.currentGame != null && !SceneManager.currentGame.gameEnded && !SceneManager.currentGame.gameReplaying) {
            SceneManager.currentGame.white = (new PlayerDialog(this)).show("Choose White Player (Then Close the Dialog)");
        }
    }

    private void cheatBtn1MouseReleased(MouseEvent e) {
        if (SceneManager.currentGame != null && !SceneManager.currentGame.gameEnded && !SceneManager.currentGame.gameReplaying) {
            SceneManager.currentGame.blackCheatModeOn = ! SceneManager.currentGame.blackCheatModeOn;
        }
    }

    private void cheatBtn2MouseReleased(MouseEvent e) {
        if (SceneManager.currentGame != null && !SceneManager.currentGame.gameEnded && !SceneManager.currentGame.gameReplaying) {
            SceneManager.currentGame.whiteCheatModeOn = ! SceneManager.currentGame.whiteCheatModeOn;
        }
    }

    private void redoBtnMouseReleased(MouseEvent e) {
        if (SceneManager.currentGame != null && !SceneManager.currentGame.gameReplaying && SceneManager.currentGame.black != null && SceneManager.currentGame.white != null && !SceneManager.currentGame.gameEnded) {
            if (SceneManager.currentGame.black.tag / 10 == 1 && SceneManager.currentGame.white.tag / 10 == 1) {
                (new TipDialog(this)).show("Don't interrupt AIs.");
            } else {
                SceneManager.currentGame.redoing = true;
                while (true) {
                    if (SceneManager.currentGame.judger.stepList.isEmpty()) break;
                    Step s = SceneManager.currentGame.judger.stepList.get(SceneManager.currentGame.judger.stepList.size() - 1);
                    Player p = (s.getColor() == 1) ? SceneManager.currentGame.black : SceneManager.currentGame.white;
                    if (p.tag / 10 == 1) {
                        SceneManager.currentGame.judger.redoOnce();
                    } else {
                        SceneManager.currentGame.judger.redoOnce();
                        break;
                    }
                }
                SceneManager.currentGame.redoing = false;
                Main.glfwCtrl.getHandles().setFrameCnt(0);
            }
        }
    }

    private void restartBtnMouseReleased(MouseEvent e) {
        if (SceneManager.currentGame != null && !SceneManager.currentGame.gameReplaying) {
            SceneManager.currentGame = new Game();
            Main.statusFrame.getStatusLab().setText("NEW GAME ~");
            Main.glfwCtrl.getHandles().setFrameCnt(0);
        }
    }

    private void draggingBtnMouseReleased(MouseEvent e) {
        OptionsManager.options.setProperty("cameraViewingMode", "1");
        OptionsManager.saveOption();
        Main.glfwCtrl.getHandles().setFrameCnt(0);
    }

    private void wanderingBtnMouseReleased(MouseEvent e) {
        OptionsManager.options.setProperty("cameraViewingMode", "2");
        OptionsManager.saveOption();
        Main.glfwCtrl.getHandles().setFrameCnt(0);
    }

    private void RoundingBtnMouseReleased(MouseEvent e) {
        OptionsManager.options.setProperty("cameraViewingMode", "3");
        OptionsManager.saveOption();
        Main.glfwCtrl.getHandles().setFrameCnt(0);
    }

    private void OverlookingBtnMouseReleased(MouseEvent e) {
        OptionsManager.options.setProperty("cameraViewingMode", "4");
        OptionsManager.saveOption();
        Main.glfwCtrl.getHandles().setFrameCnt(0);
    }

    private void backBtnMouseReleased(MouseEvent e) {
        Main.glfwCtrl.getGlfwApp().getWindow().setVisible(false);
        Main.mainFrame.setVisible(true);
        this.setVisible(false);
    }

    private void simpleChessBtnMouseReleased(MouseEvent e) {
        if (OptionsManager.options.getProperty("simplifyChess").equals("off")) {
            OptionsManager.options.setProperty("simplifyChess", "on");
        } else {
            OptionsManager.options.setProperty("simplifyChess", "off");
        }
        OptionsManager.saveOption();
    }

    private void boardLightBtnMouseReleased(MouseEvent e) {
        if (OptionsManager.options.getProperty("lightOfBoard").equals("off")) {
            OptionsManager.options.setProperty("lightOfBoard", "on");
        } else {
            OptionsManager.options.setProperty("lightOfBoard", "off");
        }
        OptionsManager.saveOption();
    }

    private void chessLightBtnMouseReleased(MouseEvent e) {
        if (OptionsManager.options.getProperty("lightOfChess").equals("off")) {
            OptionsManager.options.setProperty("lightOfChess", "on");
        } else {
            OptionsManager.options.setProperty("lightOfChess", "off");
        }
        OptionsManager.saveOption();
    }

    private void showPlaceBtnMouseReleased(MouseEvent e) {
        if (OptionsManager.options.getProperty("showLegalPlace").equals("off")) {
            OptionsManager.options.setProperty("showLegalPlace", "on");
        } else {
            OptionsManager.options.setProperty("showLegalPlace", "off");
        }
        OptionsManager.saveOption();
        Main.glfwCtrl.getHandles().setFrameCnt(0);
    }

    private void showLastPlaceBtnMouseReleased(MouseEvent e) {
        if (OptionsManager.options.getProperty("showLastPlace").equals("off")) {
            OptionsManager.options.setProperty("showLastPlace", "on");
        } else {
            OptionsManager.options.setProperty("showLastPlace", "off");
        }
        OptionsManager.saveOption();
        Main.glfwCtrl.getHandles().setFrameCnt(0);
    }

    private void moreSettingsBtnMouseReleased(MouseEvent e) {
        moreSettingsShown = !moreSettingsShown;
        correctSize();
    }

    private void surLightBtnMouseReleased(MouseEvent e) {
        int Il = (int) (Math.round(10 * Float.parseFloat(OptionsManager.options.getProperty("hdrLightFactor")))) + 1;
        if (Il > 12) Il = 0;
        OptionsManager.options.setProperty("hdrLightFactor", Float.toString(0.1f * Il));
        OptionsManager.saveOption();
        Main.glfwCtrl.getHandles().setFrameCnt(0);
    }

    private void simpleBoardBtnMouseReleased(MouseEvent e) {
        if (OptionsManager.options.getProperty("simplifyBoard").equals("off")) {
            OptionsManager.options.setProperty("simplifyBoard", "on");
        } else {
            OptionsManager.options.setProperty("simplifyBoard", "off");
        }
        OptionsManager.saveOption();
    }

    private void specularBoardBtnMouseReleased(MouseEvent e) {
        if (OptionsManager.options.getProperty("chessBoardSpecular").equals("off")) {
            OptionsManager.options.setProperty("chessBoardSpecular", "on");
        } else {
            OptionsManager.options.setProperty("chessBoardSpecular", "off");
        }
        OptionsManager.saveOption();
    }

    private void hdrBtnMouseReleased(MouseEvent e) {
        // TODO
        String rst = (new InputDialog(this)).show("Type the name of .hdr file (Without '.hdr')");
        OptionsManager.options.setProperty("hdrFilename", rst);
        if (!rst.isEmpty()) {
            (new TipDialog(this)).show("Take effect after restarting.");
        }
        OptionsManager.saveOption();
    }

    private void depthBtnMouseReleased(MouseEvent e) {
        int Il = Integer.parseInt(OptionsManager.options.getProperty("rayTracingDepth")) + 1;
        if (Il > MAX_DEPTH) Il = 0;
        OptionsManager.options.setProperty("rayTracingDepth", Integer.toString(Il));
        OptionsManager.saveOption();
        Main.glfwCtrl.getHandles().setFrameCnt(0);
    }

    private void loadBtnMouseReleased(MouseEvent e) {
        if (SceneManager.currentGame != null && !SceneManager.currentGame.gameReplaying) {
            Game g = (new ProfileDialog(this)).show("Choose One Profile (Then Close the Dialog)");
            if (g == null) {
                (new TipDialog(this)).show("Did not choose or picked an invalid profile!");
            } else {
                SceneManager.currentGame = g;
                Main.glfwCtrl.getHandles().setFrameCnt(0);
            }
        }
    }

    private void saveBtnMouseReleased(MouseEvent e) {
        if (SceneManager.currentGame != null && !SceneManager.currentGame.gameReplaying && SceneManager.currentGame.black != null && SceneManager.currentGame.white != null) {
            boolean is = true;
            if (!SceneManager.currentGame.name.isEmpty()) {
                is = (new ConfirmDialog(this)).show("Save as a new profile?");
            }
            if (is) {
                String rst = (new InputDialog(this)).show("Type the name of your profile");
                if (rst.isEmpty()) {
                    return;
                }
                if (ProfileManager.isExist(rst)) {
                    (new TipDialog(this)).show("Invalid or existed profile name!");
                } else {
                    if (SceneManager.currentGame != null) {
                        ProfileManager.saveAs(SceneManager.currentGame, rst);
                        SceneManager.currentGame.name = rst;
                    } else {
                        (new TipDialog(this)).show("There is no game.");
                    }
                }
            } else {
                ProfileManager.saveAs(SceneManager.currentGame, SceneManager.currentGame.name);
            }
        }
    }

    private void replayBtnMouseReleased(MouseEvent e) {
        if (SceneManager.currentGame != null && SceneManager.currentGame.white != null && SceneManager.currentGame.black != null && !SceneManager.currentGame.gameReplaying) {
            int del = 75;
            String rst = (new InputDialog(this)).show("Type the time interval (0 - 5000 ms)");
            if (rst.isEmpty()) return;
            try {
                int tmp = Integer.parseInt(rst);
                if (tmp >= 0 && tmp <= 5000) {
                    del = tmp;
                    ReplayThread rthr = new ReplayThread();
                    rthr.setDel(del);
                    rthr.start();
                } else {
                    throw new Exception();
                }
            } catch (Exception ex) {
                (new TipDialog(this)).show("Invalid Number.");
            }
        }
        if (SceneManager.currentGame != null && SceneManager.currentGame.gameReplaying) {
            SceneManager.currentGame.replayGoAhead = false;
        }
    }

    private void thisWindowClosing(WindowEvent e) {
        backBtnMouseReleased(null);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        statusLab = new JLabel();
        ply1 = new JPanel();
        infLab1 = new JLabel();
        modBtn1 = new JLabel();
        ptr1 = new JLabel();
        cheatBtn1 = new JLabel();
        ply2 = new JPanel();
        infLab2 = new JLabel();
        modBtn2 = new JLabel();
        ptr2 = new JLabel();
        cheatBtn2 = new JLabel();
        restartBtn = new JLabel();
        redoBtn = new JLabel();
        backBtn = new JLabel();
        draggingBtn = new JLabel();
        wanderingBtn = new JLabel();
        RoundingBtn = new JLabel();
        OverlookingBtn = new JLabel();
        cameraSettingPane = new JLabel();
        simpleBoardBtn = new JLabel();
        specularBoardBtn = new JLabel();
        simpleChessBtn = new JLabel();
        hdrBtn = new JLabel();
        boardLightBtn = new JLabel();
        chessLightBtn = new JLabel();
        depthBtn = new JLabel();
        showPlaceBtn = new JLabel();
        showLastPlaceBtn = new JLabel();
        gameSettingPane = new JLabel();
        moreSettingsBtn = new JLabel();
        playerSeatPane = new JLabel();
        surLightBtn = new JLabel();
        viewingSettingPane = new JLabel();
        loadBtn = new JLabel();
        saveBtn = new JLabel();
        profilePane = new JLabel();
        replayBtn = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setTitle("Status Inspector");
        setIconImage(new ImageIcon("assets/guiimages/icon.jpg").getImage());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- statusLab ----
        statusLab.setFont(new Font("Roboto Light", Font.BOLD, 18));
        statusLab.setHorizontalAlignment(SwingConstants.CENTER);
        statusLab.setBackground(new Color(102, 102, 102));
        statusLab.setOpaque(true);
        statusLab.setForeground(Color.white);
        statusLab.setText("Messages.");
        contentPane.add(statusLab);
        statusLab.setBounds(5, 5, 385, 115);

        //======== ply1 ========
        {
            ply1.setBackground(new Color(205, 205, 205));
            ply1.setFont(new Font("Roboto Light", Font.BOLD, 16));
            ply1.setForeground(Color.white);
            ply1.setLayout(null);

            //---- infLab1 ----
            infLab1.setText("TestBlack");
            infLab1.setFont(new Font("Roboto Light", Font.BOLD, 11));
            infLab1.setForeground(Color.darkGray);
            ply1.add(infLab1);
            infLab1.setBounds(10, 20, 140, 25);

            //---- modBtn1 ----
            modBtn1.setBackground(new Color(255, 204, 102));
            modBtn1.setOpaque(true);
            modBtn1.setFont(new Font("Roboto Light", Font.BOLD, 13));
            modBtn1.setText("CHANGE");
            modBtn1.setHorizontalAlignment(SwingConstants.CENTER);
            modBtn1.setForeground(Color.white);
            modBtn1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    modBtn1MouseReleased(e);
                }
            });
            ply1.add(modBtn1);
            modBtn1.setBounds(10, 45, 70, 50);

            //---- ptr1 ----
            ptr1.setBackground(Color.black);
            ptr1.setOpaque(true);
            ptr1.setFont(new Font("Roboto Light", Font.ITALIC, 12));
            ptr1.setForeground(Color.white);
            ptr1.setText("2");
            ptr1.setHorizontalTextPosition(SwingConstants.CENTER);
            ptr1.setHorizontalAlignment(SwingConstants.CENTER);
            ply1.add(ptr1);
            ptr1.setBounds(5, 5, 150, 15);

            //---- cheatBtn1 ----
            cheatBtn1.setBackground(new Color(0, 204, 102));
            cheatBtn1.setOpaque(true);
            cheatBtn1.setText("CHEAT");
            cheatBtn1.setHorizontalAlignment(SwingConstants.CENTER);
            cheatBtn1.setFont(new Font("Roboto Light", Font.BOLD, 13));
            cheatBtn1.setForeground(Color.white);
            cheatBtn1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    cheatBtn1MouseReleased(e);
                }
            });
            ply1.add(cheatBtn1);
            cheatBtn1.setBounds(80, 45, 70, 50);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < ply1.getComponentCount(); i++) {
                    Rectangle bounds = ply1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = ply1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                ply1.setMinimumSize(preferredSize);
                ply1.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(ply1);
        ply1.setBounds(400, 25, 160, 105);

        //======== ply2 ========
        {
            ply2.setBackground(new Color(159, 159, 159));
            ply2.setFont(new Font("Roboto Light", Font.BOLD, 16));
            ply2.setForeground(Color.white);
            ply2.setLayout(null);

            //---- infLab2 ----
            infLab2.setText("TestWhite");
            infLab2.setFont(new Font("Roboto Light", Font.BOLD, 11));
            infLab2.setForeground(Color.darkGray);
            ply2.add(infLab2);
            infLab2.setBounds(10, 20, 140, 25);

            //---- modBtn2 ----
            modBtn2.setBackground(new Color(255, 204, 102));
            modBtn2.setOpaque(true);
            modBtn2.setHorizontalAlignment(SwingConstants.CENTER);
            modBtn2.setFont(new Font("Roboto Light", Font.BOLD, 13));
            modBtn2.setText("CHANGE");
            modBtn2.setForeground(Color.white);
            modBtn2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    modBtn2MouseReleased(e);
                }
            });
            ply2.add(modBtn2);
            modBtn2.setBounds(10, 45, 70, 50);

            //---- ptr2 ----
            ptr2.setBackground(Color.white);
            ptr2.setOpaque(true);
            ptr2.setFont(new Font("Roboto Light", Font.ITALIC, 12));
            ptr2.setText("2");
            ptr2.setHorizontalAlignment(SwingConstants.CENTER);
            ply2.add(ptr2);
            ptr2.setBounds(5, 5, 150, 15);

            //---- cheatBtn2 ----
            cheatBtn2.setBackground(new Color(255, 102, 102));
            cheatBtn2.setOpaque(true);
            cheatBtn2.setFont(new Font("Roboto Light", Font.BOLD, 13));
            cheatBtn2.setText("CHEAT");
            cheatBtn2.setHorizontalAlignment(SwingConstants.CENTER);
            cheatBtn2.setForeground(Color.white);
            cheatBtn2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    cheatBtn2MouseReleased(e);
                }
            });
            ply2.add(cheatBtn2);
            cheatBtn2.setBounds(80, 45, 70, 50);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < ply2.getComponentCount(); i++) {
                    Rectangle bounds = ply2.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = ply2.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                ply2.setMinimumSize(preferredSize);
                ply2.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(ply2);
        ply2.setBounds(400, 135, 160, 105);

        //---- restartBtn ----
        restartBtn.setBackground(new Color(255, 204, 102));
        restartBtn.setOpaque(true);
        restartBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        restartBtn.setText("Start a new game");
        restartBtn.setHorizontalAlignment(SwingConstants.CENTER);
        restartBtn.setForeground(Color.white);
        restartBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                restartBtnMouseReleased(e);
            }
        });
        contentPane.add(restartBtn);
        restartBtn.setBounds(5, 125, 125, 50);

        //---- redoBtn ----
        redoBtn.setBackground(new Color(255, 102, 102));
        redoBtn.setOpaque(true);
        redoBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        redoBtn.setText("Redo");
        redoBtn.setHorizontalAlignment(SwingConstants.CENTER);
        redoBtn.setForeground(Color.white);
        redoBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                redoBtnMouseReleased(e);
            }
        });
        contentPane.add(redoBtn);
        redoBtn.setBounds(135, 125, 125, 25);

        //---- backBtn ----
        backBtn.setBackground(new Color(204, 204, 204));
        backBtn.setOpaque(true);
        backBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        backBtn.setText("Back to menu");
        backBtn.setHorizontalAlignment(SwingConstants.CENTER);
        backBtn.setForeground(Color.white);
        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                backBtnMouseReleased(e);
            }
        });
        contentPane.add(backBtn);
        backBtn.setBounds(265, 125, 125, 50);

        //---- draggingBtn ----
        draggingBtn.setBackground(new Color(51, 153, 255));
        draggingBtn.setOpaque(true);
        draggingBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        draggingBtn.setText("Dragging Mode");
        draggingBtn.setHorizontalAlignment(SwingConstants.CENTER);
        draggingBtn.setForeground(Color.white);
        draggingBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                draggingBtnMouseReleased(e);
            }
        });
        contentPane.add(draggingBtn);
        draggingBtn.setBounds(15, 365, 130, 40);

        //---- wanderingBtn ----
        wanderingBtn.setBackground(new Color(153, 204, 255));
        wanderingBtn.setOpaque(true);
        wanderingBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        wanderingBtn.setText("Wandering Mode");
        wanderingBtn.setHorizontalAlignment(SwingConstants.CENTER);
        wanderingBtn.setForeground(Color.white);
        wanderingBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                wanderingBtnMouseReleased(e);
            }
        });
        contentPane.add(wanderingBtn);
        wanderingBtn.setBounds(150, 365, 130, 40);

        //---- RoundingBtn ----
        RoundingBtn.setBackground(new Color(153, 204, 255));
        RoundingBtn.setOpaque(true);
        RoundingBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        RoundingBtn.setText("Rounding Mode");
        RoundingBtn.setHorizontalAlignment(SwingConstants.CENTER);
        RoundingBtn.setForeground(Color.white);
        RoundingBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                RoundingBtnMouseReleased(e);
            }
        });
        contentPane.add(RoundingBtn);
        RoundingBtn.setBounds(285, 365, 130, 40);

        //---- OverlookingBtn ----
        OverlookingBtn.setBackground(new Color(153, 204, 255));
        OverlookingBtn.setOpaque(true);
        OverlookingBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        OverlookingBtn.setText("Overlooking Mode");
        OverlookingBtn.setHorizontalAlignment(SwingConstants.CENTER);
        OverlookingBtn.setForeground(Color.white);
        OverlookingBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                OverlookingBtnMouseReleased(e);
            }
        });
        contentPane.add(OverlookingBtn);
        OverlookingBtn.setBounds(420, 365, 130, 40);

        //---- cameraSettingPane ----
        cameraSettingPane.setBackground(new Color(224, 224, 224));
        cameraSettingPane.setOpaque(true);
        cameraSettingPane.setFont(new Font("Roboto Light", Font.BOLD, 13));
        cameraSettingPane.setHorizontalAlignment(SwingConstants.CENTER);
        cameraSettingPane.setText("Camera Settings");
        cameraSettingPane.setVerticalAlignment(SwingConstants.TOP);
        cameraSettingPane.setForeground(Color.darkGray);
        contentPane.add(cameraSettingPane);
        cameraSettingPane.setBounds(10, 345, 545, 65);

        //---- simpleBoardBtn ----
        simpleBoardBtn.setBackground(new Color(153, 204, 255));
        simpleBoardBtn.setOpaque(true);
        simpleBoardBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        simpleBoardBtn.setText("Simplify Board");
        simpleBoardBtn.setHorizontalAlignment(SwingConstants.CENTER);
        simpleBoardBtn.setForeground(Color.white);
        simpleBoardBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                simpleBoardBtnMouseReleased(e);
            }
        });
        contentPane.add(simpleBoardBtn);
        simpleBoardBtn.setBounds(15, 480, 130, 40);

        //---- specularBoardBtn ----
        specularBoardBtn.setBackground(new Color(153, 204, 255));
        specularBoardBtn.setOpaque(true);
        specularBoardBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        specularBoardBtn.setText("Specular Board ");
        specularBoardBtn.setHorizontalAlignment(SwingConstants.CENTER);
        specularBoardBtn.setForeground(Color.white);
        specularBoardBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                specularBoardBtnMouseReleased(e);
            }
        });
        contentPane.add(specularBoardBtn);
        specularBoardBtn.setBounds(285, 480, 130, 40);

        //---- simpleChessBtn ----
        simpleChessBtn.setBackground(new Color(153, 204, 255));
        simpleChessBtn.setOpaque(true);
        simpleChessBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        simpleChessBtn.setText("Simplify Chess");
        simpleChessBtn.setHorizontalAlignment(SwingConstants.CENTER);
        simpleChessBtn.setForeground(Color.white);
        simpleChessBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                simpleChessBtnMouseReleased(e);
            }
        });
        contentPane.add(simpleChessBtn);
        simpleChessBtn.setBounds(15, 435, 130, 40);

        //---- hdrBtn ----
        hdrBtn.setBackground(new Color(153, 204, 255));
        hdrBtn.setOpaque(true);
        hdrBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        hdrBtn.setText("Hdr Background");
        hdrBtn.setHorizontalAlignment(SwingConstants.CENTER);
        hdrBtn.setForeground(Color.white);
        hdrBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                hdrBtnMouseReleased(e);
            }
        });
        contentPane.add(hdrBtn);
        hdrBtn.setBounds(285, 435, 130, 40);

        //---- boardLightBtn ----
        boardLightBtn.setBackground(new Color(153, 204, 255));
        boardLightBtn.setOpaque(true);
        boardLightBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        boardLightBtn.setText("Light of Board");
        boardLightBtn.setHorizontalAlignment(SwingConstants.CENTER);
        boardLightBtn.setForeground(Color.white);
        boardLightBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                boardLightBtnMouseReleased(e);
            }
        });
        contentPane.add(boardLightBtn);
        boardLightBtn.setBounds(150, 480, 130, 40);

        //---- chessLightBtn ----
        chessLightBtn.setBackground(new Color(153, 204, 255));
        chessLightBtn.setOpaque(true);
        chessLightBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        chessLightBtn.setText("Light of Chess");
        chessLightBtn.setHorizontalAlignment(SwingConstants.CENTER);
        chessLightBtn.setForeground(Color.white);
        chessLightBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                chessLightBtnMouseReleased(e);
            }
        });
        contentPane.add(chessLightBtn);
        chessLightBtn.setBounds(150, 435, 130, 40);

        //---- depthBtn ----
        depthBtn.setBackground(new Color(51, 153, 255));
        depthBtn.setOpaque(true);
        depthBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        depthBtn.setText("Raytracing Depth");
        depthBtn.setHorizontalAlignment(SwingConstants.CENTER);
        depthBtn.setForeground(Color.white);
        depthBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                depthBtnMouseReleased(e);
            }
        });
        contentPane.add(depthBtn);
        depthBtn.setBounds(420, 480, 130, 40);

        //---- showPlaceBtn ----
        showPlaceBtn.setBackground(new Color(153, 204, 255));
        showPlaceBtn.setOpaque(true);
        showPlaceBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        showPlaceBtn.setText("Show Legal Places");
        showPlaceBtn.setHorizontalAlignment(SwingConstants.CENTER);
        showPlaceBtn.setForeground(Color.white);
        showPlaceBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                showPlaceBtnMouseReleased(e);
            }
        });
        contentPane.add(showPlaceBtn);
        showPlaceBtn.setBounds(15, 300, 265, 35);

        //---- showLastPlaceBtn ----
        showLastPlaceBtn.setBackground(new Color(153, 204, 255));
        showLastPlaceBtn.setOpaque(true);
        showLastPlaceBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        showLastPlaceBtn.setText("Show Last Place");
        showLastPlaceBtn.setHorizontalAlignment(SwingConstants.CENTER);
        showLastPlaceBtn.setForeground(Color.white);
        showLastPlaceBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                showLastPlaceBtnMouseReleased(e);
            }
        });
        contentPane.add(showLastPlaceBtn);
        showLastPlaceBtn.setBounds(285, 300, 265, 35);

        //---- gameSettingPane ----
        gameSettingPane.setBackground(new Color(224, 224, 224));
        gameSettingPane.setOpaque(true);
        gameSettingPane.setFont(new Font("Roboto Light", Font.BOLD, 13));
        gameSettingPane.setHorizontalAlignment(SwingConstants.CENTER);
        gameSettingPane.setText("Game Settings");
        gameSettingPane.setVerticalAlignment(SwingConstants.TOP);
        gameSettingPane.setForeground(Color.darkGray);
        contentPane.add(gameSettingPane);
        gameSettingPane.setBounds(10, 280, 545, 60);

        //---- moreSettingsBtn ----
        moreSettingsBtn.setBackground(new Color(102, 153, 255));
        moreSettingsBtn.setOpaque(true);
        moreSettingsBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        moreSettingsBtn.setText("More Settings...");
        moreSettingsBtn.setHorizontalAlignment(SwingConstants.CENTER);
        moreSettingsBtn.setForeground(Color.white);
        moreSettingsBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                moreSettingsBtnMouseReleased(e);
            }
        });
        contentPane.add(moreSettingsBtn);
        moreSettingsBtn.setBounds(0, 250, 570, 20);

        //---- playerSeatPane ----
        playerSeatPane.setBackground(new Color(224, 224, 224));
        playerSeatPane.setOpaque(true);
        playerSeatPane.setFont(new Font("Roboto Light", Font.BOLD, 13));
        playerSeatPane.setHorizontalAlignment(SwingConstants.CENTER);
        playerSeatPane.setText("Player Seats");
        playerSeatPane.setVerticalAlignment(SwingConstants.TOP);
        playerSeatPane.setForeground(Color.darkGray);
        contentPane.add(playerSeatPane);
        playerSeatPane.setBounds(395, 5, 170, 240);

        //---- surLightBtn ----
        surLightBtn.setBackground(new Color(51, 153, 255));
        surLightBtn.setOpaque(true);
        surLightBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        surLightBtn.setText("Surrounding Light");
        surLightBtn.setHorizontalAlignment(SwingConstants.CENTER);
        surLightBtn.setForeground(Color.white);
        surLightBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                surLightBtnMouseReleased(e);
            }
        });
        contentPane.add(surLightBtn);
        surLightBtn.setBounds(420, 435, 130, 40);

        //---- viewingSettingPane ----
        viewingSettingPane.setBackground(new Color(224, 224, 224));
        viewingSettingPane.setOpaque(true);
        viewingSettingPane.setFont(new Font("Roboto Light", Font.BOLD, 13));
        viewingSettingPane.setHorizontalAlignment(SwingConstants.CENTER);
        viewingSettingPane.setText("Video Settings");
        viewingSettingPane.setVerticalAlignment(SwingConstants.TOP);
        viewingSettingPane.setForeground(Color.darkGray);
        contentPane.add(viewingSettingPane);
        viewingSettingPane.setBounds(10, 415, 545, 110);

        //---- loadBtn ----
        loadBtn.setBackground(new Color(204, 153, 255));
        loadBtn.setOpaque(true);
        loadBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        loadBtn.setText("Load Game");
        loadBtn.setHorizontalAlignment(SwingConstants.CENTER);
        loadBtn.setForeground(Color.white);
        loadBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                loadBtnMouseReleased(e);
            }
        });
        contentPane.add(loadBtn);
        loadBtn.setBounds(10, 200, 185, 40);

        //---- saveBtn ----
        saveBtn.setBackground(new Color(204, 153, 255));
        saveBtn.setOpaque(true);
        saveBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        saveBtn.setText("Save Game");
        saveBtn.setHorizontalAlignment(SwingConstants.CENTER);
        saveBtn.setForeground(Color.white);
        saveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                saveBtnMouseReleased(e);
            }
        });
        contentPane.add(saveBtn);
        saveBtn.setBounds(200, 200, 185, 40);

        //---- profilePane ----
        profilePane.setBackground(new Color(224, 224, 224));
        profilePane.setOpaque(true);
        profilePane.setFont(new Font("Roboto Light", Font.BOLD, 13));
        profilePane.setHorizontalAlignment(SwingConstants.CENTER);
        profilePane.setText("Profile Manager");
        profilePane.setVerticalAlignment(SwingConstants.TOP);
        profilePane.setForeground(Color.darkGray);
        contentPane.add(profilePane);
        profilePane.setBounds(5, 180, 385, 65);

        //---- replayBtn ----
        replayBtn.setBackground(new Color(51, 204, 255));
        replayBtn.setOpaque(true);
        replayBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        replayBtn.setText("Replay");
        replayBtn.setHorizontalAlignment(SwingConstants.CENTER);
        replayBtn.setForeground(Color.white);
        replayBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                replayBtnMouseReleased(e);
            }
        });
        contentPane.add(replayBtn);
        replayBtn.setBounds(135, 150, 125, 25);

        contentPane.setPreferredSize(new Dimension(570, 570));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel statusLab;
    private JPanel ply1;
    private JLabel infLab1;
    private JLabel modBtn1;
    private JLabel ptr1;
    private JLabel cheatBtn1;
    private JPanel ply2;
    private JLabel infLab2;
    private JLabel modBtn2;
    private JLabel ptr2;
    private JLabel cheatBtn2;
    private JLabel restartBtn;
    private JLabel redoBtn;
    private JLabel backBtn;
    private JLabel draggingBtn;
    private JLabel wanderingBtn;
    private JLabel RoundingBtn;
    private JLabel OverlookingBtn;
    private JLabel cameraSettingPane;
    private JLabel simpleBoardBtn;
    private JLabel specularBoardBtn;
    private JLabel simpleChessBtn;
    private JLabel hdrBtn;
    private JLabel boardLightBtn;
    private JLabel chessLightBtn;
    private JLabel depthBtn;
    private JLabel showPlaceBtn;
    private JLabel showLastPlaceBtn;
    private JLabel gameSettingPane;
    private JLabel moreSettingsBtn;
    private JLabel playerSeatPane;
    private JLabel surLightBtn;
    private JLabel viewingSettingPane;
    private JLabel loadBtn;
    private JLabel saveBtn;
    private JLabel profilePane;
    private JLabel replayBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public JLabel getStatusLab() {
        return statusLab;
    }

    public void setStatusLab(JLabel statusLab) {
        this.statusLab = statusLab;
    }

    public JPanel getPly1() {
        return ply1;
    }

    public void setPly1(JPanel ply1) {
        this.ply1 = ply1;
    }

    public JLabel getInfLab1() {
        return infLab1;
    }

    public JLabel getDepthBtn() {
        return depthBtn;
    }

    public void setDepthBtn(JLabel depthBtn) {
        this.depthBtn = depthBtn;
    }

    public void setInfLab1(JLabel infLab1) {
        this.infLab1 = infLab1;
    }

    public JLabel getShowLastPlaceBtn() {
        return showLastPlaceBtn;
    }

    public void setShowLastPlaceBtn(JLabel showLastPlaceBtn) {
        this.showLastPlaceBtn = showLastPlaceBtn;
    }

    public JLabel getModBtn1() {
        return modBtn1;
    }

    public void setModBtn1(JLabel modBtn1) {
        this.modBtn1 = modBtn1;
    }

    public JLabel getPtr1() {
        return ptr1;
    }

    public void setPtr1(JLabel ptr1) {
        this.ptr1 = ptr1;
    }

    public JLabel getCheatBtn1() {
        return cheatBtn1;
    }

    public void setCheatBtn1(JLabel cheatBtn1) {
        this.cheatBtn1 = cheatBtn1;
    }

    public JPanel getPly2() {
        return ply2;
    }

    public JLabel getSimpleBoardBtn() {
        return simpleBoardBtn;
    }

    public void setSimpleBoardBtn(JLabel simpleBoardBtn) {
        this.simpleBoardBtn = simpleBoardBtn;
    }

    public JLabel getSpecularBoardBtn() {
        return specularBoardBtn;
    }

    public void setSpecularBoardBtn(JLabel specularBoardBtn) {
        this.specularBoardBtn = specularBoardBtn;
    }

    public JLabel getHdrBtn() {
        return hdrBtn;
    }

    public void setHdrBtn(JLabel hdrBtn) {
        this.hdrBtn = hdrBtn;
    }

    public JLabel getMoreSettingsBtn() {
        return moreSettingsBtn;
    }

    public void setMoreSettingsBtn(JLabel moreSettingsBtn) {
        this.moreSettingsBtn = moreSettingsBtn;
    }

    public JLabel getLoadBtn() {
        return loadBtn;
    }

    public void setLoadBtn(JLabel loadBtn) {
        this.loadBtn = loadBtn;
    }

    public JLabel getSaveBtn() {
        return saveBtn;
    }

    public void setSaveBtn(JLabel saveBtn) {
        this.saveBtn = saveBtn;
    }

    public void setPly2(JPanel ply2) {
        this.ply2 = ply2;
    }

    public JLabel getInfLab2() {
        return infLab2;
    }

    public void setInfLab2(JLabel infLab2) {
        this.infLab2 = infLab2;
    }

    public JLabel getModBtn2() {
        return modBtn2;
    }

    public void setModBtn2(JLabel modBtn2) {
        this.modBtn2 = modBtn2;
    }

    public JLabel getPtr2() {
        return ptr2;
    }

    public void setPtr2(JLabel ptr2) {
        this.ptr2 = ptr2;
    }

    public JLabel getCheatBtn2() {
        return cheatBtn2;
    }

    public JLabel getSurLightBtn() {
        return surLightBtn;
    }

    public void setSurLightBtn(JLabel surLightBtn) {
        this.surLightBtn = surLightBtn;
    }

    public void setCheatBtn2(JLabel cheatBtn2) {
        this.cheatBtn2 = cheatBtn2;
    }

    public JLabel getRestartBtn() {
        return restartBtn;
    }

    public void setRestartBtn(JLabel restartBtn) {
        this.restartBtn = restartBtn;
    }

    public JLabel getRedoBtn() {
        return redoBtn;
    }

    public void setRedoBtn(JLabel redoBtn) {
        this.redoBtn = redoBtn;
    }

    public JLabel getBackBtn() {
        return backBtn;
    }

    public void setBackBtn(JLabel backBtn) {
        this.backBtn = backBtn;
    }

    public JLabel getDraggingBtn() {
        return draggingBtn;
    }

    public void setDraggingBtn(JLabel draggingBtn) {
        this.draggingBtn = draggingBtn;
    }

    public JLabel getWanderingBtn() {
        return wanderingBtn;
    }

    public void setWanderingBtn(JLabel wanderingBtn) {
        this.wanderingBtn = wanderingBtn;
    }

    public JLabel getRoundingBtn() {
        return RoundingBtn;
    }

    public void setRoundingBtn(JLabel roundingBtn) {
        RoundingBtn = roundingBtn;
    }

    public JLabel getOverlookingBtn() {
        return OverlookingBtn;
    }

    public void setOverlookingBtn(JLabel overlookingBtn) {
        OverlookingBtn = overlookingBtn;
    }

    public JLabel getSimpleChessBtn() {
        return simpleChessBtn;
    }

    public void setSimpleChessBtn(JLabel simpleChessBtn) {
        this.simpleChessBtn = simpleChessBtn;
    }

    public JLabel getBoardLightBtn() {
        return boardLightBtn;
    }

    public void setBoardLightBtn(JLabel chessLightBtn) {
        this.boardLightBtn = chessLightBtn;
    }

    public JLabel getChessLightBtn() {
        return chessLightBtn;
    }

    public void setChessLightBtn(JLabel chessLightBtn) {
        this.chessLightBtn = chessLightBtn;
    }

    public JLabel getShowPlaceBtn() {
        return showPlaceBtn;
    }

    public void setShowPlaceBtn(JLabel showPlaceBtn) {
        this.showPlaceBtn = showPlaceBtn;
    }
}
