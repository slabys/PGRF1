package model;

import java.awt.*;
import java.util.ArrayList;

public class Polygon {

    private ArrayList<Point> polygonPointList = new ArrayList<Point>();
    private Line line;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private Color color;
    private boolean edit = true;
    private model.Point editPoint;

    public Polygon() {
    }

    public Polygon(Line line) {
        this.line = line;
    }

    public ArrayList<Point> getPolygonPointList() {
        return polygonPointList;
    }

    public ArrayList<Line> getEdges(Polygon clipper) {
        ArrayList<Line> edges = new ArrayList<>();

        for (int i = 0; i < clipper.getSize();i++){
            if(i < clipper.getSize()-1){
                edges.add(new Line(
                        clipper.getPolygonPointList().get(i).getX(), clipper.getPolygonPointList().get(i).getY(),
                        clipper.getPolygonPointList().get(i+1).getX(), clipper.getPolygonPointList().get(i+1).getY(),
                        clipper.getColor().getRGB()
                ));
                if(i == clipper.getSize()){
                    edges.add(new Line(
                            clipper.getPolygonPointList().get(0).getX(), clipper.getPolygonPointList().get(0).getY(),
                            clipper.getPolygonPointList().get(i).getX(), clipper.getPolygonPointList().get(i).getY(),
                            clipper.getColor().getRGB()
                    ));
                }
            }
        }
        return edges;
    }

    public void setPolygonPointList(ArrayList<Point> polygonPointList) {
        this.polygonPointList = polygonPointList;
    }

    public int getSize(){
        return polygonPointList.size();
    }

    public void addPoint(Point point){
        polygonPointList.add(point);
    }

    public void editClosest(int x, int y){

        double prev = Double.MAX_VALUE;
        if(polygonPointList.size() > 0 && edit){
            for (model.Point pointTmp : polygonPointList) {
                if (pointTmp.getDistance(x, y) < prev) {
                    prev = pointTmp.getDistance(x, y);
                    editPoint = pointTmp;
                }
            }
            edit = false;
        }else{
            editPoint.setX(x);
            editPoint.setY(y);
            edit = true;
        }
    }

    public void clear(Polygon result) {
        result.getPolygonPointList().clear();
    }
}
