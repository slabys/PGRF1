package clip;

import model.Line;
import model.Point;
import model.Polygon;

public class Clipper {

    public static Polygon clip(Polygon polygon, Polygon clipPolygon) {
        Polygon result = new Polygon();

        for (Line edge : clipPolygon.getEdges(clipPolygon)) {
            Point v1 = polygon.getPolygonPointList().get(polygon.getSize() - 1);
            for (Point v2 : polygon.getPolygonPointList()) {
                if (isInside(v2, edge)) {
                    if (isInside(v1, edge)) {
                        result.addPoint(intersection(v1, v2, edge));
                    }
                    result.addPoint(v2);
                } else {
                    if (isInside(v1, edge)) {
                        result.addPoint(intersection(v1, v2, edge));
                    }
                }
                v1 = v2;
            }
        }

        return result;
    }

    private static Point intersection(Point v1, Point v2, Line edge) {
        double xTop = (v1.getX() * v2.getY() - v2.getX() * v1.getY()) * (edge.getX1() - edge.getX2()) -
                (edge.getX1() * edge.getY2() - edge.getX2() * edge.getY1()) * (v1.getX() - v2.getX());
        double yTop = (v1.getX() * v2.getY() - v2.getX() * v1.getY()) * (edge.getY1() - edge.getY2()) -
                (edge.getX1() * edge.getY2() - edge.getX2() * edge.getY1()) * (v1.getY() - v2.getY());
        double bottom = (v1.getX() - v2.getX()) * (edge.getY1() - edge.getY2()) -
                (v1.getY() - v2.getY()) * (edge.getX1() - edge.getX2());

        double x0 = xTop / bottom;
        double y0 = yTop / bottom;


        return new Point(x0, y0);
    }

    private static boolean isInside(Point v2, Line edge) {
        double top = Math.abs(v2.getX() * (edge.getY2() - edge.getY1()) - v2.getY() * (edge.getX2() - edge.getX1()) +
                edge.getX2() * edge.getY1() - edge.getY2() * edge.getX1());
        double bottom = Math.sqrt(
                Math.pow(edge.getY2() - edge.getY1(), 2)
                        +
                        Math.pow(edge.getX2() - edge.getX1(), 2));
        double distance = top / bottom;
        return distance > 0;
    }
}
