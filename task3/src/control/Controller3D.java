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
    private Camera cameraView = new Camera().withPosition(new Vec3D(-7, 4.5, 5));
    private int xPrev = 0, yPrev = 0;
    private double yTransform = 0, xTransform = 0, zTransform = 0,
            yInc = 0, xInc = 0, zInc = 0, zoom = 1;
    private boolean perspective = true;

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
                    cameraView = cameraView.addAzimuth((float) Math.PI * (e.getX() - xPrev) / (float) panel.getWidth());
                    cameraView = cameraView.addZenith((float) Math.PI * (e.getY() - yPrev) / (float) panel.getWidth());
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
                if(e.getKeyCode() == KeyEvent.VK_SHIFT){
                    cameraView = cameraView.up(0.1);
                    update();
                }
                if(e.getKeyCode() == KeyEvent.VK_CONTROL){
                    cameraView = cameraView.down(0.1);
                    update();
                }
                if(e.getKeyCode() == KeyEvent.VK_W) {
                    cameraView = cameraView.forward(0.1);
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
                if(e.getKeyCode() == KeyEvent.VK_P){
                    if(perspective){
                        renderer.setProjection(new Mat4OrthoRH(
                                20,20,0.1,10));
                    }else{
                        renderer.setProjection(new Mat4PerspRH(
                                (float)Math.PI/1.75,1, 0, 10));
                    }
                    update();
                    perspective = !perspective;
                }

                switch(e.getKeyCode()){
                    case KeyEvent.VK_E:
                        zInc +=0.1;
                        break;
                    case KeyEvent.VK_Q:
                        zInc -= 0.1;
                        break;
                    case KeyEvent.VK_R:
                        yInc += 0.1;
                        break;
                    case KeyEvent.VK_F:
                        yInc -= 0.1;
                        break;
                    case KeyEvent.VK_Z:
                        xInc -= 0.1;
                        break;
                    case KeyEvent.VK_C:
                        xInc += 0.1;
                        break;

                    case KeyEvent.VK_UP:
                        yTransform += 0.1;
                        break;
                    case KeyEvent.VK_DOWN:
                        yTransform -= 0.1;
                        break;
                    case KeyEvent.VK_LEFT:
                        xTransform -= 0.1;
                        break;
                    case KeyEvent.VK_RIGHT:
                        xTransform += 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD8:
                        zTransform += 0.1;
                        break;
                    case KeyEvent.VK_NUMPAD5:
                        zTransform -= 0.1;
                        break;
                    case KeyEvent.VK_1:
                        System.out.println(zoom);
                        zoom += 0.1;
                        break;
                    case KeyEvent.VK_2:
                        System.out.println(zoom);
                        zoom -= 0.1;
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
                        .mul(new Mat4Transl(xTransform, yTransform,zTransform))
                        .mul(new Mat4Scale(zoom,zoom,zoom)));

        renderer.render(xAxis);
        renderer.render(yAxis);
        renderer.render(zAxis);

        renderer.render(tetrahedron);
        renderer.render(cube);
        renderer.render(circle);

        renderer.setView(cameraView.getViewMatrix());
    }
}
