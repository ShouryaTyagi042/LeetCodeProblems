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

    static int[][] grid ;
    static int[][] dis ;
    static final int INF = Integer.MAX_VALUE ;
    static Queue<Integer> queue = new ArrayDeque<>();
    static ArrayList<Integer> list = new ArrayList<>();
    static final int[][] dir = {
        {1,0},
        {-1,0},
        {0,1},
        {0,-1}
    };

    static public class GridHelper {
        // Convert (row, col) -> 1D index
        public static int toId(int i, int j, int m) {
            return i * m + j;
        }

        // Convert 1D index -> row
        public static int getRow(int id, int m) {
            return id / m;
        }

        // Convert 1D index -> column
        public static int getCol(int id, int m) {
            return id % m;
        }

        // Convert 1D index -> {row, col}
        public static int[] toCell(int id, int m) {
            return new int[]{id / m, id % m};
        }
    }


    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        int n = fs.nextInt() ;
        int m = fs.nextInt() ;
        grid = new int[n][m] ;
        dis = new int[n][m] ;
        for(int i = 0 ; i < n ; i++) {
            Arrays.fill(dis[i], INF) ;
            for(int j = 0 ; j < m ; j++) {
                int val = fs.nextInt() ;
                grid[i][j] = val ;
                if(val == 2 ) {
                    dis[i][j] = 0 ;
                    queue.offer(GridHelper.toId(i,j,m)) ;
                } else if (val == 1) {
                    list.add(GridHelper.toId(i,j,m)) ;
                }
            }
        }

        while(!queue.isEmpty()) {
            int cell = queue.poll() ;
            int row = GridHelper.getRow(cell , m ) ;
            int col = GridHelper.getCol(cell  , m ) ;


            for(int[] d : dir) {
                int nr = row + d[0] ;
                int nc = col + d[1] ;
                if(nr >= 0 && nr < n && nc >= 0 && nc < m ) {
                    if(grid[nr][nc] == 0) continue ;
                    if(dis[nr][nc] != INF) continue ;
                    dis[nr][nc] = dis[row][col] + 1;
                    queue.offer(GridHelper.toId(nr,nc,m)) ;
                }
            }
        }

        int ans = 0 ;

        for(int cell : list) {
            int row = GridHelper.getRow(cell , m ) ;
            int col = GridHelper.getCol(cell , m ) ;
            if(dis[row][col] == INF) {
                System.out.println(-1);
                return ;
            } else {
                ans = Math.max(ans, dis[row][col]) ;
            }
        }

        out.append(ans) ;

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

