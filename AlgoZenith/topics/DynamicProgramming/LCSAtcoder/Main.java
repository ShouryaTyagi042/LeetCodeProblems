
import java.io.*;
import java.util.*;

public class Main {

    public static String a;
    public static String b;
    public static int[][] dp;
    public static int[][] back;
    public static String ans = "";

    public static int recur(int i, int j) {
        if (i < 0 || j < 0) return 0;

        if (dp[i][j] != -1) return dp[i][j];

        int res;

        if (a.charAt(i) == b.charAt(j)) {
            res = 1 + recur(i - 1, j - 1);
            back[i][j] = (int) a.charAt(i);
        } else {
            int l1 = recur(i - 1, j);
            int l2 = recur(i, j - 1);

            if (l1 > l2) {
                res = l1;
                back[i][j] = 0;
            } else {
                res = l2;
                back[i][j] = 1;
            }
        }

        return dp[i][j] = res;
    }

    static void generate(int i, int j) {
        if (i < 0 || j < 0) return;

        int optimal = back[i][j];

        if (optimal == 0) {
            generate(i - 1, j);
        } else if (optimal == 1) {
            generate(i, j - 1);
        } else {
            generate(i - 1, j - 1);
            ans += (char) optimal;
        }
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();

        a = fs.next();
        b = fs.next();

        dp = new int[a.length()][b.length()];
        back = new int[a.length()][b.length()];

        for (int i = 0; i < a.length(); i++) {
            Arrays.fill(dp[i], -1);
        }

        int lcsLength = recur(a.length() - 1, b.length() - 1);

        generate(a.length() - 1, b.length() - 1);

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
            do {
                c = readByte();
            } while (c <= ' ');
            return c;
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
    }
}
