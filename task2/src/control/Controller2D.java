package control;

import clip.Clipper;
import fill.PatterFill;
import fill.ScanLine;
import fill.SeedFill;
import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.PolygonRasterizer;
import rasterize.Raster;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller2D implements Controller {

    private final Panel panel;

    private Point p1, p2, c1, c2;
    private Point firstPoint, lastPoint, firstPointClipper, lastPointClipper;
    private MouseEvent currentPos;
    private boolean firstAction = true;
    private boolean firstActionClipper = true;
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;
    private SeedFill seedFill;
    private ScanLine scanLine;

    private Polygon polygon = new Polygon();
    private Polygon clipper = new Polygon();

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
        setColors();
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!e.isShiftDown() && SwingUtilities.isLeftMouseButton(e)) {
                    polygonDraw(e.getX(), e.getY());
                    update();
                }
                if (e.isShiftDown()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        clipperDraw(e.getX(), e.getY());
                        update();
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        //TODO
                    }
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
                } else if (SwingUtilities.isMiddleMouseButton(e)) { //Edit closest polygon point
                    polygon.editClosest(e.getX(), e.getY());
                    update();
                } else if (SwingUtilities.isRightMouseButton(e)) { //SeedFill
                    update();
                    seedFill.setSeed(new Point(e.getX(), e.getY()));
                    seedFill.fill();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //TODO
                    } else if (SwingUtilities.isMiddleMouseButton(e)) {
                        //TODO
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        //TODO
                    }
                }

                if (e.isShiftDown()) {
                    if (SwingUtilities.isMiddleMouseButton(e)) {
                        clipper.editClosest(e.getX(), e.getY());
                        update();
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        update();
                        clipper.addPoint(clipper.getPolygonPointList().get(0));
                        scanLine.setPolygon(Clipper.clip(polygon, clipper));
                        scanLine.fill();
                        clipper.getPolygonPointList().remove(clipper.getSize() - 1);
                    }
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        update();
                        currentPos = e;
                        if (clipper.getPolygonPointList().size() > 1) {
                            lineRasterizer.rasterize(new Line(
                                    new Point(currentPos.getX(), currentPos.getY()), firstPointClipper, Color.CYAN.getRGB()));
                            lineRasterizer.rasterize(new Line(
                                    new Point(currentPos.getX(), currentPos.getY()), lastPointClipper, Color.CYAN.getRGB()));
                        }
                    }
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    update();
                    currentPos = e;
                    if (polygon.getPolygonPointList().size() > 1) {
                        lineRasterizer.rasterize(new Line(
                                new Point(currentPos.getX(), currentPos.getY()), firstPoint, Color.CYAN.getRGB()));
                        lineRasterizer.rasterize(new Line(
                                new Point(currentPos.getX(), currentPos.getY()), lastPoint, Color.CYAN.getRGB()));
                    }

                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                }
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
            lineRasterizer.rasterize(new Line(p1, p2, Color.RED.getRGB()));
            p1 = p2;
        }
        update();
    }

    private void clipperDraw(int x, int y) {
        if (firstActionClipper) {
            c1 = new Point(x, y);
            firstPointClipper = c1;
            clipper.addPoint(c1);
            firstActionClipper = !firstActionClipper;
        } else {
            c2 = new Point(x, y);
            lastPointClipper = c2;
            clipper.addPoint(c2);
            lineRasterizer.rasterize(new Line(c1, c2, Color.PINK.getRGB()));
            c1 = c2;
        }
        update();
    }

    private void update() {
        panel.clear();
        //Redraw polygon and clipper on move
        polygonRasterizer.rasterize(polygon);
        polygonRasterizer.rasterize(clipper);
        panel.repaint();
    }

    private void hardClear() {
        panel.clear();
        polygon = new Polygon();
        clipper = new Polygon();
        setColors();
        firstPoint = null;
        lastPoint = null;
        firstAction = true;
        firstActionClipper = true;
    }

    private void setColors() {
        //Setting colors
        seedFill.setPatter(PatterFill.Patterns.NewMethod);
        scanLine.setFillColor(Color.BLUE);
        polygon.setColor(Color.RED);
        clipper.setColor(Color.PINK);
    }

}
