package fill;

import model.Point;
import rasterize.Raster;

public class SeedFill implements Filler {

    private final Raster raster;
    private Point seed;
    private int fillColor = 0xffff00;
    private int backgroundColor = 0x000000;
    private PatterFill patterFill = new PatterFill();
    private PatterFill.Patterns pattern;

    public Point getSeed() {
        return seed;
    }

    public void setSeed(Point seed) {
        this.seed = seed;
        backgroundColor = raster.getPixel(seed.getX(), seed.getY());
    }

    public SeedFill(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void fill() {
        seedFill(seed);
    }

    private void seedFill(Point seed){
        if (seed.getX() >= 0 && seed.getY() >= 0 &&
                seed.getX() < raster.getWidth() && seed.getY() < raster.getHeight()) {

            if (raster.getPixel(seed.getX(), seed.getY()) == backgroundColor) {

                raster.setPixel(seed.getX(), seed.getY(), patterFill.paint(seed.getX(), seed.getY(), pattern));

                seedFill(new Point(seed.getX() + 1, seed.getY()));
                seedFill(new Point(seed.getX() - 1, seed.getY()));
                seedFill(new Point(seed.getX(), seed.getY() + 1));
                seedFill(new Point(seed.getX(), seed.getY() - 1));
            }
        }
    }

    public void setPatter(PatterFill.Patterns pattern) {
        this.pattern = pattern;
    }
}
