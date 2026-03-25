import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

        StringBuilder sb = new StringBuilder(br.readLine()); // ✅ single state

        while (q-- > 0) {
            st = new StringTokenizer(br.readLine());
            String type = st.nextToken();

            if (type.equals("pop_back")) {
                if (sb.length() > 0)
                    sb.setLength(sb.length() - 1);
            }

            else if (type.equals("front")) {
                out.append(sb.charAt(0)).append('\n');
            }

            else if (type.equals("back")) {
                out.append(sb.charAt(sb.length() - 1)).append('\n');
            }

            else if (type.equals("print")) {
                int pos = Integer.parseInt(st.nextToken());
                out.append(sb.charAt(pos - 1)).append('\n');
            }

            else if (type.equals("substr")) {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                if (l > r) { int tmp = l; l = r; r = tmp; }

                out.append(sb.substring(l - 1, r)).append('\n');
            }

            else if (type.equals("sort")) {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                if (l > r) { int tmp = l; l = r; r = tmp; }

                char[] arr = sb.substring(l - 1, r).toCharArray();
                Arrays.sort(arr);

                sb.replace(l - 1, r, new String(arr));
            }

            else if (type.equals("reverse")) {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                if (l > r) { int tmp = l; l = r; r = tmp; }

                String sub = sb.substring(l - 1, r);
                sb.replace(l - 1, r, new StringBuilder(sub).reverse().toString());
            }

            else if (type.equals("push_back")) {
                char c = st.nextToken().charAt(0);
                sb.append(c);
            }
        }

        System.out.print(out);
    }
}
