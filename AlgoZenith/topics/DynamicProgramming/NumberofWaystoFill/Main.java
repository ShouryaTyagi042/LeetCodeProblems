import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static String s;
    static int[][][] dp;
    static String ans  = "";

    static int recur(int i, int last, int first) {

        // Base case
        if (i == n) {
            // Circular condition:
            // last character must be different from first character
            if (last == first) return 0;
            return 1;
        }

        // Cache check
        if (dp[i][last][first] != -1) {
            return dp[i][last][first];
        }

        // Compute
        int ans = 0;

        int[] choices = {0, 1, 2};

        if (s.charAt(i) != '?') {
            choices = new int[]{s.charAt(i) - 'A'};
        }

        for (int x : choices) {
            // Adjacent characters cannot be same
            if (x == last) continue;
            if(i==0) {
                ans += recur(i+1, x , x) ;
            } else {
                ans += recur(i + 1, x, first);
            }
        }

        return dp[i][last][first] = ans;
    }

    static void generate(int i , int last, int first) {
        if (i == n) {
            if (last == first) return ;
            return ;
        }

        int[] choices = {0, 1, 2};

        if (s.charAt(i) != '?') {
            choices = new int[]{s.charAt(i) - 'A'};
        }

        for (int x : choices) {
            // Adjacent characters cannot be same
            if (x == last) continue;
            if(i==0) {
                if(recur(i+1, x , x) > 0) {
                    ans += (char)('A' + x) ;
                    generate(i+1, x , x) ;
                    return ;
                }
            } else {
                if(recur(i + 1, x, first) > 0) {
                    ans += (char)('A' + x);
                    generate(i+1, x , first) ;
                    return ;
                }
            }
        }
    }

    static void solve() throws Exception {

        FastScanner fs = new FastScanner();

        n = fs.nextInt();
        s = fs.next();

        dp = new int[n][3][3];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }

        generate(0,-1,-1) ;

        System.out.println(ans);
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
