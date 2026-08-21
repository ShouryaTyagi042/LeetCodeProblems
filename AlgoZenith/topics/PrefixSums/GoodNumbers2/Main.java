import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(' ');
        }
        StringTokenizer st = new StringTokenizer(sb.toString());

        final int N = 1000005;
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        int[] arr = new int[N];  // note that N and n are different.

        for (int i = 1; i <= n; i++) {
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            // use the technique of Partial sum to build the value at each index.
            arr[l]++;
            arr[r + 1]--;
        }
        // finally build the prefix sum.
        for (int i = 1; i < N; i++) arr[i] += arr[i - 1];

        // keep the values 1 where values >=k or else 0.
        for (int i = 1; i < N; i++) arr[i] = (arr[i] >= k) ? 1 : 0;

        // now to answer query in O(1), we will build prefix sum on this 0/1 array to
        // get count of positions.
        int[] prefixSum = new int[N];
        prefixSum[0] = 0;
        for (int i = 1; i < N; i++) {
            prefixSum[i] = prefixSum[i - 1] + arr[i];
        }

        while (q-- > 0) {
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            // gives the count of 1 positions in O(1).
            int ans = prefixSum[r] - prefixSum[l - 1];
            out.println(ans);
        }

        out.flush();
        out.close();
    }
}
