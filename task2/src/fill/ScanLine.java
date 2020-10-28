package fill;

import model.Line;
import model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class ScanLine implements Filler {

    Polygon polygon;

    private void process(){
        List<Line> linesList = new ArrayList<>();
        List<Integer> intersections = new ArrayList<>();
        int yMin, yMax;
        int i = 0;

            Line line = new Line(polygon.polygonPointList.get(i),polygon.polygonPointList.get(i+1), 0x0);


    }

    @Override
    public void fill() {

    }
}
