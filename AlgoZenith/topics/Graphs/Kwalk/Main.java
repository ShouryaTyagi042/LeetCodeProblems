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

    static int n;
    static ArrayList<ArrayList<Integer>> graph;

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
    static Cell start ;
    static Cell end ;

    static ArrayDeque<Cell> queue = new ArrayDeque<>();


    static final int[][] dir = {
        {-2, -1}, {-2, 1},
        {-1, -2}, {-1, 2},
        {1, -2}, {1, 2},
        {2, -1}, {2, 1}
    };

    static boolean isValid(int row, int col) {
        return row > 0 && row <= n &&
               col > 0 && col <= n ;
    }

    static ArrayList<Cell> getNeighbours(Cell curr) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        for (int[] d : dir) {
            int nr = curr.row + d[0];
            int nc = curr.col + d[1];

            if (isValid(nr, nc)) {
                neighbours.add(new Cell(nr, nc));
            }
        }

        return neighbours;
    }

    static void bfs(Cell start) {

        vis = new boolean[n+1][n+1];
        dis = new int[n+1][n+1];

        for (int i = 1; i <= n; i++) {
            Arrays.fill(dis[i], Integer.MAX_VALUE);
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
                    queue.offer(next);
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

        int srow = fs.nextInt() ;
        int scol = fs.nextInt() ;
        start = new Cell(srow, scol) ;
        int erow = fs.nextInt() ;
        int ecol = fs.nextInt() ;
        end = new Cell(erow, ecol) ;

        bfs(start) ;

        if(vis[end.row][end.col]) {
            out.append(dis[end.row][end.col]).append('\n') ;

        } else {
            out.append(-1).append('\n') ;
        }

        }

        System.out.println(out);

    }


}

