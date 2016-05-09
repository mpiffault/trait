package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.geometry.Point;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public interface Selectable extends Drawable {
    boolean isSelectable(Point point);
    void drawSelected(Graphics2D g2);
    boolean isInBox(Rectangle2D finalSelectionBox);
}
