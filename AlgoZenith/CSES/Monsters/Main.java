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
            int c, val = 0;

            do {
                c = readByte();
            } while (c <= ' ');

            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = readByte();
            }

            return val;
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
    }

    static char[][] graph;

    // Earliest time monster can reach a cell
    static int[][] disM;

    // Earliest time player can reach a cell
    static int[][] disP;

    /*
        Direction used by player to ENTER a cell.

        Example:

        parent[r][c] = 'D'

        means player reached (r,c) by moving DOWN
        from its parent.
    */
    static char[][] parent;

    static int n, m;

    static int startRow;
    static int startCol;

    // directions:
    // D, U, R, L
    static final int[] dr = {1, -1, 0, 0};
    static final int[] dc = {0, 0, 1, -1};
    static final char[] move = {'D', 'U', 'R', 'L'};

    static final int INF = Integer.MAX_VALUE;

    static void solve() throws Exception {

        FastScanner fs = new FastScanner();

        n = fs.nextInt();
        m = fs.nextInt();

        graph = new char[n][m];

        disM = new int[n][m];
        disP = new int[n][m];

        parent = new char[n][m];

        /*
            Primitive BFS queue.

            Instead of:

            Queue<Cell>

            encode:

            position = row * m + col
        */
        int[] queue = new int[n * m];

        int head = 0;
        int tail = 0;

        // -------------------------
        // READ GRID
        // -------------------------

        for (int i = 0; i < n; i++) {

            Arrays.fill(disM[i], INF);
            Arrays.fill(disP[i], INF);

            String row = fs.next();

            graph[i] = row.toCharArray();

            for (int j = 0; j < m; j++) {

                if (graph[i][j] == 'M') {

                    disM[i][j] = 0;

                    // immediately add monster to BFS
                    queue[tail++] = i * m + j;

                } else if (graph[i][j] == 'A') {

                    startRow = i;
                    startCol = j;
                }
            }
        }

        // ==========================================
        // EARLY EXIT #1
        //
        // Player already starts at boundary
        // ==========================================

        if (isBoundary(startRow, startCol)) {

            System.out.println("YES");
            System.out.println(0);
            System.out.println();

            return;
        }

        // ==========================================
        // MULTI-SOURCE BFS FOR MONSTERS
        // ==========================================

        while (head < tail) {

            int pos = queue[head++];

            int row = pos / m;
            int col = pos % m;

            for (int k = 0; k < 4; k++) {

                int nr = row + dr[k];
                int nc = col + dc[k];

                // Outside grid
                if (nr < 0 || nr >= n || nc < 0 || nc >= m)
                    continue;

                // Wall
                if (graph[nr][nc] == '#')
                    continue;

                // Already visited by monster BFS
                if (disM[nr][nc] != INF)
                    continue;

                disM[nr][nc] = disM[row][col] + 1;

                queue[tail++] = nr * m + nc;
            }
        }

        // ==========================================
        // BFS FOR PLAYER
        // ==========================================

        /*
            Reuse the same queue.

            No need to allocate another one.
        */

        head = 0;
        tail = 0;

        disP[startRow][startCol] = 0;

        queue[tail++] = startRow * m + startCol;

        int exitRow = -1;
        int exitCol = -1;

        while (head < tail) {

            int pos = queue[head++];

            int row = pos / m;
            int col = pos % m;

            for (int k = 0; k < 4; k++) {

                int nr = row + dr[k];
                int nc = col + dc[k];

                // Outside grid
                if (nr < 0 || nr >= n || nc < 0 || nc >= m)
                    continue;

                // Wall
                if (graph[nr][nc] == '#')
                    continue;

                // Already visited
                if (disP[nr][nc] != INF)
                    continue;

                int newDist = disP[row][col] + 1;

                // ==================================
                // IMPORTANT PRUNING
                //
                // Monster must arrive STRICTLY later
                // ==================================

                if (newDist >= disM[nr][nc])
                    continue;

                disP[nr][nc] = newDist;

                // Store HOW we reached this cell
                parent[nr][nc] = move[k];

                // ==================================
                // EARLY EXIT #2
                //
                // First safe boundary reached by BFS
                // is a shortest escape.
                // ==================================

                if (isBoundary(nr, nc)) {

                    exitRow = nr;
                    exitCol = nc;

                    break;
                }

                queue[tail++] = nr * m + nc;
            }

            if (exitRow != -1)
                break;
        }

        // ==========================================
        // NO ESCAPE
        // ==========================================

        if (exitRow == -1) {

            System.out.println("NO");

            return;
        }

        // ==========================================
        // BUILD PATH
        // ==========================================

        String path = buildPath(exitRow, exitCol);

        System.out.println("YES");
        System.out.println(path.length());
        System.out.println(path);
    }

    // ------------------------------------------
    // Check whether cell is on grid boundary
    // ------------------------------------------

    static boolean isBoundary(int row, int col) {

        return row == 0 ||
               row == n - 1 ||
               col == 0 ||
               col == m - 1;
    }

    // ------------------------------------------
    // Reconstruct player path
    // ------------------------------------------

    static String buildPath(int row, int col) {

        StringBuilder path = new StringBuilder();

        /*
            Walk BACKWARDS from exit to A.

            parent[r][c] tells us which move was
            originally used to enter (r,c).
        */

        while (row != startRow || col != startCol) {

            char direction = parent[row][col];

            path.append(direction);

            /*
                Reverse the move.

                If we entered this cell using D:

                    parent
                       |
                       D
                       ↓
                    current

                then parent is one row above.
            */

            if (direction == 'D') {

                row--;

            } else if (direction == 'U') {

                row++;

            } else if (direction == 'R') {

                col--;

            } else if (direction == 'L') {

                col++;
            }
        }

        // We constructed exit -> start,
        // so reverse it.
        return path.reverse().toString();
    }

    public static void main(String[] args) throws Exception {
        solve();
    }
}
