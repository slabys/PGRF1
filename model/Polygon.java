package model;

import java.util.ArrayList;

public class Polygon {

    private ArrayList<Point> polygonPointList = new ArrayList<Point>();

    public Polygon() {
    }

    public Polygon(ArrayList<Point> polygon) {
        this.polygonPointList = polygonPointList;
    }

    public ArrayList<Point> getPolygonPointList() {
        return polygonPointList;
    }

    public void setPolygonPointList(ArrayList<Point> polygonPointList) {
        this.polygonPointList = polygonPointList;
    }

    public void addPoint(Point point){
        polygonPointList.add(point);
    }
}
