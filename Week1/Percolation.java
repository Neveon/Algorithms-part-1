import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size = 0;
    private boolean[] nodes;
    private WeightedQuickUnionUF wquf;
    private int openCount = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        size = n; // Grid size (n x n grid)

        // wquf initializes with 0 through (n^2)-1 elements
        // The +2 are for the virtual top and bottom sites, n^2 and n^2 + 1 respectively
        wquf = new WeightedQuickUnionUF(n*n + 2); // Check connections
        nodes = new boolean[n*n]; // true if site is open
    }

    private int to1D(int x, int y) {
        // 4 x 4 (2,1) -> ((2-1) * 4 ) + 1) - 1 = 4th index (5th element)
        return (x - 1) * size + y - 1;
    }

    private boolean isValidCoordinates(int i, int j) {
        if (i > 0 && i <= size && j > 0 && j <= size) return true;

        return false;
    }

    // opens the site (row, col) if it !isopen
    public void open(int row, int col) {
        if (!isValidCoordinates(row, col)) throw new IllegalArgumentException();

        // node index of row,col
        int index = to1D(row, col);

        // Check if isOpen already, if not then open site
        if (!isOpen(row, col)) nodes[index] = true;
        openCount += 1;

        //    	System.out.println("Row: " + row + " and Col: " + col + " opened");
        // Connect the open site to top or bottom if applicable
        // First check if in first row (0 -> n in nodes indices), top site
        // Second check if in last row, bottom site
        if (index < size) {
            wquf.union(size*size, index); // union virtual top site
            
            // Corner case - size = 1
            if (size == 1) {
                wquf.union(1, 2);
            }

            // Check if below site is open, then union to root of index
            if (isValidCoordinates(row+1, col) && isOpen(row+1, col)) wquf.union(index, to1D(row+1, col));
            return;
        } else if (index < size*size && index >= (size*size) - size) { 
            wquf.union((size*size) + 1, index); // union virtual bottom site
            
            // Check if above site is open, then union to root of index
            if (isValidCoordinates(row-1, col) && isOpen(row-1, col)) wquf.union(index, to1D(row-1, col));
            return;
        } else {
            // Otherwise check row-1, row+1, and col-1, col+1 isOpen - check coordinates
            // union this new open sites to adjacent open sites

            // Preventing throwing exception in isOpen by validating first
            if (isValidCoordinates(row-1, col) && isOpen(row-1, col)) wquf.union(to1D(row-1, col), index);
            if (isValidCoordinates(row, col-1) && isOpen(row, col-1)) wquf.union(to1D(row, col-1), index);
            if (isValidCoordinates(row, col+1) && isOpen(row, col+1)) wquf.union(to1D(row, col+1), index);
            if (isValidCoordinates(row+1, col) && isOpen(row+1, col)) wquf.union(to1D(row+1, col), index);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValidCoordinates(row, col)) throw new IllegalArgumentException();

        // !false = true, false if not open
        if (!nodes[to1D(row, col)]) return false;

        //    	System.out.println("Row: " + row + " and Col: " + col + " is open");
        return true;
    }

    // is the site (row, col) full? Path of this site connects to top site?
    public boolean isFull(int row, int col) {
        if (!isValidCoordinates(row, col)) throw new IllegalArgumentException();

        // Check if site is open and connects to top site (n^2)
        if (isOpen(row, col) && wquf.find(to1D(row, col)) == size*size) {
            return true;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    // Use virtual top and bottom sites n^2 and (n^2)+1
    public boolean percolates() {

        // When .find(n^2) == .find((n^2)) +1)
        if (wquf.find(size*size) == wquf.find((size*size) + 1)) return true;
        return false;
    }

    // test client (optional)
        public static void main(String[] args) {
        	Percolation x = new Percolation(1);

        	x.open(1,1);
    //    	x.open(2,1);
        	System.out.println(x.percolates());

    //    	while(!x.percolates()) {
    //    		int i = StdRandom.uniform(1, 3);
    //    		int j = StdRandom.uniform(1, 3);
    //
    //    		if(!x.isOpen(i, j)) x.open(i, j);
    //    		 
    //    	}
    //    	
    //    	System.out.println("Number of open sites: " + x.numberOfOpenSites());
        }
}
