package tech.gralerfics.controller.wind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputDialog extends JDialog {
    String result;

    public InputDialog(Window owner) {
        super(owner);
        this.setModal(true);
        initComponents();
    }

    public String show(String message) {
        infLabel.setText(message);
        this.setVisible(true);
        return textField.getText();
    }

    private void cancelBtnMouseReleased(MouseEvent e) {
        textField.setText("");
        setVisible(false);
        dispose();
    }

    private void confirmBtnMouseReleased(MouseEvent e) {
        setVisible(false);
        dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        textField = new JTextField();
        confirmBtn = new JLabel();
        cancelBtn = new JLabel();
        infLabel = new JLabel();
        textFieldGround = new JLabel();

        //======== this ========
        setTitle("Input Something");
        setIconImage(new ImageIcon("assets/guiimages/icon.jpg").getImage());
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- textField ----
        textField.setBorder(null);
        textField.setFont(new Font("Roboto Condensed", Font.BOLD, 14));
        textField.setMargin(new Insets(2, 12, 2, 12));
        contentPane.add(textField);
        textField.setBounds(10, 30, 275, 30);

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
        confirmBtn.setBounds(5, 65, 140, 40);

        //---- cancelBtn ----
        cancelBtn.setOpaque(true);
        cancelBtn.setBackground(new Color(255, 102, 102));
        cancelBtn.setText("Cancel");
        cancelBtn.setHorizontalAlignment(SwingConstants.CENTER);
        cancelBtn.setForeground(Color.white);
        cancelBtn.setFont(new Font("Roboto Condensed", Font.BOLD, 16));
        cancelBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                cancelBtnMouseReleased(e);
            }
        });
        contentPane.add(cancelBtn);
        cancelBtn.setBounds(150, 65, 140, 40);

        //---- infLabel ----
        infLabel.setText("Input something...");
        infLabel.setFont(new Font("Roboto Light", Font.PLAIN, 14));
        contentPane.add(infLabel);
        infLabel.setBounds(10, 5, 275, 25);

        //---- textFieldGround ----
        textFieldGround.setBackground(Color.white);
        textFieldGround.setOpaque(true);
        contentPane.add(textFieldGround);
        textFieldGround.setBounds(5, 30, 285, 30);

        contentPane.setPreferredSize(new Dimension(295, 110));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTextField textField;
    private JLabel confirmBtn;
    private JLabel cancelBtn;
    private JLabel infLabel;
    private JLabel textFieldGround;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
