import java.io.*;
import java.util.*;

public class Main {
    static int n ;
    static int k ;
    static int[] arr ;
    static Pair[] dp = new Pair[5001];

    static class Pair {
        int first;   // maximum length
        int second;  // number of ways

        Pair(int x, int y) {
            this.first = x;
            this.second = y;
        }
    }

    static Pair merge(Pair a, Pair b) {
        if (a.first > b.first) {
            return a;
        }

        if (b.first > a.first) {
            return b;
        }

        // Same maximum length -> add number of ways
        return new Pair(a.first, a.second + b.second);
    }

    static Pair recur(int i) {

        if (dp[i] != null) {
            return dp[i];
        }

        // Only arr[i]
        Pair ans = new Pair(1, 1);

        for (int j = i - 1; j >= 0; j--) {

            if (Math.abs((long) arr[j] - arr[i]) >= k) {

                Pair prev = recur(j);

                // Append arr[i] to every optimal subsequence ending at j
                Pair candidate = new Pair(
                    prev.first + 1,
                    prev.second
                );

                ans = merge(ans, candidate);
            }
        }

        return dp[i] = ans;
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();

        n = fs.nextInt();
        k = fs.nextInt();

        arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = fs.nextInt();
        }

        Pair ans = new Pair(0, 0);

        for (int i = 0; i < n; i++) {
            ans = merge(ans, recur(i));
        }

        System.out.println(ans.first + " " + ans.second);
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
