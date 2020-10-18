package rasterize;

import model.Line;
import model.Point;
import model.Polygon;

import java.awt.*;

public class PolygonRasterizer{

    LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon){

        for(int i = 0; i < polygon.getPolygonPointList().size();i++){
            if(polygon.getPolygonPointList().size() > i+1){
                Point firstPoint = polygon.getPolygonPointList().get(i);
                Point secondPoint = polygon.getPolygonPointList().get(i+1);
                Line line = new Line(firstPoint, secondPoint, 0xff0000);
                lineRasterizer.rasterize(line);
            }
        }
        if(polygon.getPolygonPointList().size()>1){
            model.Line line = new model.Line(
                    polygon.getPolygonPointList().get(0),
                    polygon.getPolygonPointList().get(polygon.getPolygonPointList().size()-1),
                    0xff0000);
            lineRasterizer.rasterize(line);
        }
    }

}
