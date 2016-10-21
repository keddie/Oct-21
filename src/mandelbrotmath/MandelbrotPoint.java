package mandelbrotmath;

public class MandelbrotPoint {
    // x coordinate of pixel (interesting in the interval -2.5 to 1)

    private double x0;
    // y coordinate of pixel (interesting in the interval -1, 1)
    private double y0;

    public MandelbrotPoint(double x, double y) {
        x0 = x;
        y0 = y;
    }

    public int computePoint() {
        double x = 0;
        double y = 0;

        int iteration = 0;
        int max_iteration = 4000;

        while ((x * x + y * y) < 4 && iteration < max_iteration) {
            double xtemp = x * x - y * y + x0;
            y = 2 * x * y + y0;
            x = xtemp;
            iteration++;
        }
        return iteration;
    }
}
