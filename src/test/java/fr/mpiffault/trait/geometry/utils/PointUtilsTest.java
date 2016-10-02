package fr.mpiffault.trait.geometry.utils;

import static fr.mpiffault.trait.geometry.utils.PointUtils.*;
import static java.lang.Math.PI;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.mpiffault.trait.geometry.Point;

public class PointUtilsTest {
    private static double EPSILON = 0.00025;

    @Test
    public void normalizeFromPointTest() throws Exception {
        Point p0 = new Point(1.0, 1.0);
        Point p1 = new Point (3.0, 3.0);

        Point p1Normalized = normalizeFromPoint(p0, p1);

        assertEquals(Math.cos(PI / 4), p1Normalized.x, EPSILON);
        assertEquals(Math.cos(PI / 4), p1Normalized.y, EPSILON);

        p0 = new Point(0.0, -1.0);
        p1 = new Point (0.0, 3.0);

        p1Normalized = normalizeFromPoint(p0, p1);

        assertEquals(0.0, p1Normalized.x, EPSILON);
        assertEquals(1.0, p1Normalized.y, EPSILON);

    }

    @Test
    public void getAngleTest() throws Exception {
        Point pNormalized = new Point(0.0, -1.0);
        assertEquals(- Math.PI / 2, getAngle(pNormalized), EPSILON);

        pNormalized = new Point(-1.0, 0.0);
        assertEquals(Math.PI, getAngle(pNormalized), EPSILON);

        pNormalized = normalizeFromPoint(new Point(0.0, 0.0),new Point(1.0, 1.0));
        assertEquals(Math.PI / 4.0, getAngle(pNormalized), EPSILON);
    }

    @Test
    public void getBissectPointTest() throws Exception {
        Point p1 = new Point(1,0);
        Point vertex = new Point(0,0);
        Point p2 = new Point(0,1);

        Point bissectPoint = getBissectPoint(p1, vertex, p2);

        assertEquals(Math.cos(PI / 4), bissectPoint.x, EPSILON);
        assertEquals(Math.cos(PI / 4), bissectPoint.y, EPSILON);

        p1 = new Point(0,1);
        vertex = new Point(0,0);
        p2 = new Point(1,0);

        bissectPoint = getBissectPoint(p1, vertex, p2);

        assertEquals(-Math.cos(3*PI / 4), bissectPoint.x, EPSILON);
        assertEquals(-Math.sin(3*PI / 4), bissectPoint.y, EPSILON);
    }

    @Test
    public void rotatePointFromOriginTest() {
        Point p1, origin, rotated;

        p1 =  new Point(1,0);
        origin = new Point(0,0);

        rotated = rotatePointFromOrigin(p1, origin, Math.PI / 2);

        assertEquals(0, rotated.x, EPSILON);
        assertEquals(1, rotated.y, EPSILON);

        p1 =  new Point(1,1);
        origin = new Point(0,0);

        rotated = rotatePointFromOrigin(p1, origin, Math.PI / 2);

        assertEquals(-1, rotated.x, EPSILON);
        assertEquals(1, rotated.y, EPSILON);

        p1 =  new Point(1 + (Math.sqrt(2)/2),Math.sqrt(2)/2);
        origin = new Point(1,0);

        rotated = rotatePointFromOrigin(p1, origin, 3 * (Math.PI / 4));

        assertEquals(0, rotated.x, EPSILON);
        assertEquals(0, rotated.y, EPSILON);
    }

}