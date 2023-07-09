class Solution {
    // Function to return a list containing the DFS traversal of the graph.
    static void dfs(int node,boolean[] vis, ArrayList<ArrayList<Integer>> adj,ArrayList<Integer> ls) {
        vis[node] = true ;
        ls.add(node) ;
        for(int it : adj.get(node)) {
            if(!vis[it]) {
                dfs(it,vis,adj,ls) ;
            }
        }

    }
    public ArrayList<Integer> dfsOfGraph(int V, ArrayList<ArrayList<Integer>> adj) {
        boolean[] vis = new boolean[v+1] ;
        vis[0] = true ;
        ArrayList<Integer> ls = new ArrayList<>();
        dfs(0,vis,adj,ls) ;
        return ls ;
    }
}
