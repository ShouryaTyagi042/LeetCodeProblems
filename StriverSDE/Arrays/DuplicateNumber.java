class Solution {
    public int findDuplicate(int[] nums) {
        int duplicate = nums[0] ;
        for(int i = 0 ; i < nums.length ; i++) {
            duplicate = duplicate & nums[i] ;
        }
        return duplicate ;

    }
}
