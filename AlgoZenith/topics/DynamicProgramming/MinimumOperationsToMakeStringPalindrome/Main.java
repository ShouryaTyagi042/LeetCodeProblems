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

    static public class GridHelper {
        public static int toId(int i, int j, int m) {
            return i * m + j;
        }

        public static int getRow(int id, int m) {
            return id / m;
        }

        public static int getCol(int id, int m) {
            return id % m;
        }

        public static int[] toCell(int id, int m) {
            return new int[]{id / m, id % m};
        }
    }

    static public class Edge {
        int to ;
        int wt ;

        Edge(int to, int wt) {
            this.to = to ;
            this.wt = wt;
        }
    }

    static final long INF = 1_000_000_000_000_000_000L;

    static ArrayList<ArrayList<Integer>> graph ;
    static boolean[] vis ;
    static int[] col ;
    static int[] component ;
    static int[] cSize ;
    static boolean isCycle = false ;
    static final int[][] dir = {
        {1,0},
        {-1,0},
        {0,1},
        {0,-1}
    };

    /*
     * Rotating left r times costs r and turns s into t = s[r..] + s[..r].
     * In t, index i pairs with index n-1-i, i.e. original indices
     * a = (r+i) % n and b = (r+n-1-i) % n, so every palindrome pair
     * satisfies (a + b) % n == (2r + n - 1) % n -- one residue class per r.
     *
     * Two letters can only be equalised by incrementing one of them all the
     * way to the other (stopping anywhere in between is never cheaper), so
     * pairCost(x, y) = min(d, 26 - d) with d = (y - x) mod 26.
     *
     * So: bucket every unordered pair by (a + b) % n once, then each rotation
     * just reads its bucket. O(n^2) time, O(n) space.
     */
    static class Solution {
        public int minOperations(String s) {
            int n = s.length();
            int[] dorivexalu = new int[n];          // letters of s as 0..25
            for (int i = 0; i < n; i++) dorivexalu[i] = s.charAt(i) - 'a';

            long[] bucket = new long[n];            // bucket[k] = palindrome cost of residue class k
            for (int a = 0; a < n; a++) {
                int ca = dorivexalu[a];
                for (int b = a + 1; b < n; b++) {
                    int d = dorivexalu[b] - ca;
                    if (d < 0) d += 26;
                    bucket[(a + b) % n] += Math.min(d, 26 - d);
                }
            }

            long best = Long.MAX_VALUE;
            for (int r = 0; r < n; r++) {
                best = Math.min(best, r + bucket[(2 * r + n - 1) % n]);
            }
            return (int) best;
        }
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();   // number of test cases

        Solution sol = new Solution();
        while (t-- > 0) {
            out.append(sol.minOperations(fs.next())).append('\n');
        }
        System.out.print(out);

    }

    public static void main(String[] args) throws Exception {
        new Thread(null, () -> {
            try {
                solve();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "solve", 1 << 26).start();   // 64 MB stack
    }


}
