package mandelbrotdisplay;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mandelbrotmath.MandelbrotSet;

public class MandelbrotDisplay extends JPanel {

    private static final long serialVersionUID = 494363481630831393L;

    MandelbrotSet theSet;

    public MandelbrotDisplay(MandelbrotSet ms) {
        this.theSet = ms;
        this.setPreferredSize(new Dimension(ms.getWidth(), ms.getHeight()));
        MyMouseListener ml = new MyMouseListener();
        this.addMouseListener(ml);
        this.addMouseMotionListener(ml);
        this.addKeyListener(ml);
    }

    @Override
    public void paint(Graphics gr) {
//		System.out.println("painting...");
        Graphics2D g = (Graphics2D) gr;
        int width = theSet.getWidth();
        int height = theSet.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int val = theSet.getPoint(x, y);
                Color col = new Color(Color.HSBtoRGB((360 * val) / 4000.0F, 1.0F, (val == 4000 ? 0.0F : 0.5F)));
                g.setColor(col);
                g.drawLine(x, y, x, y);
            }
        }
    }

    @Override
    public boolean isFocusTraversable() {
        return true;
    }

    private class MyMouseListener implements MouseMotionListener, MouseListener, KeyListener {

        private boolean started = false;
        private int startx, starty;
        private int endx;

        @Override
        public void mouseDragged(MouseEvent e) {
            int ex = e.getX();
            if (started && ex > startx) {
                endx = ex;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            started = true;
            startx = e.getX();
            starty = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (started) {
                int ex = e.getX();
                if (ex > startx) {
                    endx = ex;
                }
                int width = MandelbrotDisplay.this.getWidth();
                int height = MandelbrotDisplay.this.getHeight();
                double xStart = theSet.scaleXCoord(startx);
                double yStart = theSet.scaleYCoord(starty);
                double xEnd = theSet.scaleXCoord(endx);
                double yEnd = theSet.scaleYCoord(starty + ((endx - startx) * height / width));

                System.out.printf("Zooming: %d, %d, %7.4f, %7.4f, %7.4f, %7.4f\n", width, height, xStart, xEnd, yEnd, yStart);
                MandelbrotDisplay.this.theSet = new MandelbrotSet(width, height, xStart, xEnd, yEnd, yStart, MandelbrotSet.Threads.STREAMED);
                MandelbrotDisplay.this.theSet.computeSet();
                MandelbrotDisplay.this.repaint();
            }
            started = false;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == ' ') {
                MandelbrotDisplay.this.theSet = new MandelbrotSet(MandelbrotDisplay.this.getWidth(), MandelbrotDisplay.this.getHeight(),
                        -2.0, 1.0, -1.0, 1.0,
                        MandelbrotSet.Threads.STREAMED);
                MandelbrotDisplay.this.theSet.computeSet();
                MandelbrotDisplay.this.repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    public static void main(String[] args) {
        MandelbrotSet ms = new MandelbrotSet(1500, 1000 /*1400*4/5*/,
                -2.0, 1.0, -1.0, 1.0,
                MandelbrotSet.Threads.STREAMED);
        ms.computeSet();
        MandelbrotDisplay md = new MandelbrotDisplay(ms);
        JFrame jf = new JFrame("Mandelbrot");
        jf.add(md, BorderLayout.CENTER);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }
}
