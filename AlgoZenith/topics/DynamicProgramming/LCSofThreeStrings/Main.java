import java.io.*;
import java.util.*;

public class Main {
    static String a ;static String b ;static String c ;
    static int[][][] dp ;

    static int recur(int i , int j , int k ) {
        // basecase
        if (i == a.length() || j == b.length() || k == c.length()) {
            return 0;
        }
        // caching
        if (dp[i][j][k] != -1) {
            return dp[i][j][k];
        }
        // compute
        int ans = 0 ;
        char first = a.charAt(i) ;
        char second = b.charAt(j) ;
        char third = c.charAt(k) ;
        if ( first == second && second == third) {
            ans += 1 + recur(i+1, j+1 , k+1) ;
        } else {
            ans += Math.max(recur(i+1, j , k) , Math.max(recur(i, j+1 , k) , recur(i, j, k+1))) ;
        }

        //return
        return dp[i][j][k] = ans;
    }



    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {
            a = fs.next() ;
            b = fs.next() ;
            c = fs.next() ;

            dp = new int[a.length() + 1][b.length() + 1][c.length() + 1];

            for (int i = 0; i < dp.length; i++) {
                for (int j = 0; j < dp[i].length; j++) {
                    Arrays.fill(dp[i][j], -1);
                }
            }

            int ans = 0 ;
            for(int i = 0 ; i < a.length() ; i++) {
                for(int j = 0 ; j < b.length() ; j++) {
                    for(int k = 0 ; k < c.length() ; k++) {
                        ans = Math.max(ans, recur(i,j,k)) ;
                    }
                }
            }

            out.append(recur(0,0,0)).append('\n') ;
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
