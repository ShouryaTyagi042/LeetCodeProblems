import java.io.*;
import java.util.*;

public class Main {

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

        public String next() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = readByte()) <= ' ') if (c == -1) return "";
            do {
                sb.append((char) c);
            } while ((c = readByte()) > ' ');
            return sb.toString();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {
            int n = fs.nextInt();

            TreeMap<Integer, Integer> towers = new TreeMap<>();

            for (int i = 0; i < n; i++) {
                int x = fs.nextInt();

                Integer key = towers.higherKey(x); // upper_bound

                if (key != null) {
                    // remove one occurrence
                    if (towers.get(key) == 1) towers.remove(key);
                    else towers.put(key, towers.get(key) - 1);
                }

                // add current block
                towers.put(x, towers.getOrDefault(x, 0) + 1);
            }

            // total towers = total frequency sum
            int ans = 0;
            for (int freq : towers.values()) ans += freq;

            out.append(ans).append('\n');
        }

        System.out.print(out);
    }
import java.io.*;
import java.util.*;

public class Main {

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

        public String next() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = readByte()) <= ' ') if (c == -1) return "";
            do {
                sb.append((char) c);
            } while ((c = readByte()) > ' ');
            return sb.toString();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {
            int n = fs.nextInt();

            TreeMap<Integer, Integer> towers = new TreeMap<>();

            for (int i = 0; i < n; i++) {
                int x = fs.nextInt();

                Integer key = towers.higherKey(x); // upper_bound

                if (key != null) {
                    // remove one occurrence
                    if (towers.get(key) == 1) towers.remove(key);
                    else towers.put(key, towers.get(key) - 1);
                }

                // add current block
                towers.put(x, towers.getOrDefault(x, 0) + 1);
            }

            // total towers = total frequency sum
            int ans = 0;
            for (int freq : towers.values()) ans += freq;

            out.append(ans).append('\n');
        }

        System.out.print(out);
    }
}
