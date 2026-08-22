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

        if (n == 1) {
            System.out.println(1);
            return ;
        }
        int[] arr = new int[n] ;
        for(int i =  0 ; i < n ; i++) {
            arr[i] = i + 1 ;
        }

        printArr(arr, out) ;

        while(nextPermutation(arr, n )) {
            printArr(arr, out) ;
        }

        System.out.println(out);


    }

    static boolean nextPermutation(int[] arr , int n) {
        int i = n - 2 ;
        while(i >=0  && arr[i] >= arr[i + 1]) {
            i -- ;
        }

        if( i < 0) return false ;

        int j = n - 1 ;
        while(arr[j] <= arr[i]) {
            j-- ;
        }

        swap(i, j, arr) ;

        reverse(i + 1, n - 1 , arr) ;

        return true ;
    }

    static void swap(int l , int r , int[] arr) {
        int temp = arr[l] ;
        arr[l] = arr[r] ;
        arr[r] = temp ;
    }

    static void reverse(int l, int r , int[] arr) {
        while(l < r) {
            swap(l, r, arr ) ;
            l += 1 ;
            r -= 1 ;
        }
    }

    static void printArr(int[] arr, StringBuilder out) {
        for(int e : arr) {
            out.append(e).append(" ") ;
        }
        out.append("\n") ;
    }

}

