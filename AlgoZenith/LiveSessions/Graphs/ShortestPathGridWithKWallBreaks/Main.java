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

    import java.util.*;

class Solution {

    static class State {
        int r;
        int c;
        int wallsBroken;

        State(int r, int c, int wallsBroken) {
            this.r = r;
            this.c = c;
            this.wallsBroken = wallsBroken;
        }
    }

    public int shortestPath(
            int[][] grid,
            int[] start,
            int[] end,
            int k
    ) {

        int n = grid.length;
        int m = grid[0].length;

        int INF = Integer.MAX_VALUE;

        // distance[r][c][wallsBroken]
        int[][][] distance = new int[n][m][k + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Arrays.fill(distance[i][j], INF);
            }
        }

        Queue<State> queue = new ArrayDeque<>();

        State startState = new State(
                start[0],
                start[1],
                0
        );

        distance[start[0]][start[1]][0] = 0;
        queue.offer(startState);

        int[][] directions = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        while (!queue.isEmpty()) {

            State current = queue.poll();

            int r = current.r;
            int c = current.c;
            int wallsBroken = current.wallsBroken;

            int currentDistance =
                    distance[r][c][wallsBroken];

            if (r == end[0] && c == end[1]) {
                return currentDistance;
            }

            for (int[] direction : directions) {

                int nr = r + direction[0];
                int nc = c + direction[1];

                if (nr < 0 || nr >= n ||
                    nc < 0 || nc >= m) {
                    continue;
                }

                int newWallsBroken = wallsBroken;

                if (grid[nr][nc] == 1) {
                    newWallsBroken++;
                }

                if (newWallsBroken > k) {
                    continue;
                }

                int newDistance = currentDistance + 1;

                if (distance[nr][nc][newWallsBroken]
                        <= newDistance) {
                    continue;
                }

                distance[nr][nc][newWallsBroken] = newDistance;

                queue.offer(
                        new State(
                                nr,
                                nc,
                                newWallsBroken
                        )
                );
            }
        }

        return -1;
    }
}



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


}
