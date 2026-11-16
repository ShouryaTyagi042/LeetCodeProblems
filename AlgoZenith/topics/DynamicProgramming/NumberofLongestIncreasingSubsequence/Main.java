class Solution {
    public int findNumberOfLIS(int[] arr) {
        int n = arr.length ;
        int[] dp = new int[n+1] ;
        int[] cnt = new int[n+1] ;

        Arrays.fill(dp, 1) ;
        Arrays.fill(cnt, 1) ;
        int maxLen = 0 ;
        for(int i = 0 ; i < n ; i++) {
            for(int j = 0 ; j < i ; j++) {
                if(arr[i] > arr[j]) {
                    int len = dp[j] + 1 ;
                    if(len  > dp[i] ) {
                        dp[i] = len ;
                        cnt[i] = cnt[j] ;
                    } else if (len == dp[i]) {
                        cnt[i] += cnt[j] ;
                    }
                }
            }
            maxLen = Math.max(maxLen, dp[i]) ;
        }
        int ans = 0 ;
        for(int i = 0 ; i < n ; i++) {
            if(dp[i] == maxLen) {
                ans += cnt[i] ;
            }
        }
        return ans ;
    }
}
