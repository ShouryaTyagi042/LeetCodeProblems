class Solution {
    public int findPeakElement(int[] nums) {
        int n = nums.length ;
        int hi = n - 1 ;
        int lo = 0 ;
        while( lo < hi) {
            int mid = lo + (hi - lo ) / 2 ;
            if(arr[mid] < arr[mid + 1]) {
                lo = mid + 1 ;
            } else {
                hi = mid ;
            }
        }
        return lo ;
    }
}
