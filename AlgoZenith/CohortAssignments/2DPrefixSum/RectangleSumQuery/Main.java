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

    static class MonotoneDeque {
        Deque<Integer> deque;

        MonotoneDeque() {
            deque = new ArrayDeque<>() ;
        }

        void insert(int val) {
            while(!deque.isEmpty() && deque.peekLast() < val ) {
                deque.pollLast() ;
            }
            deque.offerFirst(val) ;
        }

        int getMax(){
            return deque.peekFirst() ;
        }

        void remove(int val) {
            if(deque.peekFirst() == val) {
                deque.pollFirst() ;
            }
        }
    }

    static long[][] build2dPrefixSum(int[][] mat, int n , int m) {
        int MAX = 1001 ;
        long[][] prefix = new long[MAX][MAX];
        for(int i = 1 ; i < n + 1  ; i++) {
            for(int j = 1 ; j < m + 1 ; j++) {
                prefix[i][j] = mat[i][j] ;
                if(i > 0 ) prefix[i][j] += prefix[i-1][j]  ;
                if(j > 0) prefix[i][j] += prefix[i][j-1]  ;
                if(i > 0 && j > 0) prefix[i][j] -= (prefix[i-1][j-1]) ;
                prefix[i][j] = (( prefix[i][j] % MOD ) + MOD ) % MOD ;
            }
        }
        return prefix ;
    }

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        int n = fs.nextInt();
        int m = fs.nextInt();
        int q = fs.nextInt();


        int[][] mat = new int[n+1][m+1] ;


        for(int i = 1 ; i < n + 1 ; i++) {
            for(int j = 1 ; j < m + 1 ; j++) {
                mat[i][j] = fs.nextInt() ;
            }
        }

        long[][] prefix = build2dPrefixSum(mat, n, m) ;

        for(int i = 0 ; i < q ; i++) {
            int x1 = fs.nextInt() ;
            int y1 = fs.nextInt() ;
            int x2 = fs.nextInt() ;
            int y2 = fs.nextInt() ;

            long sum = prefix[x2][y2] ;
            if(y1 > 0) sum -= prefix[x2][y1-1] ;
            if(x1 > 0) sum -= prefix[x1-1][y2] ;
            if(x1 > 0 && y1 >0 ) sum += prefix[x1-1][y1-1] ;

            sum = ((sum % MOD) + MOD ) % MOD ;

            out.append(sum).append('\n') ;

        }

        System.out.println(out);

    }


}

