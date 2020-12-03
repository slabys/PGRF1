package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Solid {
    private List<Vertex> vertices = new ArrayList<>();
    private List<Integer> indicies = new ArrayList<>();
    private Color color = Color.RED;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Integer> getIndicies() {
        return indicies;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}
