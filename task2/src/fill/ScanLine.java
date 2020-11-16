package fill;

import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLine implements Filler {

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    private Polygon polygon;
    private Color fillColor = Color.GREEN; //naplnit setrem, nebo soucasti polygonu
    private int borderColor; //naplnit setrem, nebo soucasti polygonu
    //private Raster raster; //nebo LineRasterizer, plneni konstruktorem
    private LineRasterizer lineRasterizer;

    public ScanLine(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    private void process() {
        List<Line> lines = new ArrayList<>();
        List<Integer> intersections = new ArrayList<>();
        int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;

        for (int i = 0; i < polygon.getSize() - 1; i++) {
            Line line = new Line(
                    polygon.getPolygonPointList().get(i),
                    polygon.getPolygonPointList().get((i + 1)),
                    0xff0000
            );
            //Horizontal line delete
            if (!line.isHorizontal()) {
                //Set orientation to one side and add it to lines
                lines.add(line.setOrientation());
                //Uptade yMax and yMin
                if (yMin > line.getY1()) yMin = line.getY1();
                if (yMax < line.getY2()) yMax = line.getY2();
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

            Collections.sort(intersections);
            //sort intersection by InsertionSort
            //intersections = insertionSort(intersections);

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
