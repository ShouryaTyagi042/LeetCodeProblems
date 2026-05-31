// QUESTION : Choose one element from any row and minimise the difference between the selected max - selected min


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
        int x, y;

        Pair(int x, int y) {
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

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int n = fs.nextInt() ;
        int m = fs.nextInt() ;
        int[][] mat = new int[n][m] ;
        Pair[] arr = new Pair[n*m];
        int idx = 0 ;
        for(int i = 0 ; i < n ; i++) {
            for(int j = 0 ; j < m ; j ++) {
                mat[i][j] = fs.nextInt() ;
                arr[idx] = new Pair(mat[i][j], i) ;
                idx ++ ;
            }
        }
        Arrays.sort(arr, (a,b) -> {
            return Integer.compare(a.x, b.x) ;
        }) ;

       int[] freq = new int[n];
int dist = 0;

int total = arr.length;
int tail = 0;
int head = -1;

int k = n;

int ans = Integer.MAX_VALUE;

while (tail < total) {

    while (head + 1 < total &&
          (dist < k || freq[arr[head + 1].y] > 0)) {

        head++;

        if (freq[arr[head].y] == 0)
            dist++;

        freq[arr[head].y]++;
    }

    if (dist == k) {

        int mini = arr[tail].x;
        int maxi = arr[head].x;

        ans = Math.min(ans, maxi - mini);
    }

    if (tail <= head) {

        freq[arr[tail].y]--;

        if (freq[arr[tail].y] == 0)
            dist--;

        tail++;

    } else {

        tail++;
        head = tail - 1;
    }
}

        System.out.println(ans);



    }


}

