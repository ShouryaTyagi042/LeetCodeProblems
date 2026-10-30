import java.io.*;
import java.util.*;

public class Main {
    static int n ;
    static int k ;
    static int[] arr ;
    static int[] dp ;

    static int recur(int level) {

        // base case
        if (level == n-1) {
            return 0;
        }

        // cache check
        if(level < n && dp[level] != -1) return dp[level] ;

        // compute
        int ans = Integer.MAX_VALUE;

        for (int i = level + 1; i <= Math.min(n, level + k); i++) {

            int cost = recur(i);

            if (cost != Integer.MAX_VALUE) {
                ans = Math.min(
                    ans,
                    cost + Math.abs(arr[i] - arr[level])
                );
            }
        }

        return  dp[level] = ans ;
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        n = fs.nextInt() ;
        k = fs.nextInt() ;
        arr = new int[n+1] ;
        dp = new int[n+1] ;
        Arrays.fill(dp, -1) ;
        for (int i = 0 ; i < n ; i++) {
            arr[i] = fs.nextInt() ;
        }
        System.out.println(recur(0));
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
