package tech.gralerfics.controller.comps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class SlidingPaneBk extends JComponent {
    ArrayList<JComponent> contents = new ArrayList<>();
    int w, h;
    int offset = 0;
    int margin = 5;

    public int getWlen() {
        int wlen = margin;
        for (JComponent jc : contents) {
            wlen += jc.getWidth() + margin;
        }
        return wlen;
    }

    public SlidingPaneBk() {
        initComponents();
    }

    private void CompPaneMouseWheelMoved(MouseWheelEvent e) {

    }

    private void thisComponentShown(ComponentEvent e) {
        CompPane.setBounds(margin, margin, w - 2 * margin, h - 2 * margin);
        internalPane.setBounds(0, 0, CompPane.getWidth(), CompPane.getHeight() - margin);
        int wlen = getWlen();
        if (wlen <= internalPane.getWidth()) {
            btnLabel.setVisible(false);
        } else {
            double ratio = 1.0 * internalPane.getWidth() / wlen;
            double prog = 1.0 * offset / (wlen - internalPane.getWidth());
            int labelwidth = (int) (CompPane.getWidth() * ratio);
            int labelx = (int) ((CompPane.getWidth() - labelwidth) * prog);
            btnLabel.setBounds(labelx, CompPane.getHeight() - margin, labelwidth, margin);
            btnLabel.setVisible(true);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        CompPane = new JPanel();
        internalPane = new JPanel();
        btnLabel = new JLabel();

        //======== this ========
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                thisComponentShown(e);
            }
        });
        setLayout(null);

        //======== CompPane ========
        {
            CompPane.setBackground(Color.white);
            CompPane.addMouseWheelListener(e -> CompPaneMouseWheelMoved(e));
            CompPane.setLayout(null);

            //======== internalPane ========
            {
                internalPane.setBackground(Color.white);
                internalPane.setLayout(null);
            }
            CompPane.add(internalPane);
            internalPane.setBounds(0, 0, 620, 280);

            //---- btnLabel ----
            btnLabel.setOpaque(true);
            btnLabel.setBackground(new Color(204, 204, 204));
            CompPane.add(btnLabel);
            btnLabel.setBounds(0, 280, 140, 5);
        }
        add(CompPane);
        CompPane.setBounds(5, 5, 620, 285);

        setPreferredSize(new Dimension(630, 295));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel CompPane;
    private JPanel internalPane;
    private JLabel btnLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
