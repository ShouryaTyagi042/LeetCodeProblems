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

    static int upperBound(int[] arr, int n , int target) {
            int hi = n - 1 ;
            int lo = 0 ;
            int ans = n ;
            while(lo <= hi) {
                int mid = lo + (hi - lo) / 2 ;
                if(arr[mid] > target ) {
                    ans = mid ;
                    hi = mid - 1 ;
                } else {
                    lo = mid + 1 ;
                }
            }
            return ans ;
    }

    static long[] fact = new long[1000100];

    static void precompute() {
        fact[0] = 1L;
        for(int i=1; i<=1000000; i++) {
            fact[i] = (fact[i-1] * i) % MOD;
        }
    }

    static long calculateNCR(int n, int r) {
        long num = fact[n] ;
        long dem = ( fact[n-r] * fact[r]) % MOD ;
        return (num * inverse(dem)) % MOD ;
    }

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();ß

        int n = fs.nextInt();
        boolean[] vis = new boolean[10] ;
        for(int i = 1 ; i <= 9 ; i++) {
            vis[i] = true ;
            recurse(n, i, vis, 1) ;
            vis[i] = false ;
        }
        System.out.println(ans);
    }

    static int ans = 0 ;

    static int[][] pos = {
    {0, 0}, // dummy for index 0
    {0, 0}, // 1
    {0, 1}, // 2
    {0, 2}, // 3
    {1, 0}, // 4
    {1, 1}, // 5
    {1, 2}, // 6
    {2, 0}, // 7
    {2, 1}, // 8
    {2, 2}  // 9
   };

   static int[][] cell = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
   };

    static void recurse(int n , int current, boolean vis[] , int currentLength) {
        if(currentLength == n) {
            // System.out.println(Arrays.toString(vis));
            ans++;
            return ;
        }

        for(int next = 1 ; next <= 9 ; next++) {
            if(!vis[next] && isValid(next, current, vis)) {
                vis[next] = true ;
                recurse(n, next, vis, currentLength + 1) ;
                vis[next] = false ;
            }
        }
    }


    static boolean isValid(int next, int curr, boolean[] vis) {
        int[] nextCor = pos[next] ;
        int[] currCor = pos[curr] ;

        if((nextCor[0] + currCor[0]) % 2 == 0 && (nextCor[1] + currCor[1]) % 2 == 0 ) {
            int midr = (nextCor[0] + currCor[0] )  / 2;
            int midc =  (nextCor[1] + currCor[1] ) / 2 ;
            return vis[cell[midr][midc]] ;
        }
        return true ;
    }


}

