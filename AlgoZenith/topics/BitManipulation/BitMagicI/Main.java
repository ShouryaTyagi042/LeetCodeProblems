import java.io.*;
import java.util.*;

public class Main {

    static class BitSet {
        long mask, allMask;
        BitSet(long mask, int n) { this.mask = mask; allMask = (1L << n) - 1; }
        void set(int i)   { mask |= 1L << i; }
        void clear(int i) { mask &= ~(1L << i); }
        void flip(int i)  { mask ^= 1L << i; }
        int check(int i) { return (int) (mask >> i & 1); }
        int all()  { return mask == allMask ? 1 : 0; }
        int any()  { return mask != 0 ? 1 : 0; }
        int none() { return mask == 0 ? 1 : 0; }
        int count() { return Long.bitCount(mask); }
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        int q = fs.nextInt() ;
        BitSet bits = new BitSet(0, 60) ;
        for(int i = 1 ; i <= q ; i++) {
            int query = fs.nextInt() ;
            if(query == 1 ) {
                int val = fs.nextInt() ;
                out.append(bits.check(val)).append('\n') ;
            }
            if(query == 2 ) {
                int val = fs.nextInt() ;
                bits.set(val) ;
                // out.append(bits.check(val)).append('\n') ;
            }

            if(query == 3 ) {
                int val = fs.nextInt() ;
                bits.clear(val) ;
                // out.append(bits.clear(val)).append('\n') ;
            }

            if(query == 4 ) {
                int val = fs.nextInt() ;
                bits.flip(val);
                // out.append(bits.check(val)).append('\n') ;
            }

            if(query == 5 ) {
                out.append(bits.all()).append('\n') ;
            }

           if(query == 6 ) {
                out.append(bits.any()).append('\n') ;
            }

           if(query == 7 ) {
                out.append(bits.none()).append('\n') ;
            }

            if(query == 8 ) {
                out.append(bits.count()).append('\n') ;
            }

           if(query == 9 ) {
                out.append(bits.mask).append('\n') ;
            }


        }
        System.out.println(out);

    }

    public static void main(String[] args) throws Exception {
        new Thread(null, () -> {
            try {
                solve();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "solve", 1 << 26).start();   // 64 MB stack
    }

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

        long nextLong() throws IOException {
            int c, sign = 1;
            long val = 0;
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

        String next() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            do {
                c = readByte();
            } while (c <= ' ');

            while (c > ' ') {
                sb.append((char) c);
                c = readByte();
            }
            return sb.toString();
        }

        double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }

        String nextLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;

            // skip any leftover newline or spaces
            while ((c = readByte()) != -1 && c == '\n');

            // read until newline
            while (c != -1 && c != '\n') {
                sb.append((char) c);
                c = readByte();
            }

            return sb.toString();
        }
    }


}
