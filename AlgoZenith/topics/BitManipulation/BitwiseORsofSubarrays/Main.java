class Solution {
    public int subarrayBitwiseORs(int[] nums) {

        Set<Integer> all = new HashSet<>();
        Set<Integer> prev = new HashSet<>();

        for (int num : nums) {

            Set<Integer> curr = new HashSet<>();

            // [num]
            curr.add(num);

            // Extend all subarrays ending at previous index
            for (int or : prev) {
                curr.add(or | num);
            }

            all.addAll(curr);
            prev = curr;
        }

        return all.size();
    }
}
