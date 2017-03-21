package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Drawable;
import fr.mpiffault.trait.dessin.Selectable;
import fr.mpiffault.trait.dessin.Table;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

public class Segment extends AbstractLine implements Drawable, Selectable {

    public Segment(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Table.FOREGROUND);
        g2.draw(this);
    }

    @Override
    public void drawSelected(Graphics2D g2) {
        g2.setColor(Table.SELECTED);
        g2.draw(this);
    }

    @Override
    public void drawNearest(Graphics2D g2) {
        BasicStroke basicStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f);
        g2.setStroke(basicStroke);
        g2.setColor(Table.NEAREST);
        g2.draw(this);
        g2.setStroke(new BasicStroke());
    }

    @Override
    public void drawHightlighted(Graphics2D g2) {
        BasicStroke basicStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f);
        g2.setStroke(basicStroke);
        g2.setColor(Table.HIGHTLIGHTED);
        g2.draw(this);
        g2.setStroke(new BasicStroke());
    }

    @Override
    public HashSet<Point> getPointSet() {
        HashSet<Point> pointsHashSet = new HashSet<>();
        pointsHashSet.add(getP1());
        pointsHashSet.add(getP2());
        return pointsHashSet;
    }

    @Override
    public boolean isInBox(Rectangle2D finalSelectionBox) {
        return finalSelectionBox.getX() <= this.getBounds2D().getX()
                && (finalSelectionBox.getX() + finalSelectionBox.getWidth()) >= (this.getBounds2D().getX() + this.getBounds2D().getWidth())
                && finalSelectionBox.getY() <= this.getBounds2D().getY()
                && (finalSelectionBox.getY() + finalSelectionBox.getHeight()) >= (this.getBounds2D().getY() + this.getBounds2D().getHeight());
    }

    @Override
    public boolean isSelectable(Point point) {
        return this.ptSegDist(point) < 3.0D;

    }

    public void setEndPoint(Point endPoint) {
        super.x2 = endPoint.getX();
        super.y2 = endPoint.getY();
    }

    @Override
    public double ptDist(Point cursorPosition) {
        return this.ptSegDist(cursorPosition);
    }

    public double getLength() {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
}
