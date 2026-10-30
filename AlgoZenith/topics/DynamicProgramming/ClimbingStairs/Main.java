import java.io.*;
import java.util.*;

public class Main {
    static int n ;
    static int m ;
    static int dp[] ;

    static int recur(int ind) {
        // pruning
        if (ind > n ) return Integer.MAX_VALUE ;
        // basecase
        if(ind == n) return 0 ;
        // cache check
        if(dp[ind] != -1)return dp[ind] ;
        // compute
        int ans  = Integer.MAX_VALUE ;
        int oneStep = recur(ind + 1);
        int mStep = recur(ind + m) ;
        if(oneStep != Integer.MAX_VALUE) {
            oneStep += + 1;
        }
        if(mStep != Integer.MAX_VALUE) {
             mStep += 1 ;
        }
        ans = Math.min(oneStep , mStep) ;
        // return
        return dp[ind] = ans ;
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {
            n = fs.nextInt() ;
            m = fs.nextInt() ;
            dp = new int[n+1] ;
            Arrays.fill(dp, -1) ;
            // int ans = Integer.MAX_VALUE ;
            // for(int i = 1 ; i <= n ; i++) {
            //     ans = Math.min(ans, recur(i)) ;
            // }

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
            do {
                c = readByte();
            } while (c <= ' ');
            return c;
        }

        long nextLong() throws IOException {
            int c = skipBlank();
            int sign = 1;

            if (c == '-') {
                sign = -1;
                c = readByte();
            }

            long val = 0;

            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = readByte();
            }

            return val * sign;
        }

        int nextInt() throws IOException {
            return (int) nextLong();
        }
    }
}
