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

        String next() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = readByte()) <= ' ') ;
            do {
                sb.append((char) c);
            } while ((c = readByte()) > ' ');
            return sb.toString();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();
        StringBuilder out = new StringBuilder();

        int t = fs.nextInt();

        while (t-- > 0) {

            ArrayList<Integer> list = new ArrayList<>();
            int q = fs.nextInt();

            for (int i = 0; i < q; i++) {
                String query = fs.next();

                if (query.equals("add")) {
                    int val = fs.nextInt();

                    int idx = Collections.binarySearch(list, val);
                    if (idx < 0) {
                        idx = -idx - 1;
                        list.add(idx, val);
                    }
                    // ignore duplicates (set behavior)
                }

                else if (query.equals("remove")) {
                    int val = fs.nextInt();

                    int idx = Collections.binarySearch(list, val);
                    if (idx >= 0) {
                        list.remove(idx);
                    }
                }

                else if (query.equals("find")) {
                    int index = fs.nextInt();

                    if (index >= list.size()) {
                        out.append(-1).append('\n');
                    } else {
                        out.append(list.get(index)).append('\n');
                    }
                }

                else if (query.equals("findpos")) {
                    int val = fs.nextInt();

                    int idx = Collections.binarySearch(list, val);
                    if (idx < 0) idx = -idx - 1;

                    out.append(idx).append('\n');
                }
            }
        }

        System.out.println(out);
    }
}
