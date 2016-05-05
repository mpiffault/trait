package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.geometry.Point;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;

import java.awt.*;

public interface Selectable extends Drawable {
    boolean isSelectable(Point point);
    void drawSelected(Graphics2D g2);
}
