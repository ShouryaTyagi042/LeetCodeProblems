import java.io.*;
import java.util.*;

public class Main {

    static long[][] data;
    static int n;
    static int capacity;
    static long[][] dp;

    static long recur(int i, int weight) {

        // Base case
        if (i == n) {
            return 0;
        }

        // Cache
        if (dp[i][weight] != -1) {
            return dp[i][weight];
        }

        // Don't take current item
        long skip = recur(i + 1, weight);

        // Take current item
        long take = 0;

        if (weight + data[i][0] <= capacity) {
            take = data[i][1] + recur(
                    i + 1,
                    (int)(weight + data[i][0])
            );
        }

        // Store answer
        return dp[i][weight] = Math.max(skip, take);
    }

    static void solve() throws Exception {

        FastScanner fs = new FastScanner();

        n = fs.nextInt();
        capacity = fs.nextInt();

        data = new long[n][2];

        for (int i = 0; i < n; i++) {
            data[i][0] = fs.nextLong(); // weight
            data[i][1] = fs.nextLong(); // value
        }

        // dp[i][weight]
        dp = new long[n][capacity + 1];

        // Initialize all states as uncomputed
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], -1);
        }

        System.out.println(recur(0, 0));
    }

    public static void main(String[] args) throws Exception {
        new Thread(null, () -> {
            try {
                solve();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "solve", 1 << 26).start();
    }

    static class FastScanner {

        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;

                if (len <= 0) {
                    return -1;
                }
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

            int c = skipBlank();
            int sign = 1;

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
