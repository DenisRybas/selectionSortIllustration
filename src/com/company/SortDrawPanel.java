package com.company;


import javax.swing.*;
import java.awt.*;

public class SortDrawPanel extends JPanel implements SortIllustrationWindow.StateViewer {


    private SortState state = null;

    @Override
    public void show(SortState ss) {
        state = ss;
        if (state != null) {
            Dimension d = new Dimension(state.getArr().length * 50 + 50, 50);
            this.setMinimumSize(d);
            this.setSize(d);
            this.setPreferredSize(d);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (state == null) return;
        Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        SortDrawItems.drawState(state, gr, new Point(30, 26));
    }

}
