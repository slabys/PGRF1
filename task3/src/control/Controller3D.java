package control;

import model.Scene;
import model.Solid;
import rasterize.LineRasterizerGraphics;
import rasterize.Raster;
import render.Renderer;
import solids.Axis;
import solids.Circle;
import solids.Cube;
import solids.Tetrahedron;
import transforms.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller3D implements Controller {

    private final Panel panel;

    private LineRasterizerGraphics lineRasterizer;

    private Axis xAxis = new Axis(Axis.Axises.XAxis, Color.RED);
    private Axis yAxis = new Axis(Axis.Axises.YAxis, Color.GREEN);
    private Axis zAxis = new Axis(Axis.Axises.ZAxis, Color.BLUE);
    private List<Solid> axis = new ArrayList();

    private Scene scene;
    private Tetrahedron tetrahedron = new Tetrahedron();
    private Cube cube = new Cube(1,1,1,4);
    private Circle circle = new Circle();
    private JComboBox comboBox = new JComboBox();

    private Renderer renderer;
    private Camera cameraView = new Camera().withPosition(new Vec3D(-7, 4.5, 5)).withFirstPerson(true);
    private int xPrev = 0, yPrev = 0;
    private double yTransform = 0, xTransform = 0, zTransform = 0,
            yInc = 0, xInc = 0, zInc = 0, zoom = 1;
    private boolean perspective = true;
    private Solid active = null;
    private Color changeColor = null;

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

        initSolids();
        initMat();
        initColors();

        panel.requestFocus();
        panel.requestFocusInWindow();
    }

    private void initColors() {
        tetrahedron.setColor(Color.CYAN);
        cube.setColor(Color.MAGENTA);
        circle.setColor(Color.orange);
    }

    private void initMat() {
        renderer.setModel(new Mat4RotXYZ(0, 0, 0)); //nastaveni matic
        renderer.setView(cameraView.getViewMatrix());
        renderer.setProjection(new Mat4PerspRH((float)Math.PI/1.75,1, 0, 10));
    }

    private void initSolids() {
        axis.add(xAxis);
        axis.add(yAxis);
        axis.add(zAxis);

        scene = new Scene(Arrays.asList(tetrahedron,circle,cube));
    }

    @Override
    public void initListeners(Panel panel) {

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    cameraView = cameraView.addAzimuth((float) Math.PI * (e.getX() - xPrev) / (float) panel.getWidth());
                    cameraView = cameraView.addZenith((float) Math.PI * (e.getY() - yPrev) / (float) panel.getWidth());
                    xPrev = e.getX();
                    yPrev = e.getY();
                    update();
                }
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xPrev = e.getX();
                yPrev = e.getY();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_F1){
                    showHint();
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
                if(e.getKeyCode() == KeyEvent.VK_T){
                    JOptionPane.showMessageDialog(null,
                            getSolidDialog(), "Choose solid dialog", JOptionPane.PLAIN_MESSAGE);
                    JOptionPane.showMessageDialog(null,
                            getTransformDialog(), comboBox.getSelectedItem().toString() + " transform dialog", JOptionPane.PLAIN_MESSAGE);
                    update();
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
                        zoom += 0.1;
                        break;
                    case KeyEvent.VK_2:
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



    private void showHint() {
        Label label = new Label("R, Q | R, F | Y, C - Object rotations \n" +
                "W, S, A, D, SHIFT, CTRL - Camera movement \n" +
                "UP, Down, Left, Right, Num8, Num5 - Object movement \n" +
                "Left mouse button - Camera movement \n" +
                "P - Change perspective \n"
        );
        JOptionPane.showMessageDialog(
                null, label.getText(), "Help window", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel getSolidDialog() {
        deActivate();

        JPanel jPanel = new JPanel();
        jPanel.add(new Label("Choose what solid do you want to change."));
        List<String> options = new ArrayList<>();

        for (Solid s : scene.getSolids()){
            String tmpStr = s.getClass().getName();
            options.add(tmpStr.substring(7));
        }

        JComboBox comboBox = new JComboBox(options.toArray());
        setComboBox(comboBox);
        comboBox.addItemListener(e -> setComboBox(comboBox));
        jPanel.add(comboBox);
        return jPanel;
    }

    private JPanel getTransformDialog() {
        JPanel jPanel = new JPanel();
        Button btnColor = new Button("Change color");
        Button btnActive = new Button(
                "Set active: " + comboBox.getSelectedItem().toString());

        for(Solid solid : scene.getSolids()){
            if(solid.getClass().getName().substring(7).equals(comboBox.getSelectedItem().toString())){
                active = solid;
                btnColor.setBackground(solid.getColor());
            }
        }

        ActionListener colorListener = actionEvent -> {
            changeColor = JColorChooser.showDialog(null, "Choose color",
                    btnColor.getBackground());
                    active.setColor(changeColor);
            if (changeColor != null) {
                btnColor.setBackground(changeColor);
            }
        };

        ActionListener transformListener = actionEvent -> {
            active.setActiveSolid();
            for (Solid s : scene.getSolids()){
                System.out.println(s.isActiveSolid());
            }
        };

        btnActive.addActionListener(transformListener);
        btnColor.addActionListener(colorListener);
        jPanel.add(btnColor);
        jPanel.add(btnActive);
        return jPanel;
    }

    private void deActivate() {
        for(Solid deActive : scene.getSolids()){
            deActive.deActiveSolid();
        }
    }

    private void setComboBox(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    private void update() {
        panel.clear();

        for(Solid as : scene.getSolids()){
            if(as.isActiveSolid()){
                as.setModel(new Mat4RotXYZ(xInc, yInc, zInc)
                                .mul(new Mat4Transl(xTransform, yTransform, zTransform))
                                .mul(new Mat4Scale(zoom,zoom,zoom)));
            }
        }

        renderer.renderAxis(axis);

        renderer.render(scene);

        renderer.setView(cameraView.getViewMatrix());
    }
}
