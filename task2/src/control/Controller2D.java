package control;

import fill.ScanLine;
import fill.SeedFill;
import model.Line;
import model.Point;
import model.Polygon;
import rasterize.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller2D implements Controller {

    private final Panel panel;

    private Point p1, p2;
    private Point firstPoint, lastPoint;
    private boolean firstAction = true;
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;
    private SeedFill seedFill;
    private ScanLine scanLine;

    private Polygon polygon = new Polygon();

    public Controller2D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners(panel);
    }

    public void initObjects(Raster raster) {
        lineRasterizer = new LineRasterizerGraphics(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);
        seedFill = new SeedFill(raster);
        scanLine = new ScanLine(lineRasterizer);
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    polygonDraw(e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isControlDown()) {
                    return;
                }

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isMiddleMouseButton(e)) { //SeedFill
                    polygon.editClosest(e.getX(), e.getY());
                    update();
                } else if (SwingUtilities.isRightMouseButton(e)) {

                    scanLine.setPolygon(polygon);
                    scanLine.fill();

                   // seedFill.setSeed(new Point(e.getX(), e.getY()));
                    //seedFill.fill();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //TODO
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        //TODO
                    }
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    if (polygon.getPolygonPointList().size() > 1) {
                        lineRasterizer.rasterize(new Line(
                                new Point(e.getX(), e.getY()),
                                firstPoint,
                                Color.CYAN.getRGB()));
                        lineRasterizer.rasterize(new Line(
                                new Point(e.getX(), e.getY()),
                                lastPoint,
                                Color.PINK.getRGB()));
                    }

                    update();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                }
                update();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //Erase everything on "C" key press
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    hardClear();
                }
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void polygonDraw(int x, int y) {
        if (firstAction) {
            p1 = new Point(x, y);
            firstPoint = p1;
            polygon.addPoint(p1);
            firstAction = !firstAction;
        } else {
            p2 = new Point(x, y);
            lastPoint = p2;
            polygon.addPoint(p2);
            lineRasterizer.rasterize(p1, p2);
            p1 = p2;
        }
        update();
    }

    private void update() {
        panel.clear();
        //Redraw polygon on move
        polygonRasterizer.rasterize(polygon);
        panel.repaint();
    }

    private void hardClear() {
        panel.clear();
        polygon = new Polygon();
        firstPoint = null;
        lastPoint = null;
        firstAction = true;
    }

}
