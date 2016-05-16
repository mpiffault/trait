package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Selectable;
import fr.mpiffault.trait.dessin.Table;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Segment extends Line2D.Double implements Drawable, Selectable {

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

    protected Point getMiddle() {
        return new Point(this.x2 - this.x1, this.y2 - this.y1);
    }

    public Point getIntersection(Segment other) {

        Point thisMiddlePoint = this.getMiddle();
        Point otherMiddlePoint = other.getMiddle();

        Point intersectionPoint = null;

        double otherCoeff, thisCoeff;
        double determinant = (-otherMiddlePoint.getX() * thisMiddlePoint.getY() + thisMiddlePoint.getX() * otherMiddlePoint.getY());

        if (determinant != 0) {
            double otherDenominator = (-thisMiddlePoint.getY() * (this.x1 - other.x1) + thisMiddlePoint.getX() * (this.y1 - other.y1));
            otherCoeff = otherDenominator / determinant;

            if (otherCoeff >= 0 && otherCoeff <= 1) {
                double thisDenominator = (otherMiddlePoint.getX() * (this.y1 - other.y1) - otherMiddlePoint.getY() * (this.x1 - other.x1));
                thisCoeff = thisDenominator / determinant;

                if (thisCoeff >= 0 && thisCoeff <= 1) {
                    intersectionPoint = new Point(this.x1 + (thisCoeff * thisMiddlePoint.getX()), this.y1 + (thisCoeff * thisMiddlePoint.getY()));
                }
            }
        }

        return intersectionPoint;
    }

    public Point getIntersection(ConstructionLine constructionLine) {
        return null;
    }
}
