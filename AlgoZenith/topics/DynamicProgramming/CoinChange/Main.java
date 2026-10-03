class Solution {
    static int[] arr;
    static int target;
    static int n;
    static int[][] dp;

    static int recur(int i, int sumt) {
        if (sumt == target) return 0;

        if (i == n) return Integer.MAX_VALUE;

        if (dp[i][sumt] != -1) {
            return dp[i][sumt];
        }

        int notTake = recur(i + 1, sumt);

        int take = Integer.MAX_VALUE;
        if (arr[i] <= target - sumt) {
            take = recur(i, sumt + arr[i]);
            if (take != Integer.MAX_VALUE) {
                take++;
            }
        }

        return dp[i][sumt] = Math.min(notTake, take);
    }

    public int coinChange(int[] coins, int amount) {
        n = coins.length;
        target = amount;
        arr = coins;
        dp = new int[n][amount + 1];

        for (int i = 0; i < n; i++) {
            java.util.Arrays.fill(dp[i], -1);
        }

        int ans = recur(0, 0);

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}
