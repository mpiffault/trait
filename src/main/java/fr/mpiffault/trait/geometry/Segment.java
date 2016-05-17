package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Drawable;
import fr.mpiffault.trait.dessin.Selectable;
import fr.mpiffault.trait.dessin.Table;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Segment extends AbstractLine implements Drawable, Selectable, Intersectable {

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
    public Point[] getIntersection(Intersectable other) {
        Point[] intersectionPoints = null;


        if (other instanceof AbstractLine) {
            AbstractLine otherLine = (AbstractLine) other;
            intersectionPoints = new Point[1];

            Point thisMiddlePoint = this.getMiddle();
            Point otherMiddlePoint = otherLine.getMiddle();


            double otherCoeff, thisCoeff;
            double determinant = (-otherMiddlePoint.getX() * thisMiddlePoint.getY() + thisMiddlePoint.getX() * otherMiddlePoint.getY());

            if (determinant != 0) {
                double otherDenominator = (-thisMiddlePoint.getY() * (this.x1 - otherLine.x1) + thisMiddlePoint.getX() * (this.y1 - otherLine.y1));
                otherCoeff = otherDenominator / determinant;
                if (other instanceof Segment) {
                    if (otherCoeff >= 0 && otherCoeff <= 1) {
                        double thisDenominator = (getThisIntersectionDenominator(otherLine, otherMiddlePoint));
                        thisCoeff = thisDenominator / determinant;

                        if (thisCoeff >= 0 && thisCoeff <= 1) {
                            intersectionPoints[0] = getIntersectionPoint(thisMiddlePoint, thisCoeff);
                        }
                    }
                } else {
                    double thisDenominator = getThisIntersectionDenominator(otherLine, otherMiddlePoint);
                    thisCoeff = thisDenominator / determinant;
                    intersectionPoints[0] = getIntersectionPoint(thisMiddlePoint, thisCoeff);
                }
            }
        }

        return intersectionPoints;
    }

    private double getThisIntersectionDenominator(AbstractLine otherLine, Point otherMiddlePoint) {
        return otherMiddlePoint.getX() * (this.y1 - otherLine.y1) - otherMiddlePoint.getY() * (this.x1 - otherLine.x1);
    }

    private Point getIntersectionPoint(Point thisMiddlePoint, double thisCoeff) {
        return new Point(this.x1 + (thisCoeff * thisMiddlePoint.getX()), this.y1 + (thisCoeff * thisMiddlePoint.getY()));
    }

    @Override
    public double ptDist(Point cursorPosition) {
        return this.ptSegDist(cursorPosition);
    }
}
