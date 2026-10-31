
import java.io.*;
import java.util.*;

public class Main {

    static int n;
    static int[] arr;
    static int[] dp;
    static long[] count;

    static final long MOD = 1_000_000_007L;

    static int recur(int i) {

        // Base case
        if (i < 0) return 0;

        // Cache check
        if (dp[i] != -1) return dp[i];

        // Every element alone forms an LIS of length 1
        dp[i] = 1;
        count[i] = 1;

        for (int j = 0; j < i; j++) {

            if (arr[j] < arr[i]) {

                int len = recur(j) + 1;

                if (len > dp[i]) {
                    dp[i] = len;
                    count[i] = count[j];
                }
                else if (len == dp[i]) {
                    count[i] = (count[i] + count[j]) % MOD;
                }
            }
        }

        return dp[i];
    }

    static void solve(FastScanner fs, StringBuilder out) throws Exception {

        n = fs.nextInt();

        arr = new int[n];
        dp = new int[n];
        count = new long[n];

        Arrays.fill(dp, -1);

        for (int i = 0; i < n; i++) {
            arr[i] = fs.nextInt();
        }

        int maxLength = 0;

        // Calculate LIS ending at every index
        for (int i = 0; i < n; i++) {
            maxLength = Math.max(maxLength, recur(i));
        }

        // Aggregate counts of all globally longest subsequences
        long ans = 0;

        for (int i = 0; i < n; i++) {
            if (dp[i] == maxLength) {
                ans = (ans + count[i]) % MOD;
            }
        }

        out.append(ans).append('\n');
    }

    public static void main(String[] args) throws Exception {

        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {
            solve(fs, out);
        }

        System.out.print(out);
    }

    static class FastScanner {

        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        private int skipBlank() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ');
            return c;
        }

        long nextLong() throws IOException {
            int c = skipBlank(), sign = 1;

            if (c == '-') {
                sign = -1;
                c = readByte();
            }

            long val = 0;

            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = readByte();
            }

            return val * sign;
        }

        int nextInt() throws IOException {
            return (int) nextLong();
        }
    }
}
