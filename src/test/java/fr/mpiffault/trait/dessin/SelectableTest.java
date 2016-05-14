package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.geometry.Point;
import fr.mpiffault.trait.geometry.Segment;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class SelectableTest {

    @Test
    public void segmentIsInBox() throws Exception {
        Segment s = new Segment(new Point(1,1), new Point(2,2));
        Rectangle2D r = new Rectangle2D.Double(0D,0D,3D,3D);
        boolean isInBox = s.isInBox(r);
        assertTrue(isInBox);
    }
    @Test
    public void segmentIsNotInBox_1() throws Exception {
        Segment s = new Segment(new Point(1,1), new Point(2,5));
        Rectangle2D r = new Rectangle2D.Double(0D,0D,3D,3D);
        boolean isInBox = s.isInBox(r);
        assertFalse(isInBox);
    }
    @Test
    public void segmentIsNotInBox_2() throws Exception {
        Segment s = new Segment(new Point(-1,1), new Point(2,2));
        Rectangle2D r = new Rectangle2D.Double(0D,0D,3D,3D);
        boolean isInBox = s.isInBox(r);
        assertFalse(isInBox);
    }
    @Test
    public void segmentIsNotInBox_3() throws Exception {
        Segment s = new Segment(new Point(-1,1), new Point(2,4));
        Rectangle2D r = new Rectangle2D.Double(0D,0D,3D,3D);
        boolean isInBox = s.isInBox(r);
        assertFalse(isInBox);
    }

    @Test
    public void verticalSegmentIsInBox() throws Exception {
        Segment s = new Segment(new Point(1,1), new Point(1,2));
        Rectangle2D r = new Rectangle2D.Double(0D,0D,3D,3D);
        boolean isInBox = s.isInBox(r);
        assertTrue(isInBox);
    }

    @Test
    public void horizontalSegmentIsInBox() throws Exception {
        Segment s = new Segment(new Point(1,1), new Point(2,1));
        Rectangle2D r = new Rectangle2D.Double(0D,0D,3D,3D);
        boolean isInBox = s.isInBox(r);
        assertTrue(isInBox);
    }

    @Test
    public void pointIsInBox() throws Exception {
        Point p = new Point(1,1);
        Rectangle2D r = new Rectangle2D.Double(0D,0D,2D,2D);
        boolean isInBox = p.isInBox(r);
        assertTrue(isInBox);
    }

}