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

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();   // number of test cases

        while (t-- > 0) {
            int n = fs.nextInt() ;
            int k = fs.nextInt() ;
            int[] arr = new int[n] ;
            for(int i = 0 ; i < n ; i++) {
                arr[i] = fs.nextInt() ;
            }

            int hi = n - 1 ;
            int lo = 0 ;
            int idx = n - 1 ;
            while (lo <= hi){
                int mid = lo + (hi - lo) / 2 ;
                if(check(arr, n , mid) == true) {
                    idx = mid ;
                    hi = mid - 1 ;
                } else {
                    lo = mid + 1 ;
                }
            }

            for(int i = 0 ; i < k ; i++) {
                int target = fs.nextInt() ;
                if(idx > 0) {
                int first = binarySearch(arr, target, n , idx - 1, 0  ) ;
                if(first >= 0 ) {
                    out.append(first + 1).append(' ') ;
                }
                }
                int second = binarySearchReverse(arr, target, n  , n - 1, idx  ) ;
                if(second >= 0) {
                    out.append(second + 1).append(' ') ;
                }
                out.append('\n') ;

            }

        }
        System.out.println(out);

    }

    static int binarySearch(int[] arr, int target, int n , int hi, int lo ) {
        while(lo <= hi) {
            int mid = lo + (hi - lo)/2 ;
            if(arr[mid] == target ) return mid ;
            if (arr[mid] > target) {
                hi = mid - 1 ;
            } else {
                lo = mid + 1 ;
            }
        }
        return -1 ;
    }

    static int binarySearchReverse(int[] arr, int target, int n , int hi, int lo ) {
        while(lo <= hi) {
            int mid = lo + (hi - lo)/2 ;
            if(arr[mid] == target ) return mid ;
            if (arr[mid] > target) {
                lo = mid + 1 ;
            } else {
                hi = mid - 1 ;
            }
        }
        return -1 ;
    }

    static boolean check(int[] arr, int n , int idx) {
        if(idx == n - 1) return true ;
        if(arr[idx] > arr[idx + 1]) return true ;
        return false ;
    }


}

