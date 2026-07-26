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

    static ArrayList<ArrayList<Integer>> graph ;
    static boolean[][] vis ;
    static int[][] component ;
    static int[] c_size ;
    static int n ;
    static int m ;
    static final int[][] DIR = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };


    static class Cell {
        int row , col ;
        Cell(int x, int y) {
            this.row = x ;
            this.col = y;
        }
    }

    static boolean isValidMove(Cell check) {
        int row = check.row ;
        int col = check.col ;

        if(row >= 0 && row < n && col >= 0 && col < m && graph.get(row).get(col) != 1 ) {
            return true ;
        }

        return false ;
    }

    static ArrayList<Cell> getNeighbours(Cell curr) {
        ArrayList<Cell> ans = new ArrayList<>() ;
        for(int i = 0 ; i < 4 ; i++) {
            int nr = curr.row + DIR[i][0] ;
            int nc = curr.col + DIR[i][1] ;
            Cell nCell = new Cell(nr, nc) ;
            if(isValidMove(nCell)) {
                ans.add(nCell) ;
            }
        }
        return ans ;
    }

    static void dfs(int row, int col,  int comp) {
        ArrayDeque<Cell> stack = new ArrayDeque<>();
        stack.push(new Cell(row, col));
        vis[row][col] = true;

        while (!stack.isEmpty()) {
            Cell curr = stack.pop();
            component[curr.row][curr.col] = comp;
            for (Cell next : getNeighbours(curr) ) {
                if (!vis[next.row][next.col]) {
                    vis[next.row][next.col] = true;
                    stack.push(next);
                }
            }
        }
    }

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt() ;
        while(t-- > 0) {
                  n = fs.nextInt() ;
        m = fs.nextInt() ;

        graph = new ArrayList<>();

        for (int i = 0; i < n ; i++) {
            graph.add(new ArrayList<>());
            for(int j = 0 ; j < m ;j++) {
              graph.get(i).add(fs.nextInt()) ;
           }
        }

        vis = new boolean[n][m] ;
        component = new int[n][m] ;

        int c = 0 ;
        for(int i = 0 ; i < n ; i++) {
            for(int j = 0 ; j < m ; j++) {
            if(!vis[i][j] && graph.get(i).get(j) == 0 ) {
                c++ ;
                dfs(i, j , c) ;
            }
            }
        }

        c_size = new int[c + 1] ;

        for(int i = 0 ; i < n ; i++) {
            for(int j = 0 ; j < m ; j++  ) {
            c_size[component[i][j]] ++ ;
            }
        }

        for(int i = 0 ; i < n ; i++) {
            for(int j = 0 ; j < m ; j++) {
                int val = graph.get(i).get(j) ;
                if(val == 0) {
                    int cn = component[i][j] ;
                    if(c_size[cn] > 1) {
                        val = c_size[cn] ;
                    }
                }
                out.append(val).append(" ") ;
            }
            out.append('\n');
        }
        }


        System.out.println(out);

    }


}

