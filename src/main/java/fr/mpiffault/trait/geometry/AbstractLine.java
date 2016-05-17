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
}
