package tech.gralerfics.controller.wind;

import java.awt.event.*;
import tech.gralerfics.controller.Main;
import tech.gralerfics.managers.resource.ImageManager;
import tech.gralerfics.persistence.options.OptionsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
//    Main.mainFrame.getGraphics().drawImage(ImageManager.MainFrameBackgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);

    public MainFrame() {
        this.setVisible(false);
        initComponents();
    }

    private void quitBtnMouseReleased(MouseEvent e) {
        OptionsManager.saveOption();
        Main.confirmToClose(this);
    }

    private void othelloooBtnMouseReleased(MouseEvent e) {
        Main.glfwCtrl.getGlfwApp().getWindow().setVisible(true);
        Main.statusFrame.setVisible(true);
        this.setVisible(false);
    }

    private void playerBtnMouseReleased(MouseEvent e) {
        Main.playerFrame.initComponents();
        Main.playerFrame.setVisible(true);
        this.setVisible(false);
    }

    private void profileBtnMouseReleased(MouseEvent e) {
        Main.profileFrame.initComponents();
        Main.profileFrame.setVisible(true);
        this.setVisible(false);
    }

    private void thisWindowClosing(WindowEvent e) {
        Main.confirmToClose(this);
    }

    private void othelloooBtnMouseEntered(MouseEvent e) {

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        playerBtn = new JLabel();
        profileBtn = new JLabel();
        quitBtn = new JLabel();
        othelloooBtn = new JLabel();
        label1 = new JLabel();
        optionBtn = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("OthellooO!");
        setBackground(Color.white);
        setResizable(false);
        setIconImage(new ImageIcon("assets/guiimages/icon.jpg").getImage());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- playerBtn ----
        playerBtn.setIcon(null);
        playerBtn.setBackground(new Color(204, 102, 255));
        playerBtn.setText("Players & Ranking");
        playerBtn.setHorizontalAlignment(SwingConstants.CENTER);
        playerBtn.setOpaque(true);
        playerBtn.setFont(new Font("Roboto Light", Font.ITALIC, 20));
        playerBtn.setForeground(Color.white);
        playerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                playerBtnMouseReleased(e);
            }
        });
        contentPane.add(playerBtn);
        playerBtn.setBounds(345, 200, 196, 38);

        //---- profileBtn ----
        profileBtn.setIcon(null);
        profileBtn.setBackground(new Color(255, 204, 0));
        profileBtn.setText("Profiles");
        profileBtn.setHorizontalAlignment(SwingConstants.CENTER);
        profileBtn.setOpaque(true);
        profileBtn.setFont(new Font("Roboto Light", Font.ITALIC, 20));
        profileBtn.setForeground(Color.white);
        profileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                profileBtnMouseReleased(e);
            }
        });
        contentPane.add(profileBtn);
        profileBtn.setBounds(140, 245, 196, 37);

        //---- quitBtn ----
        quitBtn.setIcon(null);
        quitBtn.setBackground(new Color(153, 153, 153));
        quitBtn.setText("Quit");
        quitBtn.setHorizontalAlignment(SwingConstants.CENTER);
        quitBtn.setOpaque(true);
        quitBtn.setFont(new Font("Roboto Light", Font.ITALIC, 20));
        quitBtn.setForeground(Color.white);
        quitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                quitBtnMouseReleased(e);
            }
        });
        contentPane.add(quitBtn);
        quitBtn.setBounds(345, 245, 196, 37);

        //---- othelloooBtn ----
        othelloooBtn.setIcon(null);
        othelloooBtn.setBackground(new Color(255, 153, 51));
        othelloooBtn.setText("OthellooO!");
        othelloooBtn.setHorizontalAlignment(SwingConstants.CENTER);
        othelloooBtn.setOpaque(true);
        othelloooBtn.setFont(new Font("Roboto Light", Font.ITALIC, 20));
        othelloooBtn.setForeground(Color.white);
        othelloooBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                othelloooBtnMouseEntered(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                othelloooBtnMouseReleased(e);
            }
        });
        contentPane.add(othelloooBtn);
        othelloooBtn.setBounds(140, 200, 196, 38);

        //---- label1 ----
        label1.setIcon(new ImageIcon("assets/guiimages/B.png"));
        contentPane.add(label1);
        label1.setBounds(-5, -5, 725, 530);

        contentPane.setPreferredSize(new Dimension(675, 500));
        pack();
        setLocationRelativeTo(getOwner());

        //---- optionBtn ----
        optionBtn.setIcon(null);
        optionBtn.setBackground(new Color(102, 153, 255));
        optionBtn.setText("Options");
        optionBtn.setHorizontalAlignment(SwingConstants.CENTER);
        optionBtn.setOpaque(true);
        optionBtn.setFont(new Font("Roboto Condensed", Font.BOLD | Font.ITALIC, 18));
        optionBtn.setForeground(Color.white);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel playerBtn;
    private JLabel profileBtn;
    private JLabel quitBtn;
    private JLabel othelloooBtn;
    private JLabel label1;
    private JLabel optionBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
