package solids;

import model.Solid;
import model.Vertex;

public class Tetrahedron extends Solid {

    public Tetrahedron(){
        super();
        getVertices().add(new Vertex(0, 5,0));
        getVertices().add(new Vertex(2, 5,0));
        getVertices().add(new Vertex(5, 10,0));
        getVertices().add(new Vertex(0, 5,2));

        getIndicies().add(0); getIndicies().add(1);
        getIndicies().add(0); getIndicies().add(2);
        getIndicies().add(0); getIndicies().add(3);
        getIndicies().add(2); getIndicies().add(1);
        getIndicies().add(3); getIndicies().add(1);
        getIndicies().add(3); getIndicies().add(2);
    }
}
