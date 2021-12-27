package tech.gralerfics.controller.wind;

import tech.gralerfics.controller.comps.ProfileConfirmingComponent;
import tech.gralerfics.controller.comps.SlidingPane;
import tech.gralerfics.gamelogic.Game;
import tech.gralerfics.persistence.profiles.ProfileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProfileDialog extends JDialog {
    public int w = 600, h = 60 + 265;
    public ProfileConfirmingComponent choice = null;

    public ProfileDialog(Window owner) {
        super(owner);
        this.setModal(true);
        ProfileManager.loadProfiles();
        choice = null;
        initComponents();
    }

    public Game show(String title) {
        this.setTitle(title);
        this.setVisible(true);
        if (choice != null) {
            return choice.game;
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
            ArrayList<Game> li = new ArrayList<>(ProfileManager.list.values());
            for (Game g : li) {
                ProfileConfirmingComponent pcc = new ProfileConfirmingComponent(g, this);
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
