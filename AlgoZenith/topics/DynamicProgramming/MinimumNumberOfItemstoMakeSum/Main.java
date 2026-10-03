import java.io.*;
import java.util.*;

public class Main {
    static int[] arr = new int[1001] ;
    static int target ;
    static int n ;
    static int[][] dp = new int[1001][1001] ;

    static int recur(int i, int sumt) {

        // Pruning
        if (sumt > target) return Integer.MAX_VALUE;

        // Base case
        if (i == n) {
            if (sumt == target) return 0;
            return Integer.MAX_VALUE;
        }

        // Cache check
        if (dp[i][sumt] != -1) {
            return dp[i][sumt];
        }

        // Don't take
        int notTake = recur(i + 1, sumt);

        // Take
        int take = recur(i + 1  , sumt + arr[i]);

        if (take != Integer.MAX_VALUE) {
            take = 1 + take;
        }

        // Save and return
        return dp[i][sumt] = Math.min(notTake, take);
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        n = fs.nextInt() ;
        target = fs.nextInt() ;
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }
        for(int i = 0 ; i < n  ; i++) {
            arr[i] = fs.nextInt() ;
        }
        System.out.println(recur(0,0));
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
            do { c = readByte(); } while (c <= ' ');
            return c;
        }

        long nextLong() throws IOException {
            int c = skipBlank(), sign = 1;
            if (c == '-') { sign = -1; c = readByte(); }
            long val = 0;
            while (c > ' ') { val = val * 10 + (c - '0'); c = readByte(); }
            return val * sign;
        }

        int nextInt() throws IOException { return (int) nextLong(); }

        String next() throws IOException {
            int c = skipBlank();
            StringBuilder sb = new StringBuilder();
            while (c > ' ') { sb.append((char) c); c = readByte(); }
            return sb.toString();
        }

        double nextDouble() throws IOException { return Double.parseDouble(next()); }

        String nextLine() throws IOException {
            int c;
            while ((c = readByte()) != -1 && c == '\n');
            StringBuilder sb = new StringBuilder();
            while (c != -1 && c != '\n') { sb.append((char) c); c = readByte(); }
            return sb.toString();
        }
    }
}
