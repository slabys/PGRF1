package model;

import transforms.Mat4;
import transforms.Mat4Identity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Solid {

    private boolean activeSolid = false;
    private List<Vertex> vertices = new ArrayList<>();
    private List<Integer> indicies = new ArrayList<>();

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    private Mat4 model = new Mat4Identity();
    private Color color = new Color(Color.RED.getRGB());

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

    public boolean isActiveSolid() {
        return activeSolid;
    }

    public void setActiveSolid() {
        this.activeSolid = true;
    }

    public void deActiveSolid() {
        this.activeSolid = false;
    }
}
