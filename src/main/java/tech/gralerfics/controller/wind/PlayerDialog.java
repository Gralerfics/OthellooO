package tech.gralerfics.controller.wind;

import tech.gralerfics.controller.comps.PlayerConfirmingComponent;
import tech.gralerfics.controller.comps.SlidingPane;
import tech.gralerfics.gamelogic.Player;
import tech.gralerfics.persistence.profiles.PlayerManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class PlayerDialog extends JDialog {
    public int w = 600, h = 60 + 175;
    public PlayerConfirmingComponent choice = null;

    public PlayerDialog(Window owner) {
        super(owner);
        this.setModal(true);
        PlayerManager.loadPlayers();
        choice = null;
        initComponents();
    }

    public Player show(String title) {
        this.setTitle(title);
        this.setVisible(true);
        if (choice != null) {
            return choice.player;
        }
        return null;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        slidingPane = new SlidingPane(w - 20, h - 45);

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("assets/guiimages/icon.jpg").getImage());
        this.setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== slidingPane ========
        {
            ArrayList<Player> li = new ArrayList<>(PlayerManager.list.values());
            li.sort(new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return Integer.compare(o1.tag, o2.tag);
                }
            });
            for (Player p : li) {
                PlayerConfirmingComponent pcc = new PlayerConfirmingComponent(p, this);
                slidingPane.contents.add(pcc);
            }
        }
        contentPane.add(slidingPane);
        slidingPane.setBounds(0, 0, slidingPane.getWidth(), slidingPane.getHeight());
        slidingPane.initComponents();

//        contentPane.setPreferredSize(new Dimension(w, h));
        pack();
        if (choice == null) setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        this.setSize(new Dimension(w, h));
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private SlidingPane slidingPane;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
