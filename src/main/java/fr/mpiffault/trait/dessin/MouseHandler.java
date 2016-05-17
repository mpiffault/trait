package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.geometry.Point;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseInputAdapter{
    public static final double SNAP_UPDATE_INTERVAL = 50D;
    private final Table table;

    public MouseHandler(Table table) {
        this.table = table;
    }

    private long previousTime = System.currentTimeMillis();

    @Override
    public void mouseClicked(MouseEvent e) {

        super.mouseClicked(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClickActions(e);
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            rightClickActions(e);
        }

        table.repaint();
    }

    private void rightClickActions(MouseEvent e) {
        switch (table.getCurrentMode()) {
            case SEGMENT:
                if (table.ongoingAction()) {
                    table.cancelCurrentAction();
                }
            default:
                break;
        }
    }

    private void leftClickActions(MouseEvent e) {
        Point point = new Point(e.getX(), e.getY());
        table.setCursorPosition(point);
        switch (table.getCurrentMode()) {
            case SELECTION:
                table.selectObjectAt(point, e.isShiftDown());
                break;
            case POINT:
                table.createPoint();
                break;
            case SEGMENT:
                if (!table.ongoingSegment()) {
                    table.initSegmentTrace();
                } else {
                    table.endSegment();
                }
                break;
            case CONSTRUCTION:
                if (!table.ongoingConstructionLine()) {
                    table.initConstructionLineTrace();
                } else {
                    table.endConstructionLine();
                }
                break;
            case CONSTRUCTION_H:
                if (!table.ongoingConstructionLine()) {
                    table.traceHorizontalLine();
                } else {
                    table.endHorizontalLine();
                }
                break;
            case CONSTRUCTION_V:
                if (!table.ongoingConstructionLine()) {
                    table.traceVerticalLine();
                } else {
                    table.endVerticalLine();
                }
                break;
            case CONSTRUCTION_A:
                if (table.ongoingConstructionLine()) {
                    table.cancelCurrentAction();
                }
                table.traceAngleLine();
                break;
            default:
                break;
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        Point point = new Point(e.getX(), e.getY());
        table.setCursorPosition(point);
        switch (table.getCurrentMode()) {
            case SELECTION:
                if (!table.ongoingSelectionBox()) {
                    table.initSelectionBox();
                }
                table.updateSelectionBox();
                break;
            default:
                break;
        }
        table.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = new Point(e.getX(), e.getY());
        table.setCursorPosition(point);

        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - previousTime;
        if (deltaTime > SNAP_UPDATE_INTERVAL) {
            previousTime = currentTime;
            table.updateNearestIntersectableList();
            table.updateNearestIntersection();
        }

        switch (table.getCurrentMode()) {
            case SEGMENT:
                if (table.ongoingSegment()) {
                    table.updateTracingSegment();
                }
            case CONSTRUCTION:
                if(table.ongoingConstructionLine()) {
                    table.updateTracingConstructionLine();
                }
        }
        table.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (table.getCurrentMode()) {
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        boolean mod = false;
        Point point = new Point(e.getX(), e.getY());
        table.setCursorPosition(point);
        switch (table.getCurrentMode()) {
            case SELECTION:
                if (table.ongoingSelectionBox()) {
                    table.endSelectionBox(e.isShiftDown());
                    mod = true;
                }
                break;
            case SEGMENT:
                break;
            default:
                break;
        }
        if (mod) {
            table.repaint();
        }
    }
}
