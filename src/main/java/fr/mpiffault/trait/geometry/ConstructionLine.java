package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Table;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.Line2D;

public class ConstructionLine implements Drawable{
    @Getter
    private final Point firstPoint;
    @Getter
    private final Point secondPoint;
    private double coefficient;
    private double shift;
    private boolean vertical = false;
    private Table table;

    public ConstructionLine(Point firstPoint, Point secondPoint, Table table) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.table = table;
        if ((secondPoint.getX() - firstPoint.getX()) != 0D) {
            coefficient = (secondPoint.getY() - firstPoint.getY()) / (secondPoint.getX() - firstPoint.getX());
            shift = firstPoint.getY() - (coefficient * firstPoint.getX());
        } else {
            vertical = true;
            coefficient = firstPoint.getX();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Table.FOREGROUND);
        final float dash[] = {7.0f};
        BasicStroke basicStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);
        g2.setStroke(basicStroke);
        double xa = 0D;
        double ya = coefficient * xa + shift;
        double xb = table.getWidth();
        double yb = coefficient * xb + shift;

        if (!vertical) {
            if (ya < 0D) {
                ya = 0D;
                xa = (ya - shift) / coefficient;
            }
            if (yb < 0D) {
                yb = 0D;
                xb = (yb - shift) / coefficient;
            }
        } else {
            xa = coefficient;
            ya = 0;
            xb = coefficient;
            yb = table.getWidth();
        }

        Point pa = new Point(xa, ya);
        Point pb = new Point(xb, yb);

        Line2D.Double line = new Line2D.Double(pa,pb);

        g2.draw(line);
        g2.setStroke(new BasicStroke());
    }
}
