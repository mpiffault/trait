package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Drawable;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.HashSet;

public class Circle extends Ellipse2D.Double implements Drawable {

    @Getter
    private Point center;

    @Getter
    private double radius;

    Circle(double xCenter, double yCenter, double radius) {
        center.x = xCenter;
        center.y = yCenter;

        setRadius(radius);
    }

    Circle(double xCenter, double yCenter) {
        this(xCenter, yCenter, 0);
    }

    public void setRadius(double radius) {
        this.radius = radius;
        super.x = center.x - radius;
        super.y = center.y - radius;
        super.height = radius * 2.0;
        super.width = radius * 2.0;
    }

    public void setRadiusByPoint(Point point) {
        setRadius(center.distance(point));
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.draw(this);
    }

    @Override
    public void drawTemporary(Graphics2D g2) {
        g2.draw(this);
    }

    @Override
    public void drawHightlighted(Graphics2D g2) {
        g2.draw(this);
    }

    @Override
    public HashSet<Point> getPointSet() {
        HashSet<Point> set = new HashSet<>();
        set.add(center);
        return set;
    }

    @Override
    public void drawNearest(Graphics2D g2) {

    }
}
