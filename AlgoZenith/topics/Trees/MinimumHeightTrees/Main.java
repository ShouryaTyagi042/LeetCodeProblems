class Solution {

    static ArrayList<ArrayList<Integer>> graph;
    static int[] parent;
    static int[] dist;
    static int n;

    static void dfs(int node, int d, int parentNode) {
        dist[node] = d;
        parent[node] = parentNode;

        for (int neigh : graph.get(node)) {
            if (neigh != parentNode) {
                dfs(neigh, d + 1, node);
            }
        }
    }

    public List<Integer> findMinHeightTrees(int n, int[][] edges) {

        if (n == 1) {
            return new ArrayList<>(List.of(0));
        }

        graph = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] edge : edges) {
            int x = edge[0];
            int y = edge[1];

            graph.get(x).add(y);
            graph.get(y).add(x);
        }

        dist = new int[n];
        parent = new int[n];

        dfs(0, 0, -1);

        int x = 0;

        for (int i = 0; i < n; i++) {
            if (dist[i] > dist[x]) {
                x = i;
            }
        }

        dfs(x, 0, -1);

        int y = x;

        for (int i = 0; i < n; i++) {
            if (dist[i] > dist[y]) {
                y = i;
            }
        }

        int diamLen = dist[y];
        List<Integer> ans = new ArrayList<>();

        int center = y;

        for (int i = 0; i < diamLen / 2; i++) {
            center = parent[center];
        }

        if (diamLen % 2 == 0) {
            ans.add(center);
        } else {
            ans.add(center);
            ans.add(parent[center]);
        }

        return ans;
    }
}
