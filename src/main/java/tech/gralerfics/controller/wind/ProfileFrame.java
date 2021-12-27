package tech.gralerfics.controller.wind;

import tech.gralerfics.controller.Main;
import tech.gralerfics.controller.comps.ProfileModifyingComponent;
import tech.gralerfics.controller.comps.SlidingPane;
import tech.gralerfics.gamelogic.Game;
import tech.gralerfics.persistence.options.GUIConstants;
import tech.gralerfics.persistence.profiles.ProfileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ProfileFrame extends JFrame {
    private boolean initialized = false;

    public ProfileFrame() {
        init();
        setVisible(false);
    }

    public void correctSize() {
        Insets insets = this.getInsets();
        this.setSize(new Dimension(GUIConstants.PROFILE_DEFAULT_HRES + insets.left + insets.right,  GUIConstants.PROFILE_DEFAULT_VRES + insets.top + insets.bottom));
    }

    public void init() {
        initComponents();
        initialized = true;
    }

    private void backBtnMouseReleased(MouseEvent e) {
        Main.mainFrame.setVisible(true);
        this.setVisible(false);
    }

    public void initSlidingContents() {
        ProfileManager.loadProfiles();
        ArrayList<Game> li = new ArrayList<>(ProfileManager.list.values());
        for (Game g : li) {
            ProfileModifyingComponent pmc = new ProfileModifyingComponent(g, this);
            slidingPane.contents.add(pmc);
        }
    }

    private void thisWindowClosing(WindowEvent e) {
        backBtnMouseReleased(null);
    }

    public void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        slidingPane = new SlidingPane(GUIConstants.PLAYER_DEFAULT_HRES - 5, 305);
        backBtn = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon("assets/guiimages/icon.jpg").getImage());
        this.setTitle("Profile Manager");
        this.setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(null);

        //======== slidingPane ========
        {
            initSlidingContents();
        }
        contentPane.add(slidingPane);
        slidingPane.setBounds(0, 0, slidingPane.getWidth(), slidingPane.getHeight());
        slidingPane.initComponents();

        //---- backBtn ----
        backBtn.setBackground(new Color(196, 196, 196));
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
        backBtn.setBounds(GUIConstants.PLAYER_DEFAULT_HRES - 155, 310, 150, 40);

        pack();
        if (!initialized) setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        correctSize();
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private SlidingPane slidingPane;
    private JLabel backBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
