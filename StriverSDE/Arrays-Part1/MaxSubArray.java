class Solution {
    public int maxSubArray(int[] nums) {
        int max_sum =   Integer.MIN_VALUE ;
        int max_ending =  0;
        for (int i = 0 ; i < nums.length ; i++ ) {
            max_ending += nums[i] ;
            if (max_ending >= max_sum) {
                max_sum = max_ending ;
            }
            if (max_ending < 0) {
                max_ending = 0 ;
            }
        }
        return max_sum ;
    }
}
