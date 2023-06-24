class Solution {
    static int binarySearch(int[] nums, int target, int s, int e) {
        while (s <= e) {
            int mid = e - (e-s)/2 ;
            if(nums[mid] == target) return mid ;
            else if (nums[mid] > target) e = mid - 1 ;
            else s = mid + 1 ;
        }
        return -1 ;
    }
    public int search(int[] nums, int target) {
        int pivot_index = 0  ;
        for(int i = 0 ; i < nums.length-1 ; i++ ) {
            if(nums[i]>nums[i+1]) {
                pivot_index = i+1 ;
                break;
            }
        }
        if(pivot_index==0) {
            return binarySearch(nums,target,0,nums.length-1) ;
        }
        else if(target < nums[nums.length-1] ) return binarySearch(nums,target,pivot_index,nums.length-1) ;
        else return binarySearch(nums,target,0,pivot_index-1) ;

    }
}
