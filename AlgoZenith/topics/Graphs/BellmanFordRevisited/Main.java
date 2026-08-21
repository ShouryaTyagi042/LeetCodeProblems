import java.io.*;
import java.util.*;

public class Main {
    static class Edge {
        int u;
        int v;
        long w;
        Edge(int u, int v, long w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        Edge[] E = new Edge[m];
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            E[i] = new Edge(a, b, - (long)c);
        }

        final long INF = 1000000000000000000L;
        long[] dist = new long[n + 1];
        for (int i = 1; i <= n; i++) dist[i] = INF;
        dist[1] = 0;

        for (int i = 1; i <= n-1; i++) {
            for (Edge e : E) {
                int u = e.u;
                int v = e.v;
                long w = e.w;
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                }
            }
        }

        boolean neg = false;
        for (Edge e : E) {
            int u = e.u;
            int v = e.v;
            long w = e.w;
            if (dist[u] != INF && dist[v] > dist[u] + w) {
                neg = true;
                break;
            }
        }

        if (neg) out.println(-1);
        else {
            out.println(-dist[n]);
        }

        out.flush();
        out.close();
    }
}
