class Solution {
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        ArrayList < ArrayList < Integer >> adj = new ArrayList < > ();
        Stack<Integer> stack = new Stack<>();
        boolean[] vis = new boolean[n] ;
       for (int i = 0; i < n; i++) {
            adj.add(new ArrayList < > ());
        }
        for (int i = 0 ; i < edges.length ; i++ ) {
                adj.get(edges[i][0]).add(edges[i][1]);
                adj.get(edges[i][1]).add(edges[i][0]) ;
        }
        stack.push(source) ;
        vis[source] = true ;
        while (!stack.isEmpty()) {
            int node = stack.pop() ;
            if(node == destination) return true ;
            for(int i : adj.get(node) ) {
                if(!vis[i]) {
                    vis[i] = true ;
                    stack.add(i) ;
                }
            }
        }
        return false ;



        
    }
}
