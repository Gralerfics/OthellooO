package tech.gralerfics.controller.comps;

import tech.gralerfics.controller.wind.*;
import tech.gralerfics.gamelogic.Player;
import tech.gralerfics.persistence.profiles.PlayerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

public class PlayerModifyingComponent extends JComponent {
    public Player player;
    public PlayerFrame father;
    public int w = 170, h = 180;

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    public PlayerModifyingComponent(Player ply, PlayerFrame father) {
        this.father = father;
        this.player = ply;
        initComponents();
    }

    private void delBtnMouseReleased(MouseEvent e) {
        if (player.tag / 10 != 1) {
            boolean rst = (new ConfirmDialog(father)).show("Are you sure? The Operation is not invertible!");
            if (rst) {
                PlayerManager.deletePlayer(player);
                father.initComponents();
            }
        }
    }

    private void modifyBtnMouseReleased(MouseEvent e) {
        if (player.tag / 10 != 1) {
            String rst = (new InputDialog(father)).show("Type your new name");
            if (rst.isEmpty()) {
                return;
            }
            if (PlayerManager.list.containsKey(rst)) {
                (new TipDialog(father)).show("Invalid or existed name!");
            } else {
                PlayerManager.modifyPlayer(player, rst, null);
                father.initComponents();
            }
        }
    }

    private void modifyColorBtnMouseReleased(MouseEvent e) {
        if (player.tag / 10 != 1) {
            Color color = (new ColorDialog(father)).show("Pick your color", player.color);
            if (color != null) {
                PlayerManager.modifyPlayer(player, player.name, color);
            }
            father.initComponents();
        }
    }

    private void netPlayer() {
        try {
            String[] target = (new InputDialog(father)).show("Input the server address with the port").split(":");
            Socket socket = new Socket(target[0], Integer.parseInt(target[1]));

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write("sent");
            pw.flush();
            socket.shutdownOutput();

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            while((info = br.readLine())!=null){
                System.out.println(info);
            }
        } catch (Exception e) {
            (new TipDialog(father)).show("Failed to connect!");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        plyNameLabel = new JLabel();
        fineLabel = new JLabel();
        failLabel = new JLabel();
        winLabel = new JLabel();
        rateLabel = new JLabel();
        delBtn = new JLabel();
        modifyBtn = new JLabel();
        modifyColorBtn = new JLabel();
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
//        plyNameLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                netPlayer();
//            }
//        });
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
        fineLabel.setBounds(60, 30, 50, 25);

        //---- failLabel ----
        failLabel.setText(String.valueOf(player.fails));
        failLabel.setHorizontalAlignment(SwingConstants.CENTER);
        failLabel.setForeground(Color.white);
        failLabel.setOpaque(true);
        failLabel.setBackground(new Color(255, 102, 102));
        failLabel.setFont(new Font("Roboto Light", Font.BOLD, 15));
        add(failLabel);
        failLabel.setBounds(115, 30, 50, 25);

        //---- winLabel ----
        winLabel.setText(String.valueOf(player.wins));
        winLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winLabel.setForeground(Color.white);
        winLabel.setOpaque(true);
        winLabel.setBackground(new Color(0, 204, 102));
        winLabel.setFont(new Font("Roboto Light", Font.BOLD, 15));
        add(winLabel);
        winLabel.setBounds(5, 30, 50, 25);

        //---- rateLabel ----
        rateLabel.setText((player.getRate() >= 0) ? (String.format("%.2f", player.getRate() * 100) + " %") : "--.-- %");
        rateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rateLabel.setForeground(Color.white);
        rateLabel.setOpaque(true);
        rateLabel.setBackground(new Color(86, 201, 222));
        rateLabel.setFont(new Font("Roboto Light", Font.BOLD, 15));
        add(rateLabel);
        rateLabel.setBounds(5, 60, 160, 20);

        //---- delBtn ----
        delBtn.setText("Delete");
        delBtn.setHorizontalAlignment(SwingConstants.CENTER);
        delBtn.setForeground(Color.white);
        delBtn.setOpaque(true);
        if (player.tag / 10 == 1) {
            delBtn.setBackground(new Color(150, 150, 150));
        } else {
            delBtn.setBackground(new Color(255, 102, 102));
        }
        delBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        delBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                delBtnMouseReleased(e);
            }
        });
        add(delBtn);
        delBtn.setBounds(5, 130, 160, 40);

        //---- modifyBtn ----
        modifyBtn.setText("Name");
        modifyBtn.setHorizontalAlignment(SwingConstants.CENTER);
        modifyBtn.setForeground(Color.white);
        modifyBtn.setOpaque(true);
        if (player.tag / 10 == 1) {
            modifyBtn.setBackground(new Color(150, 150, 150));
        } else {
            modifyBtn.setBackground(new Color(129, 216, 255));
        }
        modifyBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        modifyBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                modifyBtnMouseReleased(e);
            }
        });
        add(modifyBtn);
        modifyBtn.setBounds(5, 85, 78, 40);

        //---- modifyColorBtn ----
        modifyColorBtn.setText("Color");
        modifyColorBtn.setHorizontalAlignment(SwingConstants.CENTER);
        modifyColorBtn.setForeground(Color.white);
        modifyColorBtn.setOpaque(true);
        if (player.tag / 10 == 1) {
            modifyColorBtn.setBackground(new Color(150, 150, 150));
        } else {
            modifyColorBtn.setBackground(new Color(129, 216, 255));
        }
        modifyColorBtn.setFont(new Font("Roboto Light", Font.BOLD, 14));
        modifyColorBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                modifyColorBtnMouseReleased(e);
            }
        });
        add(modifyColorBtn);
        modifyColorBtn.setBounds(87, 85, 78, 40);

        //---- bkColor ----
        bkColor.setBackground(new Color(234, 234, 234));
        bkColor.setOpaque(true);
        bkColor.setFont(new Font("Roboto Light", Font.BOLD, 14));
        add(bkColor);
        bkColor.setBounds(0, -5, 170, 180);

        setPreferredSize(new Dimension(w, h));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel plyNameLabel;
    private JLabel fineLabel;
    private JLabel failLabel;
    private JLabel winLabel;
    private JLabel rateLabel;
    private JLabel delBtn;
    private JLabel modifyBtn;
    private JLabel modifyColorBtn;
    private JLabel bkColor;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
