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
        long wt ;

        Edge(int to, long wt) {
            this.to = to ;
            this.wt = wt;
        }
    }

    static ArrayList<ArrayList<Edge>> graph ;
    static boolean[] vis ;
    static long[] dist ;
    static PriorityQueue<Edge> pq = new PriorityQueue<>((a,b) -> Long.compare(a.wt, b.wt));

    static final long INF = 1_000_000_000_000_000_000L;

     class Solution {
        static public class Edge {
            int to ;
            int wt ;

            Edge(int to, int wt) {
                this.to = to ;
                this.wt = wt;
            }
        }

        static ArrayList<ArrayList<Edge>> graph ;
        static int[] vis ;
        static int[] dist ;
        static int[] dist2 ;
        static PriorityQueue<Edge> pq = new PriorityQueue<>((a,b) -> Integer.compare(a.wt, b.wt));

        static final long INF = 1_000_000_000_000_000_000L;

        public void dijkstra(int sc, int n, int change) {
        dist[sc] = 0;
        pq.offer(new Edge(sc, 0));

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();

            int node = curr.to;
            int currTime = curr.wt;
            int departTime = currTime;

            if(vis[node] == 0) {
                dist[node] = departTime ;
                vis[node] ++ ;
            } else if (vis[node] == 1 && departTime != dist[node]) {
                dist2[node] = departTime ;
                vis[node]++ ;
            } else {
                continue ;
            }

            if ((departTime / change) % 2 == 1) {
                departTime += change - (departTime % change);
            }

            for (Edge next : graph.get(node)) {
                int newTime = departTime + next.wt;

                if (newTime < dist2[next.to]) {
                    pq.offer(new Edge(next.to, newTime));
                }
                    }
                }
        }


        public int secondMinimum(int n, int[][] edges, int time, int change) {
            graph = new ArrayList<>() ;
            vis = new int[n+1] ;
            dist = new int[n+1] ;
            dist2 = new int[n+1] ;
            Arrays.fill(dist, Integer.MAX_VALUE) ;
            Arrays.fill(dist2, Integer.MAX_VALUE) ;
            for(int i = 0 ; i <= n ; i++) {
                graph.add(new ArrayList<>()) ;
            }
            for(int[] edge : edges) {
                graph.get(edge[0]).add(new Edge(edge[1], time)) ;
                graph.get(edge[1]).add(new Edge(edge[0], time)) ;
            }
            dijkstra(1,n, change) ;
            return dist2[n] ;
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
