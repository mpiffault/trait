package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.geometry.Point;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseInputAdapter{
    private final Table table;

    public MouseHandler(Table table) {
        this.table = table;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        super.mouseClicked(e);
        Point point = new Point(e.getX(), e.getY());
        switch (table.getCurrentMode()) {
            case SELECTION:
                boolean shiftDown = e.getModifiersEx() == MouseEvent.SHIFT_DOWN_MASK;
                table.selectObjectAt(point, shiftDown);
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
        switch (table.getCurrentMode()) {
            case SELECTION:
                break;
            case POINT:
                break;
            case SEGMENT:
                break;
            case CONSTRUCTION:
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point point = new Point(e.getX(), e.getY());
        switch (table.getCurrentMode()) {
            case SELECTION:
                table.initSelectRectangle(point);
                break;
            case SEGMENT:
                table.initSegmentTrace(point);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (table.getCurrentMode()) {
            case SELECTION:
                if (table.selectRectangleInited()) {
                    table.endSelectRectangle();
                }
                break;
            case SEGMENT:
                if (table.segmentTracing()) {
                    table.endSegment();
                }
                break;
            default:
                break;
        }
    }
}
