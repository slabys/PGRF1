package solids;

import model.Solid;
import model.Vertex;

public class Axis extends Solid {

    public enum Axises {
        XAxis(10,0,0),
        YAxis(0,10,0),
        ZAxis(0,0,10);

        public final int x, y, z;

        Axises(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public Axis(Axises axisType){
        super();
        //TODO
        switch (axisType) {
            case XAxis -> {
                getVertices().add(new Vertex(0, 0,0));
                getVertices().add(new Vertex(Axises.XAxis.x, Axises.XAxis.y, Axises.XAxis.z));
            }
            case YAxis -> {
                getVertices().add(new Vertex(0, 0,0));
                getVertices().add(new Vertex(Axises.YAxis.x, Axises.YAxis.y, Axises.YAxis.z));
            }
            case ZAxis -> {
                getVertices().add(new Vertex(0, 0,0));
                getVertices().add(new Vertex(Axises.ZAxis.x, Axises.ZAxis.y, Axises.ZAxis.z));
            }
        }

        getIndicies().add(0); getIndicies().add(1);
    }
}