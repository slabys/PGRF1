import rasterize.FilledLineRasterizer;
import rasterize.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LineDraw {

    private JPanel jPanel;
    private RasterBufferedImage rasterBufferedImage;
    private int x1,x2,y1,y2;
    private FilledLineRasterizer filledLineRasterizer;
    private boolean i = true;

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
        //clear(0xA222FF);
        //Draw Line
        filledLineRasterizer.rasterize(x1,y1,x2,y2, Color.YELLOW);
        jPanel.repaint();
    }

    public LineDraw(int width, int height){
        JFrame jFrame = new JFrame();
        jFrame.setLayout(new BorderLayout());
        jFrame.setTitle(this.getClass().getName());
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        rasterBufferedImage = new RasterBufferedImage(width, height);
        filledLineRasterizer = new FilledLineRasterizer(rasterBufferedImage);
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
        jFrame.pack();
        jFrame.setVisible(true);

        jPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    if (i) {
                        x1 = e.getX();
                        y1 = e.getY();
                        i = false;
                    } else {
                        x2 = e.getX();
                        y2 = e.getY();
                        i = true;
                        draw();
                    }
                    jPanel.repaint();
                }
            }
        });

        jPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (jPanel.getWidth()<1 || jPanel.getHeight()<1)
                    return;
                if (jPanel.getWidth()<=rasterBufferedImage.getWidth()
                        && jPanel.getHeight()<=rasterBufferedImage.getHeight()) //no resize if new one is smaller
                    return;
                RasterBufferedImage newRaster = new RasterBufferedImage(jPanel.getWidth(), jPanel.getHeight());

                newRaster.draw(rasterBufferedImage);
                rasterBufferedImage = newRaster;
                filledLineRasterizer = new FilledLineRasterizer(rasterBufferedImage);

            }
        });

    }

    public void present(Graphics graphics){
        rasterBufferedImage.repaint(graphics);
    }
}
