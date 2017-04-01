package fr.mpiffault.trait.dessin.action;

import fr.mpiffault.trait.dessin.DrawableAdapter;
import fr.mpiffault.trait.geometry.Point;

import java.awt.*;

public class ActionAdapter<T> extends DrawableAdapter implements Action<T> {
    boolean terminated = false;
    boolean canceled = true;

    @Override
    public void initAt(Point point) {

    }

    @Override
    public void updateAt(Point point) {

    }

    @Override
    public void leftClickAt(Point point) {

    }

    @Override
    public void leftButtonDownAt(Point point) {

    }

    @Override
    public void leftButtonUpAt(Point point) {

    }

    @Override
    public void rightClick() {

    }

    @Override
    public void doubleClickAt(Point point) {

    }

    @Override
    public T getResult() {
        return null;
    }

    @Override
    public void draw(Graphics2D g2) {

    }
}
