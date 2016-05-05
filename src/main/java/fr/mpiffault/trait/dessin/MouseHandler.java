package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.geometry.Point;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseInputAdapter{
    private Table table;

    public MouseHandler(Table table) {
        this.table = table;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        super.mouseClicked(e);
        Point point = new Point(e.getX(), e.getY());
        switch (table.getCurrentMode()) {
            case SELECTION:
                table.selectObjectAt(point);
                break;
            case POINT:
                table.createPoint(point);
                break;
            case SEGMENT:
                break;
            case CONSTRUCTION:
                break;
            default:
                break;
        }

        table.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }
}
