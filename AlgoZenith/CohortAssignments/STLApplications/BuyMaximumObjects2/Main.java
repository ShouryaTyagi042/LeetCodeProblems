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

        double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }

        String nextLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;

            // skip any leftover newline or spaces
            while ((c = readByte()) != -1 && c == '\n');

            // read until newline
            while (c != -1 && c != '\n') {
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

    static long inverse(long n) {
        return modPow(n, MOD - 2, MOD) ;
    }

    static class Pair  {
        long x, y;

        Pair(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int n = fs.nextInt() ;
        int q = fs.nextInt() ;

        long[] arr = new long[n] ;

        for (int i = 0 ; i < n ; i++ ) {
            arr[i] = fs.nextLong() ;
        }

        Arrays.sort(arr) ;

        // build the prefix sum
        for(int i = 1 ;  i < n ; i++) {
            arr[i] += arr[i-1] ;
        }

        for(int i = 0 ; i < q ; i++) {
            int m = fs.nextInt() ;
            int ans = getUpperBound(arr, m) ;
            out.append(ans ).append('\n') ;
        }

        System.out.println(out);

    }

    public static int getLowerBound(int[] arr, int target) {
        int start = 0 ;
        int end = arr.length  ;
        while( start < end) {
            int mid = start + ( end - start ) / 2 ;
            if(arr[mid] < target) {
                start = mid + 1 ;
            } else {
                end = mid ;
            }
        }
        return end ;
    }

    public static int getUpperBound(long[] arr, int target) {
        int start = 0 ;
        int end = arr.length  ;
        while( start < end) {
            int mid = start + ( end - start ) / 2 ;
            if(arr[mid] <= target) {
                start = mid + 1 ;
            } else {
                end = mid ;
            }
        }
        return end ;
    }

}

