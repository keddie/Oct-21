package mandelbrotmath;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import mandelbrotstream.CrossRange;

public class MandelbrotSet {

    public enum Threads {
        MULTITHREADED, SINGLETHREAD, STREAMED
    };
    private int[][] theSet;
    private double xMin, xMax, yMin, yMax;
    private double xRatio, yRatio;

//    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private ExecutorService executor = ForkJoinPool.commonPool();
    private CountDownLatch latch;
    private final Threads threading;

    public MandelbrotSet(
            int horizontalPixels, int verticalPixels,
            double xMin, double xMax,
            double yMin, double yMax,
            Threads threading) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

        theSet = new int[horizontalPixels][verticalPixels];

        xRatio = (xMax - xMin) / theSet.length;
        yRatio = (yMax - yMin) / theSet[0].length;

        latch = new CountDownLatch(theSet[0].length);
        // flag indicating whether to run single or multi-threaded
        this.threading = threading;
    }

    public double scaleXCoord(int x) {
        return xMin + (x * xRatio);
    }

    public double scaleYCoord(int y) {
        return yMax - (y * yRatio);
    }

    // single thread version
    public void computeRowSingleThread(int y, double yCoord) {
        for (int x = 0; x < theSet.length; x++) {
            double xCoord = scaleXCoord(x);
            MandelbrotPoint mp = new MandelbrotPoint(xCoord, yCoord);
            theSet[x][y] = mp.computePoint();
            // System.out.printf("Column %6.2f, row %6.2f, value %d\n",
            // xCoord, yCoord, theSet[x][y]);
        }
    }

    // Multithread version
    public void computeRowMultiThread(final int y, final double yCoord) {
        executor.execute(new Runnable() {
            public void run() {
                for (int x = 0; x < theSet.length; x++) {
                    double xCoord = scaleXCoord(x);
                    MandelbrotPoint mp = new MandelbrotPoint(xCoord, yCoord);
                    theSet[x][y] = mp.computePoint();
//                    System.out.printf("Column %6.2f, row %6.2f, value %d\n",
//                            xCoord, yCoord, theSet[x][y]);
                }
                latch.countDown();
            }
        });
    }

    private static void mergeArrays(Integer[][] target, Integer[][] source) {
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[0].length; j++) {
                if (source[i][j] != null) {
                    target[i][j] = source[i][j];
                }
            }
        }
    }

//    public void computeSetStreaming() {
//        System.out.println("computeSetStreaming, theSet.length is " + theSet.length);
//        // create stream of x,y locations
//        Integer[][] data = IntStream.range(0, theSet.length)
//                .parallel()
//                .unordered()
//                .mapToObj(x -> x)
//                .flatMap(x -> StreamSupport.stream(new CrossRange(x, theSet[0].length).spliterator(), true))
//                // map to MandlebrotPoints,
////                .peek(p-> System.out.println(p[0] + "," + p[1]))
//                .map(p -> new Integer[]{new Integer(p[0]), new Integer(p[1]),
//                          new Integer(new MandlebrotPoint(scaleXCoord(p[0]), scaleYCoord(p[1])).computePoint())})
//                // then collect into the Set.
//                .collect(() -> new Integer[theSet.length][theSet[0].length],
//                        (bucket, tuple) -> bucket[tuple[0]][tuple[1]] = (Integer) tuple[2],
//                        MandlebrotSet::mergeArrays);
//        for (int i = 0; i < theSet.length; i++) {
//            for (int j = 0; j < theSet[0].length; j++) {
//                theSet[i][j] = data[i][j];
//            }
//        }
//        System.out.println("Streaming computation completed...");
//    }

    public void computeSetStreaming() {
        System.out.println("computeSetStreaming2, theSet.length is " + theSet.length);
        // create stream of x,y locations
        IntStream.range(0, theSet.length)
                .parallel()
                .unordered()
                .mapToObj(x -> x)
                .flatMap(x -> StreamSupport.stream(new CrossRange(x, theSet[0].length).spliterator(), true))
                // map to MandlebrotPoints,
                .map(p -> new int[]{p[0], p[1],
                          new MandelbrotPoint(scaleXCoord(p[0]), scaleYCoord(p[1])).computePoint()})
                // then collect into the Set.
                .forEach(t->theSet[t[0]][t[1]]=t[2]);
                System.out.println("Streaming computation completed...");
    }
    
    public void computeSet() {
        long startTime = System.currentTimeMillis();
        if (threading == Threads.STREAMED) {
            computeSetStreaming();
        } else {
            for (int y = 0; y < theSet[0].length; y++) {
                double yCoord = scaleYCoord(y);
//            System.out.println("yCoord is " + yCoord);
                if (threading == Threads.MULTITHREADED) {
                    computeRowMultiThread(y, yCoord);
                } else {
                    computeRowSingleThread(y, yCoord);
                }
            }
            if (threading == Threads.MULTITHREADED) {
                // wait for all jobs to complete before returning...
                try {
                    latch.await();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.printf(
                "\nTotal elapsed time performing computations is %d\n",
                (endTime - startTime));
    }

    public int getPoint(int x, int y) {
        return theSet[x][y];
    }

    public int getWidth() {
        return theSet.length;
    }

    public int getHeight() {
        return theSet[0].length;
    }
}
