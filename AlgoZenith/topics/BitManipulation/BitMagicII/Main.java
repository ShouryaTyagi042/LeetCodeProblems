import java.io.*;
import java.util.*;

public class Main {

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {
            long n = fs.nextLong();

            // 1. 64-bit binary representation
            String s = Long.toBinaryString(n);
            for (int i = s.length(); i < 64; i++) out.append('0');
            out.append(s).append('\n');

            // 2. MSB position
            out.append(n == 0 ? -1 : 63 - Long.numberOfLeadingZeros(n)).append('\n');

            // 3. Rightmost set bit position
            out.append(n == 0 ? -1 : Long.numberOfTrailingZeros(n)).append('\n');

            // 4. Power of 2
            out.append(n > 1 && (n & (n - 1)) == 0 ? 1 : 0).append('\n');

            // 5. Biggest power of 2 dividing n
            out.append(n == 0 ? -1 : n & -n).append('\n');

            // 6. Smallest power of 2 >= n, k > 0
            if (n <= 2)
                out.append(2).append('\n');
            else
                out.append(1L << (64 - Long.numberOfLeadingZeros(n - 1))).append('\n');
        }

        System.out.print(out);
    }

    public static void main(String[] args) throws Exception {
        new Thread(null, () -> {
            try {
                solve();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "solve", 1 << 26).start();
    }

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

        private int skipBlank() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ');
            return c;
        }

        long nextLong() throws IOException {
            int c = skipBlank();
            int sign = 1;

            if (c == '-') {
                sign = -1;
                c = readByte();
            }

            long val = 0;

            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = readByte();
            }

            return val * sign;
        }

        int nextInt() throws IOException {
            return (int) nextLong();
        }
    }
}
