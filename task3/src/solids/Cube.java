package solids;

import model.Solid;
import model.Vertex;

public class Cube extends Solid {
    public Cube(int x,int y,int z,int length){
        super();
        getVertices().add(new Vertex(x, y, z));
        getVertices().add(new Vertex(x + length, y, z));
        getVertices().add(new Vertex(x, y + length, z));
        getVertices().add(new Vertex(x, y, z + length));
        getVertices().add(new Vertex(x + length, y, z + length));
        getVertices().add(new Vertex(x, y + length, z + length));
        getVertices().add(new Vertex(x + length, y + length, z));
        getVertices().add(new Vertex(x + length,y+length,z+length));

        getIndicies().add(0);getIndicies().add(1);
        getIndicies().add(0);getIndicies().add(2);
        getIndicies().add(0);getIndicies().add(3);
        getIndicies().add(1);getIndicies().add(4);
        getIndicies().add(1);getIndicies().add(6);
        getIndicies().add(2);getIndicies().add(6);
        getIndicies().add(4);getIndicies().add(3);
        getIndicies().add(4);getIndicies().add(7);
        getIndicies().add(7);getIndicies().add(6);
        getIndicies().add(7);getIndicies().add(5);
        getIndicies().add(5);getIndicies().add(2);
        getIndicies().add(5);getIndicies().add(3);
    }
}
