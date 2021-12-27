package tech.gralerfics.controller.comps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class SlidingPane extends JComponent {
    public ArrayList<JComponent> contents = new ArrayList<>();
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

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    public SlidingPane(int width, int height) {
        this.w = width;
        this.h = height;
    }

    private void CompPaneMouseWheelMoved(MouseWheelEvent e) {
        int rot = e.getWheelRotation();
        offset += rot * 50;
        adjustComps();
    }

    public void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        CompPane = new JPanel();
        internalPane = new JPanel();
        btnLabel = new JLabel();

        //======== this ========
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

                //======== Contents ========
                int wl = 0;
                for (JComponent jc : contents) {
                    wl += margin;
                    jc.setBounds(wl, margin, jc.getWidth(), jc.getHeight());
                    wl += jc.getWidth();
                    jc.setVisible(true);
                    internalPane.add(jc);
                }
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

        adjustComps();
    }

    private void adjustComps() {
        CompPane.setBounds(margin, margin, w - margin, h - margin);
        internalPane.setBounds(0, 0, CompPane.getWidth(), CompPane.getHeight() - margin);
        int wlen = getWlen();
        if (offset > wlen - internalPane.getWidth()) offset = wlen - internalPane.getWidth();
        if (offset < 0) offset = 0;
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
        internalPane.setBounds(-offset, 0, getWlen(), CompPane.getHeight() - margin);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel CompPane;
    private JPanel internalPane;
    private JLabel btnLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
