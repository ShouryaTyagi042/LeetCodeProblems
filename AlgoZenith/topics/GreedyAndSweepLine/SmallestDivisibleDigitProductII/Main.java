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



    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();   // number of test cases

        while (t-- > 0) {

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
        }, "solve", 1 << 26).start();   // 64 MB stack
    }

    class Solution {
        // DIGIT_EXP[d] = exponents of [2, 3, 5, 7] contributed by digit d
        private static final int[][] DIGIT_EXP = new int[10][4];
        static {
            DIGIT_EXP[1] = new int[]{0, 0, 0, 0};
            DIGIT_EXP[2] = new int[]{1, 0, 0, 0};
            DIGIT_EXP[3] = new int[]{0, 1, 0, 0};
            DIGIT_EXP[4] = new int[]{2, 0, 0, 0};
            DIGIT_EXP[5] = new int[]{0, 0, 1, 0};
            DIGIT_EXP[6] = new int[]{1, 1, 0, 0};
            DIGIT_EXP[7] = new int[]{0, 0, 0, 1};
            DIGIT_EXP[8] = new int[]{3, 0, 0, 0};
            DIGIT_EXP[9] = new int[]{0, 2, 0, 0};
        }

        // minDigits23[a][b] = fewest digits (from {2,3,4,6,8,9}) needed so that
        // the sum of their 2-exponents >= a and sum of their 3-exponents >= b.
        private int[][] minDigits23;

        public String smallestNumber(String num, long t) {
            int e2 = 0, e3 = 0, e5 = 0, e7 = 0;
            long rem = t;
            while (rem % 2 == 0) { rem /= 2; e2++; }
            while (rem % 3 == 0) { rem /= 3; e3++; }
            while (rem % 5 == 0) { rem /= 5; e5++; }
            while (rem % 7 == 0) { rem /= 7; e7++; }
            if (rem != 1) return "-1"; // t has a prime factor a digit can never provide

            buildMinDigits23(e2, e3);

            int n = num.length();
            char[] digits = num.toCharArray();

            // prefix[i] = exponents contributed by digits[0..i-1]
            int[] pa = new int[n + 1], pb = new int[n + 1], pc = new int[n + 1], pd = new int[n + 1];
            boolean zeroFree = true;
            int firstZero = -1;
            for (int i = 0; i < n; i++) {
                int dg = digits[i] - '0';
                if (dg == 0) {
                    zeroFree = false;
                    if (firstZero == -1) firstZero = i;
                }
                int[] ex = DIGIT_EXP[dg];
                pa[i + 1] = pa[i] + ex[0];
                pb[i + 1] = pb[i] + ex[1];
                pc[i + 1] = pc[i] + ex[2];
                pd[i + 1] = pd[i] + ex[3];
            }

            if (zeroFree && pa[n] >= e2 && pb[n] >= e3 && pc[n] >= e5 && pd[n] >= e7) {
                return num;
            }

            // Any candidate that keeps a prefix of num unchanged must stop before
            // the first zero, so the increment position can be at most firstZero.
            int startPos = (firstZero == -1) ? n - 1 : firstZero;

            for (int pos = startPos; pos >= 0; pos--) {
                int origDigit = digits[pos] - '0';
                int startD = (pos == firstZero) ? 1 : origDigit + 1;
                int remLen = n - pos - 1;

                for (int d = startD; d <= 9; d++) {
                    int[] ex = DIGIT_EXP[d];
                    int needA = Math.max(0, e2 - (pa[pos] + ex[0]));
                    int needB = Math.max(0, e3 - (pb[pos] + ex[1]));
                    int needC = Math.max(0, e5 - (pc[pos] + ex[2]));
                    int needD = Math.max(0, e7 - (pd[pos] + ex[3]));
                    int minNeeded = minDigits23[needA][needB] + needC + needD;

                    if (minNeeded <= remLen) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(num, 0, pos);
                        sb.append((char) ('0' + d));
                        sb.append(buildSuffix(remLen, needA, needB, needC, needD));
                        return sb.toString();
                    }
                }
            }

            // No same-length candidate works: use the smallest length >= n + 1
            // that can possibly satisfy the full requirement.
            int minLenTotal = minDigits23[e2][e3] + e5 + e7;
            int len = Math.max(n + 1, minLenTotal);
            return buildSuffix(len, e2, e3, e5, e7);
        }

        private void buildMinDigits23(int maxA, int maxB) {
            minDigits23 = new int[maxA + 1][maxB + 1];
            for (int a = 0; a <= maxA; a++) {
                for (int b = 0; b <= maxB; b++) {
                    if (a == 0 && b == 0) {
                        minDigits23[a][b] = 0;
                        continue;
                    }
                    int best = Integer.MAX_VALUE;
                    for (int d = 2; d <= 9; d++) {
                        if (d == 5 || d == 7) continue;
                        int[] ex = DIGIT_EXP[d];
                        int na = Math.max(0, a - ex[0]);
                        int nb = Math.max(0, b - ex[1]);
                        if (na == a && nb == b) continue; // no progress, would self-reference
                        int cand = 1 + minDigits23[na][nb];
                        if (cand < best) best = cand;
                    }
                    minDigits23[a][b] = best;
                }
            }
        }

        // Smallest zero-free string of exactly `len` digits whose digit product
        // covers at least 2^a * 3^b * 5^c * 7^d.
        private String buildSuffix(int len, int a, int b, int c, int d) {
            StringBuilder sb = new StringBuilder();
            for (int pos = 0; pos < len; pos++) {
                int remLen = len - pos - 1;
                for (int digit = 1; digit <= 9; digit++) {
                    int[] ex = DIGIT_EXP[digit];
                    int na = Math.max(0, a - ex[0]);
                    int nb = Math.max(0, b - ex[1]);
                    int nc = Math.max(0, c - ex[2]);
                    int nd = Math.max(0, d - ex[3]);
                    int minNeeded = minDigits23[na][nb] + nc + nd;
                    if (minNeeded <= remLen) {
                        sb.append((char) ('0' + digit));
                        a = na; b = nb; c = nc; d = nd;
                        break;
                    }
                }
            }
            return sb.toString();
        }
    }

}
