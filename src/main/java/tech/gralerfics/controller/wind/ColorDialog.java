package tech.gralerfics.controller.wind;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.*;

public class ColorDialog extends JDialog {
    Color result;

    public ColorDialog(Window owner) {
        super(owner);
        this.setModal(true);
        initComponents();
    }

    public Color show(String message, Color initc) {
        result = initc;
        reds.setValue((int) (initc.getRed() / 2.55));
        greens.setValue((int) (initc.getGreen() / 2.55));
        blues.setValue((int) (initc.getBlue() / 2.55));
        infLabel.setText(message);
        this.setVisible(true);
        return result;
    }

    private void change() {
        int r = (int) Math.floor(2.55 * reds.getValue());
        int g = (int) Math.floor(2.55 * greens.getValue());
        int b = (int) Math.floor(2.55 * blues.getValue());
        red.setText("" + r);
        green.setText("" + g);
        blue.setText("" + b);
        result = new Color(r, g, b);
        finalColor.setBackground(result);
    }

    private void confirmBtnMouseReleased(MouseEvent e) {
        setVisible(false);
        dispose();
    }

    private void redsStateChanged(ChangeEvent e) {
        change();
    }

    private void greensStateChanged(ChangeEvent e) {
        change();
    }

    private void bluesStateChanged(ChangeEvent e) {
        change();
    }

    private void thisWindowClosing(WindowEvent e) {
        result = null;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        confirmBtn = new JLabel();
        infLabel = new JLabel();
        reds = new JSlider();
        redlab = new JLabel();
        red = new JLabel();
        green = new JLabel();
        greens = new JSlider();
        greenlab = new JLabel();
        blue = new JLabel();
        blues = new JSlider();
        bluelab = new JLabel();
        finalColor = new JLabel();

        //======== this ========
        setTitle("Choose your color");
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
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
        confirmBtn.setBounds(5, 130, 210, 40);

        //---- infLabel ----
        infLabel.setText("Input something...");
        infLabel.setFont(new Font("Roboto Light", Font.PLAIN, 14));
        contentPane.add(infLabel);
        infLabel.setBounds(10, 5, 275, 25);

        //---- reds ----
        reds.setValue(80);
        reds.addChangeListener(e -> redsStateChanged(e));
        contentPane.add(reds);
        reds.setBounds(55, 35, 155, 25);

        //---- redlab ----
        redlab.setText("Red");
        redlab.setHorizontalAlignment(SwingConstants.CENTER);
        redlab.setFont(new Font("Roboto Light", Font.ITALIC, 14));
        contentPane.add(redlab);
        redlab.setBounds(10, 35, 45, 25);

        //---- red ----
        red.setText("204");
        red.setHorizontalAlignment(SwingConstants.CENTER);
        red.setFont(new Font("Roboto Light", Font.ITALIC, 14));
        contentPane.add(red);
        red.setBounds(210, 35, 45, 25);

        //---- green ----
        green.setText("204");
        green.setHorizontalAlignment(SwingConstants.CENTER);
        green.setFont(new Font("Roboto Light", Font.ITALIC, 14));
        contentPane.add(green);
        green.setBounds(210, 65, 45, 25);

        //---- greens ----
        greens.setValue(80);
        greens.addChangeListener(e -> greensStateChanged(e));
        contentPane.add(greens);
        greens.setBounds(55, 65, 155, 25);

        //---- greenlab ----
        greenlab.setText("Green");
        greenlab.setHorizontalAlignment(SwingConstants.CENTER);
        greenlab.setFont(new Font("Roboto Light", Font.ITALIC, 14));
        contentPane.add(greenlab);
        greenlab.setBounds(10, 65, 45, 25);

        //---- blue ----
        blue.setText("204");
        blue.setHorizontalAlignment(SwingConstants.CENTER);
        blue.setFont(new Font("Roboto Light", Font.ITALIC, 14));
        contentPane.add(blue);
        blue.setBounds(210, 95, 45, 25);

        //---- blues ----
        blues.setValue(80);
        blues.addChangeListener(e -> bluesStateChanged(e));
        contentPane.add(blues);
        blues.setBounds(55, 95, 155, 25);

        //---- bluelab ----
        bluelab.setText("Blue");
        bluelab.setHorizontalAlignment(SwingConstants.CENTER);
        bluelab.setFont(new Font("Roboto Light", Font.ITALIC, 14));
        contentPane.add(bluelab);
        bluelab.setBounds(10, 95, 45, 25);

        //---- finalColor ----
        finalColor.setOpaque(true);
        finalColor.setBackground(new Color(204, 204, 204));
        contentPane.add(finalColor);
        finalColor.setBounds(220, 130, 40, 40);

        contentPane.setPreferredSize(new Dimension(265, 175));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel confirmBtn;
    private JLabel infLabel;
    private JSlider reds;
    private JLabel redlab;
    private JLabel red;
    private JLabel green;
    private JSlider greens;
    private JLabel greenlab;
    private JLabel blue;
    private JSlider blues;
    private JLabel bluelab;
    private JLabel finalColor;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
