package mandelbrotstream;

import java.util.Iterator;

public class CrossRange implements Iterable<int[]> {
    private final int x, yLimit;
    private int nextY;
    
    public CrossRange(int x, int yLimit) { 
        this.x = x;
        this.yLimit = yLimit;
    }

    @Override
    public Iterator<int[]> iterator() {
        return new Iterator<int[]>() {
            @Override
            public boolean hasNext() {
                return nextY < yLimit;
            }

            @Override
            public int[] next() {
                return new int[]{ x, nextY++ };
            }
        };
    }
}
