package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Selectable;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;

import java.awt.*;
import java.awt.geom.Line2D;

public class Segment extends Line2D.Double implements Drawable, Selectable {

    private Color color = Color.BLACK;

    public Segment(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(this.color);
        g2.draw(this);
        new Point(super.getP1()).draw(g2);
        new Point(super.getP2()).draw(g2);
    }

    @Override
    public void drawSelected(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.draw(this);
        new Point(super.getP1()).draw(g2);
        new Point(super.getP2()).draw(g2);
    }

    @Override
    public boolean isSelectable(Point point) {
        return this.ptLineDist(point) < 3.0D;
    }

    public void setEndPoint(Point endPoint) {
        super.x2 = endPoint.getX();
        super.y2 = endPoint.getY();
    }
}
