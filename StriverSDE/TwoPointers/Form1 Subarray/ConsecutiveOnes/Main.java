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
    }

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

    int t = fs.nextInt();   // number of test cases

    while (t-- > 0) {
        int n = fs.nextInt();
        int k = fs.nextInt();


        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = fs.nextInt();
        }
        int ans = solve(n,k,arr) ;
        System.out.println(ans);
    }


    }

    static int solve(int n, int k, int[] arr) {
        int tail = 0 ;
        int head = -1;

        // data structure
        int count0 = k ;
        int ans = 0;

        while(tail < n) {
            while(head + 1 < n && (arr[head+1] == 1 || count0 > 0)) {
                if(arr[head + 1] == 0) count0 -= 1 ;
                head ++ ;
            }

            // update the ans
            ans = Math.max(ans, head - tail + 1) ;

            if(tail > head) { // empty subarray
                tail ++ ;
                head = tail  - 1 ;
            } else {
                tail ++ ;
                if(arr[tail - 1] == 0) {
                    count0 += 1 ;
                }
            }

        }
        return ans;
    }


}
