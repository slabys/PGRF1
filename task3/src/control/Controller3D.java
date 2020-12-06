package control;

import rasterize.LineRasterizerGraphics;
import rasterize.Raster;
import render.Renderer;
import solids.Axis;
import solids.Cube;
import solids.Circle;
import solids.Tetrahedron;
import transforms.Camera;
import transforms.Mat4RotXYZ;
import transforms.Vec3D;
import view.Panel;

import java.awt.*;
import java.awt.event.*;

public class Controller3D implements Controller {

    private final Panel panel;

    private LineRasterizerGraphics lineRasterizer;

    private Axis xAxis = new Axis(Axis.Axises.XAxis, Color.RED);
    private Axis yAxis = new Axis(Axis.Axises.YAxis, Color.GREEN);
    private Axis zAxis = new Axis(Axis.Axises.ZAxis, Color.BLUE);
    private Tetrahedron tetrahedron = new Tetrahedron();
    private Cube cube = new Cube(1,1,1,4);
    private Circle circle = new Circle();

    private Renderer renderer;
    private double zInc = 0;
    private Camera cameraView = new Camera().withPosition(new Vec3D(-5, 0, 0));
    private int xPrev, yPrev;

    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners(panel);
        update();
    }

    public void initObjects(Raster raster) {
        lineRasterizer = new LineRasterizerGraphics(raster);
        renderer = new Renderer(raster, lineRasterizer);
        renderer.setView(cameraView.getViewMatrix());
        panel.requestFocus();
        panel.requestFocusInWindow();

        tetrahedron.setColor(Color.CYAN);
        cube.setColor(Color.MAGENTA);
        circle.setColor(Color.orange);
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("aaa");
                cameraView = cameraView.addAzimuth(Math.PI * (float)(xPrev - e.getX()) / (float)panel.getWidth());
                cameraView = cameraView.addZenith(Math.PI * (float)(e.getY() - yPrev) / (float)panel.getHeight());
                xPrev = e.getX();
                yPrev = e.getY();
                System.out.println(cameraView);
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
                if(e.getKeyCode() == KeyEvent.VK_P){
                    zInc += 0.1;
                    renderer.setModel(new Mat4RotXYZ(Math.PI / 2, Math.PI / 3, zInc+Math.PI / 4));
                    //renderer.setModel(new Mat4RotXYZ(Math.PI/3, Math.PI/4, zInc*Math.PI/5));
                }
                if(e.getKeyCode() == KeyEvent.VK_W) {
                    cameraView = cameraView.forward(0.1) ;
                    update();
                }
                if(e.getKeyCode() == KeyEvent.VK_S){
                    cameraView = cameraView.backward(0.1);
                    update();
                }
                if(e.getKeyCode() == KeyEvent.VK_D){
                    cameraView = cameraView.right(0.1);
                    update();
                }
                if(e.getKeyCode() == KeyEvent.VK_A){
                    cameraView = cameraView.left(0.1);
                    update();
                }
                update();
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

    private void update() {
        panel.clear();

        renderer.render(xAxis);
        renderer.render(yAxis);
        renderer.render(zAxis);

        renderer.render(tetrahedron);
        renderer.render(cube);
        renderer.render(circle);

        renderer.setView(cameraView.getViewMatrix());
    }

    private void hardClear() {
        panel.clear();
    }
}
