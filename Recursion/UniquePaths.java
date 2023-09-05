class Solution {
    int[][] memo ;
    public int uniquePaths(int m, int n) {
        memo = new int[m][n] ;
        return dp(0,0,m,n) ;
    }
    private int dp(int currentCol, int currentRow , int m , int n  ) {
        if(currentRow == m-1 && currentCol == n-1) return 1 ;
        int right = 0 ;
        int bottom = 0 ;
        if(memo[currentCol,currentRow] != null) return memo[currentCol,currentRow]  ;
        if(currentCol < m-1) right =  dp(currentCol+1,currentRow,m,n);
        if(currentRow < n-1) bottom =  dp(currentCol,currentRow+1,m,n) ;
        return memo[currentCol][currentRow] = right + bottom  ;

    }
}
