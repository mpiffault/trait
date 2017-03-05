package fr.mpiffault.trait.dessin;

import fr.mpiffault.trait.Utils;
import fr.mpiffault.trait.dessin.action.SelectionBox;
import fr.mpiffault.trait.dessin.action.TracingSegment;
import fr.mpiffault.trait.geometry.*;
import fr.mpiffault.trait.geometry.Point;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static fr.mpiffault.trait.Utils.isDebugMode;

public class Table extends JPanel {

    public static final Color BACKGROUND = Color.DARK_GRAY;
    public static final Color FOREGROUND = Color.WHITE;
    public static final Color SELECTED = Color.MAGENTA;
    public static final Color HIGHTLIGHTED = Color.GREEN;
    public static final Color NEAREST = Color.BLUE;
    public static final Color UI_TEXT = Color.RED;

    @Getter
    private int width, height;

    @Getter
    private ModeEnum currentMode;

    @Setter
    private Point cursorPosition = new Point(0D,0D);

    private final LinkedList<Drawable> activeLayer;
    private LinkedList<ConstructionLine> constructionLayer;
    private final Set<Selectable> selected = new LinkedHashSet<>();

    private SelectionBox selectionBox;
    private TracingSegment tracingSegment;
    private ConstructionLine constructionLine;
    private List<Intersectable> nearestIntersectableList;
    private Intersectable nearestIntersectable;
    private ArrayList<Point> nearestSnapPointList = new ArrayList<>();

    @Getter
    private Point nearestSnapPoint;

    private Curve tracingCurve;

    @Getter
    @Setter
    private boolean ongoingClick;

    public Table(int width, int height) {

        this.width = width;
        this.height = height;
        this.setBackground(BACKGROUND);

        this.currentMode = ModeEnum.SELECTION;

        activeLayer = new LinkedList<>();
        constructionLayer = new LinkedList<>();

        nearestIntersectableList = new ArrayList<>();
    }

    /* DRAWING */

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(this.width != this.getParent().getWidth()) this.width = this.getParent().getWidth();
        if(this.height != this.getParent().getHeight()) this.height = this.getParent().getHeight();
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
        for (ConstructionLine constructionLine : constructionLayer) {
            constructionLine.draw(g2);
        }
        for (Drawable drawable : activeLayer) {
            if (drawable instanceof Selectable && selected.contains(drawable)) {
                ((Selectable)drawable).drawSelected(g2);
            } else {
                drawable.draw(g2);
            }
        }

        nearestIntersectableList.stream().filter(intersectable -> intersectable instanceof Drawable)
        .forEach(intersectable -> ((Drawable)intersectable).drawHightlighted(g2));

        if (this.nearestIntersectable != null) {
            this.nearestIntersectable.drawNearest(g2);
        }
        if (this.nearestSnapPoint != null) {
            this.nearestSnapPoint.drawHightlighted(g2);
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
        if (ongoingCurve()) {
            this.tracingCurve.draw(g2);
        }
    }

    private void paintModeLabel(Graphics2D g2) {
        g2.setColor(UI_TEXT);
        g2.drawString(currentMode.getName(), 20, 20);
    }

    public void setCurrentMode(ModeEnum modeEnum) {
        this.currentMode = modeEnum;
    }

    public boolean ongoingAction() {
        return ongoingSelectionBox() || ongoingSegment() || ongoingConstructionLine() || this.ongoingClick;
    }

    public void cancelCurrentAction() {
        this.selectionBox = null;
        this.tracingSegment = null;
        this.constructionLine = null;
        this.ongoingClick = false;
    }

    public void deleteSelectedObjects() {
        if (!this.selected.isEmpty()) {
            activeLayer.removeAll(selected);
            this.selected.clear();
        }
        updateNearestIntersectableList();
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
        Point initPoint = getClicPoint();
        this.tracingSegment = new TracingSegment(initPoint);
        if (isDebugMode()) {
            System.out.println("initSegment");
        }
    }

    private Point getClicPoint() {
        return this.nearestSnapPoint != null ? this.nearestSnapPoint : this.cursorPosition;
    }

    public boolean ongoingSegment() {
        return this.tracingSegment != null;
    }

    public void updateTracingSegment() {
        if (ongoingSegment()) {
            this.tracingSegment.setEndPoint(getClicPoint());
        }
    }

    public void endSegment() {
        Segment newSegment = this.tracingSegment;
        activeLayer.add(newSegment);
        this.tracingSegment = null;
        if (isDebugMode()) {
            System.out.println("endSegment");
        }
    }

