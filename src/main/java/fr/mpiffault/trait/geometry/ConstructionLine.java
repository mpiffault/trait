package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.dessin.Drawable;
import fr.mpiffault.trait.dessin.Table;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashSet;

import static java.lang.Math.*;
import static java.lang.Math.PI;

public class ConstructionLine extends AbstractLine implements Drawable {
    private double coefficient;
    private double shift;
    private boolean vertical = false;
    private final Table table;
    private Line2D.Double line;

    public ConstructionLine(Point firstPoint, Point secondPoint, Table table) {
        super(firstPoint, secondPoint);
        this.table = table;
        calculateCoefficients();
    }

    public ConstructionLine(double coefficient, double shift, Table table) {
        super(calculateFirstPoint(coefficient, shift), calculateSecondPoint(coefficient, shift));
        this.coefficient = coefficient;
        this.shift = shift;
        this.table = table;
    }

    private static Point calculateFirstPoint(double coefficient, double shift) {
        return new Point(0,shift);
    }

    private static Point calculateSecondPoint(double coefficient, double shift) {
        return new Point(1,coefficient + shift);
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
        drawInternal(g2);
    }

    @Override
    public void drawTemporary(Graphics2D g2) {
        g2.setColor(Table.TEMPORARY);
        drawInternal(g2);
    }

    private void drawInternal(Graphics2D g2) {
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

    private double getPerpendicularAngle() {
        if (!this.vertical) {
            double lineAngle = atan(-coefficient);
            return lineAngle - (PI / 2);
        }
        return 0;
    }

    public ConstructionLine parallelByDistanceOver(double distance) {
        if (!vertical) {
            double perpendicularAngle = getPerpendicularAngle();
            double dY = 1 / (Math.cos(-(PI / 2.0) - perpendicularAngle)) * distance;
            return new ConstructionLine(this.coefficient, (this.shift + dY), this.table);
        }
        double x = this.x1 - distance;
        return new ConstructionLine(new Point(x, 0), new Point(x, 1), table);
    }

    public ConstructionLine parallelByDistanceUnder(double distance) {
        if (!vertical) {
            double perpendicularAngle = getPerpendicularAngle();
            double dY = 1 / (Math.cos(-(PI / 2.0) - perpendicularAngle)) * distance;
            return new ConstructionLine(this.coefficient, this.shift - dY, this.table);
        }
        double x = this.x1 + distance;
        return new ConstructionLine(new Point(x, 0), new Point(x, 1), table);
    }

    @Override
    public void drawNearest(Graphics2D g2) {
        g2.setColor(Table.NEAREST);
        drawInternal(g2);

        Point cursor = table.getCursorPosition();

        if (isCursorOver(cursor, line)) {
            this.parallelByDistanceOver(table.getCurrentValue()).drawTemporary(g2);
        } else {
            this.parallelByDistanceUnder(table.getCurrentValue()).drawTemporary(g2);
        }
    }

    private boolean isCursorOver(Point cursor, Double line) {
        return (line.x2 - line.x1)*(cursor.y - line.y1) > (line.y2 - line.y1)*(cursor.x - line.x1);
    }

    @Override
    public void drawHightlighted(Graphics2D g2) {
//        final float dash[] = {7.0f};
//        BasicStroke basicStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);
//        g2.setStroke(basicStroke);
//        g2.setColor(Table.HIGHTLIGHTED);
//
//        calculateLineToDraw();
//
//        g2.draw(line);
//        g2.setStroke(new BasicStroke());
    }

    @Override
    public HashSet<Point> getPointSet() {
        return new HashSet<>();
    }

    @Override
    public double ptDist(Point cursorPosition) {
        return this.line.ptLineDist(cursorPosition);
    }
}
