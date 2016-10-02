package fr.mpiffault.trait.geometry;

import fr.mpiffault.trait.geometry.utils.PointUtils;
import lombok.Getter;
import lombok.Setter;

public class Vertex {

    @Getter
    @Setter
    private Vertex previous;

    @Getter
    @Setter
    private Point self;

    @Getter
    @Setter
    private Vertex next;

    public Vertex(Point position) {
        this.self = position;
    }

    public boolean isFirst(){
        return previous == null;
    }

    public Vertex getFirst() {
        if (this.isFirst()) {
            return this;
        }
        return this.getPrevious().getFirst();
    }

    public boolean isLast() {
        return next == null;
    }

    public Vertex getLast() {
        if (this.isLast()) {
            return this;
        }
        return this.getNext().getLast();
    }

    public boolean isEnd() {
        return isFirst() || isLast();
    }

    public void addToQueue(Point newPoint) {
        addToQueue(new Vertex(newPoint));
    }

    public void addToQueue(Vertex newVertex) {
        if (isLast()) {
            this.setNext(newVertex);
            newVertex.setPrevious(this);
        } else {
            next.addToQueue(newVertex);
        }
    }

    public void addToHead(Point newPoint) {
        addToHead(new Vertex(newPoint));
    }

    public void addToHead(Vertex newVertex) {
        if (isFirst()) {
            this.setPrevious(newVertex);
            newVertex.setNext(this);
        } else {
            previous.addToHead(newVertex);
        }
    }

    public Point getBissectPoint() {
        if (!isEnd()) {
            return PointUtils.getBissectPoint(previous.self, self, next.self);
        } else {
            return self;
        }

    }

}
