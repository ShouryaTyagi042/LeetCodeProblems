class Solution {

    int[][] dp;

    int recur(int l, int r, int[] arr) {
        // No balloons in this interval
        if (l > r) {
            return 0;
        }

        // Already calculated
        if (dp[l][r] != -1) {
            return dp[l][r];
        }

        int ans = 0;

        // Choose the balloon that is burst LAST
        for (int mid = l; mid <= r; mid++) {

            int coins =
                recur(l, mid - 1, arr)
                + recur(mid + 1, r, arr)
                + arr[l - 1] * arr[mid] * arr[r + 1];

            ans = Math.max(ans, coins);
        }

        return dp[l][r] = ans;
    }

    public int maxCoins(int[] nums) {

        int n = nums.length;

        // Add virtual balloons with value 1
        int[] arr = new int[n + 2];

        arr[0] = 1;
        arr[n + 1] = 1;

        for (int i = 1; i <= n; i++) {
            arr[i] = nums[i - 1];
        }

        dp = new int[n + 2][n + 2];

        for (int i = 0; i < n + 2; i++) {
            Arrays.fill(dp[i], -1);
        }

        return recur(1, n, arr);
    }
}
