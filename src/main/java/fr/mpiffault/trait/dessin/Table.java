package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.dessin.action.SelectionBox;
import fr.mpiffault.trait.dessin.action.TracingSegment;
import fr.mpiffault.trait.geometry.Point;
import fr.mpiffault.trait.geometry.Segment;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class Table extends JPanel {

    public static final int MAIN_LAYER = 0;
    public static final int PROTO_LAYER = 1;

    @Getter
    private int width, height;

    @Getter
    private Mode currentMode;

    private final ArrayList<LinkedList<Drawable>> layers = new ArrayList<>();

    //private final ArrayList<Selectable> selected = new ArrayList<>();
    private final Set<Selectable> selected = new LinkedHashSet<>();
    private SelectionBox selectionBox;
    private TracingSegment tracingSegment;

    public Table(int width, int height) {
        this.width = width;
        this.height = height;

        this.currentMode = Mode.SELECTION;

        LinkedList<Drawable> mainLayer = new LinkedList<>();
        LinkedList<Drawable> prototypeLayer = new LinkedList<>();
        layers.add(mainLayer);
        layers.add(prototypeLayer);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintDrawables(g2);
        paintModeLabel(g2);
    }

    private void paintDrawables(Graphics2D g2) {
        for (LinkedList<Drawable> layer : layers) {
            for (Drawable drawable : layer) {
                if (drawable instanceof Selectable && selected.contains(drawable)) {
                    ((Selectable)drawable).drawSelected(g2);
                } else {
                    drawable.draw(g2);
                }
            }
        }
        if (this.selectionBox != null) {
            this.selectionBox.draw(g2);
        }
        if (this.tracingSegment != null) {
            this.tracingSegment.draw(g2);
        }
    }

    private void paintModeLabel(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.drawString(currentMode.getName(), 20, 20);
    }

    public void createPoint(Point point) {
        layers.get(MAIN_LAYER).add(point);
        this.repaint();
    }

    public void setCurrentMode(Mode mode) {
        this.currentMode = mode;
    }

    public void selectObjectAt(Point point, boolean addToSelection) {
        Selectable elected = electObjectAt(point);
        if (elected == null) {
            if (!addToSelection) {
                selected.clear();
            }
        } else {
            if (addToSelection) {
                if (!selected.contains(elected)) {
                    selected.add(elected);
                } else {
                    selected.remove(elected);
                }
            } else {
                selected.clear();
                selected.add(elected);
            }
        }
    }

    private Selectable electObjectAt(Point point) {
        List<Selectable> selectables = getEligibleObjectsAt(point);
        for (Selectable selectable : selectables) {
            return selectable;
        }
        return null;
    }

    private List<Selectable> getEligibleObjectsAt(Point point) {
        ArrayList<Selectable> eligibles = new ArrayList<Selectable>();
        for (LinkedList<Drawable> layer : layers) {
            for (Drawable drawable : layer) {
                if (drawable instanceof Selectable) {
                    if (((Selectable)drawable).isSelectable(point)) {
                        eligibles.add((Selectable)drawable);
                    }

                }
            }
        }
        return eligibles;
    }

    public void initSelectionBox(Point point) {
        this.selectionBox = new SelectionBox(point);
    }

    public void initSegmentTrace(Point point) {
        this.tracingSegment = new TracingSegment(point);
    }

    public boolean ongoingSegment() {
        return this.tracingSegment != null;
    }

    public void endSegment() {
        Segment newSegment = this.tracingSegment;
        this.layers.get(MAIN_LAYER).add(newSegment);
        this.tracingSegment = null;
    }

    public boolean ongoingSelectionBox() {
        return this.selectionBox != null;
    }

    public void endSelectionBox(Point point, boolean addToSelection) {

        Rectangle2D finalSelectionBox = this.selectionBox.getRectangle2D();

        if (!addToSelection) {
            this.selected.clear();
        }
        for (LinkedList<Drawable> layer : layers) {
            for (Drawable drawable : layer){
                if (drawable instanceof Selectable) {
                    if (((Selectable)drawable).isInBox(finalSelectionBox)) {
                        this.selected.add((Selectable)drawable);
                    }
                }
            }
        }


        this.selectionBox = null;
    }

    public void updateSelectionRectangle(Point point) {
        if (ongoingSelectionBox()) {
            this.selectionBox.setEndPoint(point);
        }
    }

    public void updateTracingSegment(Point point) {
        if (ongoingSegment()) {
            this.tracingSegment.setEndPoint(point);
        }
    }

    public boolean ongoingAction() {
        return ongoingSegment() || ongoingSelectionBox();
    }

    public void cancelCurrentAction() {
        this.tracingSegment = null;
        this.selectionBox = null;
    }
}
