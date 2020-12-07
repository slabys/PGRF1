package control;

import rasterize.LineRasterizerGraphics;
import rasterize.Raster;
import render.Renderer;
import solids.Axis;
import solids.Cube;
import solids.Circle;
import solids.Tetrahedron;
import transforms.*;
import view.Panel;

import javax.swing.*;
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
    private Camera cameraView = new Camera().withPosition(new Vec3D(-5, 0, 0)).addRadius(90);
    private int xPrev = 0, yPrev = 0;
    private double yTrans = 0,xTrans = 0, yInc = 0, xInc = 0, zInc = 0, zoom = 1;

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

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    cameraView = cameraView.addAzimuth(Math.PI * (xPrev - e.getX() ) / (float) panel.getWidth());
                    cameraView = cameraView.addZenith(Math.PI * (e.getX() - yPrev) / (float) panel.getHeight());
                    update();
                    xPrev = e.getX();
                    yPrev = e.getY();
                    System.out.println(cameraView);
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
                if(e.getKeyCode() == KeyEvent.VK_R){
                    renderer.setModel(new Mat4RotXYZ(Math.PI / 2, Math.PI / 3, zInc+Math.PI / 4));
                    //renderer.setModel(new Mat4RotXYZ(Math.PI/3, Math.PI/4, zInc*Math.PI/5));
                }
                if(e.getKeyCode() == KeyEvent.VK_SHIFT){
                    cameraView = cameraView.up(0.1);
                    update();
                }
                if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                    cameraView = cameraView.down(0.1);
                    update();
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
                if(e.getKeyCode() == KeyEvent.VK_O){
                    renderer.setProjection(new Mat4OrthoRH(
                            10,10,0.1,10));
                    update();
                }
                if(e.getKeyCode() == KeyEvent.VK_I){
                    renderer.setProjection(new Mat4PerspRH(
                            (float)Math.PI/6,1, 0, 100));
                    update();
                }

                switch(e.getKeyCode()){
                    case KeyEvent.VK_NUMPAD9:
                        zInc +=0.1;
                        break;
                    case KeyEvent.VK_NUMPAD7:
                        zInc -= 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD8:
                        yInc += 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD5:
                        yInc -= 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD4:
                        xInc -= 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD6:
                        xInc += 0.1;
                        break;
                    case KeyEvent.VK_RIGHT:
                        xTrans += 0.1;
                        break;
                    case KeyEvent.VK_LEFT:
                        xTrans -= 0.1;
                        break;
                    case KeyEvent.VK_UP:
                        yTrans += 0.1;
                        break;
                    case KeyEvent.VK_DOWN:
                        yTrans -= 0.1;
                        break;
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

        renderer.setModel(
                new Mat4RotXYZ(xInc, yInc, zInc)
                        .mul(new Mat4Transl(xTrans,yTrans,0))
                        .mul(new Mat4Scale(zoom,zoom,zoom)));

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
