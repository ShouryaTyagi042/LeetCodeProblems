import java.io.*;
import java.util.*;

public class Main {
    static int n ;
    static String s ;
    static long[] dp ;

    static long recur(int i) {
        // Base case
        if (i == s.length()) return 1;

        // A leading zero cannot be decoded
        if (s.charAt(i) == '0') return 0;

        if(dp[i] != -1) return dp[i] ;

        long ans = recur(i + 1);

        // Try decoding two digits
        if (i + 1 < s.length()) {
            int num = (s.charAt(i) - '0') * 10
                    + (s.charAt(i + 1) - '0');

            if (num >= 10 && num <= 26) {
                ans += recur(i + 2);
            }
        }

        return dp[i] = ans;
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        boolean flag = true ;
        while(flag == true) {
            s = fs.next() ;
            if(s.charAt(0) - '0' == 0) {
                break ;
            }
            dp = new long[s.length() + 2] ;
            Arrays.fill(dp, -1) ;
            out.append(recur(0)).append('\n') ;
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
