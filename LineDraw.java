import model.Line;
import rasterize.LineRasterizerTrivial;
import rasterize.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LineDraw {

    private JPanel jPanel;
    private RasterBufferedImage rasterBufferedImage;
    private int x1,x2,y1,y2;
    private LineRasterizerTrivial lineRasterizerTrivial;
    int i = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new LineDraw(800, 600).start()
        );
    }

    public void start() {
        clear(0xaaaaaa);
        rasterBufferedImage.getGraphics().drawString("Use mouse and try to resize.", 5, 15);
        jPanel.repaint();
    }

    public void clear(int color) {
        rasterBufferedImage.setClearColor(color);
        rasterBufferedImage.clear();
    }

    private void draw(){
        clear(0xA222FF);
        //Draw Line
        lineRasterizerTrivial.drawLine(x1,y1,x2,y2);
        jPanel.repaint();
    }

    public LineDraw(int width, int height){
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new BorderLayout());
        jFrame.setTitle(this.getClass().getName());
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        rasterBufferedImage = new RasterBufferedImage(width, height);
        lineRasterizerTrivial = new LineRasterizerTrivial(rasterBufferedImage);
        jPanel = new JPanel(){
            private static final long serialVersionUID = 1L;
            @Override
            public void paintComponent(Graphics graphics){
                super.paintComponent(graphics);
                present(graphics);
            }
        };

        jPanel.setPreferredSize(new Dimension(width, height));

        jFrame.add(jPanel, BorderLayout.CENTER);
        jFrame.add(jPanel);
        jFrame.pack();
        jFrame.setVisible(true);

        jPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    if (i == 0) {
                        x1 = e.getX();
                        y1 = e.getY();
                        i++;
                    } else {
                        x2 = e.getX();
                        y2 = e.getY();
                        i = 0;
                        draw();
                    }
                    jPanel.repaint();
                }
            }
        });
    }

    public void present(Graphics graphics){
        rasterBufferedImage.repaint(graphics);
    }
}
