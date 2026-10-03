import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static int[][] data;
    static int[][] dp;

    static int recur(int i, int last) {
        // base case
        if (i == n) {
            return 0;
        }

        // cache check
        if (dp[i][last] != -1) {
            return dp[i][last];
        }

        // compute
        int ans = 0;

        for (int j = 0; j < 3; j++) {
            if (last != j) {
                ans = Math.max(ans, data[i][j] + recur(i + 1, j));
            }
        }

        // return
        return dp[i][last] = ans;
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();

        n = fs.nextInt();

        data = new int[n][3];

        // last can be 0, 1, 2, or 3
        // 3 means "no activity chosen previously"
        dp = new int[n][4];

        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], -1);

            for (int j = 0; j < 3; j++) {
                data[i][j] = fs.nextInt();
            }
        }

        // 3 = no previous activity
        System.out.println(recur(0, 3));
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

        String next() throws IOException {
            int c = skipBlank();
            StringBuilder sb = new StringBuilder();

            while (c > ' ') {
                sb.append((char) c);
                c = readByte();
            }

            return sb.toString();
        }

        double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }

        String nextLine() throws IOException {
            int c;

            while ((c = readByte()) != -1 && c == '\n');

            StringBuilder sb = new StringBuilder();

            while (c != -1 && c != '\n') {
                sb.append((char) c);
                c = readByte();
            }

            return sb.toString();
        }
    }
}
