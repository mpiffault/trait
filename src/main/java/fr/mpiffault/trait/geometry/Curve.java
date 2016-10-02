package fr.mpiffault.trait.geometry;

import java.awt.*;
import java.util.ArrayList;

import fr.mpiffault.trait.dessin.Drawable;
import fr.mpiffault.trait.dessin.Table;
import fr.mpiffault.trait.geometry.utils.PointUtils;
import lombok.Setter;

public class Curve implements Drawable {

    @Setter
    private Vertex origin;

    private int roundness;

    public Curve (Point origin) {
        this.origin = new Vertex(origin);
    }

    public void addPoint(Point point) {
        origin.addToQueue(point);
    }

    private boolean isFirstPoint(Point p) {
        return p == origin.getSelf();
    }

    private boolean isLastPoint(Point p) {
        return p == origin.getLast().getSelf();
    }

    @Override
    public void draw(Graphics2D g2) {
        Vertex v1 = origin;
        Vertex v2 = origin.getNext();
        if (v1.isLast()) {
            v1.getSelf().draw(g2);
        } else {
            do {
                Point ctrl1 = PointUtils.rotatePointFromOrigin(v1.getBissectPoint(), v1.getSelf(), -(Math.PI / 2));
                Point ctrl2 = PointUtils.rotatePointFromOrigin(v2.getBissectPoint(), v2.getSelf(), Math.PI / 2);
                new SimpleBezier(v1.getSelf(), ctrl1, v2.getSelf(), ctrl2).draw(g2);
                ctrl1.draw(g2, Color.CYAN);
                ctrl2.draw(g2, Color.YELLOW);
                v1.getBissectPoint().draw(g2, Color.RED);
                v2.getBissectPoint().draw(g2, Color.ORANGE);
                v1 = v2;
                v2 = v1.getNext();
            } while (!v1.isLast());
        }

    }

    @Override
    public void drawHightlighted(Graphics2D g2) {

    }
}
