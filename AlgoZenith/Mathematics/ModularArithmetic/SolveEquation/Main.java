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

        String nextLine() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = readByte()) != -1 && c != '\n') {
                sb.append((char) c);
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
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();
        while(t-- > 0) {
            String line = fs.nextLine();
            solve(line) ;
        }


    }

    static long modInv(long a, long mod) {
        return modPow(a, mod - 2, mod);
    }

    static long apply(long x, long y, char op, long mod) {
        if (op == '+') return (x % mod + y % mod) % mod;
        if (op == '-') {
          long ans =  (x % mod  - y %  mod) % mod;
          ans %= mod  ;
          ans = ( ans + mod) % mod ;
          return ans ;
        }
        if (op == '*') return ((x % mod)  * (y % mod) % mod);
        // op == '/'
        return (x * modInv(y, mod)) % mod;
    }
        static long add(long a, long b) {
        return ((a % mod) + (b % mod)) % mod;
    }

    static long sub(long a, long b) {
        long ans = ((a % mod) - (b % mod)) % mod;
        ans %= mod;
        ans = (ans + mod) % mod;
        return ans;
    }

    static long mul(long a, long b) {
        return ((a % mod) * (b % mod)) % mod;
    }

    static long power(long a, long b) {
        long ans = 1;
        while (b > 0) {
            if ((b & 1) == 1) {
                ans = ((ans % mod) * (a % mod)) % mod;
            }
            a = ((a % mod) * (a % mod)) % mod;
            b >>= 1;
        }
        return ans;
    }


    static long res(char op, long a, long b) {
        if (op == '+')
            return add(a, b);
        else if (op == '-')
            return sub(a, b);
        else if (op == '*')
            return mul(a, b);
        else
            return mul(a, power(b, mod - 2));
    }

    static long mod = 0 ;

    static void solve(String line) {
          // extract numbers
        long[] nums = new long[4];
        int idx = 0, cur = 0;
        boolean reading = false;
        char[] op = new char[2] ;
        int idx2 = 0 ;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (Character.isDigit(ch)) {
                cur = cur * 10 + (ch - '0');
                reading = true;
            } else if (ch == '/' || ch == '*' || ch == '-' || ch == '+') {
                op[idx2++] = ch ;
            }
            else {
                if (reading) {
                    nums[idx++] = cur;
                    cur = 0;
                    reading = false;
                }
            }

        }
        if (reading) nums[idx] = cur;

        long a = nums[0];
        long b = nums[1];
        long c = nums[2];
        mod = nums[3];

        char op1 = op[0] ;
        char op2 = op[1] ;

            long ans = 0;
            if (op1 == '+' || op1 == '-') {
                if (op2 == '+' || op2 == '-') {
                    ans = res(op1, a, b);
                    ans = res(op2, ans, c);
                } else {
                    ans = res(op2, b, c);
                    ans = res(op1, a, ans);
                }
            } else {
                ans = res(op1, a, b);
                ans = res(op2, ans, c);
            }
            System.out.println(ans);

    }

}

