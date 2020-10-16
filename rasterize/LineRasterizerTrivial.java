package rasterize;

public class LineRasterizerTrivial extends LineRasterizer{
    public LineRasterizerTrivial(Raster raster) {
        super(raster);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2){

        int dx = x2-x1, dy = y2-y1;

        if (Math.abs(dy) < Math.abs(dx)) {
            if (x2 < x1) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;

                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }

            double k = (y2 - y1) / (double) (x2 - x1);
            double q = y1 - (k * x1);
            System.out.println("1: " + k +" , " + q);
            if (k < 1) {
                for (int x = x1; x <= x2; x++) {
                    double y = (k * x) + q;
                    raster.setPixel(x, (int) y, this.color.getRGB());
                }
            }
            if (k > 1) {
                for (int y = y1; y <= y2; y++) {
                    double x = (y - q) / k;
                    raster.setPixel((int) x, y, this.color.getRGB());
                }
            }
        } else {
            if (y2 < y1) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;

                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }

            double k = (y2 - y1) / (double) (x2 - x1);
            double q = y1 - (k * x1);
            System.out.println("2: " + k +" , " + q);

            if (k < 1) {
                for (int x = x1; x <= x2; x++) {
                    double y = (k * x) + q;
                    raster.setPixel(x, (int) y, this.color.getRGB());
                }
            }
            if (k > 1) {
                for (int y = y1; y <= y2; y++) {
                    double x = (y - q) / k;
                    raster.setPixel((int) x, y, this.color.getRGB());
                }
            }
        }
    }
}
