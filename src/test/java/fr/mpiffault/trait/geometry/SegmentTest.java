package fr.mpiffault.trait.geometry;

import org.junit.Assert;
import org.junit.Test;

public class SegmentTest {

    @Test
    public void findIntersection() {
        Segment s1 = new Segment(new Point(0,0), new Point(5,5));
        Segment s2 = new Segment(new Point(0,5), new Point(5,0));

        Point intersection = s1.getIntersection(s2);

        Assert.assertEquals(2.5D, intersection.getX(), Double.MIN_VALUE);
        Assert.assertEquals(2.5D, intersection.getY(), Double.MIN_VALUE);
    }
}