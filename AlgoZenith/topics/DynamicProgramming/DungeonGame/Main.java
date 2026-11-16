class Solution {
    static int n ;
    static int m ;
    static int[][] dp ;

    static int recur(int i , int j, int[][] dungeon) {
        if(i >= n || i < 0 || j >= m || j < 0) return Integer.MAX_VALUE ;

        if(i == n-1 && j == m-1) {
            return Math.max(1, 1- dungeon[i][j] ) ;
        }

        if(dp[i][j] != -1) return dp[i][j] ;
        return dp[i][j] =  Math.max(1 ,  ( Math.min(recur(i+1,j,dungeon) , recur(i,j+1,dungeon))  - dungeon[i][j]) ) ;
    }

    public int calculateMinimumHP(int[][] dungeon) {
        n = dungeon.length ;
        m = dungeon[0].length ;
        dp = new int[n][m] ;
        for(int i = 0 ; i < n ; i++) {
            Arrays.fill(dp[i] , -1) ;
        }
        return recur(0,0,dungeon) ;
    }
}
