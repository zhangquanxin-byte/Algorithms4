package chapter1;

import edu.princeton.cs.algs4.*;

public class VisualAccumulator {
    private int N;          // number of data values
    private double total = 0.0;   // sample variance * (n-1)

    public VisualAccumulator(int trails, double max) {
        StdDraw.setXscale(0,trails);
        StdDraw.setYscale(0, max);
        StdDraw.setPenRadius(.005);
    }

    /**
     * Adds the specified data value to the accumulator.
     * @param  x the data value
     */
    public void addDataValue(double x) {
        N++;
        total+=x;
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.point(N, x);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(N, total/N);
    }

    /**
     * Returns the mean of the data values.
     * @return the mean of the data values
     */
    public double mean() {
        return total/N;
    }

    /**
     * Returns the number of data values.
     * @return the number of data values
     */
    public int count() {
        return N;
    }

    /**
     * Returns a string representation of this accumulator.
     * @return a string representation of this accumulator
     */
    public String toString() {
        return "n = " + N + ", mean = " + mean();
    }

    public static void main(String[] args) {
        final int T = Integer.parseInt(args[0]);
        VisualAccumulator a = new VisualAccumulator(T,1);
        for (int i = 0; i <T ; i++) {
            a.addDataValue(StdRandom.random());
        }

        StdOut.println(a);
    }

}
