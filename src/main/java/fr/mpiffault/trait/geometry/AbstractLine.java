package fr.mpiffault.trait.geometry;

import java.awt.geom.Line2D;

public abstract class AbstractLine extends Line2D.Double implements Intersectable {
    public AbstractLine(Point firstPoint, Point secondPoint) {
        super.x1 = firstPoint.getX();
        super.y1 = firstPoint.getY();
        super.x2 = secondPoint.getX();
        super.y2 = secondPoint.getY();
    }

    protected Point getMiddle() {
        return new Point(this.x2 - this.x1, this.y2 - this.y1);
    }

    @Override
    public Point[] getIntersection(Intersectable other) {
        Point[] intersectionPoints = null;


        if (other instanceof AbstractLine) {
            AbstractLine otherLine = (AbstractLine) other;

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
                            intersectionPoints = new Point[1];
                            intersectionPoints[0] = getIntersectionPoint(thisMiddlePoint, thisCoeff);
                        }
                    }
                } else {
                    double thisDenominator = getThisIntersectionDenominator(otherLine, otherMiddlePoint);
                    thisCoeff = thisDenominator / determinant;
                    intersectionPoints = new Point[1];
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

    public Point getP1() {
        return new Point(super.x1, super.y1);
    }

    public Point getP2() {
        return new Point(super.x2, super.y2);
    }
}
