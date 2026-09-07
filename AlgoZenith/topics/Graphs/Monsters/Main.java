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

static int[][] dir = {
    {1, 0},
    {-1, 0},
    {0, 1},
    {0, -1}
};

static boolean isValid(int row, int col, int n, int m, char[][] graph) {
    return row >= 0 && row < n &&
           col >= 0 && col < m &&
           graph[row][col] != '#';
}

static void solve() throws Exception {

    FastScanner fs = new FastScanner();
    StringBuilder out = new StringBuilder();

    int n = fs.nextInt();
    int m = fs.nextInt();

    char[][] graph = new char[n][m];

    int[] pCell = new int[2];
    ArrayList<int[]> mCells = new ArrayList<>();

    for (int i = 0; i < n; i++) {
        String row = fs.next();

        for (int j = 0; j < m; j++) {
            char ch = row.charAt(j);

            if (ch == 'M') {
                mCells.add(new int[]{i, j});
                graph[i][j] = '.';
            } else if (ch == 'A') {
                pCell[0] = i;
                pCell[1] = j;
                graph[i][j] = '.';
            } else {
                graph[i][j] = ch;
            }
        }
    }

    // Player BFS
    Queue<int[]> pQueue = new ArrayDeque<>();

    boolean[][] pVis = new boolean[n][m];
    int[][] pDis = new int[n][m];
    int[][][] pParent = new int[n][m][2];

    for (int i = 0; i < n; i++) {
        Arrays.fill(pDis[i], Integer.MAX_VALUE);
    }

    pQueue.offer(pCell);
    pVis[pCell[0]][pCell[1]] = true;
    pDis[pCell[0]][pCell[1]] = 0;

    while (!pQueue.isEmpty()) {

        int[] cell = pQueue.poll();

        int row = cell[0];
        int col = cell[1];

        for (int[] d : dir) {

            int nRow = row + d[0];
            int nCol = col + d[1];

            if (isValid(nRow, nCol, n, m, graph)
                    && !pVis[nRow][nCol]) {

                pVis[nRow][nCol] = true;
                pDis[nRow][nCol] = pDis[row][col] + 1;

                pParent[nRow][nCol][0] = row;
                pParent[nRow][nCol][1] = col;

                pQueue.offer(new int[]{nRow, nCol});
            }
        }
    }

    // Monster BFS
    Queue<int[]> mQueue = new ArrayDeque<>();

    boolean[][] mVis = new boolean[n][m];
    int[][] mDis = new int[n][m];

    for (int i = 0; i < n; i++) {
        Arrays.fill(mDis[i], Integer.MAX_VALUE);
    }

    for (int[] cell : mCells) {
        int row = cell[0];
        int col = cell[1];

        mQueue.offer(cell);
        mVis[row][col] = true;
        mDis[row][col] = 0;
    }

    while (!mQueue.isEmpty()) {

        int[] cell = mQueue.poll();

        int row = cell[0];
        int col = cell[1];

        for (int[] d : dir) {

            int nRow = row + d[0];
            int nCol = col + d[1];

            if (isValid(nRow, nCol, n, m, graph)
                    && !mVis[nRow][nCol]) {

                mVis[nRow][nCol] = true;
                mDis[nRow][nCol] = mDis[row][col] + 1;

                mQueue.offer(new int[]{nRow, nCol});
            }
        }
    }

    // Find an escape cell
    int endRow = -1;
    int endCol = -1;

    for (int i = 0; i < n; i++) {

        for (int j = 0; j < m; j++) {

            if (i != 0 && i != n - 1 &&
                j != 0 && j != m - 1) {
                continue;
            }

            if (graph[i][j] == '.'
                    && pDis[i][j] < mDis[i][j]) {

                endRow = i;
                endCol = j;
                break;
            }
        }

        if (endRow != -1) break;
    }

    if (endRow == -1) {
        out.append("NO\n");
        System.out.print(out);
        return;
    }

    // Reconstruct path
    StringBuilder path = new StringBuilder();

    int row = endRow;
    int col = endCol;

    while (row != pCell[0] || col != pCell[1]) {

        int pRow = pParent[row][col][0];
        int pCol = pParent[row][col][1];

        if (pRow == row - 1) {
            path.append('D');
        } else if (pRow == row + 1) {
            path.append('U');
        } else if (pCol == col - 1) {
            path.append('R');
        } else if (pCol == col + 1) {
            path.append('L');
        }

        row = pRow;
        col = pCol;
    }

    path.reverse();

    out.append("YES\n");
    out.append(path.length()).append('\n');
    out.append(path).append('\n');

    System.out.print(out);
}

    static boolean isValid(int row, int col, int n , int m, char[][] graph) {
        if(row < n && col < m && row >= 0 && col >= 0) {
            if(graph[row][col] != '#') {
                return true ;
            }
        }
        return false ;
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
