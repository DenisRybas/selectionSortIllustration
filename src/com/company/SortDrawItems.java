package com.company;


import java.awt.*;

public class SortDrawItems {
    private static Font font = new Font(Font.SERIF, Font.PLAIN, 20);

    public static void drawItem(int value, Graphics2D g, Point position, Color color, int size) {
        g.setColor(color);
        g.fillOval((int) (position.getX() - size / 2), (int) (position.getY() - size / 2), size, size);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawOval((int) (position.getX() - size / 2), (int) (position.getY() - size / 2), size, size);
        g.setFont(font);
        String val = String.valueOf(value);
        int w = g.getFontMetrics().stringWidth(val);
        g.drawString(val, (int) (position.getX() - w / 2), (int) (position.getY() + 7));
    }

    private static final int STEP = 50;

    public static void drawState(SortState ss, Graphics2D g, Point start) {
        int[] arr = ss.getArr();
        for (int i = 0; i < arr.length; i++) {
            Point p = new Point((int) (start.getX() + i * STEP), (int) (start.getY()));
            Color c = Color.WHITE;
            int size = 40;

            if (ss.getType() != SortState.Type.StartedAndFinished) {
                if (i < ss.getI()) {
                    c = Color.GRAY;
                }
                if (i == ss.getMin() && i != ss.getMin() + 1) {
                    c = Color.MAGENTA;
                    size = 50;
                }
                if (i == ss.getI()) {
                    c = Color.YELLOW;
                    size = 50;
                }
                if (i == ss.getJ()) {
                    if (ss.getType() == SortState.Type.Compare) {
                        c = Color.YELLOW;
                        size = 50;
                    } else if (ss.getType() == SortState.Type.Change) {
                        c = Color.RED;
                        size = 50;
                    } else if (ss.getType() == SortState.Type.FindNew && i != ss.getPreviousMin() + 1) {
                        c = Color.MAGENTA;
                        size = 50;
                    }
                }
            }
            drawItem(arr[i], g, p, c, size);
        }
    }
}
