package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Selectable;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Line2D;

public class Segment extends Line2D.Double implements Drawable, Selectable {

    @Getter
    @Setter
    private Point from, to;
    private Color color = Color.BLACK;

    public Segment(Point from, Point to) {
        super(from, to);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(this.color);
        g2.draw(this);
        from.draw(g2);
        to.draw(g2);
    }

    @Override
    public void drawSelected(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.draw(this);
        from.drawSelected(g2);
        to.drawSelected(g2);
    }

    @Override
    public boolean isSelectable(Point point) {
        return this.ptLineDist(point) < 3.0D;
    }
}
