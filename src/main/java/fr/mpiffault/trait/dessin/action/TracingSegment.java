package fr.mpiffault.trait.dessin.action;

import fr.mpiffault.trait.geometry.Point;
import fr.mpiffault.trait.geometry.Segment;

import java.awt.*;

public class TracingSegment extends ActionAdapter<Segment>{
    private Segment segment;

//    public TracingSegment(Point point) {
//        super(point, point);
//    }

    @Override
    public void initAt(Point point) {
        segment = new Segment(point, point);
    }

    @Override
    public void updateAt(Point point) {
        if (!terminated) {
            segment.setEndPoint(point);
        }
    }

    @Override
    public void leftButtonDownAt(Point point) {

    }

    @Override
    public void leftButtonUpAt(Point point) {
        segment.setEndPoint(point);
        this.terminated = true;
    }

    @Override
    public void rightClick() {
        canceled = true;
    }

    @Override
    public void doubleClickAt(Point point) {

    }

    @Override
    public Segment getResult() {
        if (terminated) {
            return segment;
        }
        return null;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.draw(segment);
    }
}
