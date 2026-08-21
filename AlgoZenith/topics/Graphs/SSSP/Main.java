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

    static int n;
    static int m;
    static ArrayList<String> graph;

    static class Cell {
        int row;
        int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    static boolean[][] vis;
    static int[][] dis;
    static Cell[][] par ;
    static final Cell BLOCKED = new Cell(-1, -1);

    static ArrayDeque<Cell> queue = new ArrayDeque<>();


    static final int[][] DIR = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };

    static boolean isValid(int row, int col) {
        return row >= 0 && row < n &&
               col >= 0 && col < m &&
               graph.get(row).charAt(col) != '#';
    }

    static ArrayList<Cell> getNeighbours(Cell curr) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        for (int[] d : DIR) {
            int nr = curr.row + d[0];
            int nc = curr.col + d[1];

            if (isValid(nr, nc)) {
                neighbours.add(new Cell(nr, nc));
            }
        }

        return neighbours;
    }

    static void bfs(Cell start) {

        vis = new boolean[n][m];
        par = new Cell[n][m];
        dis = new int[n][m];

        for (int i = 0; i < n; i++) {
            Arrays.fill(dis[i], Integer.MAX_VALUE);
            Arrays.fill(par[i], BLOCKED) ;
        }

        dis[start.row][start.col] = 0;
        queue.offer(start);

        while (!queue.isEmpty()) {

            Cell cur = queue.poll();

            if(vis[cur.row][cur.col]) continue ;
            vis[cur.row][cur.col] = true ;

            for (Cell next : getNeighbours(cur)) {

                if (dis[next.row][next.col] > dis[cur.row][cur.col]    ) {
                    dis[next.row][next.col] = dis[cur.row][cur.col] + 1;
                    par[next.row][next.col] = cur ;
                    queue.offer(next);
                }
            }
        }
    }

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        n = fs.nextInt() ;
        m = fs.nextInt() ;

        graph = new ArrayList<String>() ;

        for(int i = 0 ; i < n ; i++) {
            graph.add(fs.next()) ;
        }

        Cell start = new Cell(1,2) ;
        Cell end = new Cell(2,6) ;

        bfs(start) ;

        if(vis[end.row][end.col]) {
            out.append("YES").append('\n') ;
            out.append(dis[end.row][end.col]).append('\n') ;
            ArrayList<Cell> path = new ArrayList<>();
            Cell cur = end ;
            while(cur != BLOCKED){
                path.add(cur);
                cur = par[cur.row][cur.col] ;
            }
            Collections.reverse(path) ;
            for(int i =1 ; i < path.size() ; i++) {
                Cell prev = path.get(i-1) ;
                Cell current = path.get(i) ;
                if(prev.row == current.row) {
                    if(prev.col > current.col) {
                        out.append("L");
                    } else {
                        out.append("R");
                    }
                } else {
                 if(prev.row > current.row) {
                       out.append("U");
                    } else {
                       out.append("D");
                    }
                }
            }

        } else {
            out.append("NO") ;
        }

        System.out.println(out);

    }


}

