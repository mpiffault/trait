package fr.mpiffault.trait.dessin.action;

import fr.mpiffault.trait.geometry.Point;
import fr.mpiffault.trait.geometry.Segment;

public class TracingSegment extends Segment{
    public TracingSegment(Point point) {
        super(point, point);
    }
}
