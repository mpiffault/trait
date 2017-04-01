package fr.mpiffault.trait.dessin.action;

import fr.mpiffault.trait.dessin.Drawable;
import fr.mpiffault.trait.dessin.DrawableAdapter;
import fr.mpiffault.trait.dessin.Table;
import fr.mpiffault.trait.geometry.Point;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

public class SelectionBox extends DrawableAdapter{
    private final Point startPoint;
    @Getter
    @Setter
    private Point endPoint;

    public SelectionBox(Point startPoint) {
        this.startPoint = startPoint;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Table.FOREGROUND);
        g2.drawRect((int)startPoint.getX(), (int)startPoint.getY(),
                (int)(endPoint.getX() - startPoint.getX()), (int)(endPoint.getY() - startPoint.getY()));
    }

    @Override
    public HashSet<Point> getPointSet() {
        HashSet<Point> pointHashSet = new HashSet<>();
        return pointHashSet;
    }

    @Override
    public void drawNearest(Graphics2D g2) {
        // Irrelevant
    }

    public Rectangle2D getRectangle2D() {
        double x = Math.min(startPoint.getX(), endPoint.getX());
        double y = Math.min(startPoint.getY(), endPoint.getY());
        double width = Math.abs(startPoint.getX() - endPoint.getX());
        double height = Math.abs(startPoint.getY() - endPoint.getY());

        return new Rectangle2D.Double(x, y, width, height);
    }
}
