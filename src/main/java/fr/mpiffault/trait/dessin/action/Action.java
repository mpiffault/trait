package fr.mpiffault.trait.dessin.action;

import fr.mpiffault.trait.geometry.Point;

import java.awt.*;

public interface Action<T> {

    void initAt(Point point);

    void updateAt(Point point);

    void leftClickAt(Point point);

    void leftButtonDownAt(Point point);

    void leftButtonUpAt(Point point);

    void rightClick();

    void doubleClickAt(Point point);

    T getResult();

    void draw(Graphics2D g2);
}
