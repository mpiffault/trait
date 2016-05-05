package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Selectable;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Point extends Point2D.Double implements Drawable, Selectable {

    @Getter
    private double x, y;

    private Color color = Color.BLACK;
    private double size = 4.0;
    private double halfSize = 2.0;

    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(this.color);
        this.drawPoint(g2);
    }

    @Override
    public void drawSelected(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        this.drawPoint(g2);
    }

    private void drawPoint(Graphics2D g2) {
        Rectangle2D.Double gPoint = new Rectangle2D.Double(this.x - halfSize, this.y - halfSize, size, size);
        g2.draw(gPoint);
    }

    @Override
    public boolean isSelectable(Point point) {
        return this.distanceSq(point.getX(), point.getY()) < 10.0D;
    }

    public void setSize(double size) {
        this.size = size;
        this.halfSize = this.size /2.0;
    }
}