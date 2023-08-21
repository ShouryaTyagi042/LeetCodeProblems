// { https://practice.geeksforgeeks.org/problems/1a4b617b70f0142a5c2b454e6fbe1b9a69ce7861/1?utm_source=youtube&utm_medium=collab_striver_ytdescription&utm_campaign=number_of_provinces }

class Solution {
    private static void dfs(int node, ArrayList<ArrayList<Integer>> adj,boolean[] vis, ArrayList<Integer> temp){
        vis[node] = true ;
        temp.add(node) ;
        for(int it : adj.get(node)) {
            if(!vis[it]) {
                dfs(it, adj, vis, temp) ;
            }
         }
    }
    public int findNumberOfGoodComponent(int V, ArrayList<ArrayList<Integer>> adj) {
        boolean[] vis = new boolean[V+1] ;
        int count = 0 ;
        for(int i = 1 ; i <= V ; i++) {
            if(!vis[i]) {
                ArrayList<Integer> temp = new ArrayList<>() ;
                dfs(i,adj,vis,edges,temp) ;
                boolean flag = false ;
                for(int i = 0 ; i < temp.size() ; i++ ) {
                    if(adj.get(i).size() != temp.size()-1) {
                        flag = true ;
                        break;
                    }
                }
                if(!flag) count++ ;
            }
        }
        return count ;
    }

}
