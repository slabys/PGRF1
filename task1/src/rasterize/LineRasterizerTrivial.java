package rasterize;

public class LineRasterizerTrivial extends LineRasterizer{
    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    public void test(){
        raster.setPixel(10,10, 0xFFFF00);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2){
        double k = (y2-y1)/(double)(x2-x1);
        double q = 0;
        for(int x = x1; x<=x2; x++) {
            double y = k*x + q;
            raster.setPixel(x, (int)y, 0xff0000);
        }
    }


}
