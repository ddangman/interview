package com.interview.string;

/**
 * Date 09/22/2014
 * @author tusroy
 * 
 * Do pattern matching using KMP algorithm
 * 
 * Runtime complexity - O(m + n) where m is length of text and n is length of pattern
 * Space complexity - O(n)
 */
public class SubstringSearch {

    /**
     * Slow method of pattern matching
     */
    public boolean hasSubstring(char[] text, char[] pattern){
        int i=0;
        int j=0;
        int k = 0;
        while(i < text.length && j < pattern.length){
            if(text[i] == pattern[j]){
                i++;
                j++;
            }else{
                j=0;
                k++;
                i = k;
            }
        }
        if(j == pattern.length){
            return true;
        }
        return false;
    }
    
    /**
     * Compute temporary array to maintain size of suffix which is same as prefix
     * Time/space complexity is O(size of pattern)
     */
    private int[] computeTemporaryArray(char pattern[]){
        //lps indicates longest proper prefix which is also same as suffix
        // at every point
        int [] lps = new int[pattern.length];
        int index =0; //index of prefix
        for(int i=1; i < pattern.length;){ //i: index of suffix
            if(pattern[i] == pattern[index]){
                // track where prefix[index] matches suffix[index]
                lps[i] = index + 1;
                // match found, increment both prefix & suffix index
                index++;
                i++;
            }else{
                if(index != 0){
                    // no match, go to prefix[index] of last matching char
                    // compare new prefix[index] to current suffix[index]
                    index = lps[index-1];
                    // do not increment suffix[index] unless prefix[index] is 0
                }else{
                    // no matching prefix string for current suffix
                    lps[i] =0; // indicates no matching prefix for current suffix
                    i++; // increment suffix[index] and continue search for matching prefix
                }
            }
        }
        return lps;
    }
    
    /**
     * KMP algorithm of pattern matching.
     */
    public boolean KMP(char []text, char []pattern){
        
        int lps[] = computeTemporaryArray(pattern);
        int i=0;
        int j=0;
        while(i < text.length && j < pattern.length){
            if(text[i] == pattern[j]){
                i++;
                j++;
            }else{
                if(j!=0){
                    j = lps[j-1];
                }else{
                    i++;
                }
            }
        }
        if(j == pattern.length){
            return true;
        }
        return false;
    }
        
    public static void main(String args[]){
        
        String str = "abcxabcdabcdabcy";
        String subString = "abcdabcy";
        SubstringSearch ss = new SubstringSearch();
        boolean result = ss.KMP(str.toCharArray(), subString.toCharArray());
        System.out.print(result);
        
    }
}
