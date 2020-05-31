import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] x;
    private int trials = 0;
    private static final double CONFIDENCE_95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();


        x = new double[trials];
        this.trials = trials;

        for (int i = 0; i < x.length; i++) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                // open random sites [1, n+1)
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);

                // Only open blocked sites
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                }
            }
            // After percolation, calculate (# open sites)/(n^2), store value
            x[i] = (double) (perc.numberOfOpenSites()) / Math.pow(n, 2);

        } // After trials end

//        System.out.println("mean" + "\t\t\t= " + mean);
//        System.out.println("stddev" + "\t\t\t= " + stddev);
//        System.out.println("95% confidence interval" + "\t= [" + confidenceLo() + ", " + confidenceHi() + "]");
    }

    public double mean() {
        return StdStats.mean(x);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        // T - 1 is undefined, return Double.NaN
        if (trials == 1) {
            return Double.NaN;
        }

        return StdStats.stddev(x);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (this.mean() - ((CONFIDENCE_95 * this.stddev()) / Math.sqrt(trials)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (this.mean() + ((CONFIDENCE_95 * this.stddev()) / Math.sqrt(trials)));
    }

    public static void main(String[] args) {
        // args[0] is n
        // args[1] is trials
        new PercolationStats(
                Integer.parseInt(args[0]), Integer.parseInt(args[1])
                );

        //		PercolationStats perc_stats = new PercolationStats(2, 1006);
    }

}
