package rasterize;

import model.Line;

import java.util.ArrayList;

public class DashedLineRasterizer extends LineRasterizer{

    private int i = 0;

    public DashedLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        midpoint(x1, y1, x2, y2);
    }

    public void drawPolygonLines(ArrayList<Line> lines){
        for(Line line : lines){
            rasterize(line);
        }
    }

    //Midpoint
    private void midpoint(int x1, int y1, int x2, int y2) {
        i++;

        int sx, sy;
        sx = (x1 + x2) / 2;
        sy = (y1 + y2) / 2;

        if ((i % 15 < 8) || (i==0)) {
            raster.setPixel(sx, sy, this.color.getRGB());
        }

        if (Math.abs(x1 - sx) > 1 || Math.abs(y1 - sy) > 1) {
            midpoint(x1, y1, sx, sy);
        }

        if (Math.abs(x2 - sx) > 1 || Math.abs(y2 - sy) > 1) {
            midpoint(sx, sy, x2, y2);
        }
    }

}
