import java.io.*;
import java.util.*;

public class Main {

    // -------- FAST INPUT --------
    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c, sign = 1, val = 0;
            do {
                c = readByte();
            } while (c <= ' ');

            if (c == '-') {
                sign = -1;
                c = readByte();
            }

            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return val * sign;
        }
    }

    // -------- GLOBAL DS --------
    static int[] freqArray = new int[1000001];
    static int distcnt = 0;

    static void remove(int val) {
        freqArray[val]--;
        if (freqArray[val] == 0) distcnt--;
    }

    static void insert(int val) {
        if (freqArray[val] == 0) distcnt++;
        freqArray[val]++;
    }

    static int check(int val, int target) {
        if (freqArray[val] == 0 && distcnt + 1 <= target) return 1;
        if (freqArray[val] > 0 && distcnt <= target) return 1;
        return 0;
    }

    static long solve(int n, int k, int[] arr) {
        int tail = 0, head = -1;
        long ans = 0L;

        while (tail < n) {
            while (head + 1 < n && check(arr[head + 1], k) == 1) {
                insert(arr[head + 1]);
                head++;
            }

            ans += head - tail + 1;

            if (tail > head) {
                tail++;
                head = tail - 1;
            } else {
                remove(arr[tail]);
                tail++;
            }
        }
        return ans;
    }

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();

        int t = fs.nextInt();

        while (t-- > 0) {
            int n = fs.nextInt();
            int k = fs.nextInt();

            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = fs.nextInt();
            }

            // 🔥 RESET GLOBAL STATE (CRITICAL)
            Arrays.fill(freqArray, 0);
            distcnt = 0;

            long ans = solve(n, k, arr);
            System.out.println(ans);
        }
    }
}
