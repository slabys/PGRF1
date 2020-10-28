package rasterize;

import model.Line;

import java.util.ArrayList;

public class DottedLineRasterizer extends LineRasterizer{

    private int dotGap = 5;

    public DottedLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        midpoint(x1, y1, x2, y2);
    }

    public void rasterize(ArrayList<Line> lines){
        for(Line line : lines){
            rasterize(line);
        }
    }

    /**
     *  Definice:
     *      Pomocí zpětné rekurze v algoritmu midpoint, která dělí úsečku na půl a vykresluje středový bod,
     *      lze zajistit, aby se vykresloval například každy x-tý bod. Toto řešení je velmi jednoduché,
     *      jelikož je možné ho implementovat v rekurzivní podmínce.
     *  Řešení:
     *      Vytvořením pomocné proměné "dotGap" jsme si nadefinovali proměnnou, která rozhoduje po kolika
     *      krocích se bude provádět rekurze a tím pádem také vykreslovat tečkovaná úsečka.
     *  Výhody:
     *      Oproti podmínce v "DashedLineRasterizer" má tato podmínka výhodu v tom, že není rekurze prováděna,
     *      pokud není dostaženo určeného počtu proměnnou "dotGap", čímž zajistíme stálost bodů při překreslování.
     *
     */
    //Midpoint
    private void midpoint(int x1, int y1, int x2, int y2) {

        int sx, sy;
        sx = (x1 + x2) / 2;
        sy = (y1 + y2) / 2;

        raster.setPixel(sx, sy, this.color.getRGB());

        if (Math.abs(x1 - sx) > dotGap || Math.abs(y1 - sy) > dotGap) {
            midpoint(x1, y1, sx, sy);
        }

        if (Math.abs(x2 - sx) > dotGap || Math.abs(y2 - sy) > dotGap) {
            midpoint(sx, sy, x2, y2);
        }
    }

}