    /* CONSTRUCTION */

    public void initConstructionLineTrace() {
        constructionLine = new ConstructionLine(getClicPoint(), getClicPoint(), this);
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
            constructionLine.setSecondPoint(getClicPoint());
        }
        constructionLayer.add(constructionLine);
        constructionLine = null;
    }

    public void traceHorizontalLine() {
        constructionLine = new ConstructionLine(getClicPoint(),
                new Point(getClicPoint().getX() + 1, getClicPoint().getY()), this);
        constructionLayer.add(constructionLine);
        constructionLine = null;
    }

    public void endHorizontalLine() {
        traceHorizontalLine();
    }

    public void traceVerticalLine() {
        constructionLine = new ConstructionLine(getClicPoint(),
                new Point(getClicPoint().getX(), getClicPoint().getY() + 1), this);
        constructionLayer.add(constructionLine);
        constructionLine = null;
    }

    public void endVerticalLine() {
        traceVerticalLine();
    }

    public void traceAngleLine() {
        // TODO
    }

    /* DETECTION */

    public void updateNearestIntersectableList() {
        List<Intersectable> intersectableList = this.activeLayer.stream()
                .filter(drawable -> drawable instanceof Intersectable)
                .map(drawable -> (Intersectable) drawable)
                .collect(Collectors.toList());

        List<Intersectable> intersectableConstructionList = this.constructionLayer.stream()
                .filter(drawable -> drawable != null)
                .map(drawable -> (Intersectable) drawable)
                .collect(Collectors.toList());

        intersectableList.addAll(intersectableConstructionList);

        intersectableList.sort((i1, i2) -> Double.compare(i1.ptDist(cursorPosition), i2.ptDist(cursorPosition)));

        this.nearestIntersectableList = intersectableList.subList(0,Math.min(intersectableList.size(), 3));
        if (!nearestIntersectableList.isEmpty()) {
            this.nearestIntersectable = this.nearestIntersectableList.get(0);
        } else {
            this.nearestIntersectable = null;
        }
    }

    // FIXME
    public void updateNearestIntersection() {
        this.nearestSnapPointList = seakAllIntersectionPoints();
        this.nearestSnapPointList.addAll(seakAllDrawablesPoints());
        if (!this.nearestSnapPointList.isEmpty()) {
            orderIntersectionPoints();
            Point localIntersection = this.nearestSnapPointList.get(0);
            if (localIntersection.distanceSq(this.cursorPosition) < 200) {
                this.nearestSnapPoint = localIntersection;
            } else {
                this.nearestSnapPoint = null;
            }
        }
    }

    private Collection<? extends Point> seakAllDrawablesPoints() {
        HashSet<Point> pointHashSet = new HashSet<>();
        for (Drawable d : this.activeLayer) {
            pointHashSet.addAll(d.getPointSet());
        }
        return pointHashSet;
    }

    private void orderIntersectionPoints() {
        if(this.nearestSnapPointList.size() > 1) {
            this.nearestSnapPointList.sort((o1, o2) -> {
                if (o1 != null && o2 != null) {
                    return Double.compare(cursorPosition.distance(o1), cursorPosition.distance(o2));
                }
                return 0;
            });
        }
    }

    // FIXME
    private ArrayList<Point> seakAllIntersectionPoints() {
        ArrayList<Point> intersectionPointsList = new ArrayList<>();
        for (Intersectable intersectable : this.nearestIntersectableList) {
            List<Intersectable> subList = this.nearestIntersectableList.subList(1, Math.min(nearestIntersectableList.size(), 3));
            if (!subList.isEmpty()) {
                for (Intersectable s2 : subList) {
                    Point[] intersections = intersectable.getIntersection(s2);
                    if(intersections != null){
                        intersectionPointsList.addAll(Arrays.asList(intersections));
                    }
                }
            }
        }
        return intersectionPointsList;
    }

    public void clearConstructionLines() {
        this.constructionLayer = new LinkedList<>();
        updateNearestIntersectableList();
    }

    public void switchToParentMode() {
        this.currentMode = this.currentMode.getParentModeEnum();
    }

    public boolean ongoingCurve() {
        return tracingCurve != null;
    }

    public void initCurveTrace() {
        if (!ongoingCurve()) {
            tracingCurve = new Curve(getClicPoint());
            System.out.println("Init curve at : " + this.cursorPosition);
        }
    }

    public void addCurvePoint() {
        if (ongoingCurve()) {
            tracingCurve.addPoint(getClicPoint());
            System.out.println("Added point : " + this.cursorPosition);
        }
    }
}