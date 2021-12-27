package tech.gralerfics.controller.wind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TipDialog extends JDialog {
    public TipDialog(Window owner) {
        super(owner);
        this.setModal(true);
        initComponents();
    }

    public void show(String message) {
        infLabel.setText(message);
        this.setVisible(true);
    }

    private void confirmBtnMouseReleased(MouseEvent e) {
        setVisible(false);
        dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        confirmBtn = new JLabel();
        infLabel = new JLabel();

        //======== this ========
        setTitle("Notice");
        setIconImage(new ImageIcon("assets/guiimages/icon.jpg").getImage());
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- confirmBtn ----
        confirmBtn.setOpaque(true);
        confirmBtn.setBackground(new Color(0, 204, 102));
        confirmBtn.setText("Confirm");
        confirmBtn.setHorizontalAlignment(SwingConstants.CENTER);
        confirmBtn.setForeground(Color.white);
        confirmBtn.setFont(new Font("Roboto Condensed", Font.BOLD, 16));
        confirmBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                confirmBtnMouseReleased(e);
            }
        });
        contentPane.add(confirmBtn);
        confirmBtn.setBounds(5, 30, 285, 40);

        //---- infLabel ----
        infLabel.setText("Show something...");
        infLabel.setFont(new Font("Roboto Light", Font.PLAIN, 14));
        contentPane.add(infLabel);
        infLabel.setBounds(10, 5, 275, 25);

        contentPane.setPreferredSize(new Dimension(295, 75));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel confirmBtn;
    private JLabel infLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
