import model.Line;
import model.Polygon;
import rasterize.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {

    private final JPanel jPanel;
    private RasterBufferedImage rasterBufferedImage;
    private FilledLineRasterizer filledLineRasterizer;
    private DashedLineRasterizer dashedLineRasterizer;
    private DottedLineRasterizer dottedLineRasterizer;
    private PolygonRasterizer polygonRasterizer;
    private model.Polygon polygon = new model.Polygon();
    private ArrayList<Line> lines = new ArrayList<>();
    private int x1,x2,y1,y2;
    private int lx1,lx2,ly1,ly2;
    private boolean polygonFirst = true;
    private boolean lineFirst = true;
    private boolean helpLines = true;
    private model.Point lastPoint;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new Main(800, 600).start()
        );
    }

    public void present(Graphics graphics){
        rasterBufferedImage.repaint(graphics);
    }

    public void start() {
        clear(0xaaaaaa);
        rasterBufferedImage.getGraphics().drawString(
                "Use left, right and middle mouse button and try to resize window.", 5, 15);
        jPanel.repaint();
    }

    public void clear(int color) {
        rasterBufferedImage.setClearColor(color);
        rasterBufferedImage.clear();
    }

    public Main(int width, int height) {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new BorderLayout());
        jFrame.setTitle(this.getClass().getName());
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        //RasterBufferedImage
        rasterBufferedImage = new RasterBufferedImage(width, height);
        //Method for initialing all the rasterizer classes
        reInit();

        jPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                present(graphics);
            }
        };

        jPanel.setPreferredSize(new Dimension(width, height));

        jFrame.add(jPanel, BorderLayout.CENTER);
        jFrame.pack();
        jFrame.setVisible(true);
        jPanel.requestFocus();
        jPanel.requestFocusInWindow();


        jPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    helpLines = true;
                    polygonDraw(e);
                }

                if (e.getButton() == MouseEvent.BUTTON2) {
                    helpLines = false;
                    polygon.editClosest(e.getX(), e.getY());
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    helpLines = false;
                    singleLineDraw(e);
                }
            }
        });

        //Clear window on press key "C"
        jPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_C){
                    polygon = new Polygon();
                    lines = new ArrayList<>();
                    lastPoint = null;
                    helpLines = true;
                    lineFirst = true;
                    clear(0xaaaaaa);
                    jPanel.repaint();
                }
            }
        });

        //Mouse move listener repainting all the components
        jPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                reDraw();
                if (polygon.getPolygonPointList().size() > 1 && helpLines) {
                    // Rasterizing moving lines for polygon
                    dashedLineRasterizer.rasterize(lastPoint.x, lastPoint.y, e.getX(), e.getY(), Color.YELLOW);
                    dashedLineRasterizer.rasterize(polygon.getPolygonPointList().get(0).x,
                            polygon.getPolygonPointList().get(0).y, e.getX(), e.getY(), Color.YELLOW);
                }
            }
        });

        //Resizable window
        jPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (jPanel.getWidth() < 1 || jPanel.getHeight() < 1)
                    return;
                if (jPanel.getWidth() <= rasterBufferedImage.getWidth()
                        && jPanel.getHeight() <= rasterBufferedImage.getHeight()) //no resize if new one is smaller
                    return;
                RasterBufferedImage newRaster = new RasterBufferedImage(jPanel.getWidth(), jPanel.getHeight());

                newRaster.draw(rasterBufferedImage);
                rasterBufferedImage = newRaster;
                reInit();
            }
        });
    }

    private void reInit() {
        filledLineRasterizer = new FilledLineRasterizer(rasterBufferedImage);
        dashedLineRasterizer = new DashedLineRasterizer(rasterBufferedImage);
        dottedLineRasterizer = new DottedLineRasterizer(rasterBufferedImage);
        polygonRasterizer = new PolygonRasterizer(filledLineRasterizer);
    }

    private void singleLineDraw(MouseEvent e) {
        if (lineFirst) {
            lx1 = e.getX();
            ly1 = e.getY();
            lineFirst=false;
        } else {
            lx2 = e.getX();
            ly2 = e.getY();
            Line line = new Line(lx1, ly1, lx2, ly2, Color.CYAN.getRGB());
            lines.add(line);
            lineFirst=true;
        }
    }

    private void polygonDraw(MouseEvent e) {
        if (polygonFirst) {
            x1 = e.getX();
            y1 = e.getY();
            model.Point point = new model.Point(x1, y1);
            polygon.addPoint(point);
            polygonFirst = false;
        } else {
            x2 = e.getX();
            y2 = e.getY();
            model.Point point = new model.Point(x2, y2);
            polygon.addPoint(point);
            lastPoint = point;
            filledLineRasterizer.rasterize(x1, y1, x2, y2, Color.GREEN);
            x1 = x2;
            y1 = y2;
        }
    }

    private void reDraw() {
        clear(0xaaaaaa);
        //Redraw single lines
        dottedLineRasterizer.rasterize(lines);
        //Redraw polygon on move
        polygonRasterizer.rasterize(polygon);
        jPanel.repaint();
    }
}
