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

    static class Pair  {
        long x, y;

        Pair(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
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
        StringBuilder out = new StringBuilder();

        long a = fs.nextLong();
        long b = fs.nextLong();

         List<Long> primes = segmentedSieve(a,b);

         System.out.println(primes.size());
         int idx = 0 ;
         while(idx < primes.size()) {
            System.out.print(primes.get(idx));
            System.out.print(" ");
            idx++;
         }


    }

    static int gcd(int a, int b) {
       if (a == 0) return b;
       return gcd(b % a, a);
    }

static List<Integer> sieve(int n) {
    boolean[] isPrime = new boolean[n + 1];
    Arrays.fill(isPrime, true);

    if (n >= 0) isPrime[0] = false;
    if (n >= 1) isPrime[1] = false;

    for (int i = 2; i * i <= n; i++) {
        if (isPrime[i]) {
            for (int j = i * i; j <= n; j += i) {
                isPrime[j] = false;
            }
        }
    }

    List<Integer> primes = new ArrayList<>();
    for (int i = 2; i <= n; i++) {
        if (isPrime[i]) primes.add(i);
    }

    return primes;
}


static List<Long> segmentedSieve(long L, long R) {

    int limit = (int) Math.sqrt(R);
    List<Integer> basePrimes = sieve(limit);

    boolean[] isPrime = new boolean[(int)(R - L + 1)];
    Arrays.fill(isPrime, true);

    if (L == 1) isPrime[0] = false;

    for (int prime : basePrimes) {

        long ceilLByP = (L + prime - 1) / prime;
        long curMul = prime * ceilLByP;

        curMul = Math.max((long)prime * prime, curMul);

        while (curMul <= R) {
            isPrime[(int)(curMul - L)] = false;
            curMul += prime;
        }
    }

    List<Long> primesInRange = new ArrayList<>();

    for (int i = 0; i <= R - L; i++) {
        if (isPrime[i]) {
            primesInRange.add(i + L) ;
        }
    }

    return primesInRange;
}



}

