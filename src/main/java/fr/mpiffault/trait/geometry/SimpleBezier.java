package fr.mpiffault.trait.geometry;


import fr.mpiffault.trait.dessin.Drawable;
import fr.mpiffault.trait.dessin.Selectable;
import fr.mpiffault.trait.dessin.Table;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;


public class SimpleBezier implements Drawable, Selectable{

    @Getter
    @Setter
    private Point pStart;
    @Getter
    @Setter
    private Point pCtrlStart;
    @Getter
    @Setter
    private Point pEnd;
    @Getter
    @Setter
    private Point pCtrlEnd;

    public SimpleBezier(Point pStart,Point pCtrlStart,Point pEnd,Point pCtrlEnd) {
        this.pStart = pStart;
        this.pCtrlStart = pCtrlStart;
        this.pEnd = pEnd;
        this.pCtrlEnd = pCtrlEnd;
    }

    public SimpleBezier() {

    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Table.FOREGROUND);
        if (pStart != null && pCtrlStart != null && pEnd != null && pCtrlEnd != null) {
            g2.draw(new CubicCurve2D.Double(
                    pStart.getX(),
                    pStart.getY(),
                    pCtrlStart.getX(),
                    pCtrlStart.getY(),
                    pCtrlEnd.getX(),
                    pCtrlEnd.getY(),
                    pEnd.getX(),
                    pEnd.getY()
            ));
        }
    }

    @Override
    public void drawTemporary(Graphics2D g2) {

    }

    @Override
    public void drawHightlighted(Graphics2D g2) {

    }

    @Override
    public HashSet<Point> getPointSet() {
        HashSet<Point> pointHashSet = new HashSet<>();
        return pointHashSet;
    }

    @Override
    public void drawNearest(Graphics2D g2) {

    }

    @Override
    public boolean isSelectable(Point point) {
        return false;
    }

    @Override
    public void drawSelected(Graphics2D g2) {

    }

    @Override
    public boolean isInBox(Rectangle2D finalSelectionBox) {
        return false;
    }
}
