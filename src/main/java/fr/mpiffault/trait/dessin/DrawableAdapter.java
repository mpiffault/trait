package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.geometry.Point;

import java.awt.*;
import java.util.HashSet;

public class DrawableAdapter implements Drawable {
    @Override
    public void draw(Graphics2D g2) {

    }

    @Override
    public void drawTemporary(Graphics2D g2) {

    }

    @Override
    public void drawHightlighted(Graphics2D g2) {

    }

    @Override
    public HashSet<Point> getPointSet() {
        return null;
    }

    @Override
    public void drawNearest(Graphics2D g2) {

    }
}
