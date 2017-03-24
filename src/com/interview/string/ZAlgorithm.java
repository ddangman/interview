package com.interview.string;

import java.util.ArrayList;
import java.util.List;

/**
 * Date 10/31/2015
 * @author Tushar Roy
 *
 * Z algorithm to pattern matching
 *
 * Time complexity - O(n + m)
 * Space complexity - O(n + m)
 *
 * http://www.geeksforgeeks.org/z-algorithm-linear-time-pattern-searching-algorithm/
 * http://www.utdallas.edu/~besp/demo/John2010/z-algorithm.htm
 */
public class ZAlgorithm {

    private int[] calculateZ(char input[]) {
        int Z[] = new int[input.length];
        // boundary of Z-box
        int left = 0;
        int right = 0;
        // nothing to compare 1st char to
        for(int k = 1; k < input.length; k++) { // k is position on Z[]
            if(k > right) {
                left = right = k; // reset box to input[0]-input[0]
                // check if Z-box is within input array
                // && compare suffix char with prefix char
                while(right < input.length && input[right] == input[right - left]) {
                    // char match, extend Z-box boundary rightward
                    right++;
                }
                /* no match */
                // right to left boundary of Z-box is length of matching substring
                Z[k] = right - left; // save size and position of matching pattern
                right--;
            } else {
                //we are operating inside Z-box
                // Z-box is suffix box that matches input prefix
                int k1 = k - left; // position in Z-box
                //if value does not stretches till right bound then just copy it.
                if(Z[k1] < right - k + 1) {
                    Z[k] = Z[k1];
                } else { //otherwise try to see if there are more matches beyond Z-box
                    left = k;
                    while(right < input.length && input[right] == input[right - left]) {
                        right++;
                    }
                    Z[k] = right - left;
                    right--;
                }
            }
        }
        return Z;
    }

    /**
     * Returns list of all indices where pattern is found in text.
     */
    public List<Integer> matchPattern(char text[], char pattern[]) {
        // holds pattern + separatingChar + text to search
        char newString[] = new char[text.length + pattern.length + 1];
        int i = 0;
        for(char ch : pattern) {
            newString[i] = ch;
            i++;
        }
        // special char separating pattern from text
        newString[i] = '$';
        i++;
        for(char ch : text) {
            newString[i] = ch;
            i++;
        }
        List<Integer> result = new ArrayList<>();
        int Z[] = calculateZ(newString);

        for(i = 0; i < Z.length ; i++) {
            if(Z[i] == pattern.length) { // complete pattern match found
                result.add(i - pattern.length - 1);
            }
        }
        return result;
    }

    public static void main(String args[]) {
        String text = "aaabcxyzaaaabczaaczabbaaaaaabc";
        String pattern = "aaabc";
        ZAlgorithm zAlgorithm = new ZAlgorithm();
        List<Integer> result = zAlgorithm.matchPattern(text.toCharArray(), pattern.toCharArray());
        result.forEach(System.out::println);
    }


}
