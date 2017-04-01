package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.dessin.action.Action;
import fr.mpiffault.trait.dessin.action.SelectionBox;
import fr.mpiffault.trait.dessin.action.TracingSegment;
import fr.mpiffault.trait.geometry.ConstructionLine;
import fr.mpiffault.trait.geometry.Curve;
import fr.mpiffault.trait.geometry.Point;

import java.awt.*;

public class OngoingAction {

    private Table table;

    private boolean clickDown = false;
    private SelectionBox selectionBox;
    private TracingSegment tracingSegment;
    private ConstructionLine tracingConstructionLine;
    private Curve tracingCurve;

    private Action currentAction;

    OngoingAction(Table table) {
        this.table = table;
    }

    public boolean exists() {
        return false;
    }

    public void update() {

    }

    public void cancel() {

    }

    public void draw(Graphics2D g2) {

    }

    // appelé au click release
    public void updateWithRightClick() {

    }

    // appelé au click release
    public void updateWithLeftClickAt(Point point) {

    }

    public void initActionAt(Point point) {
        if (currentAction != null) {
            return;
        }

        switch (table.getCurrentMode()) {
            case POINT:
                break;
            case SEGMENT:
                break;
            case SEGMENT_H:
                break;
            case SEGMENT_V:
                break;
            case CIRCLE:
                break;
            case CONSTRUCTION:
                break;
            case CONSTRUCTION_H:
                break;
            case CONSTRUCTION_V:
                break;
            case CONSTRUCTION_P:
                break;
            case CONSTRUCTION_C:
                break;
            case CURVE:
                break;
            case MESURE:
                break;
            default:
                break;
        }
    }
}
