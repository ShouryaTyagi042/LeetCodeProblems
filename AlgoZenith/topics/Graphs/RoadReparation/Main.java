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

    static public class DSU {
        int[] size ;
        int[] parent;
        DSU(int n) {
            size = new int[n+1] ;
            parent = new int[n+1] ;
            for(int i = 0 ; i <= n ; i++) {
                parent[i] = i ;
                size[i] = 1 ;
            }
        }

        int find(int x) {
            if(parent[x] == x) return x ;
            return parent[x] = find(parent[x]) ;
        }

        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if(rootX == rootY) {
                return false;
            }

            if(size[rootX] < size[rootY]) {
                int temp = rootX;
                rootX = rootY;
                rootY = temp;
            }

            parent[rootY] = rootX;
            size[rootX] += size[rootY];

            return true;
        }
    }

    static public class Edge {
        int to ;
        int from ;
        int wt ;

        Edge(int from , int to, int wt) {
            this.from = from ;
            this.to = to ;
            this.wt = wt;
        }
    }


    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        int n = fs.nextInt() ;
        int m = fs.nextInt() ;
        Edge[] edges = new Edge[m] ;

        for(int i = 0 ; i < m ; i++) {
            int u = fs.nextInt() ;
            int v = fs.nextInt() ;
            int wt = fs.nextInt() ;
            edges[i] = new Edge(u,v,wt) ;

        }

        Arrays.sort(edges, (a,b) -> (Integer.compare(a.wt, b.wt)));
        DSU dsu = new DSU(n) ;
        long cost = 0 ;
        int eCount = 0 ;

        for(Edge ed : edges) {
            if(dsu.union(ed.to, ed.from)) {
                eCount ++ ;
                cost += ed.wt ;

                if(eCount == n - 1) {
                    break ;
                }
            }
        }

        if(eCount == n - 1) {
            out.append(cost) ;
        } else {
            out.append("IMPOSSIBLE") ;
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
