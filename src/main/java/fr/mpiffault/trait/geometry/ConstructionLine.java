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
        double perpendicularAngle = getPerpendicularAngle();

        // double dY = Math.sqrt(1.0D + Math.pow(tan(perpendicularAngle), 2.0D)) * distance;
        double dY = 1 / (Math.cos(-(PI / 2.0) - perpendicularAngle)) * distance;

        if (table.isLogCoeff()) {
            System.out.println("Main line coeff: " + (-this.coefficient) + " -> Angle: " + toDegrees(atan(-coefficient))
                    + "\nPerpendicular angle: " + toDegrees(perpendicularAngle)
                    + "\ndY: " + dY + " -> Shift = " + this.shift + " + " + dY + " = " + (this.shift + dY));
        }
        return new ConstructionLine(this.coefficient, (this.shift + dY), this.table);
    }

    public ConstructionLine parallelByDistanceUnder(double distance) {
        double perpendicularAngle = getPerpendicularAngle();
        double dX = cos(perpendicularAngle) * distance;
        double dY = sin(perpendicularAngle) * distance;
        return new ConstructionLine(this.coefficient, this.shift - dY, this.table);
    }

    @Override
    public void drawNearest(Graphics2D g2) {
        g2.setColor(Table.NEAREST);
        final float dash[] = {7.0f};
        BasicStroke basicStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);
        g2.setStroke(basicStroke);

        calculateLineToDraw();

        g2.draw(line);
        g2.setStroke(new BasicStroke());

        this.parallelByDistanceOver(1000.0).draw(g2);
        //this.parallelByDistanceUnder(10.0).draw(g2);
    }

    @Override
    public void drawHightlighted(Graphics2D g2) {
        final float dash[] = {7.0f};
        BasicStroke basicStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);
        g2.setStroke(basicStroke);
        g2.setColor(Table.HIGHTLIGHTED);

        calculateLineToDraw();

        g2.draw(line);
        g2.setStroke(new BasicStroke());
    }

    @Override
    public HashSet<Point> getPointSet() {
        HashSet<Point> pointHashSet = new HashSet<Point>();
        return pointHashSet;
    }

    @Override
    public double ptDist(Point cursorPosition) {
        return this.line.ptLineDist(cursorPosition);
    }
}
