package tech.gralerfics.controller.comps;

import tech.gralerfics.controller.wind.PlayerDialog;
import tech.gralerfics.gamelogic.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayerConfirmingComponent extends JComponent {
    public Player player;
    public int w = 170, h = 170;
    private PlayerDialog target;

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    public PlayerConfirmingComponent(Player ply, PlayerDialog confirmTarget) {
        this.player = ply;
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
        plyNameLabel = new JLabel();
        fineLabel = new JLabel();
        failLabel = new JLabel();
        winLabel = new JLabel();
        conLable = new JLabel();
        bkColor = new JLabel();

        //======== this ========
        setBackground(new Color(153, 153, 153));
        setOpaque(true);
        setFont(new Font("Roboto Light", Font.BOLD, 14));
        setLayout(null);

        //---- plyNameLabel ----
        plyNameLabel.setText(player.name);
        plyNameLabel.setFont(new Font("Roboto Light", Font.PLAIN, 13));
        plyNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        plyNameLabel.setBackground(player.color);
        plyNameLabel.setOpaque(true);
        plyNameLabel.setForeground(Color.darkGray);
        add(plyNameLabel);
        plyNameLabel.setBounds(0, 0, 170, 25);

        //---- fineLabel ----
        fineLabel.setText(String.valueOf(player.fines));
        fineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fineLabel.setForeground(Color.white);
        fineLabel.setOpaque(true);
        fineLabel.setBackground(new Color(255, 204, 0));
        fineLabel.setFont(new Font("Roboto Light", Font.BOLD, 15));
        add(fineLabel);
        fineLabel.setBounds(60, 30, 50, 70);

        //---- failLabel ----
        failLabel.setText(String.valueOf(player.fails));
        failLabel.setHorizontalAlignment(SwingConstants.CENTER);
        failLabel.setForeground(Color.white);
        failLabel.setOpaque(true);
        failLabel.setBackground(new Color(255, 102, 102));
        failLabel.setFont(new Font("Roboto Light", Font.BOLD, 15));
        add(failLabel);
        failLabel.setBounds(115, 30, 50, 70);

        //---- winLabel ----
        winLabel.setText(String.valueOf(player.wins));
        winLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winLabel.setForeground(Color.white);
        winLabel.setOpaque(true);
        winLabel.setBackground(new Color(0, 204, 102));
        winLabel.setFont(new Font("Roboto Light", Font.BOLD, 15));
        add(winLabel);
        winLabel.setBounds(5, 30, 50, 70);

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
        conLable.setBounds(5, 105, 160, 60);

        //---- bkColor ----
        bkColor.setBackground(new Color(234, 234, 234));
        bkColor.setOpaque(true);
        bkColor.setFont(new Font("Roboto Light", Font.BOLD, 14));
        add(bkColor);
        bkColor.setBounds(0, -5, 170, 175);

        setPreferredSize(new Dimension(w, h));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel plyNameLabel;
    private JLabel fineLabel;
    private JLabel failLabel;
    private JLabel winLabel;
    private JLabel conLable;
    private JLabel bkColor;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
