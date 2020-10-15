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
        double q = y1-k*x1;

        if(Math.abs(y2-y1) < Math.abs(x2-x1)){
            if(x2<x1){
                int tmp = x1;
                x1 = x2;
                x2 = tmp;
                tmp = y1;
                y1 = y2;
                y2 = tmp;
                for(int x = x1; x<=x2; x++) {
                    double y = k*x + q;
                    raster.setPixel(x, (int)y, 0xff0000);
                }
            }else{
                if(y2 < y1){
                    int tmp = x1;
                    x1 = x2;
                    x2 = tmp;
                    tmp = y1;
                    y1 = y2;
                    y2 = tmp;
                }
                for(int y = y1; y<=y2; y++) {
                    double x = (y-q)/k;
                    raster.setPixel(y, (int)x, 0xff0000);
                }
            }
        }

    }
}
