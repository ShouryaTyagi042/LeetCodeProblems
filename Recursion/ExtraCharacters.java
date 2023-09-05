 class Solution {
    HashSet<String> set ;
    int[] memo;
    public int minExtraChar(String s, String[] dictionary) {
        int n = s.length() ;
        memo = new int[n] ;
        set = new HashSet<>(Arrays.asList(dictionary)) ;
        return dp(0,n,s) ;
    }
    private static int dp(int start, int n, String s) {
        if(start == n) return 0 ;
        if(memo[start] != null) {
            return memo[start] ;
        }
        int ans = dp(start+1,n,s)+1;
        for(int end = start ; end < n ; end ++) {
            String sub = s.substring(start,end +1 ) ;
            if(set.contains(sub)) {
                ans = Math.min(ans,dp(end+1,n,s)) ;
            }
        }
        return memo[start] = ans ;
    }
}
