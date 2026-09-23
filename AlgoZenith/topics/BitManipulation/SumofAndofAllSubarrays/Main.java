import java.io.*;
import java.util.*;

public class Main {

    // static void solve() throws Exception {
    //     FastScanner fs = new FastScanner();
    //     StringBuilder out = new StringBuilder();
    //     int n = fs.nextInt() ;
    //     int[] arr = new int[n] ;
    //     for(int i = 0 ; i < n ; i++) {
    //         arr[i] = fs.nextInt() ;
    //     }
    //     long ans = 0 ;
    //     for(int i = 0 ; i < 31 ; i++) {
    //         ArrayList<Integer> list = new ArrayList<>() ;
    //         for(int j = 0 ; j < n ;j++) {
    //             if((arr[j] & (1 << i) )  != 0) {
    //                 list.add(1) ;
    //             } else {
    //                 list.add(0) ;
    //             }
    //         }
    //         ans += countOneSubarrays(list)  * (1 << i);
    //     }

    //     System.out.println(ans);
    // }

    static long countOneSubarrays(ArrayList<Integer> list) {
        long temp = 0 ;
        long ans = 0 ;
        for(int val : list) {
            if(val == 1) {
                temp ++ ;
                ans += temp ;
            } else {
                temp = 0 ;
            }
        }
        // System.out.println(ans);
        return ans ;
    }

    // Extending Ends Solution

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();
        int n = fs.nextInt() ;
        int[] arr = new int[n] ;
        for(int i = 0 ; i < n ; i++) {
            arr[i] = fs.nextInt() ;
        }
        long ans = 0 ;
        HashMap<Integer, Integer> lastAndFreq = new HashMap<>() ;
        for(int i = 0 ; i < n ; i++) {
            HashMap<Integer, Integer> currAndFreq = new HashMap<>() ;
            for(var entry : lastAndFreq.entrySet()) {
                int key = entry.getKey() ;
                int val = entry.getValue() ;
                int andVal = key & arr[i] ;
                currAndFreq.put(
                    andVal,
                    currAndFreq.getOrDefault(andVal, 0) + val
                );
            }

            currAndFreq.put(
                    arr[i],
                    currAndFreq.getOrDefault(arr[i], 0) + 1
            );

            for(var entry : currAndFreq.entrySet()) {
                ans += (long) entry.getValue() * entry.getKey() ;
            }

            lastAndFreq = currAndFreq ;
        }

        System.out.println(ans);
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
            do { c = readByte(); } while (c <= ' ');
            return c;
        }

        long nextLong() throws IOException {
            int c = skipBlank(), sign = 1;
            if (c == '-') { sign = -1; c = readByte(); }
            long val = 0;
            while (c > ' ') { val = val * 10 + (c - '0'); c = readByte(); }
            return val * sign;
        }

        int nextInt() throws IOException { return (int) nextLong(); }

        String next() throws IOException {
            int c = skipBlank();
            StringBuilder sb = new StringBuilder();
            while (c > ' ') { sb.append((char) c); c = readByte(); }
            return sb.toString();
        }

        double nextDouble() throws IOException { return Double.parseDouble(next()); }

        String nextLine() throws IOException {
            int c;
            while ((c = readByte()) != -1 && c == '\n');
            StringBuilder sb = new StringBuilder();
            while (c != -1 && c != '\n') { sb.append((char) c); c = readByte(); }
            return sb.toString();
        }
    }
}
