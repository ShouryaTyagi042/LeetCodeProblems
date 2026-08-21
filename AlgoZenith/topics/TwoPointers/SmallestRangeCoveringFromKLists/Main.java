class Solution {

    static class Pair {
        int x, y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public int[] smallestRange(List<List<Integer>> nums) {

        int n = nums.size();

        List<Pair> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            for (int val : nums.get(i)) {

                list.add(new Pair(val, i));
            }
        }

        Collections.sort(list, (a, b) -> Integer.compare(a.x, b.x));

        int[] freq = new int[n];

        int dist = 0;

        int tail = 0;
        int head = -1;

        int total = list.size();

        int bestL = -100000;
        int bestR = 100000;

        while (tail < total) {

            while (head + 1 < total &&
                  (dist < n || freq[list.get(head + 1).y] > 0)) {

                head++;

                if (freq[list.get(head).y] == 0)
                    dist++;

                freq[list.get(head).y]++;
            }

            if (dist == n) {

                int left = list.get(tail).x;
                int right = list.get(head).x;

                if ((right - left < bestR - bestL) ||
                    (right - left == bestR - bestL && left < bestL)) {

                    bestL = left;
                    bestR = right;
                }
            }

            if (tail <= head) {

                freq[list.get(tail).y]--;

                if (freq[list.get(tail).y] == 0)
                    dist--;

                tail++;

            } else {

                tail++;
                head = tail - 1;
            }
        }

        return new int[]{bestL, bestR};
    }
}
