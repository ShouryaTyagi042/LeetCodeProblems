import java.io.*;
import java.util.*;

public class Main {

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


    static void dijkstra(int sc) {
        dist[sc] = 0 ;
        pq.offer(new Edge(sc, 0)) ;
        while(!pq.isEmpty()) {
            Edge edge = pq.poll() ;
            int node = edge.to ;
            long wt = edge.wt ;
            if(vis[node]) continue ;
            vis[node] = true ;
            for(Edge neigh : graph.get(node)) {
                int neighNode = neigh.to ;
                long neighWt = neigh.wt ;
                if(dist[neighNode] > dist[node] + neighWt) {
                    dist[neighNode] = dist[node] + neighWt ;
                    pq.offer(new Edge(neighNode, dist[neighNode])) ;
                }
            }
        }

    }



    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();   // number of test cases

        while (t-- > 0) {
            int n = fs.nextInt() ;
            int m = fs.nextInt() ;
            graph = new ArrayList<>() ;
            vis = new boolean[100100] ;
            dist = new long[100100] ;
            Arrays.fill(dist, INF ) ;
            for(int i = 0 ; i <= n ; i++) {
                graph.add(new ArrayList<>()) ;
            }

            for(int j = 0 ; j < m ; j++) {
                int x = fs.nextInt() ;
                int y = fs.nextInt() ;
                long wt = fs.nextLong() ;
                graph.get(x).add(new Edge(y, wt)) ;
            }

            dijkstra(1) ;

            for(int i = 1 ; i <= n ; i++) {
                if(dist[i] != INF) {
                    out.append(dist[i]).append(" ") ;
                } else {
                    out.append(-1).append(" ") ;
                }
            }
            out.append('\n') ;
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


}
