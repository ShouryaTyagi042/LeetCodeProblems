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

    static final long MOD = 1_000_000_007L;

    static long modPow(long base, long exp, long mod) {
        long result = 1 ;
        base %= mod ;
        while( exp > 0) {
            // check if power is odd
            if  ((exp & 1) == 1 ) {
                result = (( result % mod) * (base % mod) ) % mod ;
            }
            base = ( (base % mod)  * (base % mod ) ) % mod ;
            exp >>= 1 ;
        }

        return result ;
    }

    // -------- MAIN --------
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();


    int t = fs.nextInt();   // number of test cases

    while (t-- > 0) {
        StringBuilder out = new StringBuilder();

        int num = fs.nextInt();
        int den = fs.nextInt();

        if ((num < 0) ^ (den < 0)) {
            out.append("-");
        }

        num = Math.abs(num) ;
        den = Math.abs(den) ;


        out.append( num / den );
        int rem = num % den ;
        if( rem == 0 ) {
            System.out.println(out);
            continue;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        out.append(".") ;
        while(rem > 0) {
            if(map.containsKey(rem)) {
                int index  = map.get(rem) ;
                out.insert(index, "(") ;
                out.append(")");
                break;
            }
            map.put(rem, out.length()) ;
            rem *= 10 ;
            out.append(rem / den) ;
            rem = rem % den ;
        }
        System.out.println(out);
    }

    }


}

