package com.interview.dynamic;

/**
 * http://www.geeksforgeeks.org/dynamic-programming-set-13-cutting-a-rod/
 */
public class CuttingRod {

    /**
     * Given a length of rod and an array indicating market value of smaller cuts of rod,
     * what is the maximum selling price if we are to cut the rod?
     * @param price array value indicates price for certain cut length of rod (index + 1)
     * @param length length of rod to cut
     * @return
     */
    public static int cutLength(int price[], int length) {
        // stores max profit (value) for each length (index)
        int max[] = new int[length + 1];
        for (int p = 1; p <= price.length; p++) { // iterate price[] of length +1
            // calculate max profit for each price of rod length.
            // prices are useable only if minimum length is met thus
            // starting index 'm' will be same as price[index]
            for (int m = p; m <= length; m++) { // current length of rod
                //max profit will be either using previous calculations or
                //factoring in current price[p - 1]. price index is increased
                //by one so we can use length zero equals price zero.
                // Using current price[p - 1], we have to cut that length from rod.
                // the best cut for the remainder of the rod is already calculated
                // current length is 'm', remainder of rod is 'm - p'
                max[m] = Math.max(max[m], price[p - 1] + max[m - p]);
            }
        }
        return max[length];
    }

    public int maxValue(int price[]){
        int max[] = new int[price.length+1];
        for(int i=1; i <= price.length; i++){
            for(int j=i; j <= price.length; j++){
                max[j] = Math.max(max[j], max[j-i] + price[i-1]);
            }
        }
        return max[price.length];
    }

    public int maxValue1(int price[]){
        int max[] = new int[price.length+1];
        for(int i=1; i <= price.length; i++){
            max[i] = price[i-1];
        }
        for(int i=1 ; i <= price.length; i++){
            for(int j=1; j < i ; j++){
                max[i] = Math.max(max[i], max[i-j] + max[j]);
            }
        }
        return max[price.length];
    }

    public int recursiveMaxValue(int price[],int len){
        if(len <= 0){
            return 0;
        }
        int maxValue = 0;
        for(int i=0; i < len;i++){
            int val = price[i] + recursiveMaxValue(price, len-i-1);
            if(maxValue < val){
                maxValue = val;
            }
        }
        return maxValue;
    }
    public static void main(String args[]){
        CuttingRod cr =new CuttingRod();
        int[] price = {3,5,8,9,10,20,22,25};
        long t1 = System.currentTimeMillis();
        int r = cr.recursiveMaxValue(price,8);
        long t2 = System.currentTimeMillis();
        System.out.println(r);
        System.out.println(t2 - t1);
    }
}
