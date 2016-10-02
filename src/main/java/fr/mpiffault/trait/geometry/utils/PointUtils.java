package fr.mpiffault.trait.geometry.utils;

import fr.mpiffault.trait.geometry.Point;

import static java.lang.Math.*;

public class PointUtils {

    public static Point getBissectPoint(Point p1, Point vertex, Point p2) {
        Point p1Normalized = normalizeFromPoint(vertex, p1);
        Point p2Normalized = normalizeFromPoint(vertex, p2);

        double bissectAngle = (getAngle(p2Normalized) + getAngle(p1Normalized)) / 2;

        if (getAngle(p2Normalized) <= getAngle(p1Normalized)) {
            bissectAngle += (Math.PI * Math.signum(bissectAngle));
        }



        double p1Degree = (getAngle(p1Normalized) / Math.PI) * 180.0;
        double p2Degree = (getAngle(p2Normalized) / Math.PI) * 180.0;
        double degreeAngle = (bissectAngle / Math.PI) * 180.0;

        System.out.println("Angle p1 = " + p1Degree + "°");
        System.out.println("Angle p2 = " + p2Degree + "°");
        System.out.println("BissectAngle = " + degreeAngle + "°");

        return new Point(vertex.x + cos(bissectAngle)*50, vertex.y - sin(bissectAngle)*50);
    }

    public static double getAngle(Point p1Normalized) {
        double signY = signum(p1Normalized.y) >= 0.0 ? -1.0 : 1.0;
        return acos(p1Normalized.x) * signY;
    }

    public static Point normalizeFromPoint(Point center, Point toNormalize) {
        double distance = center.distance(toNormalize);
        return new Point((toNormalize.x - center.x) / distance,
                (toNormalize.y - center.y) / distance);
    }

    public static Point rotatePointFromOrigin (Point point, Point origin, double angle) {
        Point originatedPoint = new Point(point.x - origin.x, point.y - origin.y);

        return new Point (origin.x + (originatedPoint.x * cos(angle) - originatedPoint.y * sin(angle))
        ,origin.y + (originatedPoint.x * sin(angle) + originatedPoint.y * cos(angle)));
    }
}
