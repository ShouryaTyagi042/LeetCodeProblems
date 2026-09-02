import java.util.*;

class Solution {

    static class Cell {
        int row;
        int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    int[][] DIR = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };

    public int nearestExit(char[][] maze, int[] entrance) {

        int n = maze.length;
        int m = maze[0].length;

        boolean[][] vis = new boolean[n][m];
        int[][] dis = new int[n][m];

        for (int i = 0; i < n; i++) {
            Arrays.fill(dis[i], Integer.MAX_VALUE);
        }

        ArrayDeque<Cell> queue = new ArrayDeque<>();

        Cell start = new Cell(entrance[0], entrance[1]);

        vis[start.row][start.col] = true;
        dis[start.row][start.col] = 0;
        queue.offer(start);

        while (!queue.isEmpty()) {

            Cell cur = queue.poll();

            for (int[] d : DIR) {

                int nr = cur.row + d[0];
                int nc = cur.col + d[1];

                if (nr < 0 || nr >= n || nc < 0 || nc >= m)
                    continue;

                if (maze[nr][nc] == '+')
                    continue;

                if (vis[nr][nc])
                    continue;

                vis[nr][nc] = true;
                dis[nr][nc] = dis[cur.row][cur.col] + 1;
                queue.offer(new Cell(nr, nc));
            }
        }

        int ans = Integer.MAX_VALUE;

        // Top row
        for (int j = 0; j < m; j++) {
            if (!(0 == entrance[0] && j == entrance[1]) && vis[0][j]) {
                ans = Math.min(ans, dis[0][j]);
            }
        }

        // Bottom row
        for (int j = 0; j < m; j++) {
            if (!(n - 1 == entrance[0] && j == entrance[1]) && vis[n - 1][j]) {
                ans = Math.min(ans, dis[n - 1][j]);
            }
        }

        // Left column
        for (int i = 0; i < n; i++) {
            if (!(i == entrance[0] && 0 == entrance[1]) && vis[i][0]) {
                ans = Math.min(ans, dis[i][0]);
            }
        }

        // Right column
        for (int i = 0; i < n; i++) {
            if (!(i == entrance[0] && m - 1 == entrance[1]) && vis[i][m - 1]) {
                ans = Math.min(ans, dis[i][m - 1]);
            }
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}
