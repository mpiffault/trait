package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Drawable;
import fr.mpiffault.trait.dessin.Selectable;
import fr.mpiffault.trait.dessin.Table;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

public class Point extends Point2D.Double implements Drawable, Selectable {

    private double size = 4.0;
    private double halfSize = 2.0;

    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0.0,0.0);
    }

    public Point(Point2D point2D) {
        this(point2D.getX(), point2D.getY());
        this.x = point2D.getX();
        this.y = point2D.getY();
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Table.FOREGROUND);
        this.drawPoint(g2);
    }

    public void draw(Graphics2D g2, Color color) {
        g2.setColor(color);
        this.drawPoint(g2);
    }

    @Override
    public void drawHightlighted(Graphics2D g2) {
        BasicStroke basicStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f);
        g2.setStroke(basicStroke);
        g2.setColor(Color.RED);
        this.drawPoint(g2);
        g2.setStroke(new BasicStroke());
    }

    @Override
    public HashSet<Point> getPointSet() {
        HashSet<Point> hashSet = new HashSet<>();
        hashSet.add(this);
        return hashSet;
    }

    @Override
    public void drawSelected(Graphics2D g2) {
        g2.setColor(Table.SELECTED);
        this.drawPoint(g2);
    }

    @Override
    public boolean isInBox(Rectangle2D finalSelectionBox) {
        System.out.println("isInBox");
        return finalSelectionBox.contains(this);
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
