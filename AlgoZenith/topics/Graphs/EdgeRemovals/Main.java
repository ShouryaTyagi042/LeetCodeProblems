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

                if (len <= 0) {
                    return -1;
                }
            }

            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            int sign = 1;
            int val = 0;

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
    }

    // -------- EDGE --------
    static class Edge {
        int u, v;

        Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    // -------- QUERY --------
    static class Query {
        int type;
        int edge;

        Query(int type, int edge) {
            this.type = type;
            this.edge = edge;
        }
    }

    // -------- DSU --------
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] == x) {
                return x;
            }

            return parent[x] = find(parent[x]);
        }

        // Returns true if two different components were merged
        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return false;
            }

            // Union by size
            if (size[rootX] < size[rootY]) {
                int temp = rootX;
                rootX = rootY;
                rootY = temp;
            }

            parent[rootY] = rootX;
            size[rootX] += size[rootY];

            return true;
        }
    }

    static void solve() throws Exception {

        FastScanner fs = new FastScanner();

        int n = fs.nextInt();
        int m = fs.nextInt();
        int q = fs.nextInt();

        // 1-indexed edges
        Edge[] edges = new Edge[m + 1];

        for (int i = 1; i <= m; i++) {
            int u = fs.nextInt();
            int v = fs.nextInt();

            edges[i] = new Edge(u, v);
        }

        // Store queries
        Query[] queries = new Query[q];

        /*
         * lastRemoval[x] =
         * index of the LAST query that removes edge x
         *
         * -1 means edge x is never removed.
         */
        int[] lastRemoval = new int[m + 1];
        Arrays.fill(lastRemoval, -1);

        // Read queries
        for (int i = 0; i < q; i++) {

            int type = fs.nextInt();

            if (type == 1) {

                int edgeNumber = fs.nextInt();

                queries[i] = new Query(1, edgeNumber);

                // Only the LAST removal matters
                lastRemoval[edgeNumber] = i;

            } else {

                queries[i] = new Query(2, -1);
            }
        }

        // -----------------------------------------
        // Build graph after ALL effective removals
        // -----------------------------------------

        DSU dsu = new DSU(n);

        int components = n;

        for (int i = 1; i <= m; i++) {

            // Edge was never removed
            if (lastRemoval[i] == -1) {

                Edge e = edges[i];

                if (dsu.union(e.u, e.v)) {
                    components--;
                }
            }
        }

        // -----------------------------------------
        // Process queries backwards
        // -----------------------------------------

        ArrayList<Integer> answers = new ArrayList<>();

        for (int i = q - 1; i >= 0; i--) {

            Query query = queries[i];

            // Type 2 -> record current number of components
            if (query.type == 2) {

                answers.add(components);

            }

            // Type 1 X -> reverse the removal
            else {

                int edgeNumber = query.edge;

                /*
                 * Only the LAST removal of this edge
                 * should be reversed.
                 */
                if (lastRemoval[edgeNumber] == i) {

                    Edge e = edges[edgeNumber];

                    // Add the edge back
                    if (dsu.union(e.u, e.v)) {
                        components--;
                    }
                }
            }
        }

        // We processed answers backwards
        Collections.reverse(answers);

        // Print
        StringBuilder out = new StringBuilder();

        for (int ans : answers) {
            out.append(ans).append('\n');
        }

        System.out.print(out);
    }

    public static void main(String[] args) throws Exception {
        solve();
    }
}
