import java.io.*;
import java.util.*;

public class Main {
    static int n ;
    static int m ;
    static int[][] grid ;
    static int[][] dp ;

    static int recur(int i, int j ) {
        // pruning
        if(i >= n  || j >= m || i < 0 || j < 0) return 0;
        // basecase
        if(grid[i][j] == 1) {
            if(i == 0 && j == 0) {
                return 1 ;
            }
        }
        // cachecheck
        if(dp[i][j] != -1) return dp[i][j] ;
        // compute
        int ans = 0 ;
        if(grid[i][j] == 1 ) {
            ans = 1 + Math.min(recur(i-1,j) , Math.min(recur(i-1, j-1) , recur(i, j-1))) ;
        }
        // return
        return dp[i][j] = ans ;
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {
            n = fs.nextInt() ;
            m = fs.nextInt() ;
            grid = new int[n][m] ;
            dp = new int[n][m] ;
            for(int i = 0 ; i < n ; i++) {
                Arrays.fill(dp[i], -1) ;
                for(int j = 0 ; j < m ; j++) {
                    grid[i][j] = fs.nextInt() ;
                }
            }
            int ans =  0 ;
            for(int i = 0 ; i < n ; i++) {
                for(int j = 0 ; j < m ; j++) {
                    ans = Math.max(ans, recur(i,j)) ;
                }
            }
            out.append(ans * ans ).append('\n') ;
        }
        System.out.println(out);
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
