package com.interview.misc;

/**
 * Date 04/28/2016
 * @author Tushar Roy
 *
 * Find range minimum query using sparse table.
 * Sparse table row indicates start index, column indicates 2^c inclusive index range of input[]
 * so value at sparse[r][c] is minimum value of input[r] to input[r + 2^c -1]
 * -1 required for ending range to include starting index but exclude ending index
 *
 * Preprocessing Time complexity O(nlogn)
     (n rows) * (logn +1 columns)
 * Query Time complexity O(1)
 * Space complexity O(nlogn)
     (n rows) * (logn +1 columns)
 *
 * Reference -
 * https://www.topcoder.com/community/data-science/data-science-tutorials/range-minimum-query-and-lowest-common-ancestor/
 */
public class SparseTableRangeMinimumQuery {

    private final int[][] sparse;
    private final int n;
    private final int[] input;

    public SparseTableRangeMinimumQuery(int[] input) {
        this.input = input;
        this.n = input.length;
        this.sparse = preprocess(input, this.n);
    }

    // n is length of input
    private int[][] preprocess(int[] input, int n) {
        // holds input[index] of minimum value
        int[][] sparse = new int[n][log2(n) + 1];
        for (int r = 0; r < input.length; r++) {
            // range == 2^0 = 1, no comparison needed for 1 element, just copy index
            sparse[r][0] = r;
        }

        // Binary Left Shift 1 << c == 2^c
        for (int c = 1; 1 << c <= n; c++) { // limit column range to n
            // range is input[r] to input[r + 2^c -1]
            // includes input[r] but excludes input[r + 2^c]
            for (int r = 0; r + (1 << c) - 1 < n; r++) { // limit row + range under n
                // store index of minimum value by comparing previously calculated values
                // min(row through range, row+range through range)
                if (input[sparse[r][c - 1]] < input[sparse[r + (1 << (c - 1))][c - 1]]) {
                    sparse[r][c] = sparse[r][c - 1];
                } else {
                    sparse[r][c] = sparse[r + (1 << (c - 1))][c - 1];
                }
            }
        }
        return sparse;
    }

    public int rangeMinimumQuery(int low, int high) {
        int l = high - low + 1;
        int k = log2(l);
        if (input[sparse[low][k]] <= input[sparse[low + l - (1<<k)][k]]) {
            return input[sparse[low][k]];
        } else {
            return input[sparse[high - (1<<k) + 1][k]];
        }
    }

    private static int log2(int n){
        if(n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    public static void main(String args[]) {
        int[] input = {2, 5, 3, 6, 4, 1, -1, 3, 4, 2};
        SparseTableRangeMinimumQuery sparseTableRangeMinimumQuery = new SparseTableRangeMinimumQuery(input);
        for (int i = 0; i < input.length; i++) {
            for (int j = i; j < input.length; j++) {
                System.out.print(sparseTableRangeMinimumQuery.rangeMinimumQuery(i, j) + " ");
            }
            System.out.println();
        }
    }
}
