package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Drawable;

public interface Intersectable extends Drawable {
    Point[] getIntersection(Intersectable otherIntersectable);

    double ptDist(Point cursorPosition);
}
