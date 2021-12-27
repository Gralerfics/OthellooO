package tech.gralerfics.controller.comps;

import tech.gralerfics.controller.wind.ProfileDialog;
import tech.gralerfics.gamelogic.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfileConfirmingComponent extends JComponent {
    public Game game;
    public int w = 170, h = 260;
    private ProfileDialog target;

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    public ProfileConfirmingComponent(Game g, ProfileDialog confirmTarget) {
        this.game = g;
        this.target = confirmTarget;
        initComponents();
    }

    private void conLableMouseReleased(MouseEvent e) {
        if (target.choice != null) {
            target.choice.conLable.setText("Confirm");
            target.choice.conLable.setBackground(new Color(0, 204, 102));
        }
        target.choice = this;
        conLable.setText("Confirmed");
        conLable.setBackground(new Color(0, 174, 82));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        gameNameLabel = new JLabel();
        conLable = new JLabel();
        bkBoard = new JLabel();
        bkColor = new JLabel();
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

        //---- conLable ----
        conLable.setText("Confirm");
        conLable.setBackground(new Color(0, 204, 102));
        conLable.setHorizontalAlignment(SwingConstants.CENTER);
        conLable.setForeground(Color.white);
        conLable.setOpaque(true);
        conLable.setFont(new Font("Roboto Light", Font.BOLD, 17));
        conLable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                conLableMouseReleased(e);
            }
        });
        add(conLable);
        conLable.setBounds(5, 195, 160, 60);

        //---- bkColor ----
        bkColor.setBackground(new Color(180, 180, 180));
        bkColor.setOpaque(true);
        add(bkColor);
        bkColor.setBounds(0, -5, 170, 265);

        setPreferredSize(new Dimension(w, h));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel gameNameLabel;
    private JLabel conLable;
    private JLabel bkBoard;
    private JLabel bkColor;
    private JLabel[][] viewLabs;
    private JLabel[][] boardLabs;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
