package fill;


import model.Point;
import rasterize.Raster;

public class SeedFillBorder implements Filler {

    private final Raster raster;
    private Point seed;
    private int fillColor = 0x0000ff;
    private int borderColorOne;
    private int borderColorTwo;

    public void setBorderColorOne(int borderColorOne) {
        this.borderColorOne = borderColorOne  & 0xffffff;
    }

    public void setBorderColorTwo(int borderColorTwo) {
        this.borderColorTwo = borderColorTwo  & 0xffffff;
    }

    public void setSeed(Point seed) {
        this.seed = seed;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor & 0xffffff;
    }

    public SeedFillBorder(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void fill() {
        if(isBorderColor()){
            seedFill(seed);
        }
    }

    private boolean isBorderColor() {
        return borderColorOne != fillColor && borderColorTwo != fillColor;
    }

    private void seedFill(Point seed) {
        if (seed.getX() >= 0 && seed.getY() >= 0 &&
                seed.getX() < raster.getWidth() && seed.getY() < raster.getHeight()) {

            int cc = raster.getPixel(seed.getX(), seed.getY()) & 0xffffff;
            if (cc != borderColorOne && cc != borderColorTwo && cc != fillColor) {

                raster.setPixel(seed.getX(), seed.getY(), fillColor);

                seedFill(new Point(seed.getX() + 1, seed.getY()));
                seedFill(new Point(seed.getX() - 1, seed.getY()));
                seedFill(new Point(seed.getX(), seed.getY() + 1));
                seedFill(new Point(seed.getX(), seed.getY() - 1));
            }
        }
    }
}
