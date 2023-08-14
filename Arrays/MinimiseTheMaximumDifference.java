class Solution {
    public int minimizeMax(int[] nums, int p) {
        Arrays.sort(nums) ;
        int[] diff = new int[nums.length-1] ;
        int j = 1 ;
        for(int i = 0 ; i < nums.length - 1 ; i++  ){
            diff[i] = nums[j++] - nums[i] ;
        }
        Arrays.sort(diff) ;
        return diff[p-1] ;
        
    }
}
