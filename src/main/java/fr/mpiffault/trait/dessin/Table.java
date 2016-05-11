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
    public static final Color UI_TEXT = Color.RED;

    @Getter
    private int width, height;

    @Getter
    private Mode currentMode;

    @Setter
    private Point cursorPosition = new Point(0D,0D);

    private final ArrayList<LinkedList<Drawable>> layers = new ArrayList<>();
    private final Set<Selectable> selected = new LinkedHashSet<>();

    private SelectionBox selectionBox;
    private TracingSegment tracingSegment;
    private ConstructionLine constructionLine;

    public Table(int width, int height) {
        this.width = width;
        this.height = height;
        this.setBackground(BACKGROUND);

        this.currentMode = Mode.SELECTION;

        LinkedList<Drawable> mainLayer = new LinkedList<>();
        layers.add(mainLayer);
    }

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
        g2.setColor(UI_TEXT);
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
            // TODO : roll between selectables
            return selectable;
        }
        return null;
    }

    private List<Selectable> getEligibleObjectsAt(Point point) {
        ArrayList<Selectable> eligibles = new ArrayList<Selectable>();
        for (LinkedList<Drawable> layer : layers) {
            eligibles.addAll(layer.stream()
                    .filter(drawable -> drawable instanceof Selectable)
                    .filter(drawable -> ((Selectable) drawable).isSelectable(point))
                    .map(drawable -> (Selectable) drawable)
                    .collect(Collectors.toList()));
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
        updateSelectionBox(point);

        Rectangle2D finalSelectionBox = this.selectionBox.getRectangle2D();

        if (!addToSelection) {
            this.selected.clear();
        }
        for (LinkedList<Drawable> layer : layers) {
            this.selected.addAll(layer.stream()
                    .filter(drawable -> drawable instanceof Selectable)
                    .filter(drawable -> ((Selectable) drawable).isInBox(finalSelectionBox))
                    .map(drawable -> (Selectable) drawable)
                    .collect(Collectors.toList()));
        }


        this.selectionBox = null;
    }

    public void updateSelectionBox(Point point) {
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

    public void deleteSelectedObjects() {
        if (!this.selected.isEmpty()) {
            this.layers.get(MAIN_LAYER).removeAll(selected);
            this.selected.clear();
        }
    }

    public boolean ongoingLine() {
        return this.constructionLine != null;
    }

    public void initLineTrace(Point point) {
        constructionLine = new ConstructionLine(point, point, this);
    }

    public void endLine(Point point) {
        if (constructionLine != null) {
            constructionLine = new ConstructionLine(constructionLine.getFirstPoint(), point, this);
        }
        this.layers.get(MAIN_LAYER).add(constructionLine);
        constructionLine = null;
    }

    public void traceHorizontalLine(Point point) {
        constructionLine = new ConstructionLine(point, new Point(point.getX() + 1, point.getY()), this);
        this.layers.get(MAIN_LAYER).add(constructionLine);
        constructionLine = null;
    }

    public void endHorizontalLine(Point point) {
        traceHorizontalLine(constructionLine.getFirstPoint());
    }

    public void traceVerticalLine(Point point) {
        constructionLine = new ConstructionLine(point, new Point(point.getX(), point.getY() + 1), this);
        this.layers.get(MAIN_LAYER).add(constructionLine);
        constructionLine = null;
    }

    public void endVerticalLine(Point point) {
        traceVerticalLine(constructionLine.getFirstPoint());
    }

    public void traceAngleLine(Point point) {
        // TODO
    }
}
