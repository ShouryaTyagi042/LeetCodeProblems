class Solution {
    public int[] xorQueries(int[] arr, int[][] queries) {
        int n = arr.length ;
        int[] prefix = new int[n] ;
        prefix[0] = arr[0] ;
        for(int i = 1 ; i < n ; i++) {
            prefix[i] = arr[i]  ^ prefix[i-1] ;
        }
        int[] ans = new int[queries.length] ;
        for( int i = 0 ; i < queries.length ; i++) {
            int[] q = queries[i] ;
            int left = q[0] ;
            int right = q[1] ;
            if(left > 0) {
                ans[i] = prefix[right] ^  prefix[left-1] ;
            } else {
                ans[i] = prefix[right] ;
            }
        }
        return ans ;
    }
}
