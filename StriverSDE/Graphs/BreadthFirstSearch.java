class Solution {
    // Function to return Breadth First Traversal of given graph.
    public ArrayList<Integer> bfsOfGraph(int V, ArrayList<ArrayList<Integer>> adj) {
        boolean[] vis = new boolean[V+1] ;
        ArrayList<Integer> bfs = new ArrayList<>() ;
        Queue <Integer> q = new LinkedList<>() ;
        q.add(0) ;
        vis[0] = true;
        while(!q.isEmpty()) {
            int node = q.poll() ;
            bfs.add(node) ;
            for(int it: adj.get(node)) {
                if(!vis[it]) {
                    q.add(it) ;
                    vis[it] = true ;
                }
            }
        }
        return bfs ;

    }
}
