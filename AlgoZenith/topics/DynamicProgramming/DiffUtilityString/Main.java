import java.io.*;
import java.util.*;

public class Main {
    static int n ;
    static int m ;
    static String str1 ;
    static String str2 ;
    static int[][] dp ;
    static int[][] back;
    static String ans  = "";

    static int recur(int l1, int l2) {
        // pruning
        // basecase
        if(l1 == n && l2 == m) return 0  ;
        // cachecheck
        if(dp[l1][l2] != -1) return dp[l1][l2] ;
        // compute
        int ans = Integer.MAX_VALUE ;
        if(l1 < n ) {
            if(recur(l1+1, l2) + 1 < ans) {
                ans = recur(l1+1, l2) + 1;
                back[l1][l2] = 0 ;
            }

        }
        if(l2 < m) {
           if(recur(l1, l2+1) + 1 < ans) {
                ans = recur(l1, l2+1) + 1;
                back[l1][l2] = 1 ;
            }
        }
        if(l1 < n && l2 < m && str1.charAt(l1) == str2.charAt(l2)) {
            if(recur(l1+1, l2+1) + 1 < ans) {
                ans = recur(l1+1, l2+1) + 1;
                back[l1][l2] = 2 ;
            }
        }
        // return
        // return ans ;
        return dp[l1][l2] =  ans ;
    }

    static void generate(int l1, int l2) {
        int optimal = back[l1][l2] ;
        if(optimal == 0) {
            ans += "-" + str1.charAt(l1) ;
            generate(l1+1, l2) ;
        } else if (optimal == 1) {
            ans += ""+"" + str1.charAt(l2) ;
            generate(l1, l2+1) ;
        } else if (optimal ==2) {
            ans += str1.charAt(l1) ;
            generate(l1+1, l2+1) ;
        }
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        str1 = fs.next() ;
        str2 = fs.next() ;
        n = str1.length() ;
        m = str2.length() ;
        dp = new int[n+1][m+1] ;
        back = new int[n+1][m+1] ;
        for(int i = 0 ; i <= n ; i++) {
            Arrays.fill(dp[i] , -1) ;
            Arrays.fill(back[i] , -1) ;
        }
        out.append(recur(0,0)).append('\n') ;
        generate(0,0) ;
        out.append(ans).append('\n') ;

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
