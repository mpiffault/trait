package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.geometry.Point;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseInputAdapter{
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
        switch (table.getCurrentMode()) {
            case SELECTION:
                table.selectObjectAt(point, e.isShiftDown());
                break;
            case POINT:
                table.createPoint(point);
                break;
            case SEGMENT:
                if (!table.ongoingSegment()) {
                    table.initSegmentTrace(point);
                } else {
                    table.endSegment();
                }
                break;
            case CONSTRUCTION:
                if (!table.ongoingLine()) {
                    table.initLineTrace(point);
                } else {
                    table.endLine(point);
                }
                break;
            case CONSTRUCTION_H:
                if (!table.ongoingLine()) {
                    table.traceHorizontalLine(point);
                } else {
                    table.endHorizontalLine(point);
                }
                break;
            case CONSTRUCTION_V:
                if (!table.ongoingLine()) {
                    table.traceVerticalLine(point);
                } else {
                    table.endVerticalLine(point);
                }
                break;
            case CONSTRUCTION_A:
                if (table.ongoingLine()) {
                    table.cancelCurrentAction();
                }
                table.traceAngleLine(point);
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
                    table.initSelectionBox(point);
                }
                table.updateSelectionBox(point);
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
        if (deltaTime > 50D) {
            // System.out.println("currTime:" + currentTime + "prevTime:" + previousTime + " dTime:" + deltaTime);
            previousTime = currentTime;
            table.updateNearestSegments();
        }

        switch (table.getCurrentMode()) {
            case SEGMENT:
                if (table.ongoingSegment()) {
                    table.updateTracingSegment();
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
        switch (table.getCurrentMode()) {
            case SELECTION:
                if (table.ongoingSelectionBox()) {
                    table.endSelectionBox(point, e.isShiftDown());
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
