package fr.mpiffault.trait.geometry;

import java.awt.*;

public interface Intersectable {
    Point[] getIntersection(Intersectable otherIntersectable);

    double ptDist(Point cursorPosition);

    void drawNearest(Graphics2D g2);
}
