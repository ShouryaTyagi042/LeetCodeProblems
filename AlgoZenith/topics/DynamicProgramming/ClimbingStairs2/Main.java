class Solution {
    public static int[] dp ;
    public int recur(int index, int[] costs, int n) {
        if(index > n ) return Integer.MAX_VALUE ;
        if(index == n) return 0 ;
        if(dp[index] != -1) return dp[index] ;
        int ans = Integer.MAX_VALUE ;
        for(int i = 1 ; i <= 3 ; i++) {
            if(index + i <= n && recur(index + i, costs, n) != Integer.MAX_VALUE)  {
                ans = Math.min(ans, recur(index + i, costs, n) + costs[index + i - 1] + (i*i)) ;
            }
        }
        return dp[index] =  ans ;
    }
    public int climbStairs(int n, int[] costs) {
        dp = new int[n+1] ;
        Arrays.fill(dp, -1) ;
        return recur(0,costs, n) ;
    }
}
