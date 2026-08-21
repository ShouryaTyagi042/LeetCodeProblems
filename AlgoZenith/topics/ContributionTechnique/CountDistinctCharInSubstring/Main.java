import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            int n = sc.nextInt();
            String s = sc.next();
            int[] prev = new int[26];
            Arrays.fill(prev, -1);
            long ans = 26L * n * (n + 1) / 2;
            for (int i = 0; i < n; i++) {
                int c = s.charAt(i) - 'a';
                long len = i - prev[c] - 1L;
                ans -= len * (len + 1) / 2;
                prev[c] = i;
            }
            for (int i = 0; i < 26; i++) {
                long len = n - prev[i] - 1L;
                ans -= len * (len + 1) / 2;
            }
            System.out.println(ans);
        }
    }
}
