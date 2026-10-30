class Solution {
    public static String a ;
    public static String b ;
    public static int[][] dp ;

    public int recur(int i , int j  ) {
        if (i < 0 || j < 0) return 0;
        if(dp[i][j] != -1) return dp[i][j] ;
        int ans = 0 ;
        if(a.charAt(i) == b.charAt(j) ) {
            ans += 1 + recur(i-1, j-1) ;
        } else {
            ans += Math.max(recur(i-1,j) , recur(i,j-1)) ;
        }
        return dp[i][j] = ans ;
    }
    public int longestCommonSubsequence(String text1, String text2) {
        a = text1 ;
        b = text2 ;
        dp = new int[a.length()+1][b.length()+ 1] ;
        for(int i = 0 ; i <= a.length() ; i++) {
            Arrays.fill(dp[i], -1) ;
        }
        return recur(text1.length() - 1, text2.length() - 1) ;
    }
}
