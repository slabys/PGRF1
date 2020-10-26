package model;

import java.util.ArrayList;

public class Polygon {

    private ArrayList<Point> polygonPointList = new ArrayList<Point>();
    private boolean edit = true;
    private model.Point editPoint;

    public Polygon() {
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

    public void editClosest(int x, int y){

        double prev = 9999;
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
}
