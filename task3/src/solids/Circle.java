package solids;

import model.Solid;
import model.Vertex;

public class Circle extends Solid {
    public Circle() {
        super();

        for (int i = 0; i < 36; i++) {
            getVertices().add(
                    new Vertex(
                            1,
                            Math.cos(Math.PI / 16 * i),
                            Math.sin(Math.PI / 16 * i)
                    )
            );
        }
        for (int i = 0; i < 35; i++) {
            getIndicies().add(i);
            getIndicies().add(i++);
        }
    }
}
