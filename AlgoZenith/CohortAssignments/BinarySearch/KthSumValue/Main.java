import java.io.*;
import java.util.*;

public class Main {

    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        long nextLong() throws IOException {
            long sign = 1;
            long val = 0;
            int c;

            do {
                c = read();
            } while (c <= ' ');

            if (c == '-') {
                sign = -1;
                c = read();
            }

            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = read();
            }

            return val * sign;
        }

        int nextInt() throws IOException {
            return (int) nextLong();
        }
    }

    static int upperBound(int[] arr, int n, int target) {
        int lo = 0;
        int hi = n - 1;
        int ans = n;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (arr[mid] > target) {
                ans = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }

        return ans;
    }

    static boolean check(int[] n, int[] m, long k, int val) {

        int nLen = n.length;
        int mLen = m.length;

        // iterate on smaller array
        if (nLen > mLen) {
            int[] temp = n;
            n = m;
            m = temp;

            int tempL = nLen;
            nLen = mLen;
            mLen = tempL;
        }

        long count = 0;

        for (int i = 0; i < nLen; i++) {
            count += upperBound(m, mLen, val - n[i]);
        }

        return count >= k;
    }

    public static void main(String[] args) throws Exception {

        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {

            int n = fs.nextInt();
            int m = fs.nextInt();
            long k = fs.nextLong();

            int[] arr = new int[n];
            int[] arr2 = new int[m];

            for (int i = 0; i < n; i++) {
                arr[i] = fs.nextInt();
            }

            for (int i = 0; i < m; i++) {
                arr2[i] = fs.nextInt();
            }

            Arrays.sort(arr);
            Arrays.sort(arr2);

            int lo = arr[0] + arr2[0];
            int hi = arr[n - 1] + arr2[m - 1];

            int ans = hi;

            while (lo <= hi) {

                int mid = lo + (hi - lo) / 2;

                if (check(arr, arr2, k, mid)) {
                    ans = mid;
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            }

            out.append(ans).append('\n');
        }

        System.out.print(out);
    }
}
