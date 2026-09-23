class Solution {
    public long countSubarrays(int[] nums, int k) {

        long ans = 0;

        Map<Integer, Long> prev = new HashMap<>();

        for (int num : nums) {

            Map<Integer, Long> curr = new HashMap<>();

            curr.put(num, curr.getOrDefault(num, 0L) + 1);

            for (Map.Entry<Integer, Long> entry : prev.entrySet()) {

                int andValue = entry.getKey();
                long count = entry.getValue();

                int newAnd = andValue & num;

                curr.put(
                    newAnd,
                    curr.getOrDefault(newAnd, 0L) + count
                );
            }

            ans += curr.getOrDefault(k, 0L);

            prev = curr;
        }

        return ans;
    }
}
