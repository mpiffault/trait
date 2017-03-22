package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.geometry.Point;

import java.awt.*;
import java.util.HashSet;

public interface Drawable {
    void draw(Graphics2D g2);

    void drawTemporary(Graphics2D g2);

    void drawHightlighted(Graphics2D g2);

    HashSet<Point> getPointSet();
}
