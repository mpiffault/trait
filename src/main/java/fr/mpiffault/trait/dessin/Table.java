package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.dessin.action.SelectionBox;
import fr.mpiffault.trait.dessin.action.TracingSegment;
import fr.mpiffault.trait.geometry.ConstructionLine;
import fr.mpiffault.trait.geometry.Point;
import fr.mpiffault.trait.geometry.Segment;
import fr.mpiffault.trait.geometry.fr.mpiffault.trait.dessin.Drawable;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Table extends JPanel {

    private static final int MAIN_LAYER = 0;

    public static final Color BACKGROUND = Color.DARK_GRAY;
    public static final Color FOREGROUND = Color.WHITE;
    public static final Color SELECTED = Color.MAGENTA;
    public static final Color HIGHTLIGHTED = Color.GREEN;
    public static final Color NEAREST = Color.BLUE;
    public static final Color UI_TEXT = Color.RED;

    @Getter
    private int width, height;

    @Getter
    private Mode currentMode;

    @Setter
    private Point cursorPosition = new Point(0D,0D);

    private final ArrayList<LinkedList<Drawable>> layers = new ArrayList<>();
    private LinkedList<Drawable> activeLayer;
    private final Set<Selectable> selected = new LinkedHashSet<>();

    private SelectionBox selectionBox;
    private TracingSegment tracingSegment;
    private ConstructionLine constructionLine;
    private List nearestSegments;
    private Segment nearestSegment;

    public Table(int width, int height) {
        this.width = width;
        this.height = height;
        this.setBackground(BACKGROUND);

        this.currentMode = Mode.SELECTION;

        LinkedList<Drawable> mainLayer = new LinkedList<>();
        activeLayer = mainLayer;
        layers.add(mainLayer);

        nearestSegments = new ArrayList<>();
    }

    /* DRAWING */

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintDrawables(g2);
        paintModeLabel(g2);
        paintCursor(g2);
    }

    private void paintCursor(Graphics2D g2) {
        g2.setColor(FOREGROUND);
        g2.drawLine(0, (int)cursorPosition.getY(), this.getWidth(), (int)cursorPosition.getY());
        g2.drawLine((int)cursorPosition.getX(), 0, (int)cursorPosition.getX(), this.getHeight());

    }

    private void paintDrawables(Graphics2D g2) {
        for (Drawable drawable : activeLayer) {
            if (drawable instanceof Selectable && selected.contains(drawable)) {
                ((Selectable)drawable).drawSelected(g2);
            } else {
                drawable.draw(g2);
            }
            if (this.nearestSegments.contains(drawable)) {
                drawable.drawHightlighted(g2);
            }
        }
        if (this.nearestSegment != null) {
            this.nearestSegment.drawNearest(g2);
        }
        if (this.ongoingSelectionBox()) {
            this.selectionBox.draw(g2);
        }
        if (this.ongoingSegment()) {
            this.tracingSegment.draw(g2);
        }
        if (this.ongoingConstructionLine()) {
            this.constructionLine.draw(g2);
        }
    }

    private void paintModeLabel(Graphics2D g2) {
        g2.setColor(UI_TEXT);
        g2.drawString(currentMode.getName(), 20, 20);
    }

    public void setCurrentMode(Mode mode) {
        this.currentMode = mode;
    }

    public boolean ongoingAction() {
        return ongoingSelectionBox() || ongoingSegment() || ongoingConstructionLine();
    }

    public void cancelCurrentAction() {
        this.selectionBox = null;
        this.tracingSegment = null;
        this.constructionLine = null;
    }

    public void deleteSelectedObjects() {
        if (!this.selected.isEmpty()) {
            activeLayer.removeAll(selected);
            this.selected.clear();
        }
    }

    /* SELECTION */

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
            // TODO : roll between selectables
            return selectable;
        }
        return null;
    }

    private List<Selectable> getEligibleObjectsAt(Point point) {
        return activeLayer.stream()
                .filter(drawable -> drawable instanceof Selectable)
                .filter(drawable -> ((Selectable) drawable).isSelectable(point))
                .map(drawable -> (Selectable) drawable)
                .collect(Collectors.toList());
    }

    public void initSelectionBox() {
        this.selectionBox = new SelectionBox(this.cursorPosition);
    }

    public boolean ongoingSelectionBox() {
        return this.selectionBox != null;
    }

    public void updateSelectionBox() {
        if (ongoingSelectionBox()) {
            this.selectionBox.setEndPoint(this.cursorPosition);
        }
    }

    public void endSelectionBox(boolean addToSelection) {
        updateSelectionBox();

        Rectangle2D finalSelectionBox = this.selectionBox.getRectangle2D();

        if (!addToSelection) {
            this.selected.clear();
        }
        this.selected.addAll(activeLayer.stream()
                .filter(drawable -> drawable instanceof Selectable)
                .filter(drawable -> ((Selectable) drawable).isInBox(finalSelectionBox))
                .map(drawable -> (Selectable) drawable)
                .collect(Collectors.toList()));

        this.selectionBox = null;
    }

    /* POINT */

    public void createPoint() {
        activeLayer.add(this.cursorPosition);
        this.repaint();
    }

    /* SEGMENT */

    public void initSegmentTrace() {
        this.tracingSegment = new TracingSegment(this.cursorPosition);
    }

    public boolean ongoingSegment() {
        return this.tracingSegment != null;
    }

    public void updateTracingSegment() {
        if (ongoingSegment()) {
            this.tracingSegment.setEndPoint(this.cursorPosition);
        }
    }

    public void endSegment() {
        Segment newSegment = this.tracingSegment;
        activeLayer.add(newSegment);
        this.tracingSegment = null;
    }

    /* CONSTRUCTION */

    public void initConstructionLineTrace() {
        constructionLine = new ConstructionLine(this.cursorPosition, this.cursorPosition, this);
    }

    public void updateTracingConstructionLine() {
        if (ongoingConstructionLine()) {
            this.constructionLine.setSecondPoint(this.cursorPosition);
        }
    }

    public boolean ongoingConstructionLine() {
        return this.constructionLine != null;
    }

    public void endConstructionLine() {
        if (constructionLine != null) {
            //constructionLine = new ConstructionLine(constructionLine.getFirstPoint(), this.cursorPosition, this);
            constructionLine.setSecondPoint(this.cursorPosition);
        }
        activeLayer.add(constructionLine);
        constructionLine = null;
    }

    public void traceHorizontalLine() {
        constructionLine = new ConstructionLine(this.cursorPosition,
                new Point(this.cursorPosition.getX() + 1, this.cursorPosition.getY()), this);
        activeLayer.add(constructionLine);
        constructionLine = null;
    }

    public void endHorizontalLine() {
        traceHorizontalLine();
    }

    public void traceVerticalLine() {
        constructionLine = new ConstructionLine(this.cursorPosition,
                new Point(this.cursorPosition.getX(), this.cursorPosition.getY() + 1), this);
        activeLayer.add(constructionLine);
        constructionLine = null;
    }

    public void endVerticalLine() {
        traceVerticalLine();
    }

    public void traceAngleLine() {
        // TODO
    }

    /* DETECTION */

    public void updateNearestSegments() {
        List<Drawable> segments = activeLayer.stream()
                .filter(drawable -> drawable instanceof Segment)
                .sorted((o1, o2) -> Double.compare(((Segment) o1).ptSegDist(cursorPosition), ((Segment) o2).ptSegDist(cursorPosition)))
                .collect(Collectors.toList());
        this.nearestSegments = segments.subList(0,Math.min(segments.size(), 3));
        if (!this.nearestSegments.isEmpty()) {
            this.nearestSegment = (Segment) segments.get(0);
        }
    }
}