package fr.mpiffault.trait.dessin.action;

import fr.mpiffault.trait.geometry.Point;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class SelectionRectangle implements Drawable{
    private Point startPoint;
    @Getter
    @Setter
    private Point endPoint;

    public SelectionRectangle(Point startPoint) {
        this.startPoint = startPoint;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawRect((int)startPoint.getX(), (int)startPoint.getY(),
                (int)(endPoint.getX() - startPoint.getX()), (int)(endPoint.getY() - startPoint.getY()));
    }
}
