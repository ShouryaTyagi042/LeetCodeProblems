import java.util.*;

class Solution {

    static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    static int[] reduceFraction(int a, int b) {
        if (a == 0) return new int[]{0, 1};
        if (b == 0) return new int[]{1, 0};

        int g = gcd(Math.abs(a), Math.abs(b));
        a /= g;
        b /= g;

        if (b < 0) {
            a = -a;
            b = -b;
        }

        return new int[]{a, b};
    }

    public int maxPoints(int[][] points) {
        int n = points.length;
        int ans = 1;

        for (int i = 0; i < n; i++) {
            HashMap<String, Integer> map = new HashMap<>();

            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                int dy = points[j][1] - points[i][1];
                int dx = points[j][0] - points[i][0];

                int[] slope = reduceFraction(dy, dx);
                String key = slope[0] + "/" + slope[1];

                map.put(key, map.getOrDefault(key, 0) + 1);
            }

            for (int val : map.values()) {
                ans = Math.max(ans, val + 1);
            }
        }

        return ans;
    }
}
