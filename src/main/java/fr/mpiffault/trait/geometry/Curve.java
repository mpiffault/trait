package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Drawable;
import fr.mpiffault.trait.geometry.utils.PointUtils;
import lombok.Setter;

import java.awt.*;

import static fr.mpiffault.trait.Utils.isDebugMode;

public class Curve implements Drawable {

    @Setter
    private Vertex origin;

    public Curve (Point origin) {
        this.origin = new Vertex(origin);
    }

    public void addPoint(Point point) {
        origin.addToQueue(point);
    }

    @Override
    public void draw(Graphics2D g2) {
        Vertex v1 = origin;
        Vertex v2 = origin.getNext();
        if (v1.isLast()) {
            v1.getSelf().draw(g2);
        } else {
            do {
                double distance = v1.getSelf().distance(v2.getSelf());
                Point ctrl1 = PointUtils.homotheticRotatePointFromOrigin(v1.getBissectPoint(), v1.getSelf(), -(Math.PI / 2), distance / Math.PI);
                Point ctrl2 = PointUtils.homotheticRotatePointFromOrigin(v2.getBissectPoint(), v2.getSelf(), Math.PI / 2, distance / Math.PI);
                new SimpleBezier(v1.getSelf(), ctrl1, v2.getSelf(), ctrl2).draw(g2);

                if (isDebugMode()) {
                    v1.getSelf().draw(g2);
                    ctrl1.draw(g2, Color.CYAN);
                    ctrl2.draw(g2, Color.YELLOW);
                }

                v1 = v2;
                v2 = v1.getNext();
            } while (!v1.isLast());
        }

    }

    @Override
    public void drawHightlighted(Graphics2D g2) {

    }
}
