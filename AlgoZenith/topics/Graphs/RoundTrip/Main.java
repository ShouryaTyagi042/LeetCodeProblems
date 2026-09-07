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


    static ArrayList<ArrayList<Integer>> graph ;
    static int[] col ;
    static int[] parent ;
    static boolean isCycle = false ;
    static final int[][] dir = {
        {1,0},
        {-1,0},
        {0,1},
        {0,-1}
    };
    static int n ;
    static int m ;

    static void dfs(int node) {
        col[node] = 2 ;
        for(int v : graph.get(node)) {
            if(v == parent[node] ) continue ;
            if(col[v] == 1) {
                parent[v] = node ;
                dfs(v) ;
            } else if (col[v] == 2) {
                isCycle = true ;
                return ;
            }
        }
        col[node] = 3 ;
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        n = fs.nextInt();
        m = fs.nextInt();
        col = new int[n+1] ;
        Arrays.fill(col, 1) ;
        graph = new ArrayList<>() ;
        parent = new int[n+1] ;
        for(int i = 0 ; i <= n ; i++ ) {
            graph.add(new ArrayList<>()) ;
        }
        for(int i = 1 ; i <= m ; i++) {
            int x = fs.nextInt();
            int y = fs.nextInt();
            graph.get(x).add(y);
            graph.get(y).add(x);
        }

        for(int i = 1 ; i <= n ; i++) {
            if(col[i] == 1) {
                dfs(i) ;
            }
        }

        if(isCycle) out.append("YES") ;
        else out.append("NO") ;

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

