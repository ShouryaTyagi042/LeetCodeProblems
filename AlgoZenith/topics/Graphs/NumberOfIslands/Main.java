class Solution {

    int[] dr = {-1, 1, 0, 0};
    int[] dc = {0, 0, -1, 1};

    public int numIslands(char[][] grid) {

        int n = grid.length;
        int m = grid[0].length;

        boolean[][] vis = new boolean[n][m];
        int islands = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                if (grid[i][j] == '1' && !vis[i][j]) {
                    islands++;
                    dfs(i, j, grid, vis);
                }
            }
        }

        return islands;
    }

    private void dfs(int r, int c, char[][] grid, boolean[][] vis) {

        vis[r][c] = true;

        for (int k = 0; k < 4; k++) {
            int nr = r + dr[k];
            int nc = c + dc[k];

            if (nr >= 0 && nr < grid.length &&
                nc >= 0 && nc < grid[0].length &&
                grid[nr][nc] == '1' &&
                !vis[nr][nc]) {

                dfs(nr, nc, grid, vis);
            }
        }
    }
}
