package model;

import java.util.List;

public class Scene {
    private List<Solid> solids;

    public List<Solid> getSolids() {
        return solids;
    }

    public void setSolids(List<Solid> solids) {
        this.solids = solids;
    }

    public Scene(List<Solid> solids) {
        this.solids = solids;
    }
}
