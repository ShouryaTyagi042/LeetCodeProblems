import java.io.*;
import java.util.*;

public class Main {

    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        private int readByte() throws IOException {
            if (ptr >= len) { len = in.read(buffer); ptr = 0; if (len <= 0) return -1; }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c, val = 0;
            do { c = readByte(); } while (c <= ' ');
            while (c > ' ') { val = val * 10 + (c - '0'); c = readByte(); }
            return val;
        }

        long nextLong() throws IOException {
            int c; long val = 0;
            do { c = readByte(); } while (c <= ' ');
            while (c > ' ') { val = val * 10 + (c - '0'); c = readByte(); }
            return val;
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();

        int n = fs.nextInt();
        long k = fs.nextLong();

        // 13! = 6,227,020,800 > 10^9 (max k), so at most last 13 are permuted
        long[] realFact = new long[14];
        realFact[0] = 1L;
        for (int i = 1; i <= 13; i++) {
            realFact[i] = realFact[i - 1] * i;
        }

        // Find m: smallest value such that realFact[m] >= k
        // The last m elements are permuted; first (n-m) are the fixed prefix
        int m = 0;
        while (m < 13 && realFact[m] < k) m++;

        int fixed = n - m;

        // Result array
        int[] result = new int[n];

        // Fill fixed prefix: 1, 2, ..., fixed
        for (int i = 0; i < fixed; i++) {
            result[i] = i + 1;
        }

        // Remaining elements in a list: [fixed+1, ..., n]
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = fixed + 1; i <= n; i++) {
            list.add(i);
        }

        // Decode k-th permutation (1-indexed) of the m remaining elements
        for (int level = m; level >= 1; level--) {
            int idx = (int) ((k - 1) / realFact[level - 1]);
            result[fixed + (m - level)] = list.get(idx);
            list.remove(idx);
            k -= (long) idx * realFact[level - 1];
        }

        // Output
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i > 0) out.append(' ');
            out.append(result[i]);
        }
        System.out.println(out);
    }
}
