import java.io.*;
import java.util.*;

public class Main {
    static String s1 ;
    static String s2 ;
    static int l1 ;
    static int l2 ;
    static  int[][] dp;

    static int recur(int i, int j) {

        // Both strings exhausted
        if (i == l1 && j == l2) {
            return 0;
        }

        // word1 exhausted: insert remaining characters of word2
        if (i == l1) {
            return l2 - j;
        }

        // word2 exhausted: delete remaining characters of word1
        if (j == l2) {
            return l1 - i;
        }

        if(dp[i][j] != -1) return  dp[i][j] ;

        if (s1.charAt(i) == s2.charAt(j)) {
            return recur(i + 1, j + 1);
        }

        int replace = 1 + recur(i + 1, j + 1);
        int delete = 1 + recur(i + 1, j);
        int insert = 1 + recur(i, j + 1);

        return dp[i][j] = Math.min(replace, Math.min(delete, insert));
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        s1 = fs.next() ;
        s2 = fs.next() ;
        l1 = s1.length() ;
        l2 = s2.length() ;
        dp = new int[l1+1][l2+1] ;
        for(int i = 0 ; i <= l1 ; i++) {
            Arrays.fill(dp[i], -1) ;
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
