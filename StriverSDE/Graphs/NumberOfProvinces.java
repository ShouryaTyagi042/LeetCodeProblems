class Solution {
    private static void dfs(int node, ArrayList<ArrayList<Integer>> adj,boolean[] vis){
        vis[node] = true ;
        for(int it : adj.get(node)) {
            if(!vis[it]) {
                dfs(it, adj, vis) ;
            }
         }
    }
    static int numProvinces(ArrayList<ArrayList<Integer>> adj, int V) {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>() ;
        for(int i = 0 ; i <= V ; i++) {
            list.add(new ArrayList<Integer>()) ;
        }
        for(int i = 0 ; i < V ; i++ ) {
            for(int j = 0 ; j < V ; j++) {
                if(adj.get(i).get(j) == 1 && i != j) {
                    list.get(i+1).add(j+1) ;
                }
            }
        }
        boolean[] vis = new boolean[V+1] ;
        int count = 0 ;
        for(int i = 1 ; i < V+1 ; i++) {
            if(vis[i] == false) {
                dfs(i,list,vis) ;
            }
        }
        return count ;
    }
};
