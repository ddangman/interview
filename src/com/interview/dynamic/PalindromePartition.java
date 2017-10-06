package com.interview.dynamic;

import java.util.*;

/**
 * Date 04/03/2016
 * @author Tushar Roy
 *
 * Partitioning the string into palindromes.
 *
 * https://leetcode.com/problems/palindrome-partitioning/
 * https://leetcode.com/problems/palindrome-partitioning-ii/
 */
public class PalindromePartition {

    /*
     * Given a string s, partition s such that every substring of the partition is a palindrome.
     * Return the minimum cuts needed for a palindrome partitioning of s.
     * https://leetcode.com/problems/palindrome-partitioning-ii/
     */
    public int minCut(String str){
        if (str.length() == 0) {
            return 0;
        }

        int[] cut = new int[str.length()];
        boolean isPal[][] = new boolean[str.length()][str.length()];
        for (int e = 1; e < str.length(); e++) {
            int min = e;
            for (int s = 0; s <= e; s++) {
                if (str.charAt(e) == str.charAt(s) && (e <= s + 1 || isPal[e - 1][s + 1])) {
                    isPal[e][s] = true;
                    min = Math.min(min, s == 0 ? 0 : 1 + cut[s - 1]);
                }
            }
            cut[e] = min;
        }
        return cut[str.length() - 1];
    }

    /**
     * Given a string s, partition s such that every substring of the partition is a palindrome.
     * https://leetcode.com/problems/palindrome-partitioning/
     */
    public List<List<String>> partition(String s) {
        Map<Integer, List<List<String>>> dp = new HashMap<>();
        List<List<String>> result =  partitionUtil(s, dp, 0);
        List<List<String>> r = new ArrayList<>();
        for (List<String> l : result) {
            r.add(l);
        }
        return r;
    }

    private List<List<String>> partitionUtil(String s, Map<Integer, List<List<String>>> dp, int start) {
        if (start == s.length()) {
            List<String> r = new ArrayList<>();
            return Collections.singletonList(r);
        }

        if (dp.containsKey(start)) {
            return dp.get(start);
        }

        List<List<String>> words = new ArrayList<>();
        for (int i = start; i < s.length(); i++) {
            if (!isPalindrome(s, start, i) ) {
                continue;
            }
            String newWord = s.substring(start, i + 1);
            List<List<String>> result = partitionUtil(s, dp, i + 1);
            for (List l : result) {
                List<String> l1 = new ArrayList<>();
                l1.add(newWord);
                l1.addAll(l);
                words.add(l1);
            }
        }
        dp.put(start, words);
        return words;
    }

    private  boolean isPalindrome(String str, int r, int t) {
        while(r < t) {
            if (str.charAt(r) != str.charAt(t)) {
                return false;
            }
            r++;
            t--;
        }
        return true;
    }
}
