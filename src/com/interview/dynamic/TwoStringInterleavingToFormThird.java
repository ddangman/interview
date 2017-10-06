package com.interview.dynamic;

/**
 *
 * http://www.geeksforgeeks.org/check-whether-a-given-string-is-an-interleaving-of-two-other-given-strings-set-2/
 */
public class TwoStringInterleavingToFormThird {

    // O(2^n)
    public boolean isInterleavedRecursive(char str1[], char str2[], char str3[],int pos1, int pos2, int pos3){
        if(pos1 == str1.length && pos2 == str2.length && pos3 == str3.length){
            return true; // recursion successful
        }
        
        if(pos3 == str3.length){
            return false; // end of recursion without using all input char
        }
        // char1 matches char3, increment pos1 && pos3, enter recursion ||
        // char2 matches char3, increment pos2 && pos3, enter recursion
        return (pos1 < str1.length 
                    && str1[pos1] == str3[pos3] 
                    && isInterleavedRecursive(str1, str2, str3, pos1+1, pos2, pos3+1))
            || (pos2 < str2.length 
                    && str2[pos2] == str3[pos3] 
                    && isInterleavedRecursive(str1, str2, str3, pos1, pos2+1, pos3+1));
        
    }
    // dynamic programming time and space: O(A.length * B.length)
    // string C is interleaved string of A & B
    public boolean isInterleaved(char[] A, char[] B, char[] C){
        // A runs horizontally, B runs vertically
        boolean T[][] = new boolean[A.length +1][B.length +1];
        
        // string C can be an interleaving of string A and string B only if 
        // sum of lengths A & B is equal to length of C
        if(A.length + B.length != C.length){
            return false;
        }
        
        // a: position of stringA, b: position of stringB
        for(int a=0; a < T.length; a++){ //fill matrix topRow down 
            for(int b=0; b < T[a].length; b++){ //fill matrix column left2right
                int c = a + b -1; //c: position on C
                // two empty strings have an empty string as interleaving
                if(a == 0 && b == 0){
                    T[a][b] = true;
                }
                // A is empty
                else if(a == 0){
                    if(C[c] == B[b-1]){
                        T[a][b] = T[a][b-1];
                    }
                }
                // B (column) is empty
                else if(b == 0){
                    if(A[a-1] == C[c]){
                        T[a][b] = T[a-1][b];
                    }
                }
                else{
                    // charA matches charC ? check if previous charB is valid ||
                    // charB matches charC ? check if previous charA is valid
                    T[a][b] = (A[a-1] == C[c] ? T[a-1][b] : false) 
                            || (B[b-1] == C[c] ? T[a][b-1] : false);
                }
            }
        }
        return T[A.length][B.length];
    }
    
    public static void main(String args[]){
        String str1 = "XXYM";
        String str2 = "XXZT";
        String str3 = "XXXZXYTM";
        TwoStringInterleavingToFormThird sti = new TwoStringInterleavingToFormThird();
        System.out.println(sti.isInterleaved(str1.toCharArray(), str2.toCharArray(), str3.toCharArray()));
    }
    
}
