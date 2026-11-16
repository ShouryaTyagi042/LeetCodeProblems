import java.io.*;
import java.util.*;

public class Main {
    static final int MOD = 1000000007;
    static int n, m;
    static int[][] grid;
    static int[][][] dp;

    static int f(int i, int j, int k) {
        // Pruning
        if (k < 0) return 0;
        if (i == n || j == m) return 0;

        // Base case
        if (i == n - 1 && j == m - 1) {
            if (k - grid[i][j] >= 0) return 1;
            else return 0;
        }

        // Return memoized result
        if (dp[i][j][k] != -1) return dp[i][j][k];

        // Transition: Move down and move right
        long ans = f(i + 1, j, k - grid[i][j]);
        ans = (ans + f(i, j + 1, k - grid[i][j])) % MOD;

        return dp[i][j][k] = (int) ans;
    }

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) return;

        StringTokenizer st = new StringTokenizer(line);
        int t = Integer.parseInt(st.nextToken());
        StringBuilder sb = new StringBuilder();

        while (t-- > 0) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            grid = new int[n][m];
            for (int i = 0; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < m; j++) {
                    grid[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            // Initialize 3D DP array with -1
            dp = new int[n][m][k + 1];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    Arrays.fill(dp[i][j], -1);
                }
            }

            sb.append(f(0, 0, k)).append("\n");
        }
        System.out.print(sb);
    }
}
