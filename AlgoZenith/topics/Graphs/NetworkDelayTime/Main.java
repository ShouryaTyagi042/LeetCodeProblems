class Solution {
    static public class Edge {
        int to ;
        int wt ;

        Edge(int to, int wt) {
            this.to = to ;
            this.wt = wt;
        }
    }

    static ArrayList<ArrayList<Edge>> graph ;
    static boolean[] vis ;
    static int[] dist ;
    static PriorityQueue<Edge> pq = new PriorityQueue<>((a,b) -> Integer.compare(a.wt, b.wt));

    static final int INF = Integer.MAX_VALUE ;

    static void dijkstras(int sc){
        dist[sc] = 0 ;
        pq.offer(new Edge(sc, 0)) ;
        while(!pq.isEmpty()) {
            Edge e = pq.poll() ;
            int node = e.to ;
            int weight = e.wt ;
            for(Edge neigh : graph.get(node)) {
                int nextNode = neigh.to ;
                int newDist = neigh.wt ;
                if(dist[nextNode] > dist[node] + newDist ) {
                    dist[nextNode] = dist[node] + newDist ;
                    pq.offer(new Edge(nextNode, dist[nextNode] )) ;
                }
            }
        }
    }

    public int networkDelayTime(int[][] times, int n, int k) {
        graph = new ArrayList<>();
        for(int i = 0 ; i <= n ; i++) {
            graph.add(new ArrayList<>()) ;
        }
        for(int[] time : times) {
            graph.get(time[0]).add(new Edge(time[1], time[2])) ;
        }
        dist = new int[n+1] ;
        Arrays.fill(dist, INF ) ;
        dijkstras(k) ;
        int ans = -1;
        for(int i = 1 ; i<= n ; i++) {
            if(dist[i] == INF) return -1 ;
            if(i != k ) ans = Math.max(ans, dist[i]);
        }
        return ans ;
    }
}
