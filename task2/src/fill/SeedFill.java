package fill;

import model.Point;
import rasterize.Raster;

public class SeedFill implements Filler {

    private final Raster raster;
    private Point seed;
    private int backgroundColor;

    public Point getSeed() {
        return seed;
    }

    public void setSeed(Point seed) {
        this.seed = seed;
        backgroundColor = raster.getPixel(seed.x, seed.y);
    }

    public SeedFill(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void fill() {
        //seedFill();
    }

    private void seedFill(Point seed, int fillColor, int backgroundColor){

        if(raster.getPixel(seed.x, seed.y) == backgroundColor){
            raster.setPixel(seed.x, seed.y, fillColor);
            seedFill(new Point(seed.x+1, seed.y), fillColor, backgroundColor);
            seedFill(new Point(seed.x+1, seed.y), fillColor, backgroundColor);
            seedFill(new Point(seed.x+1, seed.y), fillColor, backgroundColor);
            seedFill(new Point(seed.x+1, seed.y), fillColor, backgroundColor);
        }
    }
}
