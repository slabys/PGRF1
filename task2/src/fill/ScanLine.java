package fill;

import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ScanLine implements Filler {

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    private Polygon polygon;
    private Color fillColor;

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    private int borderColor;
    private LineRasterizer lineRasterizer;

    public ScanLine(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    private void process() {
        List<Line> lines = new ArrayList<>();
        List<Integer> intersections = new ArrayList<>();
        int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;

        for (int i = 0; i < polygon.getSize(); i++) {

            Line line = new Line(
                    polygon.getPolygonPointList().get(i),
                    polygon.getPolygonPointList().get((i + 1)%polygon.getSize()),
                    fillColor.getRGB()
            );

            //Horizontal line delete
            if (!line.isHorizontal()) {
                //Set orientation to one side and add it to lines
                Line l = line.setOrientation();
                lines.add(l);
                //Update yMax and yMin
                if (yMin > l.getY2()) yMin = l.getY2();
                if (yMax < l.getY1()) yMax = l.getY1();
            }
        }

        for (int y = yMin; y < yMax; y++) {
            intersections.clear();
            //Find intersection of lines with line "y"
            for (Line li : lines) {
                if (li.isIntersection(y)) {
                    int x = li.getIntersection(y);
                    intersections.add(x);
                }
            }

            //sort intersection by InsertionSort
            intersections = insertionSort(intersections);

            for (int i = 0; i < intersections.size(); i += 2) {
                if (intersections.size() > i + 1) {
                    lineRasterizer.rasterize(new Line(
                            new Point(intersections.get(i), y),
                            new Point(intersections.get(i + 1), y),
                            fillColor.getRGB())
                    );
                }
            }
        }
    }

    private List insertionSort(List array) {
        int n = array.size();
        for (int i = 1; i < n; ++i) {
            int key = (int) array.get(i);
            int j = i - 1;

            while (j >= 0 && (int) array.get(j) > key) {
                array.set(j+1, array.get(j));
                j = j - 1;
            }
            array.set(j+1, key);
        }
        return array;
    }

    @Override
    public void fill() {
        process();
    }
}
