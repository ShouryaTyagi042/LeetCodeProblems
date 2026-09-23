import java.io.*;
import java.util.*;

public class Main {
    static ArrayList<ArrayList<Integer>> graph ;
    static int n ;
    static int[] subz ;
    static long ans = 0 ;

    static void dfs(int node, int parent) {
        subz[node] += 1 ;
        for(int neigh : graph.get(node)) {
            if(neigh != parent) {
                dfs(neigh, node) ;
                subz[node] += subz[neigh] ;
            }
        }
        ans += (long) subz[node] * (n - subz[node]) ;
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        n = fs.nextInt() ;
        graph = new ArrayList<>() ;
        for(int i = 0 ; i <= n ; i++) {
            graph.add(new ArrayList<>()) ;
        }
        subz = new int[n+1] ;
        for(int i = 1 ; i <= n - 1 ; i++) {
            int x = fs.nextInt() ;
            int y = fs.nextInt() ;
            graph.get(x).add(y) ;
            graph.get(y).add(x) ;
        }
        dfs(1,0) ;
        System.out.println(ans);

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

