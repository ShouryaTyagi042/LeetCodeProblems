import java.util.* ;
import java.io.*;
public class Solution {
    public static int frogJump(int n, int heights[]) {
        int[] memo = new int[n] ;
        Arrays.fill(memo,-1) ;
        return helper(n-1,heights,memo) ;
    }
    private static int helper(int n, int heights[], int memo[] ){
        if(n==0) return 0;
        if(memo[n] != -1) return memo[n] ;
        int left = helper(n-1,heights,memo) + Math.abs(heights[n] - heights[n-1]) ;
        int right = Integer.MAX_VALUE ;
        if( n > 1) {
            right = helper(n-2,heights,memo) + Math.abs(heights[n]-heights[n-2]) ;
        }
        return memo[n] = Math.min(left,right) ;
    }

}
