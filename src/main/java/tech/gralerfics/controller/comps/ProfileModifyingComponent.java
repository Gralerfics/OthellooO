package tech.gralerfics.controller.comps;

import tech.gralerfics.controller.wind.ConfirmDialog;
import tech.gralerfics.controller.wind.InputDialog;
import tech.gralerfics.controller.wind.ProfileFrame;
import tech.gralerfics.controller.wind.TipDialog;
import tech.gralerfics.gamelogic.Game;
import tech.gralerfics.persistence.profiles.ProfileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfileModifyingComponent extends JComponent {
    public Game game;
    public ProfileFrame father;
    public int w = 170, h = 360;

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    public ProfileModifyingComponent(Game g, ProfileFrame father) {
        this.father = father;
        this.game = g;
        initComponents();
    }

    private void delBtnMouseReleased(MouseEvent e) {
        boolean rst = (new ConfirmDialog(father)).show("Are you sure? The Operation is not invertible!");
        if (rst) {
            ProfileManager.deleteProfile(game.name);
            father.initComponents();
        }
    }

    private void modifyBtnMouseReleased(MouseEvent e) {
        String rst = (new InputDialog(father)).show("Type its new name");
        if (rst.isEmpty()) {
            return;
        }
        if (ProfileManager.list.containsKey(rst)) {
            (new TipDialog(father)).show("Invalid or existed name!");
        } else {
            ProfileManager.modifyProfile(game, rst);
            father.initComponents();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        gameNameLabel = new JLabel();
        bkColor = new JLabel();
        delBtn = new JLabel();
        modifyBtn = new JLabel();
        viewLabs = new JLabel[8][8];
        boardLabs = new JLabel[8][8];

        //======== this ========
        setOpaque(true);
        setFont(new Font("Roboto Light", Font.BOLD, 14));
        setLayout(null);

        //---- gameNameLabel ----
        gameNameLabel.setText(game.name);
        gameNameLabel.setFont(new Font("Roboto Light", Font.PLAIN, 13));
        gameNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameNameLabel.setBackground(new Color(255, 188, 0));
        gameNameLabel.setOpaque(true);
        gameNameLabel.setForeground(Color.darkGray);
        add(gameNameLabel);
        gameNameLabel.setBounds(0, 0, 170, 25);

        //---- delBtn ----
        delBtn.setText("Delete");
        delBtn.setHorizontalAlignment(SwingConstants.CENTER);
        delBtn.setForeground(Color.white);
        delBtn.setOpaque(true);
        delBtn.setBackground(new Color(255, 102, 102));
        delBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        delBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                delBtnMouseReleased(e);
            }
        });
        add(delBtn);
        delBtn.setBounds(5, 240, 160, 40);

        //---- modifyBtn ----
        modifyBtn.setText("Modify");
        modifyBtn.setHorizontalAlignment(SwingConstants.CENTER);
        modifyBtn.setForeground(Color.white);
        modifyBtn.setOpaque(true);
        modifyBtn.setBackground(new Color(129, 216, 255));
        modifyBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        modifyBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                modifyBtnMouseReleased(e);
            }
        });
        add(modifyBtn);
        modifyBtn.setBounds(5, 195, 160, 40);

        //---- contents ----
        int Ox = 5, Oy = 30, L = 14, mar = 6;
        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j ++) {
                viewLabs[i][j] = new JLabel();
                boardLabs[i][j] = new JLabel();
                if (game.judger.contents[j][i] == 1) {
                    viewLabs[i][j].setBackground(new Color(0, 0, 0));
                } else if (game.judger.contents[j][i] == -1) {
                    viewLabs[i][j].setBackground(new Color(255, 255, 255));
                } else {
                    viewLabs[i][j].setVisible(false);
                }
                if ((i + j) % 2 == 1) {
                    boardLabs[i][j].setBackground(new Color(218, 169, 85, 194));
                } else {
                    boardLabs[i][j].setBackground(new Color(222, 203, 137, 176));
                }
                viewLabs[i][j].setOpaque(true);
                boardLabs[i][j].setOpaque(true);
                viewLabs[i][j].setBounds(Ox + j * (L + mar) + mar / 2, Oy + i * (L + mar) + mar / 2, L, L);
                boardLabs[i][j].setBounds(Ox + j * (L + mar), Oy + i * (L + mar), L + mar, L + mar);
                add(viewLabs[i][j]);
                add(boardLabs[i][j]);
            }
        }

        //---- bkColor ----
        bkColor.setBackground(new Color(180, 180, 180));
        bkColor.setOpaque(true);
        add(bkColor);
        bkColor.setBounds(0, -5, 170, 290);

        setPreferredSize(new Dimension(w, h));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel gameNameLabel;
    private JLabel delBtn;
    private JLabel modifyBtn;
    private JLabel bkColor;
    private JLabel[][] viewLabs;
    private JLabel[][] boardLabs;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
