#!/bin/bash

# Check if folder name is provided
if [ -z "$1" ]; then
  echo "Usage: ./cp_setup.sh <folder_name>"
  exit 1
fi

FOLDER_NAME="$1"

# Create folder
mkdir -p "$FOLDER_NAME"

# Create Main.java with basic template
cat > "$FOLDER_NAME/Main.java" << EOF
import java.io.*;
import java.util.*;

public class Main {

    static final long MOD = 1_000_000_007L;
    static final long INF = 1_000_000_000_000_000_000L;
    static final int[][] dir = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static ArrayList<ArrayList<Integer>> graph;
    static boolean[] vis;
    static int[] col, component, cSize;
    static boolean isCycle = false;

    // -------- MODULAR ARITHMETIC --------
    static long modPow(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }

    static long inverse(long n) { return modPow(n, MOD - 2, MOD); }

    static long[] fact = new long[1000100];

    static void precompute() {
        fact[0] = 1L;
        for (int i = 1; i <= 1000000; i++) fact[i] = fact[i - 1] * i % MOD;
    }

    static long calculateNCR(int n, int r) {
        return fact[n] * inverse(fact[n - r] * fact[r] % MOD) % MOD;
    }

    /** First index holding a value strictly greater than target, else n. */
    static int upperBound(int[] arr, int n, int target) {
        int lo = 0, hi = n - 1, ans = n;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] > target) { ans = mid; hi = mid - 1; }
            else lo = mid + 1;
        }
        return ans;
    }

    // -------- STRUCTURES --------
    static class Pair {
        long first, second;
        Pair(long x, long y) { first = x; second = y; }
        @Override public String toString() { return "(" + first + ", " + second + ")"; }
    }

    static class MonotoneDeque {
        Deque<Integer> deque = new ArrayDeque<>();
        void insert(int val) {
            while (!deque.isEmpty() && deque.peekLast() < val) deque.pollLast();
            deque.offerFirst(val);
        }
        int getMax() { return deque.peekFirst(); }
        void remove(int val) { if (deque.peekFirst() == val) deque.pollFirst(); }
    }

    static public class GridHelper {
        public static int toId(int i, int j, int m) { return i * m + j; }
        public static int getRow(int id, int m) { return id / m; }
        public static int getCol(int id, int m) { return id % m; }
        public static int[] toCell(int id, int m) { return new int[]{id / m, id % m}; }
    }

    static public class DSU {
        int[] size, parent;
        DSU(int n) {
            size = new int[n + 1];
            parent = new int[n + 1];
            for (int i = 0; i <= n; i++) { parent[i] = i; size[i] = 1; }
        }
        int find(int x) { return parent[x] == x ? x : (parent[x] = find(parent[x])); }
        /** False when the two were already joined. */
        boolean union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX == rootY) return false;
            if (size[rootX] < size[rootY]) { int t = rootX; rootX = rootY; rootY = t; }
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
            return true;
        }
    }

    static public class Edge {
        int to, wt;
        Edge(int to, int wt) { this.to = to; this.wt = wt; }
    }

    static class BitSet {
        long mask, allMask;
        BitSet(long mask, int n) { this.mask = mask; allMask = (1L << n) - 1; }
        void set(int i)   { mask |= 1L << i; }
        void clear(int i) { mask &= ~(1L << i); }
        void flip(int i)  { mask ^= 1L << i; }
        boolean check(int i) { return (mask & (1L << i)) != 0; }
        boolean all() { return mask == allMask; }
        boolean any() { return mask != 0; }
        boolean none() { return mask == 0; }
        int count() { return Long.bitCount(mask); }
    }

    static void solve() throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();   // number of test cases

        while (t-- > 0) {

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

        /** Skip whitespace, return the first byte of the next token. */
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
EOF

# Create input.txt
touch "$FOLDER_NAME/input.txt"

# Create expected.txt
touch "$FOLDER_NAME/expected.txt"

echo "✅ Folder '$FOLDER_NAME' created with Main.java and input.txt"
