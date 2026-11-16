import java.io.*;
import java.util.*;

public class Main {
    static int n ;
    static int m ;
    static int[][] grid ;
    static int[][] dp ;

    static int recur(int i, int j) {
        if(i >= n  || j >= m || i < 0 || j < 0) return Integer.MAX_VALUE;
        // basecase
        if(i == n - 1 && j == m - 1 ) {
            return Math.max(1, 1 - grid[i][j] ) ;
        }

        if(dp[i][j] != -1) return dp[i][j] ;

        int ans = 0 ;
        int right = recur(i, j+1) ;
        int down = recur(i+1, j) ;
        ans = Math.max(1 , Math.min(right , down) - grid[i][j] ) ;

        return dp[i][j] =  ans ;

    }


    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {
            n = fs.nextInt();
            m = fs.nextInt();
            grid = new int[n][m] ;
            dp = new int[n][m] ;
            for(int i = 0 ; i < n ; i++) {
                Arrays.fill(dp[i], -1) ;
                for(int j = 0 ; j < m ; j++) {
                    grid[i][j] = fs.nextInt() ;
                }
            }
            out.append(recur(0,0)).append('\n') ;

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
