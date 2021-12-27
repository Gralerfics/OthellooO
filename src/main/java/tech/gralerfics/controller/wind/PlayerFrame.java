package tech.gralerfics.controller.wind;

import tech.gralerfics.controller.Main;
import tech.gralerfics.controller.comps.PlayerModifyingComponent;
import tech.gralerfics.controller.comps.SlidingPane;
import tech.gralerfics.gamelogic.Player;
import tech.gralerfics.persistence.options.GUIConstants;
import tech.gralerfics.persistence.profiles.PlayerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class PlayerFrame extends JFrame {
    private boolean initialized = false;

    public PlayerFrame() {
        init();
        setVisible(false);
    }

    public void correctSize() {
        Insets insets = this.getInsets();
        this.setSize(new Dimension(GUIConstants.PLAYER_DEFAULT_HRES + insets.left + insets.right,  GUIConstants.PLAYER_DEFAULT_VRES + insets.top + insets.bottom));
    }

    public void init() {
        initComponents();
        initialized = true;
    }

    private void backBtnMouseReleased(MouseEvent e) {
        Main.mainFrame.setVisible(true);
        this.setVisible(false);
    }

    private void addBtnMouseReleased(MouseEvent e) {
        String rst = (new InputDialog(this)).show("Type your name");
        if (rst.isEmpty()) {
            return;
        }
        if (PlayerManager.list.containsKey(rst)) {
            (new TipDialog(this)).show("Invalid or existed name!");
        } else {
            Color color = (new ColorDialog(this)).show("Pick your color", new Color(204, 204, 204));
            if (color == null) {
                return;
            }
            Player np = new Player();
            np.name = rst;
            np.color = color;
            PlayerManager.newPlayer(np);
            initComponents();
        }
    }

    public void initSlidingContents() {
        PlayerManager.loadPlayers();
        ArrayList<Player> li = new ArrayList<>(PlayerManager.list.values());
        li.sort((o1, o2) -> Double.compare(o2.getRate(), o1.getRate()));
        for (Player p : li) {
            PlayerModifyingComponent pmc = new PlayerModifyingComponent(p, this);
            slidingPane.contents.add(pmc);
        }
    }

    private void thisWindowClosing(WindowEvent e) {
        backBtnMouseReleased(null);
    }

    public void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        slidingPane = new SlidingPane(GUIConstants.PLAYER_DEFAULT_HRES - 5, 195);
        addBtn = new JLabel();
        backBtn = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon("assets/guiimages/icon.jpg").getImage());
        this.setTitle("Player Manager");
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

        //---- addBtn ----
        addBtn.setBackground(new Color(92, 227, 83));
        addBtn.setOpaque(true);
        addBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        addBtn.setText("Create Player");
        addBtn.setHorizontalAlignment(SwingConstants.CENTER);
        addBtn.setForeground(Color.white);
        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                addBtnMouseReleased(e);
            }
        });
        contentPane.add(addBtn);
        addBtn.setBounds(5, 200, 150, 40);

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
        backBtn.setBounds(GUIConstants.PLAYER_DEFAULT_HRES - 155, 200, 150, 40);

        pack();
        if (!initialized) setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        correctSize();
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private SlidingPane slidingPane;
    private JLabel addBtn;
    private JLabel backBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
