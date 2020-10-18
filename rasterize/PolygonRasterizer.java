package rasterize;

import model.Line;
import model.Point;
import model.Polygon;

public class PolygonRasterizer extends LineRasterizer{


    public PolygonRasterizer(Raster raster) {
        super(raster);
    }

    public void rasterize(Polygon polygon){

        for(int i = 0; i <= polygon.getPolygonPointList().size();i =+ 1){
            Point firstPoint = polygon.getPolygonPointList().get(i);
            Point secondPoint = polygon.getPolygonPointList().get(i+1);
            Line line = new Line(firstPoint, secondPoint, 0xff0000);
            rasterize(line);
        }
    }

}
