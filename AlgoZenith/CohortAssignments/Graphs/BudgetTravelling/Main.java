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
    int to;
    int wt;

    Edge(int to, int wt) {
        this.to = to;
        this.wt = wt;
    }
}

static public class State implements Comparable<State> {
    int city;
    int fuel;
    long cost;

    State(int city, int fuel, long cost) {
        this.city = city;
        this.fuel = fuel;
        this.cost = cost;
    }

    @Override
    public int compareTo(State other) {
        return Long.compare(this.cost, other.cost);
    }
}

static final long INF = 1_000_000_000_000_000_000L;

static ArrayList<ArrayList<Edge>> graph;
static long[][] cost;
static PriorityQueue<State> queue;
static long[] prices;

static void solve() throws Exception {

    FastScanner fs = new FastScanner();

    int n = fs.nextInt();
    int m = fs.nextInt();

    graph = new ArrayList<>();

    for (int i = 0; i <= n; i++) {
        graph.add(new ArrayList<>());
    }

    // Roads
    for (int i = 0; i < m; i++) {

        int x = fs.nextInt();
        int y = fs.nextInt();
        int p = fs.nextInt();

        graph.get(x).add(new Edge(y, p));
        graph.get(y).add(new Edge(x, p));
    }

    // Fuel prices
    prices = new long[n + 1];

    for (int i = 1; i <= n; i++) {
        prices[i] = fs.nextInt();
    }

    int sc = fs.nextInt();
    int dest = fs.nextInt();
    int capacity = fs.nextInt();

    // cost[city][fuel]
    cost = new long[n + 1][capacity + 1];

    for (int i = 0; i <= n; i++) {
        Arrays.fill(cost[i], INF);
    }

    queue = new PriorityQueue<>();

    // Start with 0 fuel
    cost[sc][0] = 0;
    queue.offer(new State(sc, 0, 0));

    while (!queue.isEmpty()) {

        State curr = queue.poll();

        int city = curr.city;
        int fuel = curr.fuel;
        long currCost = curr.cost;

        // Stale state
        if (currCost > cost[city][fuel]) {
            continue;
        }

        /*
         * OPTION 1:
         * Buy one unit of fuel.
         */
        if (fuel < capacity) {

            int newFuel = fuel + 1;

            long newCost =
                    currCost + prices[city];

            if (newCost < cost[city][newFuel]) {

                cost[city][newFuel] = newCost;

                queue.offer(
                    new State(
                        city,
                        newFuel,
                        newCost
                    )
                );
            }
        }

        /*
         * OPTION 2:
         * Drive to another city.
         */
        for (Edge ed : graph.get(city)) {

            if (ed.wt <= fuel) {

                int newCity = ed.to;
                int newFuel = fuel - ed.wt;

                // Driving costs no additional money.
                long newCost = currCost;

                if (newCost < cost[newCity][newFuel]) {

                    cost[newCity][newFuel] = newCost;

                    queue.offer(
                        new State(
                            newCity,
                            newFuel,
                            newCost
                        )
                    );
                }
            }
        }
    }

    /*
     * We can reach destination with
     * any amount of fuel remaining.
     */
    long answer = INF;

    for (int fuel = 0; fuel <= capacity; fuel++) {
        answer = Math.min(answer, cost[dest][fuel]);
    }

    if (answer == INF) {
        System.out.println(-1);
    } else {
        System.out.println(answer);
    }
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
