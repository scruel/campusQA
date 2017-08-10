package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Scruel on 2016/6/23.
 * Personal github - https://github.com/scruel
 */
public class MoveEveryWhereMouseListener extends MouseAdapter {
    private int x;
    private int y;
    private Container container;

    public MoveEveryWhereMouseListener(Container container) {
        this.container = container;
    }

    @Override public void mousePressed(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    @Override public void mouseDragged(MouseEvent e) {
        int moveX = e.getX() - this.x;
        int moveY = e.getY() - this.y;
        Point p = container.getLocation();
        container.setLocation(p.x + moveX, p.y + moveY);
    }
}
