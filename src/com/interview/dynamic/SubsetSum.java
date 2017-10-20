package com.interview.dynamic;
/*
 * Date 09/23/2014
 * @author Tushar Roy
 *
 * Given an array of non-negative numbers and a total, is there subset of numbers in this array which adds up
 * to given total. Another variation is given an array is it possible to split it up into 2 equal
 * sum partitions. Partition need not be equal sized. Just equal sum.
 *
 * Time complexity - O(input.size * total_sum)
 * Space complexity - O(input.size*total_sum)
 *
 * Youtube video - https://youtu.be/s6FhG--P7z0
 */
public class SubsetSum {

    // boolean variation of 0/1 knapsack
    public boolean subsetSum(int input[], int total) {
        //input[]==subsetIntegerArray so input[i-1]==subsetInt
        boolean T[][] = new boolean[input.length + 1][total + 1];
        for (int i = 0; i <= input.length; i++) {
            T[i][0] = true; //sum==0 is always possible
        }

        for (int v = 1; v <= input.length; v++) {
            for (int t = 1; t <= total; t++) {
                if (t - input[v - 1] >= 0) { // useable value
                    //true if above row is true || [row-1][col-subsetInt]
                    T[v][t] = T[v - 1][t] || T[v - 1][t - input[v - 1]];
                } else {
                    // if (subsetInt <= total), copy from above row
                    T[v][t] = T[v-1][t];
                }
            }
        }
        return T[input.length][total]; //answer

    }

    public boolean partition(int arr[]) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        if (sum % 2 != 0) {
            return false;
        }
        sum = sum / 2; // target sum is half summation
        boolean[][] T = new boolean[arr.length + 1][sum + 1];

        for (int i = 0; i <= arr.length; i++) {
            T[i][0] = true;
        }

        for (int v = 1; v <= arr.length; v++) {
            for (int s = 1; s <= sum; s++) {
                if (s - arr[v - 1] >= 0) {
                    T[v][s] = T[v - 1][s] || T[v - 1][s - arr[v - 1]];
                } else {
                    T[v][s] = T[v-1][s];
                }
            }
        }
        return T[arr.length][sum];
    }

    public static void main(String args[]) {
        SubsetSum ss = new SubsetSum();
        int arr[] = {1, 3, 5, 5, 2, 1, 1, 6};
        System.out.println(ss.partition(arr));

        int arr1[] = {2, 3, 7, 8};
        System.out.print(ss.subsetSum(arr1, 11));

    }
}
