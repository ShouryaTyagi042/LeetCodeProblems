import java.io.*;
import java.util.*;

public class Main {

    // -------- FAST INPUT --------
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

        int nextInt() throws IOException {
            int c, sign = 1, val = 0;
            do {
                c = readByte();
            } while (c <= ' ');

            if (c == '-') {
                sign = -1;
                c = readByte();
            }

            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return val * sign;
        }

        long nextLong() throws IOException {
            int c, sign = 1;
            long val = 0;
            do {
                c = readByte();
            } while (c <= ' ');

            if (c == '-') {
                sign = -1;
                c = readByte();
            }

            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return val * sign;
        }

        String next() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = readByte();
            } while (c <= ' ');

            while (c > ' ') {
                sb.append((char) c);
                c = readByte();
            }
            return sb.toString();
        }
    }

    static final long MOD = 1_000_000_007L;

    static long modPow(long base, long exp, long mod) {
        long result = 1 ;
        base %= mod ;
        while( exp > 0) {
            // check if power is odd
            if  ((exp & 1) == 1 ) {
                result = (( result % mod) * (base % mod) ) % mod ;
            }
            base = ( (base % mod)  * (base % mod ) ) % mod ;
            exp >>= 1 ;
        }

        return result ;
    }

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int n = fs.nextInt();   // number of test cases
        int q = fs.nextInt();
        long[] prefixArr = new long[n] ;
        prefixArr[0] = fs.nextLong();
        for(int i = 1 ; i < n ; i++) {
            long sum  = prefixArr[i-1] + fs.nextLong() ;
            if(sum > 0) {
                prefixArr[i] = sum % MOD ;
            } else {
                prefixArr[i] = ( sum + MOD ) % MOD ;
            }
        }


        while( q > 0) {
            int l = fs.nextInt();
            int r = fs.nextInt();
            long ans = 0;

            if(l == 1) {
                ans =  prefixArr[r-1] ;
            } else {
                ans = prefixArr[r-1] - prefixArr[l-2] ;
            }

            if( ans < 0 ) {
                System.out.println((ans + MOD) % MOD);
            } else {
                System.out.println(ans % MOD);
            }

            q--;
        }


    }

}

