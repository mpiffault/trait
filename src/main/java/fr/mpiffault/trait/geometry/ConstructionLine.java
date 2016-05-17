package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Drawable;
import fr.mpiffault.trait.dessin.Table;

import java.awt.*;
import java.awt.geom.Line2D;

public class ConstructionLine extends AbstractLine implements Drawable, Intersectable {
    private double coefficient;
    private double shift;
    private boolean vertical = false;
    private Table table;
    private Line2D.Double line;

    public ConstructionLine(Point firstPoint, Point secondPoint, Table table) {
        super(firstPoint, secondPoint);
        this.table = table;
        calculateCoefficients();
    }

    public void setSecondPoint(Point point) {
        this.x2 = point.getX();
        this.y2 = point.getY();
        calculateCoefficients();
    }

    private void calculateCoefficients() {
        if ((this.x2 - this.x1) != 0D) {
            vertical = false;
            coefficient = (this.y2 - this.y1) / (this.x2 - this.x1);
            shift = this.y1 - (coefficient * this.x1);
        } else {
            vertical = true;
            coefficient = this.x1;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Table.FOREGROUND);
        final float dash[] = {7.0f};
        BasicStroke basicStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);
        g2.setStroke(basicStroke);

        calculateLineToDraw();

        g2.draw(line);
        g2.setStroke(new BasicStroke());
    }

    private void calculateLineToDraw() {
        double xa = 0D;
        double ya = coefficient * xa + shift;
        double xb = table.getWidth();
        double yb = coefficient * xb + shift;

        if (vertical) {
            xa = coefficient;
            ya = 0;
            xb = coefficient;
            yb = table.getWidth();
        } else {
            if (ya < 0D) {
                ya = 0D;
                xa = (ya - shift) / coefficient;
            }
            if (yb < 0D) {
                yb = 0D;
                xb = (yb - shift) / coefficient;
            }
        }

        Point pa = new Point(xa, ya);
        Point pb = new Point(xb, yb);

        line = new Double(pa,pb);
    }

    @Override
    public void drawNearest(Graphics2D g2) {
        g2.setColor(Table.FOREGROUND);
        final float dash[] = {7.0f};
        BasicStroke basicStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);
        g2.setStroke(basicStroke);

        calculateLineToDraw();

        g2.draw(line);
        g2.setStroke(new BasicStroke());
    }

    @Override
    public void drawHightlighted(Graphics2D g2) {

    }

    @Override
    public double ptDist(Point cursorPosition) {
        return this.line.ptLineDist(cursorPosition);
    }
}
